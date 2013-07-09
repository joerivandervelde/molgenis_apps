/**
 * 
 */
package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.molgenis.cluster.DataValue;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.util.Entity;
import org.molgenis.wormqtl.etc.HumanToWorm;

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
	}

	private QtlFinderHDModel model = new QtlFinderHDModel();

	public QtlFinderHDModel getMyModel()
	{
		return model;
	}

	@Override
	public void handleRequest(Database db, MolgenisRequest request)
	{
		if (request.getString("screen") != null)
		{
			this.model.setScreenType(request.getString("screen"));
			this.model.setCartView(false);
			this.model.setShowResults(false);
		}
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

					// Human Disease search
					if (action.equals("diseaseSearch"))
					{
						this.model.setDisease(request.getString("diseaseSelect"));
						super.model.setScreenType("");

						HumanDiseaseSearch hds = new HumanDiseaseSearch();
						hds.diseaseSearch(model, db);
					}

					// Region search
					if (action.equals("regionSearch"))
					{
						super.model.setScreenType("");
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

							SetRegion sr = new SetRegion();
							sr.setRegion(start, end, chromosome, db, 1, model);

						}
					}

					// QTL search
					if (action.equals("QtlSearch"))
					{
						super.model.setScreenType("");
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

							QtlSearch qs = new QtlSearch();
							qs.qtlSearch(dataset, start, end, chromosome, threshold, model, db, this.getModel());
						}
					}

					// QTL search per probe
					if (action.equals("traitRegionSearch"))
					{
						super.model.setScreenType("");
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

							TraitRegionSearch trs = new TraitRegionSearch();

							trs.traitRegionSearch(trait, dataset, threshold, model, db, this.getModel());
						}
					}

					// Phenotype comparison with worm list selection
					if (action.equals("comparePhenotypesWorm"))
					{
						this.model.setSelectedWormPhenotype(request.getString("wormPhenotype"));

						ComparePhenotypes cp = new ComparePhenotypes();
						cp.comparePhenotypesWorm(model, this.getModel(), this.model.getSelectedWormPhenotype());
						this.model.setShowWorm(true);
					}

					// Phenotype comparison with human list selection
					if (action.equals("comparePhenotypesHuman"))
					{
						this.model.setSelectedHumanPhenotype(request.getString("humanPhenotype"));

						ComparePhenotypes cp = new ComparePhenotypes();
						cp.comparePhenotypesHuman(model, this.getModel(), this.model.getSelectedHumanPhenotype());
						this.model.setShowWorm(false);
					}

					// Ortholog Search
					if (action.equals("humanGeneSearch"))
					{
						super.model.setScreenType("");
						String[] humanGeneQuery = request.getString("enspIds").split(", ");
						if (humanGeneQuery.length == 0)
						{
							this.setMessages(new ScreenMessage(
									"Please fill in at least one human ENSP protein identifier", false));
						}

						OrthologSearch os = new OrthologSearch();
						os.orthologSearch(humanGeneQuery, model, db);
					}

					if (action.equals("plotOverlap"))
					{
						PlotOverlap po = new PlotOverlap();
						po.plotOverlap(model, db);

						this.model.setScreenType("showOverlapTable");

					}

					// Reset
					if (action.equals("reset"))
					{
						this.model.setShowResults(false);
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
						this.model.setShowResults(false);
						this.model.setAllOverlaps(null);
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
				// Format: Worm gene \t Worm phenotype
				File wormToPhenotype = new File(storage, "classicalWormPhenotypes.csv");

				HumanToWorm h2w = new HumanToWorm(diseaseMap, orthologs, diseaseProteinCount, wormToPhenotype);
				this.model.setHumanToWorm(h2w);

			}

			if (this.model.getDataSet() == null)
			{
				this.model.setDataSet(this.model.getDataSets().get(0));
			}

			if (model.getShoppingCart() == null)
			{
				this.model.setShoppingCart(new HashMap<String, Entity>());
			}

			if (this.model.getShowResults() == null)
			{
				this.model.setShowResults(false);
			}

			if (this.model.getDisease() == null)
			{
				this.model.setDisease(this.model.getHumanToWorm().getDiseaseToHuman().keySet().iterator().next());
			}

			if (this.model.getShowTable() == null)
			{
				this.model.setShowTable(false);
			}

			if (this.model.getGeneAssociatedDiseases() == null)
			{
				this.model.setGeneAssociatedDiseases(new LinkedHashMap<String, List<String>>());
			}

			if (this.model.getScreenType() == null || this.model.getScreenType() == "")
			{
				this.model.setScreenType("humanDisease");
			}

			if (this.model.getSelectedWormPhenotype() == null)
			{
				this.model.setSelectedWormPhenotype(this.model.getHumanToWorm().getWormToPhenotype().keySet()
						.iterator().next());
			}

			if (this.model.getSelectedHumanPhenotype() == null)
			{
				this.model.setSelectedHumanPhenotype(this.model.getHumanToWorm().getDiseaseToHuman().keySet()
						.iterator().next());
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
