package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Probe;

public class OrthologSearch
{

	/**
	 * Ortholog Search
	 * 
	 * User can submit human genes. The algorithm will then determine the
	 * ortholog genes belonging to these human genes.
	 * 
	 * TODO: Greatly increase the input possibilities
	 * 
	 * @author Mark de Haan
	 * @param db
	 * @param model
	 * @param humanGeneQuery
	 */

	public void orthologSearch(String[] humanGeneQuery, QtlFinderHDModel model, Database db) throws Exception
	{
		model.setHits(new HashMap<String, Entity>());
		model.setProbeToGene(new HashMap<String, Gene>());

		List<String> enpsIDs = new ArrayList<String>(Arrays.asList(humanGeneQuery));
		model.setHumanGeneQuery(new ArrayList<String>());
		for (String enpsID : enpsIDs)
		{
			String ortholog = model.getHumanToWorm().getHumanGeneToWormGene(enpsID);
			if (ortholog == null)
			{
				continue;
			}
			model.getHumanGeneQuery().add(ortholog);
		}

		List<Probe> probes = db.find(Probe.class, new QueryRule(Probe.SYMBOL, Operator.IN, model.getHumanGeneQuery()));

		for (Probe p : probes)
		{
			model.getHits().put(p.getName(), p);
		}
	}
}
