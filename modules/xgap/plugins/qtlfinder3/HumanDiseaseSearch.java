package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.Entity;
import org.molgenis.wormqtl.etc.HypergeometricTest;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Probe;

public class HumanDiseaseSearch
{

	/**
	 * Disease Search
	 * 
	 * User selects a disease from a dropdown list, genes that are associated
	 * with selected disease via ortholog matching are put in the shopping cart
	 * 
	 * @author Mark de Haan
	 * @param model
	 * @param db
	 * @throws Exception
	 */
	public void diseaseSearch(QtlFinderHDModel model, Database db) throws Exception
	{
		model.setCartView(false);
		model.setShowResults(true);

		model.setHits(new HashMap<String, Entity>());
		model.setProbeToGene(new HashMap<String, Gene>());

		List<Probe> probesInRegion = new ArrayList<Probe>();
		HypergeometricTest hg = new HypergeometricTest();

		int proteinCount = model.getHumanToWorm().retrieve(model.getDisease());
		int orthologSpecific = model.getHumanToWorm().getDiseaseToHuman().get(model.getDisease()).size();

		model.setHyperTestProbability(hg.hyperGeometricTest(47361, 4988, proteinCount, orthologSpecific));

		// Call humanToWorm algorithm to convert disease
		// into a list of one or more worm genes
		List<String> wormGenes = model.getHumanToWorm().convert(model.getDisease());

		// Call the database with the list of worm genes to
		// get normal shopping cart view with probes to shop
		probesInRegion = db.find(Probe.class, new QueryRule(Probe.SYMBOL, Operator.IN, wormGenes));
		probesInRegion.addAll(db.find(Probe.class, new QueryRule(Probe.REPORTSFOR_NAME, Operator.IN, wormGenes)));

		for (Probe p : probesInRegion)
		{
			model.getHits().put(p.getName(), p);
		}

		model.setShoppingCart(model.getHits());
	}
}
