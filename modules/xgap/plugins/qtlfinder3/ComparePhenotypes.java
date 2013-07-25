package plugins.qtlfinder3;

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

import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.wormqtl.etc.HypergeometricTest;

public class ComparePhenotypes
{

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
		Integer numberOfOverlappingGenes;

		Map<String, Map<String, Integer>> overlapPerDiseasePerSource = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Double>> pvalsPerDiseasePerSource = new HashMap<String, Map<String, Double>>();

		Set<String> wormGenesForThisWormPhenotype = new HashSet<String>();
		for (String p : phenotypes)
		{
			wormGenesForThisWormPhenotype.addAll(model.getHumanToWorm().wormPhenotypeToWormGenes(p,
					model.getDiseaseMapping()));
		}

		List<String> wormGenesForThisHumanDisease;

		Integer numberOfGenesForWormPhenotype = wormGenesForThisWormPhenotype.size();
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
				intersection.retainAll(wormGenesForThisWormPhenotype);
				numberOfOverlappingGenes = intersection.size();

				int allHumanGenesForDisease = model.getHumanToWorm().humanDiseaseToHumanGenes(thisPhenotype, source)
						.size();

				Double p = hg.hyperGeometricTest(numberOfOrthologs, numberOfGenesForWormPhenotype,
						allHumanGenesForDisease, numberOfOverlappingGenes);

				ao.put(thisPhenotype, numberOfOverlappingGenes);
				dp.put(thisPhenotype, p);

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

		List<String> humanGenesForThisWormPhenotype;

		Integer numberOfGenesForHumanDisease = humanGenesForThisHumanDisease.size();
		Integer numberOfOrthologs = model.getHumanToWorm().numberOfOrthologsBetweenHumanAndWorm();

		HypergeometricTest hg = new HypergeometricTest();

		// Go through all of the human disease phenotypes that have at least 1
		// gene with a worm gene
		for (String source : model.getHumanToWorm().wormSourceNames())
		{
			Map<String, Integer> ao = new HashMap<String, Integer>();
			Map<String, Double> dp = new HashMap<String, Double>();

			for (String thisPhenotype : model.getHumanToWorm().wormPhenotypesWithOrthology(source))
			{
				numberOfOverlappingGenes = 0;
				humanGenesForThisWormPhenotype = model.getHumanToWorm()
						.wormPhenotypeToHumanGenes(thisPhenotype, source);

				// Go through the list of genes associated with the human
				// phenotype
				// and keep count how many are overlapping with the genes
				// associated
				// with the worm phenotype

				Set<String> intersection = new HashSet<String>(humanGenesForThisWormPhenotype);
				intersection.retainAll(humanGenesForThisHumanDisease);
				numberOfOverlappingGenes = intersection.size();

				int allWormGenesForPhenotype = model.getHumanToWorm().wormPhenotypeToWormGenes(thisPhenotype, source)
						.size();

				Double p = hg.hyperGeometricTest(numberOfOrthologs, numberOfGenesForHumanDisease,
						allWormGenesForPhenotype, numberOfOverlappingGenes);

				ao.put(thisPhenotype, numberOfOverlappingGenes);
				dp.put(thisPhenotype, p);

			}
			Map<String, Integer> sorted = sortByValues(ao);
			overlapPerDiseasePerSource.put(source, sorted);
			pvalsPerDiseasePerSource.put(source, dp);
		}

		model.setAllOverlaps(overlapPerDiseasePerSource);
		model.setAllProbabilities(pvalsPerDiseasePerSource);
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
