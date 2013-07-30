package plugins.qtlfinder3.methods;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder3.QtlFinderHDModel;
import plugins.qtlfinder3.resources.HumanToWorm;

public class ComparePhenotypes
{

	/**
	 * compare a custom list of worm probes (e.g. as result from a region
	 * select) vs. human diseases
	 * 
	 * @param model
	 * @param screenModel
	 * @param phenotypes
	 * @throws Exception
	 */
	public void compareGenesWorm(QtlFinderHDModel model, ScreenModel screenModel, List<Entity> probes) throws Exception
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

		//compareWorm(model, screenModel, wormGenes);
		
		compare(model.getHumanToWorm(), wormGenes);
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
	public void comparePhenotypesWorm(QtlFinderHDModel model, ScreenModel screenModel, List<String> phenotypes)
			throws Exception
	{
		Set<String> wormGenesForThisWormPhenotype = new HashSet<String>();
		for (String p : phenotypes)
		{
			wormGenesForThisWormPhenotype.addAll(model.getHumanToWorm().wormPhenotypeToWormGenes(p,
					model.getDiseaseMapping()));
		}
	//	compareWorm(model, screenModel, wormGenesForThisWormPhenotype);
		compare(model.getHumanToWorm(), wormGenesForThisWormPhenotype);
	}
	
