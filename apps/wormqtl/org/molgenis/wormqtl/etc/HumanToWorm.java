package org.molgenis.wormqtl.etc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

	public <diseaseToHuman> HumanToWorm(File diseaseMap, File orthologs, File diseaseProteinCount)
			throws FileNotFoundException
	{
		System.out.println("> Building Hash maps....");

		diseaseToHuman = new LinkedHashMap<String, List<String>>();
		humanToWorm = new LinkedHashMap<String, String>();
		diseaseToProtein = new LinkedHashMap<String, String>();

		Scanner dM = new Scanner(diseaseMap);
		Scanner h2w = new Scanner(orthologs);
		Scanner dpc = new Scanner(diseaseProteinCount);

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
	}

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

	public Integer retrieve(String disease)
	{
		return Integer.parseInt(diseaseToProtein.get(disease));
	}
}
