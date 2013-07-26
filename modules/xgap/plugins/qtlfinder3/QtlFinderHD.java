/**
 * 
 */
package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.molgenis.cluster.DataValue;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.util.Entity;
import org.molgenis.wormqtl.etc.GeneMappingDataSource;
import org.molgenis.wormqtl.etc.HumanToWorm2;

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
						this.model.setShowAnyResultToUser(true);
						this.model.setShowResults(true);
						this.model.setCartView(false);

						List<String> disease = request.getList("diseaseSelect");
						this.model.setDiseases(disease);
						HumanDiseaseSearch hds = new HumanDiseaseSearch();
						hds.diseaseSearch(model, db);
					}

					// Region search
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
							this.model.setShowAnyResultToUser(true);
							this.model.setShowResults(true);

							Integer start = request.getInt("regionStart");
							Integer end = request.getInt("regionEnd");
							Integer chromosome = request.getInt("regionChr");

							SetRegion sr = new SetRegion();
							sr.setRegion(start, end, chromosome, db, true, model);
						}
					}

					// QTL search
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
							this.model.setShowAnyResultToUser(true);
							this.model.setShowResults(true);
							this.model.setCartView(false);

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
						if (request.getString("traitInput") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a trait in the form"
									+ "of a probe name.", false));
						}
						else
						{
							this.model.setShowAnyResultToUser(true);
							this.model.setShowResults(true);
							this.model.setCartView(false);

							String trait = request.getString("traitInput");
							String dataset = request.getString("regionDataSetSelect");
							double threshold = request.getInt("lodThreshold");

							TraitRegionSearch trs = new TraitRegionSearch();

							trs.traitRegionSearch(trait, dataset, threshold, model, db, this.getModel());
						}
					}

					if (action.equals("plotOverlap"))
					{
						this.model.setCartView(false);
						this.model.setShowResults(false);

						List<Entity> cart = new ArrayList<Entity>(this.model.getShoppingCart().values());
						new ComparePhenotypes().compareGenesWorm(model, this.getModel(), cart);
					}

					// Phenotype comparison with worm list selection
					if (action.equals("comparePhenotypes"))
					{
						this.model.setShowAnyResultToUser(true);

						List<String> phenoDiseases = request.getList("comparePheno");

						ComparePhenotypes cp = new ComparePhenotypes();

						if (this.model.getHumanToWorm().humanSourceNames().contains(this.model.getDiseaseMapping()))
						{
							cp.comparePhenotypesHuman(model, this.getModel(), phenoDiseases);
						}
						else
						{
							cp.comparePhenotypesWorm(model, this.getModel(), phenoDiseases);
						}
					}

					// Ortholog Search
					if (action.equals("humanGeneSearch"))
					{
						String[] humanGeneQuery = request.getString("enspIds").split(", ");
						if (humanGeneQuery.length == 0)
						{
							this.setMessages(new ScreenMessage(
									"Please fill in at least one human ENSP protein identifier", false));
						}

						OrthologSearch os = new OrthologSearch();
						os.orthologSearch(humanGeneQuery, model, db);
					}

					// Change disease mapping by reloading
					if (action.equals("mappingChange"))
					{
						this.model.setShowAnyResultToUser(false);

						String diseaseMapping = request.getString("diseaseMapping");

						if (diseaseMapping.equals(this.model.getDiseaseMapping()))
						{
							this.setMessages(new ScreenMessage("Already selected: '" + diseaseMapping + "'.", true));
						}
						else
						{

							this.model.setDiseaseMapping(diseaseMapping);
							this.setMessages(new ScreenMessage("Selected '" + diseaseMapping + "'.", true));
						}
					}

					if (action.equals("searchChange"))
					{
						this.model.setShowAnyResultToUser(false);
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
						this.model.setShowResults(false);
						this.model.setAllOverlaps(null);
						this.model.setDiseases(null);
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
			 * Pre-loads the hashmaps used by the HumanToWorm class by reading
			 * in files
			 * 
			 * @author Mark de Haans
			 */
			if (model.getHumanToWorm() == null)
			{
				MolgenisFileHandler filehandle = new MolgenisFileHandler(db);
				File storage = filehandle.getFileStorage(true, db);

				GeneMappingDataSource omim = new GeneMappingDataSource(new File(storage, "human_disease_OMIM.csv"),
						"OMIM");
				GeneMappingDataSource dga = new GeneMappingDataSource(new File(storage, "human_disease_DGA.csv"), "DGA");

				GeneMappingDataSource gwascentral = new GeneMappingDataSource(new File(storage,
						"human_disease_GWASCENTRAL.csv"), "GWAS Central");
				GeneMappingDataSource gwascatalog = new GeneMappingDataSource(new File(storage,
						"human_disease_GWASCATALOG.csv"), "GWAS Catalog");
				GeneMappingDataSource wormPheno = new GeneMappingDataSource(new File(storage, "worm_disease.csv"),
						"WormBase");
				GeneMappingDataSource humanToWorm = new GeneMappingDataSource(new File(storage, "orthologs.csv"),
						"INPARANOID");

				List<GeneMappingDataSource> humanSources = new ArrayList<GeneMappingDataSource>();
				humanSources.add(omim);
				humanSources.add(dga);
				humanSources.add(gwascentral);
				humanSources.add(gwascatalog);

				List<GeneMappingDataSource> wormSources = new ArrayList<GeneMappingDataSource>();
				wormSources.add(wormPheno);

				HumanToWorm2 h2w2 = new HumanToWorm2(humanSources, wormSources, humanToWorm, db);

				this.model.setHumanToWorm(h2w2);

			}

			if (this.model.getShowAnyResultToUser() == null)
			{
				this.model.setShowAnyResultToUser(false);
			}

			if (this.model.getDiseaseMapping() == null)
			{
				this.model.setDiseaseMapping(this.model.getHumanToWorm().humanSourceNames().toArray()[0].toString());
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

			if (this.model.getScreenType() == null || this.model.getScreenType() == "")
			{
				this.model.setScreenType("humanDisease");
			}

			if (this.model.getCartView() == null)
			{
				this.model.setCartView(false);
			}

			// if (this.model.getPhenotypeMapping() == null)
			// {
			// at the moment, there is only one! therefore we don't show it
			// in the GUI but code it here to keep the rest of the code
			// consistent
			// TODO

			// this.model.setPhenotypeMapping("WormBase");
			// }
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