	/**
	 * Input: collection of genes
	 * 
	 * same as below but with many informative printlns...
	 * 
	 * @throws Exception
	 */
	public void compareDEBUG(HumanToWorm h2w, Set<String> sample) throws Exception
	{
		//prune input (regardless of human or worm genes)
		System.out.println("genes original size = " + sample.size());
		sample.retainAll(h2w.allGenesInOrthologs());
		int sampleSize = sample.size();
		System.out.println("genes pruned ('sample') size = " + sampleSize);
		
		for(String source : h2w.allSources())
		{
			System.out.println("source " + source + " original size = " + h2w.sourceToGenes(source).size());
			int populationSize = h2w.sourceToGenesWithOrthologs(source).size();
			System.out.println("source " + source + " pruned ('population') size = " + populationSize);
			
			for (String disOrPheno : h2w.disOrPhenoFromSource(source))
			{
				Set<String> genesForDisOrPheno = new HashSet<String>(h2w.genesForDisOrPheno(disOrPheno, source));
				
				System.out.println("dis/phen " + disOrPheno + " size = " + genesForDisOrPheno.size());

				genesForDisOrPheno.retainAll(h2w.allGenesInOrthologs());
				int successStates = genesForDisOrPheno.size();
				
				System.out.println("disease " + disOrPheno + " pruned ('success states') size = " + successStates);
				
				int overlap = h2w.overlap(sample, genesForDisOrPheno);
				
				System.out.println("overlap ('successes') size = " + overlap);
				
				HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
						sampleSize);
				System.out.println((overlap == 0 ? "" : "## OVERLAP FOUND: ") + "P-VALUE: " + h.upperCumulativeProbability(overlap));
			}	
		}
	}
	
	/**
	 * Compare a set of genes against all diseases/phenotypes in the sources
	 * 
	 * @param h2w
	 * @param sample
	 * @throws Exception
	 */
	public void compare(HumanToWorm h2w, Set<String> sample) throws Exception
	{
		sample.retainAll(h2w.allGenesInOrthologs());
		int sampleSize = sample.size();
		
		for(String source : h2w.allSources())
		{
			int populationSize = h2w.sourceToGenesWithOrthologs(source).size();
			for (String disOrPheno : h2w.disOrPhenoFromSource(source))
			{
				Set<String> genesForDisOrPheno = new HashSet<String>(h2w.genesForDisOrPheno(disOrPheno, source));
				genesForDisOrPheno.retainAll(h2w.allGenesInOrthologs());
				int successStates = genesForDisOrPheno.size();
				int overlap = h2w.overlap(sample, genesForDisOrPheno);
				HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
						sampleSize);
				double pval = h.upperCumulativeProbability(overlap);
			}	
		}
	}
	
	
	

	/**
	 * Input: collection of genes
	 * 
	 * @throws Exception
	 */
	public void compare2(HumanToWorm h2w, Set<String> genes, boolean wormInput) throws Exception
	{

		System.out.println("genes original size = " + genes.size());

		// prune input list to only contain orthologs
		if (wormInput)
		{
			genes.retainAll(h2w.allWormGenesInOrthologs());
		}
		else
		{
			genes.retainAll(h2w.allHumanGenesInOrthologs());
		}
		int sampleSize = genes.size();
		
		System.out.println("genes pruned ('sample') size = " + sampleSize);

		// compare versus human diseases
		for (String source : h2w.humanSourceNames())
		{

			System.out.println("human source " + source + " original size = " + h2w.humanDiseasesToHumanGenes(h2w.humanDiseasesWithOrthology(source), source).size());
			int populationSize = h2w.humanDiseasesToHumanGenesWithOrthology(h2w.humanDiseasesWithOrthology(source), source).size();
			System.out.println("human source " + source + " pruned ('population') size = " + populationSize);

			// each disease is a 'success state'
			for (String disease : h2w.humanDiseasesWithOrthology(source))
			{
				if (wormInput)
				{
					List<String> wormGenesForThisHumanDisease = h2w.humanDiseaseToWormGenes(disease, source);

					System.out.println("disease " + disease + " size = " + wormGenesForThisHumanDisease.size());

					wormGenesForThisHumanDisease.retainAll(h2w.allWormGenesInOrthologs());

					int successStates = wormGenesForThisHumanDisease.size();

					System.out.println("disease " + disease + " pruned ('success states') size = " + successStates);

					wormGenesForThisHumanDisease.retainAll(genes);

					int overlap = wormGenesForThisHumanDisease.size();

					System.out.println("overlap ('successes') size = " + overlap);

					HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
							sampleSize);
					System.out.println((overlap == 0 ? "" : "## OVERLAP FOUND: ") + "P-VALUE: " + h.upperCumulativeProbability(overlap));
				}
				else
				{
					List<String> humanGenesForThisHumanDisease = h2w.humanDiseaseToHumanGenes(disease, source);

					System.out.println("disease " + disease + " size = " + humanGenesForThisHumanDisease.size());

					humanGenesForThisHumanDisease.retainAll(h2w.allHumanGenesInOrthologs());

					int successStates = humanGenesForThisHumanDisease.size();

					System.out.println("disease " + disease + " pruned ('success states') size = " + successStates);

					humanGenesForThisHumanDisease.retainAll(genes);

					int overlap = humanGenesForThisHumanDisease.size();

					System.out.println("overlap ('successes') size = " + overlap);

					HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
							sampleSize);
					System.out.println((overlap == 0 ? "" : "## OVERLAP FOUND: ") + "P-VALUE: " + h.upperCumulativeProbability(overlap));
				}

			}
		}

		// compare versus worm phenotypes
		for (String source : h2w.wormSourceNames())
		{
			System.out.println("worm source " + source + " original size = "
					+ h2w.wormPhenotypesToWormGenes(h2w.wormPhenotypesWithOrthology(source), source).size());
			int populationSize = h2w.wormPhenotypesToWormGenesWithOrthology(h2w.wormPhenotypesWithOrthology(source),
					source).size();
			System.out.println("worm source " + source + " pruned ('population') size = " + populationSize);

			for (String phenotype : h2w.wormPhenotypesWithOrthology(source))
			{
				if (wormInput)
				{
					List<String> wormGenesForThisWormPhenotype = h2w.wormPhenotypeToWormGenes(phenotype, source);

					System.out.println("phenotype " + phenotype + " size = " + wormGenesForThisWormPhenotype.size());

					wormGenesForThisWormPhenotype.retainAll(h2w.allWormGenesInOrthologs());

					int successStates = wormGenesForThisWormPhenotype.size();

					System.out.println("phenotype " + phenotype + " pruned ('success states') size = " + successStates);

					wormGenesForThisWormPhenotype.retainAll(genes);

					int overlap = wormGenesForThisWormPhenotype.size();

					System.out.println("overlap ('successes') size = " + overlap);

					HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
							sampleSize);
					System.out.println((overlap == 0 ? "" : "## OVERLAP FOUND: ") + "P-VALUE: " + h.upperCumulativeProbability(overlap));
				}
				else
				{
					List<String> wormGenesForThisWormPhenotype = h2w.wormPhenotypeToWormGenes(phenotype, source);

					System.out.println("phenotype " + phenotype + " size = " + wormGenesForThisWormPhenotype.size());

					wormGenesForThisWormPhenotype.retainAll(h2w.allWormGenesInOrthologs());

					int successStates = wormGenesForThisWormPhenotype.size();

					System.out.println("phenotype " + phenotype + " pruned ('success states') size = " + successStates);

					wormGenesForThisWormPhenotype.retainAll(genes);

					int overlap = wormGenesForThisWormPhenotype.size();

					System.out.println("overlap ('successes') size = " + overlap);

					HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates,
							sampleSize);
					System.out.println((overlap == 0 ? "" : "## OVERLAP FOUND: ") + "P-VALUE: " + h.upperCumulativeProbability(overlap));
				}
			}
		}

	}

	/**
	 * generic function that takes a set of worm genes and compares vs human
	 * diseases
	 * 
	 * @param model
	 * @param screenModel
	 * @param phenotypes
	 * @throws Exception
	 */
	public void compareWorm(QtlFinderHDModel model, ScreenModel screenModel, Set<String> wormGenes) throws Exception
	{
		Integer numberOfOverlappingGenes;

		Map<String, Map<String, Integer>> overlapPerDiseasePerSource = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Double>> pvalsPerDiseasePerSource = new HashMap<String, Map<String, Double>>();

		List<String> wormGenesForThisHumanDisease;

		Integer numberOfGenesForWormPhenotype = wormGenes.size();
		Integer numberOfOrthologs = model.getHumanToWorm().numberOfOrthologsBetweenHumanAndWorm();

		HypergeometricTest hg = new HypergeometricTest();

		// Go through all of the human disease phenotypes that have at least 1
		// gene with a worm gene
		for (String source : model.getHumanToWorm().humanSourceNames())

		{
			Map<String, Integer> ao = new HashMap<String, Integer>();
			Map<String, Double> dp = new HashMap<String, Double>();

			for (String thisPhenotype : model.getHumanToWorm().humanDiseasesWithOrthology(source))
			{
				numberOfOverlappingGenes = 0;
				wormGenesForThisHumanDisease = model.getHumanToWorm().humanDiseaseToWormGenes(thisPhenotype, source);

				// Go through the list of genes associated with the human
				// phenotype
				// and keep count how many are overlapping with the genes
				// associated
				// with the worm phenotype

				Set<String> intersection = new HashSet<String>(wormGenesForThisHumanDisease);
				intersection.retainAll(wormGenes);
				numberOfOverlappingGenes = intersection.size();

				int allHumanGenesForDisease = model.getHumanToWorm().humanDiseaseToHumanGenes(thisPhenotype, source)
						.size();

				Double p = hg.hyperGeometricTest(numberOfOrthologs, numberOfGenesForWormPhenotype,
						allHumanGenesForDisease, numberOfOverlappingGenes);

				if (numberOfOverlappingGenes > 0)
				{
					ao.put(thisPhenotype, numberOfOverlappingGenes);
					dp.put(thisPhenotype, p);
				}

			}
			Map<String, Integer> sorted = sortByValues(ao);
			overlapPerDiseasePerSource.put(source, sorted);
			pvalsPerDiseasePerSource.put(source, dp);
		}

		model.setAllOverlaps(overlapPerDiseasePerSource);
		model.setAllProbabilities(pvalsPerDiseasePerSource);
	}

	public void comparePhenotypesHuman(QtlFinderHDModel model, ScreenModel screenModel, List<String> phenotypes)
			throws Exception
	{
		Integer numberOfOverlappingGenes;

		Map<String, Map<String, Integer>> overlapPerDiseasePerSource = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Double>> pvalsPerDiseasePerSource = new HashMap<String, Map<String, Double>>();

		Set<String> humanGenesForThisHumanDisease = new HashSet<String>();
		for (String p : phenotypes)
		{
			humanGenesForThisHumanDisease.addAll(model.getHumanToWorm().humanDiseaseToHumanGenes(p,
					model.getDiseaseMapping()));
		}
		
		compare(model.getHumanToWorm(), humanGenesForThisHumanDisease);

//		List<String> humanGenesForThisWormPhenotype;
//
//		Integer numberOfGenesForHumanDisease = humanGenesForThisHumanDisease.size();
//		Integer numberOfOrthologs = model.getHumanToWorm().numberOfOrthologsBetweenHumanAndWorm();
//
//		HypergeometricTest hg = new HypergeometricTest();
//
//		// Go through all of the human disease phenotypes that have at least 1
//		// gene with a worm gene
//		for (String source : model.getHumanToWorm().wormSourceNames())
//		{
//			Map<String, Integer> ao = new HashMap<String, Integer>();
//			Map<String, Double> dp = new HashMap<String, Double>();
//
//			for (String thisPhenotype : model.getHumanToWorm().wormPhenotypesWithOrthology(source))
//			{
//				numberOfOverlappingGenes = 0;
//				humanGenesForThisWormPhenotype = model.getHumanToWorm()
//						.wormPhenotypeToHumanGenes(thisPhenotype, source);
//
//				// Go through the list of genes associated with the human
//				// phenotype
//				// and keep count how many are overlapping with the genes
//				// associated
//				// with the worm phenotype
//
//				Set<String> intersection = new HashSet<String>(humanGenesForThisWormPhenotype);
//				intersection.retainAll(humanGenesForThisHumanDisease);
//				numberOfOverlappingGenes = intersection.size();
//
//				int allWormGenesForPhenotype = model.getHumanToWorm().wormPhenotypeToWormGenes(thisPhenotype, source)
//						.size();
//
//				Double p = hg.hyperGeometricTest(numberOfOrthologs, numberOfGenesForHumanDisease,
//						allWormGenesForPhenotype, numberOfOverlappingGenes);
//
//				if (numberOfOverlappingGenes > 0)
//				{
//					ao.put(thisPhenotype, numberOfOverlappingGenes);
//					dp.put(thisPhenotype, p);
//				}
//
//			}
//			Map<String, Integer> sorted = sortByValues(ao);
//			overlapPerDiseasePerSource.put(source, sorted);
//			pvalsPerDiseasePerSource.put(source, dp);
//		}
//
//		model.setAllOverlaps(overlapPerDiseasePerSource);
//		model.setAllProbabilities(pvalsPerDiseasePerSource);
	}

	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map)
	{
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>()
		{

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}
