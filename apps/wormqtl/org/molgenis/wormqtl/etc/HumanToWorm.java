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

	public LinkedHashMap<String, List<String>> getDiseaseToHuman()
	{
		return diseaseToHuman;
	}

	public LinkedHashMap<String, String> getHumanToWorm()
	{
		return humanToWorm;
	}

	public <diseaseToHuman> HumanToWorm(File omimMap, File orthologs) throws FileNotFoundException
	{
		System.out.println("> Building Hash maps....");

		diseaseToHuman = new LinkedHashMap<String, List<String>>();
		humanToWorm = new LinkedHashMap<String, String>();

		Scanner omimScan = new Scanner(omimMap);
		Scanner h2w = new Scanner(orthologs);

		// Loop through OMIM file, skip the first line (header)
		omimScan.nextLine();
		while (omimScan.hasNext())
		{
			String line = omimScan.nextLine();
			String enpsID = line.split("\",\"")[1];
			String disease = line.split("\",\"")[2].split("  \"")[0];

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
			String s = h2w.nextLine();
			String enpsID = s.split("	")[0];
			String wbgeneID = s.split("	")[1];

			// Add the human ID as key, WB id as value
			humanToWorm.put(enpsID, wbgeneID);
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
}
