/**
 * 
 */
package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Marker;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder2.QtlFinder2;
import decorators.MolgenisFileHandler;

/**
 * @author mark
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

					// //////////////
					// REGION SEARCH
					// //////////////
					if (action.equals("regionSearch"))
					{
						List<Probe> probesInRegion = new ArrayList<Probe>();

						if (request.getInt("regionStart") == null || request.getInt("regionEnd") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a starting and ending point "
									+ "for your region search. An entire chromosome selection will result in to "
									+ "many hits, overloading your browser", false));
						}
						else
						{
							this.model.setRegionStartLocation(request.getInt("regionStart"));
							this.model.setRegionEndLocation(request.getInt("regionEnd"));
							this.model.setRegionChromosome(request.getInt("regionChr"));

							Integer start = this.model.getRegionStartLocation();
							Integer end = this.model.getRegionEndLocation();

							List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR,
									Operator.LESS, this.model.getRegionChromosome()));

							for (Chromosome chr : chrNeeded)
							{
								start = start + chr.getBpLength();
								end = end + chr.getBpLength();
							}

							probesInRegion = db.find(Probe.class,
									new QueryRule(Probe.BPSTART, Operator.GREATER, start), new QueryRule(Probe.BPSTART,
											Operator.LESS, end));
						}

						this.model.setProbeToGene(new HashMap<String, Gene>());
						this.model.setHits(new HashMap<String, Entity>());

						for (Probe p : probesInRegion)
						{
							model.getHits().put(p.getName(), p);
						}
					}

					// //////////////
					// DISEASE SEARCH
					// //////////////
					if (action.equals("shop"))
					{
						this.model.setDisease(request.getString("diseaseSelect"));

						// Call humanToWorm algorithm to convert disease into a
						// list
						// of one or more worm genes
						List<String> wormGenes = this.model.getHumanToWorm().convert(this.model.getDisease());

						// Call the database with the list of worm genes to get
						// normal shopping cart view with probes to shop
						List<? extends Entity> probes = db.find(ObservationElement.class, new QueryRule(
								ObservationElement.NAME, Operator.IN, wormGenes));

						probes = db.load((Class) ObservationElement.class, probes);

						for (Entity p : probes)
						{
							this.model.getShoppingCart().put(p.get("name").toString(), p);
						}

						// Turn on the cart view
						this.model.setCartView(true);

						// Because the shoppingCart macro needs hits, return a
						// null
						// map. Hits are not relevant for the current search.
						this.model.setHits(new HashMap<String, Entity>());
						this.model.setShoppingCart(genesToProbes(db, 100, this.model.getShoppingCart()));
						this.model.setProbeToGene(new HashMap<String, Gene>());

					}

					// /////////////////////
					// REGION QTL SEARCH
					// /////////////////////
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
							this.model.setDataSet(request.getString("dataSetSelect"));
							this.model.setQtlRegionStartLocation(request.getInt("QtlRegionStart"));
							this.model.setQtlRegionEndLocation(request.getInt("QtlRegionEnd"));
							this.model.setQtlRegionChromosome(request.getInt("QtlRegionChr"));
							this.model.setLodThreshold(Double.valueOf(request.getInt("lodThreshold")));

							Integer start = this.model.getQtlRegionStartLocation();
							Integer end = this.model.getQtlRegionEndLocation();

							List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR,
									Operator.LESS, this.model.getQtlRegionChromosome()));

							for (Chromosome chr : chrNeeded)
							{
								start = start + chr.getBpLength();
								end = end + chr.getBpLength();
							}

							DataMatrixHandler dmh = new DataMatrixHandler(db);
							Data selectDataset = db.find(Data.class,
									new QueryRule(Data.NAME, Operator.EQUALS, this.model.getDataSet())).get(0);

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
										+ "chromosome " + this.model.getQtlRegionChromosome(), false));
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

									// cut out slice with our flanking markers
									// (start,
									// stop)
									DataMatrixInstance slice = dataMatrix.getSubMatrixByOffset(0,
											dataMatrix.getNumberOfRows(), colStart, colStop - colStart);

									// we want "1" value per row (trait) with a
									// value
									// GREATER than
									// THRESHOLD
									QueryRule findAboveThreshold = new QueryRule("1", Operator.GREATER,
											this.model.getLodThreshold());

									// apply filter and get result: number of
									// rows
									// (traits)
									// are now
									// reduced
									DataMatrixInstance traitsAboveThreshold = slice
											.getSubMatrix2DFilterByRow(findAboveThreshold);

									List<String> rowNames = traitsAboveThreshold.getRowNames();

									Class<? extends Entity> traitClass = db.getClassForName(selectDataset
											.getTargetType());
									List<? extends Entity> traits = db.find(traitClass, new QueryRule(
											ObservationElement.NAME, Operator.IN, rowNames));

									HashMap<String, Entity> hits = new HashMap<String, Entity>();
									for (Entity t : traits)
									{
										hits.put(t.get(ObservationElement.NAME).toString(), t);
									}

									this.model.setHits(hits);
									this.model.setProbeToGene(new HashMap<String, Gene>());

								}
								else
								{

								}
							}
						}
					}
					// //////
					// RESET
					// //////
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

	@Override
	public void reload(Database db)
	{

		if (model.getSelectedSearch() == null)
		{
			this.model.setSelectedSearch("diseaseToWorm");
		}

		if (model.getShoppingCart() == null)
		{
			Map<String, Entity> shoppingCart = new HashMap<String, Entity>();
			this.model.setShoppingCart(shoppingCart);
		}

		if (this.model.getCartView() == null)
		{
			this.model.setCartView(false);
		}

		try
		{
			if (model.getDataSets() == null)
			{
				// give user dropdown of datasets with LOD scores
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

			if (model.getHumanToWorm() == null)
			{
				MolgenisFileHandler filehandle = new MolgenisFileHandler(db);
				File storage = filehandle.getFileStorage(true, db);

				// Format: "disease","ENSP ID"
				File omimMap = new File(storage, "omimTransTable.csv");
				// Format: ENSP ID \t WBGene ID
				File orthologs = new File(storage, "speciesTransTable.csv");

				HumanToWorm h2w = new HumanToWorm(omimMap, orthologs);
				this.model.setHumanToWorm(h2w);

			}

			if (this.model.getDisease() == null)
			{
				this.model.setDisease(this.model.getHumanToWorm().getDiseaseToHuman().keySet().iterator().next());
			}

			if (this.model.getDataSet() == null)
			{
				this.model.setDataSet(this.model.getDataSets().get(0));
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
