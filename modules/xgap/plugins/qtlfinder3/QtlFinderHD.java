/**
 * 
 */
package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.framework.db.Database;
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

					if (action.equals("regionSearch"))
					{
						List<Probe> probesInRegion = new ArrayList<Probe>();

						if (request.getString("regionStart") == null || request.getString("regionEnd") == null)
						{
							this.setMessages(new ScreenMessage("Please fill in a starting and ending point "
									+ "for your region search. An entire chromosome selection will result in to "
									+ "many hits, overloading your browser", false));
						}
						else
						{
							this.model.setRegionStartLocation(Integer.valueOf(request.getString("regionStart")));
							this.model.setRegionEndLocation(Integer.valueOf(request.getString("regionEnd")));
							this.model.setRegionChromosome(Integer.valueOf(request.getString("regionChr")));

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

				}

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
