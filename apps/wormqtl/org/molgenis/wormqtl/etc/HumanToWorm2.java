package org.molgenis.wormqtl.etc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.molgenis.framework.db.Database;
import org.molgenis.xgap.Probe;

public class HumanToWorm2
{

	Map<String, GeneMappingDataSource> humanSources;
	Map<String, GeneMappingDataSource> wormSources;
	GeneMappingDataSource humanToWormOrthologs;

	Map<String, Map<String, List<String>>> probeToSourceToDisease;
	Map<String, List<String>> humanDiseasesHavingOrthologyPerSource;
	Map<String, List<String>> wormPhenotypeHavingOrthologyPerSource;

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
			GeneMappingDataSource humanToWormOrthologs, Database db) throws Exception
	{
		// put all human disease sources in a map
		Map<String, GeneMappingDataSource> humanSourcesMap = new HashMap<String, GeneMappingDataSource>();
		for (GeneMappingDataSource g : humanSources)
		{
			humanSourcesMap.put(g.getName(), g);
		}
		this.humanSources = humanSourcesMap;

		// put all worm phenotype sources in a map
		Map<String, GeneMappingDataSource> wormSourcesMap = new HashMap<String, GeneMappingDataSource>();
		for (GeneMappingDataSource g : wormSources)
		{
			wormSourcesMap.put(g.getName(), g);
		}
		this.wormSources = wormSourcesMap;

		// save ortholog mapping
		// validate that there all relations are 1-to-1
		List<String> humanGenesInOrthologs = new ArrayList<String>();
		List<String> wormGenesInOrthologs = new ArrayList<String>();
		for (String humanGene : humanToWormOrthologs.getAllGenes())
		{
			if (humanGenesInOrthologs.contains(humanGene))
			{
				// throw new
				// Exception("Duplicate human gene in ortholog mapping: " +
				// humanGene);
				// TODO: allow multiple mappings?
			}
			humanGenesInOrthologs.add(humanGene);

			List<String> wormGene = humanToWormOrthologs.getMapping(humanGene);
			if (wormGene.size() != 1)
			{
				// throw new
				// Exception("There is not exactly 1 mapping for human gene: " +
				// humanGene);
				// TODO: allow multiple mappings?
			}

			if (wormGenesInOrthologs.contains(wormGene))
			{
				// throw new
				// Exception("Duplicate worm gene in ortholog mapping: " +
				// wormGene);
				// TODO: allow multiple mappings?
			}
			wormGenesInOrthologs.add(wormGene.get(0));
		}
		this.humanToWormOrthologs = humanToWormOrthologs;

		// create humanDiseasesHavingOrthologyPerSource
		Map<String, List<String>> humanDiseasesHavingOrthologyPerSource = new HashMap<String, List<String>>();
		for (String source : this.humanSources.keySet())
		{
			List<String> diseasesWithOrthology = new ArrayList<String>();
			for (String disease : this.humanSources.get(source).getAllMappings())
			{
				if (!(Collections.disjoint(this.humanSources.get(source).getGenes(disease),
						this.humanToWormOrthologs.getAllGenes())))
				{
					diseasesWithOrthology.add(disease);
				}
			}
			humanDiseasesHavingOrthologyPerSource.put(source, diseasesWithOrthology);
		}
		this.humanDiseasesHavingOrthologyPerSource = humanDiseasesHavingOrthologyPerSource;

		// create wormPhenotypeHavingOrthologyPerSource
		Map<String, List<String>> wormPhenotypeHavingOrthologyPerSource = new HashMap<String, List<String>>();
		for (String source : this.wormSources.keySet())
		{
			List<String> phenotypesWithOrthology = new ArrayList<String>();
			for (String phenotype : this.wormSources.get(source).getAllMappings())
			{
				if (!(Collections.disjoint(this.wormSources.get(source).getGenes(phenotype),
						this.humanToWormOrthologs.getAllMappings())))
				{
					phenotypesWithOrthology.add(phenotype);
				}
			}
			wormPhenotypeHavingOrthologyPerSource.put(source, phenotypesWithOrthology);
		}
		this.wormPhenotypeHavingOrthologyPerSource = wormPhenotypeHavingOrthologyPerSource;

		// preload complex probe-disease mapping
		Map<String, Map<String, List<String>>> probeToSourceToDisease = new HashMap<String, Map<String, List<String>>>();
		for (Probe p : db.find(Probe.class))
		{
			String geneName = null;
			if (p.getReportsFor_Name() != null && p.getReportsFor_Name().length() > 0)
			{
				geneName = p.getReportsFor_Name();
			}
			else if (p.getSymbol() != null && p.getSymbol().length() > 0)
			{
				geneName = p.getSymbol();
			}

			if (geneName == null)
			{
				continue;
			}

			String humanOrtholog = wormGeneToHumanGene(geneName);

			if (humanOrtholog == null)
			{
				continue;
			}

			for (String source : humanSourceNames())
			{
				List<String> diseases = humanGeneToHumanDisease(humanOrtholog, source);
				if (diseases != null && diseases.size() > 0)
				{
					if (probeToSourceToDisease.keySet().contains(p.getName()))
					{
						probeToSourceToDisease.get(p.getName()).put(source, diseases);
					}
					else
					{
						Map<String, List<String>> sourceToDiseases = new HashMap<String, List<String>>();
						sourceToDiseases.put(source, diseases);
						probeToSourceToDisease.put(p.getName(), sourceToDiseases);
					}
				}
			}
		}

		this.probeToSourceToDisease = probeToSourceToDisease;

		// find out for which human diseases have genes for which there is at
		// least 1 worm ortholog
		Set<String> allHumanGenesWithOrtholog = this.humanToWormOrthologs.geneToMapping.keySet();
		for (String source : this.humanSources.keySet())
		{
			for (String disease : this.humanSources.get(source).getAllMappings())
			{
				List<String> humanGenes = this.humanSources.get(source).getGenes(disease);

				if (!Collections.disjoint(humanGenes, allHumanGenesWithOrtholog))
				{
					// FIXME: enable when data is curated to check for unwanted
					// disease entries
					System.out.println("Source '" + source + "', human disease '" + disease + "' has worm orthologs!");
					// TODO: allow diseases with no orthologs, ie. useless ones?
				}
			}
		}

	}

	/**
	 * total number of ortholog mappings
	 * 
	 * @return
	 */
	public int numberOfOrthologsBetweenHumanAndWorm()
	{
		return this.humanToWormOrthologs.getAllGenes().size();
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
	// public List<String> humanDiseaseToHumanGenesHavingWormOrtholog(String
	// disease, String sourceName)
	// {
	// List<String> result = new ArrayList<String>();
	// // TODO: implement
	// return result;
	// }

	/**
	 * 
	 * @param dataSource
	 * @return
	 */
	public List<String> humanDiseasesWithOrthology(String dataSource)
	{
		return this.humanDiseasesHavingOrthologyPerSource.get(dataSource);
	}

	/**
	 * 
	 * @param dataSource
	 * @return
	 */
	public List<String> wormPhenotypesWithOrthology(String dataSource)
	{
		return this.wormPhenotypeHavingOrthologyPerSource.get(dataSource);
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

	public List<String> wormPhenotypeToHumanGenes(String phenotype, String dataSource) throws Exception
	{
		List<String> wormGenes = this.wormSources.get(dataSource).getGenes(phenotype);
		List<String> humanGenes = new ArrayList<String>();

		for (String wormGene : wormGenes)
		{
			String humanGene = wormGeneToHumanGene(wormGene);
			humanGenes.add(humanGene);
		}

		return humanGenes;
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
			// throw new
			// Exception("There are multiple mappings in worm for human gene '"
			// + humanGene + "'");
			// TODO: allow multiple mappings?
		}
		return wormGenes.get(0);
	}

	/**
	 * Get all disease related to a human gene, from a specific data source
	 * (right now: only "WormBase")
	 * 
	 * @param sourceName
	 * @return
	 */
	public List<String> humanGeneToHumanDisease(String gene, String sourceName)
	{
		return humanSources.get(sourceName).getMapping(gene);
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
		if (humanGenes == null)
		{
			return null;
		}
		if (humanGenes.size() > 1)
		{
			// throw new
			// Exception("There are multiple mappings in human for worm gene '"
			// + wormGene + "'");
			// TODO: allow multiple mappings?
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

	/**
	 * From a worm probe, get all human disease, for each data source. This is
	 * preloaded because its a complicated mapping
	 * (probe-wbgene-humangene-humandisease/source)
	 * 
	 * @param probe
	 * @param dataSource
	 * @return
	 */
	public Map<String, List<String>> wormProbeToDataSourceToHumanDiseases(String probe)
	{
		Map<String, List<String>> dataSourceToDiseases = probeToSourceToDisease.get(probe);
		if (dataSourceToDiseases == null)
		{
			return new HashMap<String, List<String>>();
		}
		return probeToSourceToDisease.get(probe);
	}

}
