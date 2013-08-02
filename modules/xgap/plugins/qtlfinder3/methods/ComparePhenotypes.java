package plugins.qtlfinder3.methods;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder3.resources.HumanToWorm;

public class ComparePhenotypes
{

	/**
	 * 
	 * @param model
	 * @param screenModel
	 * @param phenotypes
	 * @throws Exception
	 */
	public static ComparePhenotypesResult comparePhenotypesHuman(HumanToWorm h2w, String source, List<String> phenotypes)
			throws Exception
	{
		Set<String> humanGenesForThisHumanDisease = new HashSet<String>();
		for (String p : phenotypes)
		{
			humanGenesForThisHumanDisease.addAll(h2w.humanDiseaseToHumanGenes(p, source));
		}
		return compare(h2w, humanGenesForThisHumanDisease);
	}

	/**
	 * compare a custom list of worm probes (e.g. as result from a region
	 * select) vs. human diseases
	 * 
	 * @param model
	 * @param screenModel
	 * @param phenotypes
	 * @throws Exception
	 */
	public static ComparePhenotypesResult compareGenesWorm(HumanToWorm h2w, List<Entity> probes) throws Exception
	{
		Set<String> wormGenes = new HashSet<String>();
		for (Entity e : probes)
		{
			Probe p = (Probe) e;
			if (p.getReportsFor_Name() != null && p.getReportsFor_Name().startsWith("WBGene"))
			{
				wormGenes.add(((Probe) p).getReportsFor_Name());
			}
			else if (p.getSymbol() != null && p.getSymbol().startsWith("WBGene"))
			{
				wormGenes.add(((Probe) p).getSymbol());
			}
		}

		return compare(h2w, wormGenes);

	}

	/**
	 * Compare Phenotypes
	 * 
	 * This function takes a worm phenotype, and calculates on the fly what the
	 * exact gene overlap is between the selected phenotype and all of the human
	 * phenotypes. It will call R scripts for the plotting of venn diagrams,
	 * these will visualize the overlap
	 * 
	 * @author Mark de Haan
	 * @param model
	 * @param screenmodel
	 * @param wormphenotype
	 * @throws Exception
	 * */
	public static ComparePhenotypesResult comparePhenotypesWorm(HumanToWorm h2w, String source, List<String> phenotypes)
			throws Exception
	{
		Set<String> wormGenesForThisWormPhenotype = new HashSet<String>();
		for (String p : phenotypes)
		{
			wormGenesForThisWormPhenotype.addAll(h2w.wormPhenotypeToWormGenes(p, source));
		}

		return compare(h2w, wormGenesForThisWormPhenotype);

	}

	/**
	 * Compare a set of genes against all diseases/phenotypes in the sources
	 * 
	 * @param h2w
	 * @param sample
	 * @throws Exception
	 */
	public static ComparePhenotypesResult compare(HumanToWorm h2w, Set<String> sample) throws Exception
	{

		ComparePhenotypesResult res = new ComparePhenotypesResult();

		int sampleSizeUnpruned = sample.size();
		sample.retainAll(h2w.allGenesInOrthologs());
		int sampleSize = sample.size();

		res.setSampleSize(sampleSizeUnpruned);
		res.setSampleSizePruned(sampleSize);
		res.setBaseThreshold(0.05);

		int populationSize = h2w.numberOfOrthologsBetweenHumanAndWorm();

		for (String source : h2w.allSources())

		{
			Map<String, Integer> phenoToSuccessStates = new HashMap<String, Integer>();
			Map<String, Integer> phenoToSuccessStatesPruned = new HashMap<String, Integer>();
			Map<String, Integer> phenoToSuccesses = new HashMap<String, Integer>();
			Map<String, Double> phenoToPval = new HashMap<String, Double>();
			Map<String, Set<String>> phenoToDetails = new HashMap<String, Set<String>>();

			res.getSourceToPopulationSize().put(source, h2w.sourceToGenes(source).size());
			res.getSourceToPopulationSizePruned().put(source, populationSize);
			res.getSourceToBonferroniThreshold().put(source,
					res.getBaseThreshold() / h2w.disOrPhenoWithOrthologyFromSource(source).size());

			for (String disOrPheno : h2w.disOrPhenoWithOrthologyFromSource(source))
			{
				Set<String> genesForDisOrPheno = new HashSet<String>(h2w.genesForDisOrPheno(disOrPheno, source));
				int successStatesUnpruned = genesForDisOrPheno.size();
				genesForDisOrPheno.retainAll(h2w.allGenesInOrthologs());
				int successStates = genesForDisOrPheno.size();

				int overlap = h2w.overlap(sample, genesForDisOrPheno);
				HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates, sampleSize);
				double pval = h.upperCumulativeProbability(overlap);

				if (overlap > 0)
				{
					phenoToSuccessStates.put(disOrPheno, successStatesUnpruned);
					phenoToSuccessStatesPruned.put(disOrPheno, successStates);
					phenoToSuccesses.put(disOrPheno, overlap);
					phenoToPval.put(disOrPheno, pval);
					phenoToDetails.put(disOrPheno, h2w.detailsForDisease(source, disOrPheno));
				}

			}

			res.getSourceToPhenoToSuccessStates().put(source, phenoToSuccessStates);
			res.getSourceToPhenoToSuccessStatesPruned().put(source, phenoToSuccessStatesPruned);
			res.getSourceToPhenoToSuccesses().put(source, phenoToSuccesses);
			res.getSourceToPhenoToPval().put(source, phenoToPval);
			res.getSourceToPhenoToDetails().put(source, phenoToDetails);
		}
		return res;
	}
}
