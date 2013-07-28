package plugins.qtlfinder3.resources;

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
	Map<String, List<String>> geneToMapping;
	Map<String, List<String>> geneToDetails;
	Map<String, List<String>> mappingToGenes;

	// obviously missing, but not needed? : mappingToDetails

	/**
	 * Wraps WormQTL-HD data files for human gene to disease, worm gene to
	 * phenotype assocations, human-worm orthologs, etc
	 * 
	 * Required format: GeneID [tab] SomeMapping [tab] Details
	 * 
	 * This class is essentially a many-to-many HashMap with some extras
	 * 
	 * 
	 * @param csvTable
	 * @throws Exception
	 */
	public GeneMappingDataSource(File csvTable, String dataSourceName) throws Exception
	{
		Map<String, List<String>> geneToMapping = new HashMap<String, List<String>>();
		Map<String, List<String>> geneToDetails = new HashMap<String, List<String>>();
		Map<String, List<String>> mappingToGenes = new HashMap<String, List<String>>();

		Scanner s = new Scanner(csvTable);

		String[] split = null;
		String geneID, mapping, details = null;

		s.nextLine(); // skip header

		while (s.hasNext())
		{
			String line = s.nextLine();

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

			// add to 'genes -> mapping' map
			if (geneToMapping.keySet().contains(geneID))
			{
				geneToMapping.get(geneID).add(mapping);
				geneToDetails.get(geneID).add(details);
			}
			else
			{
				List<String> mappingList = new ArrayList<String>();
				List<String> detailsList = new ArrayList<String>();
				mappingList.add(mapping);
				detailsList.add(details);
				geneToMapping.put(geneID, mappingList);
				geneToDetails.put(geneID, detailsList);
			}

			// add to 'mapping -> genes' map
			if (mappingToGenes.keySet().contains(mapping))
			{
				mappingToGenes.get(mapping).add(geneID);
			}
			else
			{
				List<String> geneList = new ArrayList<String>();
				geneList.add(geneID);
				mappingToGenes.put(mapping, geneList);
			}

		}

		s.close();

		this.dataSourceName = dataSourceName;
		this.geneToMapping = geneToMapping;
		this.geneToDetails = geneToDetails;
		this.mappingToGenes = mappingToGenes;
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
		return mappingToGenes.keySet();
	}

	/**
	 * Get available genes
	 * 
	 * @return
	 */
	public Set<String> getAllGenes()
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
