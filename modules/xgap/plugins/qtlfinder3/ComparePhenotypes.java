package plugins.qtlfinder3;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	public void comparePhenotypesWorm(QtlFinderHDModel model, ScreenModel screenModel, String phenotype)
			throws Exception
	{
		Integer numberOfOverlappingGenes;
		Map<String, Integer> ao = new HashMap<String, Integer>();
		Map<String, Double> dp = new HashMap<String, Double>();

		List<String> wormGenesForThisWormPhenotype = model.getHumanToWorm().getWormToPhenotype().get(phenotype);
		List<String> wormGenesForThisHumanPhenotype;

		Integer wormPhenoGeneNumber = model.getHumanToWorm().getWormToPhenotype().get(phenotype).size();
		Integer numberOfOrthologs = 30000;

		HypergeometricTest hg = new HypergeometricTest();

		// Go through all of the human disease phenotypes
		for (String thisPhenotype : model.getHumanToWorm().getDiseaseToHuman().keySet())
		{
			numberOfOverlappingGenes = 0;
			wormGenesForThisHumanPhenotype = model.getHumanToWorm().convert(thisPhenotype);

			// Go through the list of genes associated with the human phenotype
			// and keep count how many are overlapping with the genes associated
			// with the worm phenotype
			for (String gene : wormGenesForThisWormPhenotype)
			{
				if (wormGenesForThisHumanPhenotype.contains(gene))
				{
					numberOfOverlappingGenes++;
				}
			}

			Double p = hg.hyperGeometricTest(numberOfOrthologs, wormPhenoGeneNumber,
					model.getHumanToWorm().retrieve(thisPhenotype), numberOfOverlappingGenes);

			ao.put(thisPhenotype, numberOfOverlappingGenes);
			dp.put(thisPhenotype, p);

		}

		Map<String, Integer> sorted = sortByValues(ao);
		model.setAllOverlaps(sorted);
		model.setAllProbabilities(dp);
	}

	public void comparePhenotypesHuman(QtlFinderHDModel model, ScreenModel screenModel, String phenotype)
	{
		Integer numberOfOverlappingGenes;
		Map<String, Integer> ao = new HashMap<String, Integer>();
		Map<String, Double> dp = new HashMap<String, Double>();

		List<String> wormGenesForThisWormPhenotype;
		List<String> wormGenesForThisHumanPhenotype = model.getHumanToWorm().getDiseaseToHuman().get(phenotype);

		Integer humanPhenoGeneNumber = Integer.parseInt(model.getHumanToWorm().getDiseaseToProtein().get(phenotype));
		Integer numberOfOrthologs = 30000;

		HypergeometricTest hg = new HypergeometricTest();

		for (String thisPhenotype : model.getHumanToWorm().getWormToPhenotype().keySet())
		{
			numberOfOverlappingGenes = 0;
			wormGenesForThisWormPhenotype = model.getHumanToWorm().getWormToPhenotype().get(thisPhenotype);

			for (String gene : wormGenesForThisHumanPhenotype)
			{
				if (wormGenesForThisWormPhenotype.contains(model.getHumanToWorm().getHumanToWorm().get(gene)))
				{
					numberOfOverlappingGenes++;
				}
			}

			System.out.println(numberOfOverlappingGenes);

			Double p = hg.hyperGeometricTest(numberOfOrthologs, wormGenesForThisWormPhenotype.size(),
					humanPhenoGeneNumber, numberOfOverlappingGenes);

			ao.put(thisPhenotype, numberOfOverlappingGenes);
			dp.put(thisPhenotype, p);
		}

		Map<String, Integer> sorted = sortByValues(ao);
		model.setAllOverlaps(sorted);
		model.setAllProbabilities(dp);
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
