/**
 * 
 */
package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import plugins.qtlfinder3.methods.ComparePhenotypesResult;
import plugins.qtlfinder3.methods.SearchFunctions;
import plugins.qtlfinder3.resources.HumanToWorm;

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
			System.out.println("Screen change imminent! " + request.getString("screen"));

			if (this.model.getHits() != null)
			{
				// Save hits before going to another search function
				if (this.model.getScreenType().equals("humanDisease"))

				{
					this.model.getDiseaseSearchResults().setDiseaseSearchHits(this.model.getHits());
					this.model.setHits(null);
				}

				else if (this.model.getScreenType().equals("genomicRegion"))
				{
					this.model.getRegionSearchResults().setRegionSearchHits(this.model.getHits());
					this.model.setHits(null);
				}
				else if (this.model.getScreenType().equals("qtlLoci"))
				{
					this.model.getQtlSearchResults().setQtlSearchHits(this.model.getHits());
					this.model.setHits(null);
				}
				else
				{
					// TODO: nothing???
				}
			}

			// When the screen changes, disease selection is removed
			if (this.model.getScreenType().equals(request.getString("screen")))
			{
				// TODO: nothing???
			}
			else
			{
				this.model.getDiseaseSearchInputState().setSelectedDiseases(null);
			}

			this.model.setScreenType(request.getString("screen"));

			// Load the correct hits when a certain search function is selected
			if (this.model.getScreenType().equals("humanDisease"))
			{
				if (this.model.getDiseaseSearchResults().getDiseaseSearchHits() != null)
				{
					this.model.setHits(this.model.getDiseaseSearchResults().getDiseaseSearchHits());
				}
			}
			else if (this.model.getScreenType().equals("genomicRegion"))
			{
				if (this.model.getRegionSearchResults().getRegionSearchHits() != null)
				{
					this.model.setHits(this.model.getRegionSearchResults().getRegionSearchHits());
				}
			}
			else if (this.model.getScreenType().equals("qtlLoci"))
			{
				if (this.model.getQtlSearchResults().getQtlSearchHits() != null)
				{
					this.model.setHits(this.model.getQtlSearchResults().getQtlSearchHits());
				}
			}
			else
			{
				// TODO: nothing???
			}

			this.model.setCartView(false);
			this.model.setShowResults(false);
		}

		if (request.getString("__action") != null)
		{
			String action = request.getString("__action");

			System.out.println("ACTION: " + action);

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
						List<String> diseases = request.getList("diseaseSelect");
						this.model.getDiseaseSearchInputState().setSelectedDiseases(diseases);

						if (diseases.size() > 25)
						{
							this.setMessages(new ScreenMessage("You selected " + diseases.size()
									+ " diseases. There is a limit of 15. " + "Please narrow down your search.", false));
						}
						else
						{
							this.model.setShowAnyResultToUser("show");
							this.model.setShowResults(true);
							this.model.setCartView(false);

							this.model.setHits(new HashMap<String, Entity>());
							this.model.setProbeToGene(new HashMap<String, Gene>());

							List<Probe> hits = SearchFunctions.diseaseSearch(db, this.model.getDiseaseMapping(),
									diseases, this.model.getHumanToWorm());

							for (Probe p : hits)
							{
								this.model.getHits().put(p.getName(), p);
							}
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

						else if (request.getString("regionStart").contains(" ")
								|| request.getString("regionEnd").contains(" "))
						{
							this.setMessages(new ScreenMessage("Make sure there are no spaces or tabs in your input",
									false));

						}
						else if (request.getString("regionStart").contains("\\w")
								|| request.getString("regionEnd").contains("\\w"))
						{
							this.setMessages(new ScreenMessage("Please fill in numbers, not letters", false));
						}

						else
						{
							this.model.setShowAnyResultToUser("show");
							this.model.setShowResults(true);

							Integer start = request.getInt("regionStart");
							Integer end = request.getInt("regionEnd");
							Integer chromosomeOrderNr = this.model.getRegionSearchInputState().getChromosomes()
									.get(request.getString("regionChr")).getOrderNr();

							this.model.getRegionSearchInputState().setSelectedStartBp(start);
							this.model.getRegionSearchInputState().setSelectedEndBp(end);

							model.setHits(new HashMap<String, Entity>());
							model.setProbeToGene(new HashMap<String, Gene>());

							List<Probe> probesInRegion = SearchFunctions.regionSearch(start, end, chromosomeOrderNr,
									db, true);

							for (Probe p : probesInRegion)
							{
								this.model.getHits().put(p.getName(), p);
							}
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
						else if (request.getString("QtlRegionStart").contains(" ")
								|| request.getString("QtlRegionEnd").contains(" "))
						{
							this.setMessages(new ScreenMessage("Make sure there are no spaces or tabs in your input",
									false));

						}
						else if (request.getString("QtlRegionStart").contains("\\w")
								|| request.getString("QtlRegionEnd").contains("\\w"))
						{
							this.setMessages(new ScreenMessage("Please fill in numbers, not letters", false));
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
							this.model.setShowResults(true);

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

							this.model.getQtlSearchInputState().setTraitInput(request.getString("traitInput"));
							this.model.getQtlSearchInputState().setSelectedDataSet(
									request.getString("regionDataSetSelect"));

							this.model.getQtlSearchInputState().setLodThreshold(request.getInt("lodThreshold"));

							this.model.setHits(new HashMap<String, Entity>());
							this.model.setProbeToGene(new HashMap<String, Gene>());
							List<Probe> probesInQtlRegion = SearchFunctions.qtlRegionSearch(this.model
									.getQtlSearchInputState().getTraitInput(), this.model.getQtlSearchInputState()
									.getSelectedDataSet(), this.model.getQtlSearchInputState().getLodThreshold(), db);

							for (Probe p : probesInQtlRegion)
							{
								this.model.getHits().put(p.getName(), p);
							}
						}
					}

					if (action.equals("plotOverlap"))
					{
						if (this.model.getShoppingCart().size() < 1)
						{
							this.setMessages(new ScreenMessage("You cannot plot a cart that is empty", false));
						}

						else
						{
							this.model.setCartView(false);
							this.model.setShowResults(false);

							List<Entity> cart = new ArrayList<Entity>(this.model.getShoppingCart().values());
							ComparePhenotypesResult res = ComparePhenotypes.compareGenesWorm(model.getHumanToWorm(),
									cart);

							if (this.model.getScreenType().equals("genomicRegion"))
							{
								res.setSampleInputs(new HashSet<String>(Arrays.asList(new String[]
								{ "chr" + this.model.getRegionSearchInputState().getSelectedChromosome() + ":"
										+ this.model.getRegionSearchInputState().getSelectedStartBp() + "-"
										+ this.model.getRegionSearchInputState().getSelectedEndBp() })));
								res.setSampleSource("Region search");
								this.model.getRegionSearchResults().setResults(res);

							}
							else if (this.model.getScreenType().equals("qtlLoci"))
							{
								res.setSampleInputs(new HashSet<String>(Arrays.asList(new String[]
								{ "todo" })));
								res.setSampleSource("QTL search");
								this.model.getQtlSearchResults().setResults(res);
							}

						}
					}

					// Phenotype comparison with worm list selection
					if (action.equals("comparePhenotypes"))
					{
						Set<String> phenoDiseases = new HashSet<String>(request.getList("comparePheno"));

						this.model.setShowAnyResultToUser("show");

						ComparePhenotypesResult res = null;

						if (this.model.getHumanToWorm().humanSourceNames().contains(this.model.getDiseaseMapping()))
						{
							res = ComparePhenotypes.comparePhenotypesHuman(this.model.getHumanToWorm(),
									this.model.getDiseaseMapping(), phenoDiseases);
						}
						else if (this.model.getHumanToWorm().wormSourceNames().contains(this.model.getDiseaseMapping()))
						{
							res = ComparePhenotypes.comparePhenotypesWorm(this.model.getHumanToWorm(),
									this.model.getDiseaseMapping(), phenoDiseases);
						}
						else
						{
							throw new Exception("Source unknown: " + this.model.getDiseaseMapping());
						}

						res.setSampleInputs(phenoDiseases);
						res.setSampleSource(this.model.getDiseaseMapping());
						this.model.getPhenoCompareResults().setResults(res);
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

					if (action.equals("changeDataset"))
					{
						this.model.getQtlSearchInputState()
								.setSelectedDataSet(request.getString("regionDataSetSelect"));
					}

					// loads example per search function for reviewer to use
					if (action.equals("loadExample"))
					{
						if (this.model.getScreenType().equals("humanDisease"))
						{
							System.out.println("Set example " + this.model.getScreenType());

							this.model.setDiseaseMapping("OMIM");
							List<String> diseases = new ArrayList<String>();

							// Disease(s) to load into input box as example
							diseases.add("Glutamine deficiency, congenital, 610015 (3)");

							this.model.getDiseaseSearchInputState().setSelectedDiseases(diseases);
						}

						if (this.model.getScreenType().equals("comparePhenotypes"))
						{
							System.out.println("Set example " + this.model.getScreenType());

							this.model.setDiseaseMapping("OMIM");
							List<String> phenoDiseases = new ArrayList<String>();

							// Disease(s) to load into input box as example
							phenoDiseases.add("Zellweger syndrome, 214100 (3)");

							this.model.getDiseaseSearchInputState().setSelectedDiseases(phenoDiseases);
						}

						if (this.model.getScreenType().equals("qtlLoci"))
						{
							System.out.println("Set example " + this.model.getScreenType());

							this.model.getQtlSearchInputState().setSelectedDataSet("rock_qtl");
							this.model.getQtlSearchInputState().setTraitInput("AGIUSA19536");
							this.model.getQtlSearchInputState().setLodThreshold(Integer.parseInt("50"));
						}

						if (this.model.getScreenType().equals("genomicRegion"))
						{
							System.out.println("Set example " + this.model.getScreenType());

							this.model.getRegionSearchInputState().setSelectedStartBp(Integer.parseInt("15430739"));
							this.model.getRegionSearchInputState().setSelectedEndBp(Integer.parseInt("16430739"));
							this.model.getRegionSearchInputState().setSelectedChromosome("V");

							System.out.println(this.model.getRegionSearchInputState().getSelectedChromosome() + " "
									+ this.model.getRegionSearchInputState().getSelectedEndBp() + " "
									+ this.model.getRegionSearchInputState().getSelectedStartBp());
						}

					}

					// Reset
					if (action.equals("reset"))
					{
						System.out.println("Go go reset!");
						// reset region search
						InitQtlFinderHDModel.freshRegionSearch(this.model, db);

						this.model.setShowResults(false);
						this.model.setQuery(null);
						this.model.setHits(null);
						this.model.setShortenedQuery(null);
						this.model.setShoppingCart(new HashMap<String, Entity>());
						this.model.setMultiplot(null);
						this.model.setReport(null);
						this.model.setQtls(null);
						this.model.setCartView(false);
						this.model.setProbeToGene(null);
						this.model.setShowResults(false);
						this.model.getDiseaseSearchInputState().setSelectedDiseases(null);
						this.model.getDiseaseSearchResults().setDiseaseSearchHits(null);
						this.model.setShowAnyResultToUser(null);

						if (this.model.getScreenType().equals("comparePhenotypes"))
						{
							this.model.getPhenoCompareResults().setResults(null);
						}

						if (this.model.getScreenType().equals("qtlLoci"))
						{
							this.model.getQtlSearchResults().setResults(null);
						}

						if (this.model.getScreenType().equals("genomicRegion"))
						{
							this.model.getRegionSearchResults().setResults(null);
						}

					}

					if (action.equals("resetAll"))
					{
						System.out.println("Go go reset all!");

						// reset region search
						InitQtlFinderHDModel.freshRegionSearch(this.model, db);

						this.model.setShowResults(false);
						this.model.setQuery(null);
						this.model.setHits(null);
						this.model.setShortenedQuery(null);
						this.model.setShoppingCart(new HashMap<String, Entity>());
						this.model.setMultiplot(null);
						this.model.setReport(null);
						this.model.setQtls(null);
						this.model.setCartView(false);
						this.model.setProbeToGene(null);
						this.model.setShowResults(false);
						this.model.getDiseaseSearchInputState().setSelectedDiseases(null);
						this.model.getDiseaseSearchResults().setDiseaseSearchHits(null);
						this.model.setShowAnyResultToUser(null);
						this.model.getPhenoCompareResults().setResults(null);
						this.model.getRegionSearchResults().setResults(null);
						this.model.getQtlSearchResults().setResults(null);
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
				HumanToWorm h2w = (HumanToWorm) this.getApplicationController().getMolgenisContext()
						.getServletContext().getAttribute("humantoworm");
				this.model = InitQtlFinderHDModel.init(db, h2w);
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
