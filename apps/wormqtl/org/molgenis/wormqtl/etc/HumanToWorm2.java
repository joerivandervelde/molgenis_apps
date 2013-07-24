package org.molgenis.wormqtl.etc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HumanToWorm2
{

	Map<String, GeneMappingDataSource> humanSources;
	Map<String, GeneMappingDataSource> wormSources;
	GeneMappingDataSource humanToWormOrthologs;

	/**
	 * Create HumanToWorm object which takes care of all the mapping between
	 * human disease, human genes, worm gene orthologs, and worm phenotypes.
	 * 
	 * The idea is to create this object once and keep it in memory. The worm
	 * probes are queried from the database and are not in this object.
	 * 
	 * @param humanSources
	 * @param wormSources
	 * @param humanToWormOrthologs
	 * @throws Exception
	 */
	public HumanToWorm2(List<GeneMappingDataSource> humanSources, List<GeneMappingDataSource> wormSources,
			GeneMappingDataSource humanToWormOrthologs) throws Exception
	{
		Map<String, GeneMappingDataSource> humanSourcesMap = new HashMap<String, GeneMappingDataSource>();
		for (GeneMappingDataSource g : humanSources)
		{
			humanSourcesMap.put(g.getName(), g);
		}
		this.humanSources = humanSourcesMap;

		Map<String, GeneMappingDataSource> wormSourcesMap = new HashMap<String, GeneMappingDataSource>();
		for (GeneMappingDataSource g : wormSources)
		{
			wormSourcesMap.put(g.getName(), g);
		}
		this.wormSources = wormSourcesMap;

		this.humanToWormOrthologs = humanToWormOrthologs;

		// find out for which human diseases have genes for which there is at
		// least 1 worm ortholog
		Set<String> allHumanGenesWithOrtholog = this.humanToWormOrthologs.geneToMapping.keySet();
		for (String source : this.humanSources.keySet())
		{
			for (String disease : this.humanSources.get(source).getAllMappings())
			{
				List<String> humanGenes = this.humanSources.get(source).getGenes(disease);

				if (Collections.disjoint(humanGenes, allHumanGenesWithOrtholog))
				{
					throw new Exception("Human disease '" + disease + "' has no worm orthologs!");
				}
			}
		}

	}

	public Set<String> humanSourceNames()
	{
		return this.humanSources.keySet();
	}

	/**
	 * Get all disease ('mapping') names for a given source
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public Set<String> allHumanDiseases(String dataSourceName)
	{
		return this.humanSources.get(dataSourceName).getAllMappings();
	}

	/**
	 * Get all disease ('mapping') names for a given source for which at least 1
	 * of the genes have a worm ortholog
	 * 
	 * @param dataSourceName
	 * @return
	 */
	// public Set<String> allHumanDiseasesHavingWormOrtholog(String
	// dataSourceName)
	// {
	// //
	// }

	/**
	 * Get all disease ('mapping') names for a given source
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public Set<String> allWormPhenotypes(String dataSourceName)
	{
		return this.humanSources.get(dataSourceName).getAllMappings();
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public List<String> humanDiseaseToHumanGenes(String disease, String sourceName)
	{
		List<String> result = new ArrayList<String>();
		// TODO: implement
		return result;
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public List<String> humanDiseaseToHumanGenesHavingWormOrtholog(String disease, String sourceName)
	{
		List<String> result = new ArrayList<String>();
		// TODO: implement
		return result;
	}

	/**
	 * This method converts a human disease to a list of orthologous worm genes
	 * 
	 * TODO: can be made more efficient with a helper map? disease -> worm genes
	 * 
	 * @param disease
	 * @return a list of worm genes that are ortholog for the disease that was
	 *         put in
	 * @throws Exception
	 */
	public List<String> humanDiseaseToWormGenes(String disease, String dataSource) throws Exception
	{
		List<String> humanGenes = this.humanSources.get(dataSource).getGenes(disease);
		List<String> wormGenes = new ArrayList<String>();

		// For every human gene (ENSP id) that is linked to the entered disease
		// get the WBgene ID(s)
		for (String humanGene : humanGenes)
		{
			String wormGene = humanGeneToWormGene(humanGene);
			wormGenes.add(wormGene);
		}

		return wormGenes;
	}

	/**
	 * Helper function to get the worm orthologs. We don't allow direct access
	 * to the hashmap so we can do some additional checks here.
	 * 
	 * @param humanGene
	 * @return
	 * @throws Exception
	 */
	public String humanGeneToWormGene(String humanGene) throws Exception
	{
		List<String> wormGenes = humanToWormOrthologs.getMapping(humanGene);
		if (wormGenes == null)
		{
			return null;
		}
		if (wormGenes.size() > 1)
		{
			throw new Exception("There are multiple mappings in worm for human gene '" + humanGene + "'");
		}
		return wormGenes.get(0);
	}

	/**
	 * Helper function to get the human orthologs. We don't allow direct access
	 * to the hashmap so we can do some additional checks here.
	 * 
	 * NOTE: uses the same object (humanToWormOrthologs) as
	 * getHumanToWormOrtholog() but now in reverse (call getGenes instead of
	 * getMapping)
	 * 
	 * @param humanGene
	 * @return
	 * @throws Exception
	 */
	public String wormGeneToHumanGene(String wormGene) throws Exception
	{
		List<String> humanGenes = humanToWormOrthologs.getGenes(wormGene);
		if (humanGenes.size() > 1)
		{
			throw new Exception("There are multiple mappings in human for worm gene '" + wormGene + "'");
		}
		return humanGenes.get(0);
	}

	/**
	 * This method takes a worm gene, and goes back through two hashmaps to
	 * determine what human disease is associated with this worm gene through
	 * ortholog matching
	 * 
	 * @param wbGene
	 * @return a list containing a wb Gene on index 0 with one (or more)
	 *         diseases associated with that gene
	 * @throws Exception
	 */
	public List<String> wormGeneToHumanDiseases(String wbGene, String dataSourceName) throws Exception
	{
		String humanOrtholog = this.wormGeneToHumanGene(wbGene);
		return this.humanSources.get(dataSourceName).getMapping(humanOrtholog);
	}

	/**
	 * Get all phenotypes related to a worm gene, from a specific data source
	 * (right now: only "WormBase")
	 * 
	 * @param sourceName
	 * @return
	 */
	public List<String> wormGeneToWormPhenotypes(String gene, String sourceName)
	{
		return wormSources.get(sourceName).getMapping(gene);
	}

	/**
	 * Get all genes associated with a phenotype
	 * 
	 * @param phenotype
	 * @param sourceName
	 * @return
	 */
	public List<String> wormPhenotypeToWormGenes(String phenotype, String sourceName)
	{
		return wormSources.get(sourceName).getGenes(phenotype);
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public List<String> wormPhenotypeToWormGenesHavingHumanOrtholog(String disease, String sourceName)
	{
		List<String> result = new ArrayList<String>();
		// TODO: implement
		return result;
	}

}
