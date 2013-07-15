package org.molgenis.wormqtl.etc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

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
	private LinkedHashMap<String, String> wormToDisease;
	private LinkedHashMap<String, List<String>> gwasPheno;
	private LinkedHashMap<String, Set<String>> gwasStudy;

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

	public LinkedHashMap<String, String> getWormToDisease()
	{
		return wormToDisease;
	}

	public void setWormToDisease(LinkedHashMap<String, String> wormToDisease)
	{
		this.wormToDisease = wormToDisease;
	}

	public LinkedHashMap<String, List<String>> getGwasPheno()
	{
		return gwasPheno;
	}

	public void setGwasPheno(LinkedHashMap<String, List<String>> gwasPheno)
	{
		this.gwasPheno = gwasPheno;
	}

	public LinkedHashMap<String, Set<String>> getGwasStudy()
	{
		return gwasStudy;
	}

	public void setGwasStudy(LinkedHashMap<String, Set<String>> gwasStudy)
	{
		this.gwasStudy = gwasStudy;
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
	 * @param gwasData
	 * @throws FileNotFoundException
	 */
	public <diseaseToHuman> HumanToWorm(File diseaseMap, File orthologs, File diseaseProteinCount, File wormPhenotypes,
			File gwasData) throws FileNotFoundException
	{
		System.out.println("> Building Hash maps....");

		diseaseToHuman = new LinkedHashMap<String, List<String>>();
		humanToWorm = new LinkedHashMap<String, String>();
		diseaseToProtein = new LinkedHashMap<String, String>();
		wormToPhenotype = new LinkedHashMap<String, List<String>>();
		gwasPheno = new LinkedHashMap<String, List<String>>();
		gwasStudy = new LinkedHashMap<String, Set<String>>();

		// Storing data in the database will make file reading obsolete
		setDiseaseMap(diseaseMap);
		Scanner h2w = new Scanner(orthologs);
		Scanner dpc = new Scanner(diseaseProteinCount);
		Scanner w2p = new Scanner(wormPhenotypes);
		Scanner gwasScan = new Scanner(gwasData);

		// Loop through OMIM file, skip the first line (header)

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

		// Loop through the gwas annotation files
		gwasScan.nextLine();
		while (gwasScan.hasNext())
		{

			// SNP(rs id) \t PUBMEDID \t
			// Authors \t Year of publication \t Journal
			String line = gwasScan.nextLine();
			String disease = line.split("\t")[0];
			String enpsID = line.split("\t")[1];
			String snps = line.split("\t")[2];

			String study = line.split("\t")[4] + " (" + line.split("\t")[5] + ") " + line.split("\t")[6] + " "
					+ line.split("\t")[3];

			// If disease is not in the hashmap yet
			if (gwasPheno.get(disease) == null)
			{
				// Put the disease with its ENSP ids into the hashmap.
				// Give a list in case multiple ENSP ids (genes) can cause the
				// same disease
				List<String> myList = new ArrayList<String>();

				myList.add(enpsID);
				gwasPheno.put(disease, myList);
			}

			// If the disease is in the hashmap
			else
			{
				// This is not the first gene causing this disease, add another
				// entry
				gwasPheno.get(disease).add(enpsID);
			}

			// If disease is not in the hashmap yet
			if (gwasStudy.get(disease) == null)
			{
				// Add study data to the hashmap set, set because no duplicate
				// journal entries, also now different studies on same disease
				// are traceable
				Set<String> mySet = new HashSet();
				mySet.add(snps);
				mySet.add(study);

				gwasStudy.put(disease, mySet);
			}
			else
			{
				// Not the first occurence of disease, add study information
				gwasStudy.get(disease).add(snps);
				gwasStudy.get(disease).add(study);

			}

		}

	}

	/**
	 * This method parses the disease map, omim or dga, into a hash with disease
	 * - human protein list key value pairs
	 * 
	 * @author Mark de Haan
	 * @param disease
	 *            map file, e.g. OMIM or DGA
	 * 
	 * */
	public void setDiseaseMap(File diseaseMap) throws FileNotFoundException
	{
		this.diseaseToHuman.clear();
		Scanner dM = new Scanner(diseaseMap);

		dM.nextLine();
		while (dM.hasNext())
		{
			String line = dM.nextLine();

			String enpsID = line.split("\t")[0];
			String disease = line.split("\t")[1];

			// If disease is not in the hashmap yet
			if (this.diseaseToHuman.get(disease) == null)
			{
				// Put the disease with its ENSP ids into the hashmap.
				// Give a list in case multiple ENSP ids (genes) can cause the
				// same disease
				List<String> myList = new ArrayList<String>();
				myList.add(enpsID);
				this.diseaseToHuman.put(disease, myList);
			}
			// If the disease is in the hashmap
			else
			{
				// This is not the first gene causing this disease, add another
				// entry
				this.diseaseToHuman.get(disease).add(enpsID);
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

	/**
	 * This method uses an enpsID to retrieve the diseases that are mapped to it
	 * from the gwas Pheno hash map. This disease can be used to retrieve study
	 * information.
	 * 
	 * @param enpsID
	 * @return disease associated with enpsID
	 * */
	public String getGwasDisease(String enpsID)
	{
		String rt = "NO";

		// Get DISEASE Key from ENPS value
		for (Entry<String, List<String>> value : getGwasPheno().entrySet())
		{
			// System.out.println(value);
			if (getGwasPheno().get(value.getKey()).contains(enpsID))
			{
				// System.out.println(value.getKey());
			}
			else
			{
				// System.out.println("No match in gwas data");
			}
		}

		return rt;

	}
}
