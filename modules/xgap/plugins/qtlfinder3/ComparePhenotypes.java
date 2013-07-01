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
	public void comparePhenotypes(QtlFinderHDModel model, ScreenModel screenModel, String wormPhenotype)
			throws Exception
	{
		Integer wormPhenoGeneNumber = model.getHumanToWorm().getWormToPhenotype().get(wormPhenotype).size();
		Integer numberOfOrthologs = 4988;
		Integer numberOfOverlappingGenes;
		Map<String, Integer> ao = new HashMap<String, Integer>();

		List<String> wormGenesForThisWormPhenotype = model.getHumanToWorm().getWormToPhenotype().get(wormPhenotype);
		List<String> wormGenesForThisHumanPhenotype;

		// Go through all of the human disease phenotypes
		for (String phenotype : model.getHumanToWorm().getDiseaseToHuman().keySet())
		{
			numberOfOverlappingGenes = 0;
			wormGenesForThisHumanPhenotype = model.getHumanToWorm().convert(phenotype);

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

			ao.put(phenotype, numberOfOverlappingGenes);
		}
		Map<String, Integer> sorted = sortByValues(ao);

		System.out.println(ao);
		System.out.println(sorted);

		model.setAllOverlaps(sorted);
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
