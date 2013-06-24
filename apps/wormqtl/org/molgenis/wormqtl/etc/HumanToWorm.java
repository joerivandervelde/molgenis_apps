package org.molgenis.wormqtl.etc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * @author Mark de Haan
 * @version 1.0
 * @since 15/04/2013
 */
public class HumanToWorm
{
	// LinkedHashMap so the list of keys always has the same order
	private LinkedHashMap<String, List<String>> diseaseToHuman;
	private LinkedHashMap<String, String> humanToWorm;
	private LinkedHashMap<String, String> diseaseToProtein;
	private LinkedHashMap<String, List<String>> wormToPhenotype;

	public LinkedHashMap<String, List<String>> getDiseaseToHuman()
	{
		return diseaseToHuman;
	}

	public LinkedHashMap<String, String> getHumanToWorm()
	{
		return humanToWorm;
	}

	public LinkedHashMap<String, String> getDiseaseToProtein()
	{
		return diseaseToProtein;
	}

	public LinkedHashMap<String, List<String>> getWormToPhenotype()
	{
		return wormToPhenotype;
	}

	/**
	 * This method takes Omim / DGA disease map, an InParanoid custom WBGene -
	 * ENSP map and a custom disease - protein count map to create three
	 * hashmaps that can be used by wormQTL to link human diseases to worm
	 * genes, and to do hyper geometric testing
	 * 
	 * @param diseaseMap
	 * @param orthologs
	 * @param diseaseProteinCount
	 * @throws FileNotFoundException
	 */
	public <diseaseToHuman> HumanToWorm(File diseaseMap, File orthologs, File diseaseProteinCount, File wormPhenotypes)
			throws FileNotFoundException
	{
		System.out.println("> Building Hash maps....");

		diseaseToHuman = new LinkedHashMap<String, List<String>>();
		humanToWorm = new LinkedHashMap<String, String>();
		diseaseToProtein = new LinkedHashMap<String, String>();
		wormToPhenotype = new LinkedHashMap<String, List<String>>();

		Scanner dM = new Scanner(diseaseMap);
		Scanner h2w = new Scanner(orthologs);
		Scanner dpc = new Scanner(diseaseProteinCount);
		Scanner w2p = new Scanner(wormPhenotypes);

		// Loop through OMIM file, skip the first line (header)
		dM.nextLine();
		while (dM.hasNext())
		{
			String line = dM.nextLine();
			// String enpsID = line.split("\",\"")[1];
			// String disease = line.split("\",\"")[2].split("  \"")[0];

			String enpsID = line.split("\t")[0];
			String disease = line.split("\t")[1];

			// If disease is not in the hashmap yet
			if (diseaseToHuman.get(disease) == null)
			{
				// Put the disease with its ENSP ids into the hashmap.
				// Give a list in case multiple ENSP ids (genes) can cause the
				// same disease
				List<String> myList = new ArrayList<String>();
				myList.add(enpsID);
				diseaseToHuman.put(disease, myList);
			}
			// If the disease is in the hashmap
			else
			{
				// This is not the first gene causing this disease, add another
				// entry
				diseaseToHuman.get(disease).add(enpsID);
			}
		}

		// Loop through human WB link file, skip the first line (header)
		h2w.nextLine();
		while (h2w.hasNext())
		{
			String line = h2w.nextLine();
			String enpsID = line.split("\t")[0];
			String wbgeneID = line.split("\t")[1].replaceAll("\\s", "");

			// Add the human ID as key, WB id as value
			humanToWorm.put(enpsID, wbgeneID);
		}

		// Loop through the protein count file, skip the first line (header)
		dpc.nextLine();
		while (dpc.hasNext())
		{
			String line = dpc.nextLine();
			String disease = line.split("\t")[0];
			String proteinCount = line.split("\t")[1];

			// Add the disease as key, and the number of proteins involved as
			// value
			diseaseToProtein.put(disease, proteinCount);
		}

		// Loop through the worm gene - phenotype file, skip the first line
		// (header)
		w2p.nextLine();
		while (w2p.hasNext())
		{
			String line = w2p.nextLine();
			String wormGene = line.split("\t")[0];
			String disease = line.split("\t")[1];

			// If disease is not in the hashmap yet
			if (wormToPhenotype.get(disease) == null)
			{
				// Put the disease with its gene into the hashmap.
				// Give a list in case one disease can have more then one gene
				List<String> myList = new ArrayList<String>();
				myList.add(wormGene);
				wormToPhenotype.put(disease, myList);
			}

			// If the disease is in the hashmap
			else
			{
				// This is not the first gene causing this disease, add another
				// entry
				wormToPhenotype.get(disease).add(wormGene);
			}
		}
	}

	/**
	 * This method converts a human disease to a list of orthologous worm genes
	 * 
	 * @param disease
	 * @return a list of worm genes that are ortholog for the disease that was
	 *         put in
	 */
	public List<String> convert(String disease)
	{

		List<String> humanGenes = diseaseToHuman.get(disease);
		List<String> wormGenes = new ArrayList<String>();

		// For every human gene (ENSP id) that is linked to the entered disease
		// get the WBgene ID(s)
		for (String humanGene : humanGenes)
		{
			String wormGene = humanToWorm.get(humanGene);
			wormGenes.add(wormGene);
		}

		return wormGenes;
	}

	/**
	 * This method takes a worm gene, and goes back through two hashmaps to
	 * determine what human disease is associated with this worm gene through
	 * ortholog matching
	 * 
	 * @param wbGene
	 * @return a list containing a wb Gene on index 0 with one (or more)
	 *         diseases associated with that gene
	 */

	public List<String> linkToDisease(String wbGene)
	{
		List<String> wormToDisease = new ArrayList<String>();
		String enpsID = new String();

		wormToDisease.add(wbGene);

		// Get ENPS ID Key from WB gene value
		for (Entry<String, String> id : getHumanToWorm().entrySet())
		{
			if (wbGene.equals(id.getValue()))
			{
				enpsID = id.getKey();
			}
		}

		// Get DISEASE Key from ENPS value
		for (Entry<String, List<String>> value : getDiseaseToHuman().entrySet())
		{
			if (getDiseaseToHuman().get(value.getKey()).contains(enpsID))
			{
				wormToDisease.add(value.getKey());
			}
		}

		// If the second entry (should be the first disease) is empty, mention
		// that there are no orthologs available
		if (wormToDisease.size() < 2)
		{
			wormToDisease.add("No ortholog available");
		}

		return wormToDisease;
	}

	/**
	 * This method uses a disease name to get the total number of proteins that
	 * is associated with said disease via hashmap
	 * 
	 * @param disease
	 * @return total number of proteins associated with a certain human disease
	 */
	public Integer retrieve(String disease)
	{
		return Integer.parseInt(diseaseToProtein.get(disease));
	}
}
