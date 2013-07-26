package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Gene;
import org.molgenis.xgap.Probe;

public class SetRegion
{
	/**
	 * This method determines what probes and genes are inside the region that
	 * was selected, either by qtl search or by region search.
	 * 
	 * Calls the HumanToWorm class to determine which diseases are mapped to the
	 * selected region. Sets table view to true to show gene - disease -
	 * probability as a result
	 * 
	 * @author Mark de Haan
	 * @param start
	 * @param end
	 * @param chromosome
	 * @param db
	 * @param search
	 * @throws Exception
	 */

	public void setRegion(Integer start, Integer end, Integer chromosome, Database db, boolean search,
			QtlFinderHDModel model) throws Exception
	{
		model.setHits(new HashMap<String, Entity>());
		model.setProbeToGene(new HashMap<String, Gene>());

		List<Probe> probesInRegion = new ArrayList<Probe>();
		List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR, Operator.LESS,
				chromosome));

		if (search)
		{
			for (Chromosome chr : chrNeeded)
			{
				start = start + chr.getBpLength();
				end = end + chr.getBpLength();
			}
		}

		probesInRegion = db.find(Probe.class, new QueryRule(Probe.BPSTART, Operator.GREATER_EQUAL, start),
				new QueryRule(Probe.BPSTART, Operator.LESS_EQUAL, end));

		for (Probe p : probesInRegion)
		{
			model.getHits().put(p.getName(), p);
		}
	}
}
