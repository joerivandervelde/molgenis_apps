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
	private LinkedHashMap<String, List<String>> diseaseToHumanOMIM;
	private LinkedHashMap<String, List<String>> diseaseToHumanDGA;
	private LinkedHashMap<String, List<String>> diseaseToHumanGWAS;
	private LinkedHashMap<String, String> humanToWorm;
	private LinkedHashMap<String, String> diseaseToProtein;
	private LinkedHashMap<String, List<String>> wormToPhenotype;
	private LinkedHashMap<String, String> wormToDisease;

	public LinkedHashMap<String, List<String>> getDiseaseToHumanGWAS()
	{
		return diseaseToHumanGWAS;
	}

	public void setDiseaseToHumanGWAS(LinkedHashMap<String, List<String>> diseaseToHumanGWAS)
	{
		this.diseaseToHumanGWAS = diseaseToHumanGWAS;
	}

	public LinkedHashMap<String, List<String>> getDiseaseToHumanDGA()
	{
		return diseaseToHumanDGA;
	}

	public void setDiseaseToHumanDGA(LinkedHashMap<String, List<String>> diseaseToHumanDGA)
	{
		this.diseaseToHumanDGA = diseaseToHumanDGA;
	}

	public LinkedHashMap<String, List<String>> getDiseaseToHuman()
	{
		return diseaseToHuman;
	}

	public void setDiseaseToHuman(LinkedHashMap<String, List<String>> diseaseToHuman)
	{
		this.diseaseToHuman = diseaseToHuman;
	}

	public LinkedHashMap<String, List<String>> getDiseaseToHumanOMIM()
	{
		return diseaseToHumanOMIM;
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

	public LinkedHashMap<String, String> getWormToDisease()
	{
		return wormToDisease;
	}

	public void setWormToDisease(LinkedHashMap<String, String> wormToDisease)
	{
		this.wormToDisease = wormToDisease;
	}

	/**
	 * This method takes Omim / DGA / GWAS disease maps, an InParanoid custom
	 * WBGene - ENSP map a custom disease - protein count map and a
	 * wormphenotype - wormgene map to create six hashmaps that can be used by
	 * wormQTL to link human diseases to worm genes, and to do hyper geometric
	 * testing
	 * 
	 * @param diseaseMapOMIM
	 * @param diseaseMapDGA
	 * @param diseaseMapGWAS
	 * @param orthologs
	 * @param diseaseProteinCountDGA
	 * @param wormPhenotypes
	 * @throws FileNotFoundException
	 */
	public <diseaseToHuman> HumanToWorm(File diseaseMapOMIM, File diseaseMapDGA, File diseaseMapGWAS, File orthologs,
			File diseaseProteinCountDGA, File wormPhenotypes) throws FileNotFoundException
	{
		System.out.println("> Building Hash maps....");

		// Storing data in the database will make file reading obsolete
		setDiseaseMaps(diseaseMapOMIM, diseaseMapDGA, diseaseMapGWAS);
		setOrthologs(orthologs);
		setDGAProteinCount(diseaseProteinCountDGA);
		setWormPhenotypes(wormPhenotypes);
	}

	/**
	 * This method parses the disease maps from omim, dga and gwas into a hash
	 * with disease - human protein list key value pairs
	 * 
	 * @author Mark de Haan
	 * @param diseaseMapGWAS
	 * @param diseaseMapDGA
	 * @param diseaseMapOMIM
	 * 
	 * */
	public void setDiseaseMaps(File diseaseMapOMIM, File diseaseMapDGA, File diseaseMapGWAS)
			throws FileNotFoundException
	{
		diseaseToHuman = new LinkedHashMap<String, List<String>>();
		diseaseToHumanOMIM = new LinkedHashMap<String, List<String>>();
		diseaseToHumanDGA = new LinkedHashMap<String, List<String>>();
		diseaseToHumanGWAS = new LinkedHashMap<String, List<String>>();

		List<Scanner> maps = new ArrayList<Scanner>();
		Scanner OMIM = new Scanner(diseaseMapOMIM);
		Scanner DGA = new Scanner(diseaseMapDGA);
		Scanner GWAS = new Scanner(diseaseMapGWAS);

		maps.add(DGA);
		maps.add(GWAS);
		maps.add(OMIM);

		Integer counter = 0;

		for (Scanner map : maps)
		{
			counter++;

			map.nextLine();
			while (map.hasNext())
			{
				String line = map.nextLine();

				String enpsID = line.split("\t")[0];
				String disease = line.split("\t")[1];

				// If disease is not in the hashmap yet
				if (diseaseToHuman.get(disease) == null)
				{
					// Put the disease with its ENSP ids into the hashmap.
					// Give a list in case multiple ENSP ids (genes) can cause
					// the
					// same disease
					List<String> myList = new ArrayList<String>();
					myList.add(enpsID);
					diseaseToHuman.put(disease, myList);
				}
				// If the disease is in the hashmap
				else
				{
					// This is not the first gene causing this disease, add
					// another
					// entry
					diseaseToHuman.get(disease).add(enpsID);
				}
			}

			if (counter == 1)
			{
				diseaseToHumanDGA.putAll(diseaseToHuman);
			}
			else if (counter == 2)
			{
				diseaseToHumanGWAS.putAll(diseaseToHuman);
			}
			else
			{
				diseaseToHumanOMIM.putAll(diseaseToHuman);
			}

			diseaseToHuman.clear();
			diseaseToHuman.putAll(diseaseToHumanOMIM);
		}
	}

	/**
	 * This method parses a file with human proteins - worm genes and puts them
	 * in a hashmap as key value pairs
	 * 
	 * @author Mark de Haan
	 * @param orthologs
	 * 
	 * */
	public void setOrthologs(File orthologs) throws FileNotFoundException
	{
		humanToWorm = new LinkedHashMap<String, String>();
		Scanner h2w = new Scanner(orthologs);

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
	}

	/**
	 * This method parses DGA diseases and the number of proteins associated
	 * with those diseases in a hash as key value pair
	 * 
	 * @author Mark de Haan
	 * @param diseaseProteinCountDGA
	 * 
	 * */
	public void setDGAProteinCount(File diseaseProteinCountDGA) throws FileNotFoundException
	{
		diseaseToProtein = new LinkedHashMap<String, String>();
		Scanner dpc = new Scanner(diseaseProteinCountDGA);

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
	}

	/**
	 * This method puts worm genes and worm phenotypes into a hash as key value
	 * pairs
	 * 
	 * @author Mark de Haan
	 * @param wormPhenotypes
	 * 
	 * */
	public void setWormPhenotypes(File wormPhenotypes) throws FileNotFoundException
	{
		wormToPhenotype = new LinkedHashMap<String, List<String>>();
		Scanner w2p = new Scanner(wormPhenotypes);

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
		List<String> humanGenes = this.diseaseToHuman.get(disease);
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

	public List<String> linkToDisease(String wbGene, String map)
	{
		try
		{
			List<String> wormToDisease = new ArrayList<String>();
			LinkedHashMap<String, List<String>> hashToBeUsed = new LinkedHashMap<String, List<String>>();
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

			if (map.equals("OMIM"))
			{
				hashToBeUsed.putAll(diseaseToHumanOMIM);
			}
			else if (map.equals("DGA"))
			{
				hashToBeUsed.putAll(diseaseToHumanDGA);
			}
			else if (map.equals("GWAS"))
			{
				hashToBeUsed.putAll(diseaseToHumanGWAS);
			}
			else
			{
				// nothing
			}

			// Get DISEASE Key from ENPS value
			for (Entry<String, List<String>> value : hashToBeUsed.entrySet())
			{
				if (hashToBeUsed.get(value.getKey()).contains(enpsID))
				{
					wormToDisease.add(value.getKey());
				}
			}

			// If the second entry (should be the first disease) is empty,
			// mention
			// that there are no orthologs available
			if (wormToDisease.size() < 2)
			{
				wormToDisease.add("No ortholog available");
			}

			System.out.println(wormToDisease);

			return wormToDisease;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method uses a disease name to get the total number of proteins that
	 * is associated with said disease via hashmap, DGA only
	 * 
	 * @param disease
	 * @return total number of proteins associated with a certain human disease
	 */
	public Integer retrieve(String disease)
	{
		return Integer.parseInt(diseaseToProtein.get(disease));
	}
}