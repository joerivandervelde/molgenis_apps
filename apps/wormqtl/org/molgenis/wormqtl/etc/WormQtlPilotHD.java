/**
 * 
 */
package org.molgenis.wormqtl.etc;

import java.util.ArrayList;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.HandleRequestDelegationException;
import org.molgenis.xgap.Probe;

/**
 * @author mark
 * 
 */
public class WormQtlPilotHD
{
	/**
	 * @throws Exception
	 * @throws HandleRequestDelegationException
	 * 
	 */
	public WormQtlPilotHD() throws HandleRequestDelegationException, Exception
	{
		HumanToWorm h2w = new HumanToWorm();

		// Call the two translation tables to translate disease into worm genes
		List<String> wbGenes = h2w.convert("Adhalinopathy, primary (1)");

		Database db = ExampleQueries.getDb("admin", "admin");
		List<Probe> probes = new ArrayList<Probe>();

		// Convert genes to probes
		for (String wbGene : wbGenes)
		{
			QueryRule r = new QueryRule(Probe.REPORTSFOR_NAME, Operator.EQUALS, wbGene);
			QueryRule o = new QueryRule(Operator.OR);
			QueryRule s = new QueryRule(Probe.SYMBOL, Operator.EQUALS, wbGene);
			List<Probe> probe = db.find(Probe.class, r, o, s);

			// Print out the Gene and matching probe from the database
			for (Probe p : probe)
			{
				System.out.println("Gene: " + wbGene + ", matching probe: " + p);

			}

			// Add the probes to the complete list of probes
			probes.addAll(probe);

		}
	}

	/**
	 * @param args
	 * @throws Exception
	 * @throws HandleRequestDelegationException
	 */
	public static void main(String[] args) throws HandleRequestDelegationException, Exception
	{
		new WormQtlPilotHD();

	}

}
