package org.molgenis.wormqtl.etc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author joeri
 */
public class GeneMappingDataSource
{
	String dataSourceName;
	List<String> genes; // really needed?
	Map<String, List<String>> geneToMapping; // useful?
	Map<String, List<String>> geneToDetails; // useful?
	Map<String, List<String>> mappingToGenes; // useful?

	// more ?

	/**
	 * Wraps WormQTL-HD data files for human gene to disease, worm gene to
	 * phenotype assocations, human-worm orthologs, etc
	 * 
	 * Required format: GeneID [tab] SomeMapping [tab] Details
	 * 
	 * 
	 * @param csvTable
	 * @throws Exception
	 */
	public GeneMappingDataSource(File csvTable, String dataSourceName) throws Exception
	{
		Map<String, List<String>> geneToDisease = new HashMap<String, List<String>>();
		Map<String, List<String>> geneToDetails = new HashMap<String, List<String>>();

		Scanner s = new Scanner(csvTable);

		while (s.hasNext())
		{
			String line = s.nextLine();

			String[] split = null;
			String geneID, mapping, details = null;

			try
			{
				split = line.split("\t");
				geneID = split[0];
				mapping = split[1];
				details = split[2];
			}
			catch (Exception e)
			{
				System.out.println("BAD FORMAT for file " + csvTable.getAbsolutePath() + " at line '" + line + "'");
				throw e;
			}

			if (genes.contains(geneID))
			{
				geneToDisease.get(geneID).add(mapping);
				geneToDetails.get(geneID).add(details);
			}
			else
			{
				genes.add(geneID);
				List<String> mappingList = new ArrayList<String>();
				List<String> detailsList = new ArrayList<String>();
				mappingList.add(mapping);
				detailsList.add(details);
				geneToDisease.put(geneID, mappingList);
				geneToDetails.put(geneID, detailsList);
			}
		}

		s.close();

		this.dataSourceName = dataSourceName;
		this.geneToMapping = geneToDisease;
		this.geneToDetails = geneToDetails;
	}

	/**
	 * Get the name of this datasource, e.g. 'DGA', 'OMIM' or 'WormBase'
	 * 
	 * @return
	 */
	public String getName()
	{
		return dataSourceName;
	}

	/**
	 * Print all available details for this human gene in HTML format.
	 * 
	 * @param gene
	 *            identifier, e.g. ENSP00000155840
	 * @return
	 */
	public List<String> getDetails(String gene)
	{
		return geneToDetails.get(gene);
	}

	/**
	 * Print all available mappings (mostly diseases) for this human gene ID
	 * 
	 * @param gene
	 *            identifier, e.g. ENSP00000155840
	 * @return
	 */
	public List<String> getMapping(String gene)
	{
		return geneToMapping.get(gene);
	}

	/**
	 * Get available mappings (mostly diseases)
	 * 
	 * @param gene
	 *            identifier, e.g. ENSP00000155840
	 * @return
	 */
	public Set<String> getAllMappings()
	{
		return geneToMapping.keySet();
	}

	/**
	 * Get all mappings (e.g. diseases, phenotypes or orthologs) for a given
	 * gene
	 * 
	 * @param mapping
	 * @return
	 */
	public List<String> getGenes(String mapping)
	{
		return mappingToGenes.get(mapping);
	}
}
