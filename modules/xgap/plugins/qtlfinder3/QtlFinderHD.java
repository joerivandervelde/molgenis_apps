/**
 * 
 */
package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import matrix.DataMatrixInstance;
import matrix.general.DataMatrixHandler;

import org.molgenis.cluster.DataValue;
import org.molgenis.data.Data;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.util.Entity;
import org.molgenis.wormqtl.etc.HumanToWorm;
import org.molgenis.wormqtl.etc.HypergeometricTest;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Marker;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder2.QtlFinder2;
import decorators.MolgenisFileHandler;

/**
 * @author Mark de Haan
 * @version 1.0
 * @since 15/04/2013
 * 
 */
public class QtlFinderHD extends QtlFinder2
{

	private static final long serialVersionUID = 1L;

	public QtlFinderHD(String name, ScreenController<?> parent)
	{
		super(name, parent);
		// TODO Auto-generated constructor stub
	}

	private QtlFinderHDModel model = new QtlFinderHDModel();

	public QtlFinderHDModel getMyModel()
	{
		return model;
	}

	/**
	 * This method determines what probes and genes are inside the region that
	 * was selected, either by qtl search or by region search.
	 * 
	 * Calls the HumanToWorm class to determine which diseases are mapped to the
	 * selected region. Sets table view to true to show gene - disease -
	 * probability as a result
	 * 
	 * @param start
	 * @param end
	 * @param chromosome
	 * @param db
	 * @param search
	 * @throws Exception
	 */
	public void setRegion(Integer start, Integer end, Integer chromosome, Database db, Integer search) throws Exception
	{
		List<Probe> probesInRegion = new ArrayList<Probe>();
		List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR, Operator.LESS,
				chromosome));

		if (search == 1)
		{
			for (Chromosome chr : chrNeeded)
			{
				start = start + chr.getBpLength();
				end = end + chr.getBpLength();
			}
		}

		probesInRegion = db.find(Probe.class, new QueryRule(Probe.BPSTART, Operator.GREATER, start), new QueryRule(
				Probe.BPSTART, Operator.LESS, end));

		for (Probe p : probesInRegion)
		{
			model.getHits().put(p.getName(), p);

			if (p.getSymbol().contains("WBGene"))
			{
				List<String> myList = this.model.getHumanToWorm().linkToDisease(p.getSymbol());
				this.model.getGeneAssociatedDiseases().put(myList.get(0), myList.subList(1, myList.size()));
			}
			else
			{
				List<String> myList = this.model.getHumanToWorm().linkToDisease(p.getReportsFor_Name());
				this.model.getGeneAssociatedDiseases().put(myList.get(0), myList.subList(1, myList.size()));
			}
		}

		this.model.setShowTable(true);
	}

	/**
	 * This method retrieves the highest scoring QTL from a certain probe and
	 * determines which marker that QTL is located on
	 * 
	 * @param dm
	 * @param probe
	 * @param threshold
	 * @return returns the name of the marker that has the highest scoring qtl
	 * @throws Exception
	 */
	public String getProbeExpression(DataMatrixInstance dm, String probe, double threshold) throws Exception
	{
		// TODO: Change method of determining region inside QTL
		// Now: Highest scoring marker + / - 10.000 bp positions = Region

		String bestMarker = "";
		// Double flankLeft = 0.0;
		// Double flankRight = 0.0;
		Double highest = 0.0;
		Integer highestIdx = 0;
		Object[] myTraitQtlScore = dm.getRow(probe);

		for (int i = 0; i < myTraitQtlScore.length; i++)
		{
			if (highest == 0)
			{
				highest = (Double) myTraitQtlScore[i];
				highestIdx = i;
			}
			else
			{
				if (highest < (Double) myTraitQtlScore[i])
				{
					highest = (Double) myTraitQtlScore[i];
					highestIdx = i;
				}
				else
				{
					continue;
				}
			}
		}

		if (highest > threshold)
		{
			// flankLeft = (Double) myTraitQtlScore[(highestIdx - 1)];
			// flankRight = (Double) myTraitQtlScore[(highestIdx + 1)];

			// System.out.println("Flanking left: " + flankLeft +
			// " The highest QTL is: " + highest + " Flanking right: "
			// + flankRight);

			bestMarker = dm.getColNames().get(highestIdx);

		}
		else
		{
			this.setMessages(new ScreenMessage(
					"There was no QTL for this probe / trait that scored above the submitted threshold", false));
		}
		return bestMarker;
	}

	@Override
	public void handleRequest(Database db, MolgenisRequest request)
	{

		if (request.getString("__action") != null)
		{
			String action = request.getString("__action");

			try
			{
				if (!action.startsWith(this.model.prefix))
				{
					super.model = this.model;
					super.handleRequest(db, request);
				}

				else
				{
					// Remove the prefix from the handle request
					action = action.substring(this.model.prefix.length());

					/**
					 * @author Mark de Haan
					 * 
					 *         Region search
					 * 
					 *         User fills in (in bp) start and stop positions,
					 *         and a chromosome. Algorithm then uses these
					 *         parameters to determine which probes are located
					 *         in this region
					 */
					if (action.equals("regionSearch"))
					{
						if (request.getString("regionStart") == null || request.getString("regionEnd") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a starting and ending point "
									+ "for your region search. An entire chromosome selection will result in to "
									+ "many hits, overloading your browser", false));
						}
						else
						{
							Integer start = request.getInt("regionStart");
							Integer end = request.getInt("regionEnd");
							Integer chromosome = request.getInt("regionChr");

							setRegion(start, end, chromosome, db, 1);
						}
					}

					/**
					 * @author Mark de Haan
					 * 
					 *         Probe QTL region search
					 * 
					 *         User fills in a probe / trait name, a lod
					 *         threshold and a dataset to search in. The
					 *         algorithm then uses these parameters to determine
					 *         the highest QTL score present for the submitted
					 *         probe. Then based on the position of the marker
					 *         containing this QTL, the surrounding region is
					 *         taken and a region search is performed. This
					 *         determines what probes are located within the QTL
					 *         peak region
					 */
					if (action.equals("traitRegionSearch"))
					{
						if (request.getString("traitInput") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a trait in the form"
									+ "of a probe name.", false));
						}
						else
						{
							String trait = request.getString("traitInput");
							String dataset = request.getString("regionDataSetSelect");
							double threshold = request.getInt("lodThreshold");

							DataMatrixHandler dmh = new DataMatrixHandler(db);

							Data selectDataset = db
									.find(Data.class, new QueryRule(Data.NAME, Operator.EQUALS, dataset)).get(0);
							DataMatrixInstance dataMatrix = dmh.createInstance(selectDataset, db);

							String highestMarker = getProbeExpression(dataMatrix, trait, threshold);

							List<Marker> highestProbe = db.find(Marker.class, new QueryRule(Marker.NAME,
									Operator.EQUALS, highestMarker));

							List<Chromosome> chromosomes = db.find(Chromosome.class, new QueryRule(Chromosome.NAME,
									Operator.EQUALS, highestProbe.get(0).getChromosome_Name()));

							// Once the marker has been determined, the region
							// within the Qtl is determines by taking the
							// starting position, and adding or retracting
							// 10.000 (temporary solution)
							Integer start = (int) (highestProbe.get(0).getBpStart() - 10000);
							Integer end = (int) (highestProbe.get(0).getBpStart() + 10000);
							Integer chromosome = chromosomes.get(0).getOrderNr();

							setRegion(start, end, chromosome, db, 0);
						}
					}

					/**
					 * @author Joeri van der Velde, Mark de Haan
					 * 
					 *         QTL Search
					 * 
					 *         User can submit a region to start and end, a
					 *         chromosome to look at, a dataset to search in and
					 *         a QTL lod score threshold. The algorithm then
					 *         looks into the selected region and returns probes
					 *         if there is a 'QTL hotspot' present in this
					 *         region. This hotspot is located at the position
					 *         where a QTL peak is above the pre determined
					 *         threshold
					 * 
					 *         TODO: Determine which disease can be mapped to
					 *         found hotspot
					 */
					if (action.equals("QtlSearch"))
					{
						if (request.getInt("QtlRegionStart") == null || request.getInt("QtlRegionEnd") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a starting and ending point "
									+ "for your region search. An entire chromosome selection will result in to "
									+ "many hits, overloading your browser", false));
						}
						else
						{
							String dataset = request.getString("dataSetSelect");
							Integer start = request.getInt("QtlRegionStart");
							Integer end = request.getInt("QtlRegionEnd");
							Integer chromosome = request.getInt("QtlRegionChr");
							Integer threshold = request.getInt("QtlLodThreshold");

							List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR,
									Operator.LESS, chromosome));

							for (Chromosome chr : chrNeeded)
							{
								start = start + chr.getBpLength();
								end = end + chr.getBpLength();
							}

							DataMatrixHandler dmh = new DataMatrixHandler(db);
							Data selectDataset = db
									.find(Data.class, new QueryRule(Data.NAME, Operator.EQUALS, dataset)).get(0);

							DataMatrixInstance dataMatrix = dmh.createInstance(selectDataset, db);

							List<String> markers = selectDataset.getFeatureType().equals(Marker.class.getSimpleName()) ? dataMatrix
									.getColNames() : dataMatrix.getRowNames();

							Query<Marker> q = db.query(Marker.class);
							// Get markers used in dataset name
							q.addRules(new QueryRule(Marker.NAME, Operator.IN, markers));
							// Get markers in specific region
							q.addRules(new QueryRule(Marker.BPSTART, Operator.GREATER_EQUAL, start));
							q.addRules(new QueryRule(Marker.BPSTART, Operator.LESS_EQUAL, end));
							// Save markers selected from region
							List<Marker> regionMarkers = q.find();

							if (regionMarkers.size() == 0)
							{
								this.setMessages(new ScreenMessage("No markers where found within this region of "
										+ "chromosome " + chromosome, false));
							}
							else
							{
								// Get lowest and highest BP number
								Marker lowest = regionMarkers.get(0);
								Marker highest = regionMarkers.get(regionMarkers.size() - 1);
								for (Marker m : regionMarkers)
								{
									if (m.getBpStart().doubleValue() < lowest.getBpStart().doubleValue())
									{
										lowest = m;
									}
									else if (m.getBpStart().doubleValue() > highest.getBpStart().doubleValue())
									{
										highest = m;
									}
								}

								// Slice selected region from datamatrix
								if (selectDataset.getFeatureType().equals(Marker.class.getSimpleName()))
								{
									int colStart = dataMatrix.getColIndexForName(lowest.getName());
									int colStop = dataMatrix.getColIndexForName(highest.getName());

									// cut out slice with our flanking
									// markers(start, stop)
									DataMatrixInstance slice = dataMatrix.getSubMatrixByOffset(0,
											dataMatrix.getNumberOfRows(), colStart, colStop - colStart);

									// we want "1" value per row (trait)
									// with a value GREATER than THRESHOLD
									QueryRule findAboveThreshold = new QueryRule("1", Operator.GREATER, threshold);

									// apply filter and get result: number
									// of rows (traits) are now reduced

									DataMatrixInstance traitsAboveThreshold = slice
											.getSubMatrix2DFilterByRow(findAboveThreshold);

									List<String> rowNames = traitsAboveThreshold.getRowNames();

									Class<? extends Entity> traitClass = db.getClassForName(selectDataset
											.getTargetType());
									List<? extends Entity> traits = db.find(traitClass, new QueryRule(
											ObservationElement.NAME, Operator.IN, rowNames));

									String wbGene;

									for (Entity t : traits)
									{
										this.model.getHits().put(t.get(ObservationElement.NAME).toString(), t);

										if (t.get("symbol") == null)
										{
											continue;
										}

										wbGene = t.get("symbol").toString();

										List<String> myList = this.model.getHumanToWorm().linkToDisease(wbGene);

										this.model.getGeneAssociatedDiseases().put(myList.get(0),
												myList.subList(1, myList.size()));
									}

									this.model.setShowTable(true);
								}
								else
								{
									// TODO: Do something that is different
								}
							}
						}
					}

					/**
					 * @author Mark de Haan
					 * 
					 *         Disease Search
					 * 
					 *         User selects a disease from a dropdown list,
					 *         genes that are associated with selected disease
					 *         via ortholog matching are put in the shopping
					 *         cart
					 * 
					 *         TODO: Change the instant adding to shopping cart
					 *         into showing a list of hits to select from
					 */
					if (action.equals("diseaseSearch"))
					{
						this.model.setDisease(request.getString("diseaseSelect"));

						HypergeometricTest hg = new HypergeometricTest();

						int proteinCount = this.model.getHumanToWorm().retrieve(this.model.getDisease());
						int orthologSpecific = this.model.getHumanToWorm().getDiseaseToHuman()
								.get(this.model.getDisease()).size();

						this.model.setHyperTestProbability(hg.hyperGeometricTest(47361, 4988, proteinCount,
								orthologSpecific));

						// Call humanToWorm algorithm to convert disease
						// into a list of one or more worm genes
						List<String> wormGenes = this.model.getHumanToWorm().convert(this.model.getDisease());

						// Call the database with the list of worm genes to
						// get normal shopping cart view with probes to shop
						List<? extends Entity> probes = db.find(ObservationElement.class, new QueryRule(
								ObservationElement.NAME, Operator.IN, wormGenes));

						probes = db.load((Class) ObservationElement.class, probes);

						for (Entity p : probes)
						{
							this.model.getShoppingCart().put(p.get("name").toString(), p);
						}

						// Turn on the cart view
						this.model.setCartView(true);

						// Because the shoppingCart macro needs hits, return
						// a null map. Hits are not relevant for the current
						// search.
						this.model.setShoppingCart(genesToProbes(db, 100, this.model.getShoppingCart()));
					}

					/**
					 * @author Mark de Haan
					 * 
					 *         Ortholog Search
					 * 
					 *         User can submit human genes. The algorithm will
					 *         then determine the ortholog genes belonging to
					 *         these human genes.
					 * 
					 *         TODO: Greatly increase the input possibilities
					 */
					if (action.equals("humanGeneSearch"))
					{
						String[] humanGeneQuery = request.getString("enspIds").split(", ");

						List<String> enpsIDs = new ArrayList<String>(Arrays.asList(humanGeneQuery));
						this.model.setHumanGeneQuery(new ArrayList<String>());
						for (String enpsID : enpsIDs)
						{
							if (this.model.getHumanToWorm().getHumanToWorm().get(enpsID) == null)
							{
								continue;
							}

							this.model.getHumanGeneQuery()
									.add(this.model.getHumanToWorm().getHumanToWorm().get(enpsID));
						}

						List<Probe> probes = db.find(Probe.class,
								new QueryRule(Probe.SYMBOL, Operator.IN, this.model.getHumanGeneQuery()));

						for (Probe p : probes)
						{
							model.getHits().put(p.getName(), p);
						}
					}

					/**
					 * @author Joeri van der Velde
					 * 
					 *         Reset
					 * 
					 *         When the user presses the reset button,
					 *         everything is returned to its original state
					 * 
					 */
					if (action.equals("reset"))
					{
						this.model.setQuery(null);
						this.model.setHits(null);
						this.model.setShortenedQuery(null);
						this.model.setShoppingCart(null);
						this.model.setMultiplot(null);
						this.model.setReport(null);
						this.model.setQtls(null);
						this.model.setCartView(false);
						this.model.setProbeToGene(null);
						this.model.setShowTable(false);
					}
				}
			}

			catch (Exception e)
			{
				e.printStackTrace();
				this.setMessages(new ScreenMessage(e.getMessage() != null ? e.getMessage() : "null", false));
			}
		}
	}

	@Override
	public String getViewName()
	{
		return "QtlFinderHD";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/qtlfinder3/QtlFinderHD.ftl";
	}

	/**
	 * @author Joeri van der Velde, Mark de Haan
	 * 
	 *         Reload
	 * 
	 *         Initiates objects that are used by the freemarker template from
	 *         the start
	 */
	@Override
	public void reload(Database db)
	{
		try
		{

			/**
			 * @author Mark de Haan
			 * 
			 *         Generates a list of datasets that contain QTL lod scores.
			 *         Used by searching algorithms that set thresholds for QTL
			 *         values
			 */
			if (model.getDataSets() == null)
			{
				// give user dropdown of datasets that contain LOD scores
				List<DataValue> dvList = db.find(DataValue.class, new QueryRule(DataValue.DATANAME_NAME,
						Operator.EQUALS, "LOD_score"));

				List<String> dataNames = new ArrayList<String>();

				for (DataValue dv : dvList)
				{
					dataNames.add(dv.getValue_Name());
				}

				// list with datasets to be shown in dropdown menu
				this.model.setDataSets(dataNames);
			}

			/**
			 * @author Mark de Haan
			 * 
			 *         Pre-loads the hashmaps used by the HumanToWorm class by
			 *         reading in files
			 */
			if (model.getHumanToWorm() == null)
			{
				MolgenisFileHandler filehandle = new MolgenisFileHandler(db);
				File storage = filehandle.getFileStorage(true, db);

				// Format: disease \t ENSP ID
				File diseaseMap = new File(storage, "DGATransTable.csv");
				// Format: ENSP ID \t WBGene ID
				File orthologs = new File(storage, "speciesTransTable.csv");
				// Format: Disease \t Number of proteins associated
				File diseaseProteinCount = new File(storage, "diseaseProteinCount.csv");

				HumanToWorm h2w = new HumanToWorm(diseaseMap, orthologs, diseaseProteinCount);
				this.model.setHumanToWorm(h2w);

			}

			if (model.getHits() == null)
			{
				this.model.setHits(new HashMap<String, Entity>());
			}

			if (model.getProbeToGene() == null)
			{
				this.model.setProbeToGene(new HashMap<String, Gene>());
			}

			if (model.getShoppingCart() == null)
			{
				this.model.setShoppingCart(new HashMap<String, Entity>());
			}

			if (this.model.getCartView() == null)
			{
				this.model.setCartView(false);
			}

			if (this.model.getDisease() == null)
			{
				this.model.setDisease(this.model.getHumanToWorm().getDiseaseToHuman().keySet().iterator().next());
			}

			if (this.model.getDataSet() == null)
			{
				this.model.setDataSet(this.model.getDataSets().get(0));
			}

			if (this.model.getShowTable() == null)
			{
				this.model.setShowTable(false);
			}

			if (this.model.getGeneAssociatedDiseases() == null)
			{
				this.model.setGeneAssociatedDiseases(new LinkedHashMap<String, List<String>>());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.setMessages(new ScreenMessage(e.getMessage() != null ? e.getMessage() : "null", false));
		}
	}

	public String getCustomHtmlHeaders()
	{
		return super.getCustomHtmlHeaders();
	}

}
