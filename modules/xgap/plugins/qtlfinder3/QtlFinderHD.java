/**
 * 
 */
package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder2.QtlFinder2;
import plugins.qtlfinder3.methods.ComparePhenotypes;
import plugins.qtlfinder3.methods.SearchFunctions;
import plugins.qtlfinder3.methods.TraitRegionSearch;

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

	private QtlFinderHDModel model;

	public QtlFinderHDModel getMyModel()
	{
		return model;
	}

	@Override
	public void handleRequest(Database db, MolgenisRequest request)
	{
		initIfNeeded(db);

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
						this.model.setShowAnyResultToUser("show");
						this.model.setShowResults(true);
						this.model.setCartView(false);

						List<String> diseases = request.getList("diseaseSelect");
						this.model.getDiseaseSearchInputState().setSelectedDiseases(diseases);

						model.setHits(new HashMap<String, Entity>());
						model.setProbeToGene(new HashMap<String, Gene>());

						List<Probe> hits = SearchFunctions.diseaseSearch(db, this.model.getDiseaseMapping(), diseases,
								this.model.getHumanToWorm());

						for (Probe p : hits)
						{
							model.getHits().put(p.getName(), p);
						}
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
							this.model.setShowAnyResultToUser("show");
							this.model.setShowResults(true);

							Integer start = request.getInt("regionStart");
							Integer end = request.getInt("regionEnd");
							Integer chromosomeOrderNr = this.model.getRegionSearchInputState().getChromosomes()
									.get(request.getString("regionChr")).getOrderNr();

							SetRegion sr = new SetRegion();
							sr.setRegion(start, end, chromosomeOrderNr, db, true, model);
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
							this.model.setShowAnyResultToUser("show");
							this.model.setShowResults(true);
							this.model.setCartView(false);

							String dataset = request.getString("dataSetSelect");
							Integer start = request.getInt("QtlRegionStart");
							Integer end = request.getInt("QtlRegionEnd");
							Integer chromosome = request.getInt("QtlRegionChr");
							Integer threshold = request.getInt("QtlLodThreshold");

							// QtlSearch qs = new QtlSearch();
							List<? extends Entity> e = SearchFunctions.qtlSearch(dataset, start, end, chromosome,
									threshold, db);

							for (Entity t : e)
							{
								this.model.getHits().put(t.get(ObservationElement.NAME).toString(), t);
							}
							model.setShowResults(true);

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
							this.model.setShowAnyResultToUser("show");
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
						this.model.setShowAnyResultToUser("show");

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

						model.setHits(new HashMap<String, Entity>());
						model.setProbeToGene(new HashMap<String, Gene>());

						model.setHumanGeneQuery(new ArrayList<String>());

						List<Probe> probes = SearchFunctions.orthologSearch(humanGeneQuery,
								this.model.getHumanToWorm(), db);

						for (Probe p : probes)
						{
							model.getHits().put(p.getName(), p);
						}
					}

					if (action.equals("regionChrChange"))
					{
						String selectedChromosome = request.getString("regionChr");
						this.model.getRegionSearchInputState().setSelectedChromosome(selectedChromosome);
						Chromosome selectedChr = this.model.getRegionSearchInputState().getChromosomes()
								.get(selectedChromosome);
						int startBp, endBp;
						if (selectedChr.getBpLength() == null)
						{
							startBp = 0;
							endBp = 0;
						}
						else
						{
							startBp = (int) (((double) selectedChr.getBpLength()) / 10.0);
							endBp = (int) (((double) selectedChr.getBpLength()) / 6.0);
						}

						this.model.getRegionSearchInputState().setSelectedStartBp(startBp);
						this.model.getRegionSearchInputState().setSelectedEndBp(endBp);
					}

					// Change disease mapping by reloading
					if (action.equals("mappingChange"))
					{
						this.model.setShowAnyResultToUser(null);

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
						System.out.println("Go change the bloody search, and set stuff"
								+ this.model.getShowAnyResultToUser());

						this.model.setShowAnyResultToUser(null);

						System.out.println("Changed it bloody, and set stuff" + this.model.getShowAnyResultToUser());
					}

					// Reset
					if (action.equals("reset"))
					{

						// reset region search
						InitQtlFinderHDModel.freshRegionSearch(this.model, db);

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
						this.model.getDiseaseSearchInputState().setSelectedDiseases(null);
						this.model.setShowAnyResultToUser(null);
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
	 * init model and handle errors TODO: give some kind of error screen when
	 * this goes wrong instead of 'white screen of death' with only stacktrace
	 * error
	 * 
	 * @param db
	 */
	public void initIfNeeded(Database db)
	{
		if (this.model == null)
		{
			try
			{
				this.model = InitQtlFinderHDModel.init(db);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.setMessages(new ScreenMessage(e.getMessage() != null ? e.getMessage() : "null", false));
			}
		}
	}

	@Override
	public void reload(Database db)
	{
		initIfNeeded(db);
	}

	public String getCustomHtmlHeaders()
	{
		return super.getCustomHtmlHeaders();
	}
}
