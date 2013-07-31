package plugins.qtlfinder3.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.molgenis.framework.db.Database;
import org.molgenis.xgap.Probe;

public class HumanToWorm
{

	Map<String, GeneMappingDataSource> humanSources;
	Map<String, GeneMappingDataSource> wormSources;
	GeneMappingDataSource humanToWormOrthologs;

	Map<String, Map<String, List<String>>> probeToSourceToDisease;
	Map<String, List<String>> humanDiseasesHavingOrthologyPerSource;
	Map<String, List<String>> wormPhenotypeHavingOrthologyPerSource;
	Set<String> allGenesInOrthologs;

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
	public HumanToWorm(List<GeneMappingDataSource> humanSources, List<GeneMappingDataSource> wormSources,
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
					// System.out.println("Source '" + source +
					// "', human disease '" + disease +
					// "' has worm orthologs!");
					// TODO: allow diseases with no orthologs, ie. useless ones?
				}
			}
		}
		
		
		HashSet<String> allGenesInOrthologs = new HashSet<String>(this.humanToWormOrthologs.getAllMappings());
		allGenesInOrthologs.addAll(this.humanToWormOrthologs.getAllGenes());
		this.allGenesInOrthologs = allGenesInOrthologs;
		

	}

	/**
	 * total number of ortholog mappings
	 * 
	 * @return
	 */
	public int numberOfOrthologsBetweenHumanAndWorm()
	{
		return this.humanToWormOrthologs.getTotalNumberOfEdges();
	}

	public Set<String> humanSourceNames()
	{
		return this.humanSources.keySet();
	}

	public Set<String> wormSourceNames()
	{
		return this.wormSources.keySet();
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
	 * All worm genes present in the ortholog mapping
	 * 
	 * @return
	 */
	public Set<String> allWormGenesInOrthologs()
	{
		return this.humanToWormOrthologs.getAllMappings();
	}

	/**
	 * All human genes present in the ortholog mapping
	 * 
	 * @return
	 */
	public Set<String> allHumanGenesInOrthologs()
	{
		return this.humanToWormOrthologs.getAllGenes();
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> allGenesInOrthologs()
	{
		return this.allGenesInOrthologs;
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public Set<String> sourceToGenes(String source) throws Exception
	{
		if(this.humanSources.keySet().contains(source))
		{
			return humanDiseasesToHumanGenes(humanDiseasesWithOrthology(source), source);
		}
		else if(this.wormSources.keySet().contains(source))
		{
			return wormPhenotypesToWormGenes(wormPhenotypesWithOrthology(source), source);
		}
		else
		{
			throw new Exception("Unknown source: " + source);
		}
	}
	
	/**
	 * 
	 */
	public Set<String> sourceToGenesWithOrthologs(String source) throws Exception
	{
		if(this.humanSources.keySet().contains(source))
		{
			return humanDiseasesToHumanGenesWithOrthology(humanDiseasesWithOrthology(source), source);
		}
		else if(this.wormSources.keySet().contains(source))
		{
			return wormPhenotypesToWormGenesWithOrthology(wormPhenotypesWithOrthology(source), source);
		}
		else
		{
			throw new Exception("Unknown source: " + source);
		}
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public Set<String> disOrPhenoFromSource(String source) throws Exception
	{
		if(this.humanSources.keySet().contains(source))
		{
			return humanSources.get(source).getAllMappings();
		}
		else if(this.wormSources.keySet().contains(source))
		{
			return wormSources.get(source).getAllMappings();
		}
		else
		{
			throw new Exception("Unknown source: " + source);
		}
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
//	private Set<String> overlapSampleCache;
	public int overlap(Set<String> sample, Set<String> genesForDisOrPheno) throws Exception
	{
//		boolean newSample = false;
//		if(this.overlapSampleCache == null || this.overlapSampleCache != sample)
//		{
//			System.out.println("NEW SAMPLE !!");
//			newSample = true;
//			this.overlapSampleCache = sample;
//		}
//		
//		if(newSample){
		//check if all sample inputs are from one organism
		boolean sampleIsHuman = this.humanToWormOrthologs.getAllGenes().containsAll(sample);
		if(!sampleIsHuman)
		{
			if(!this.humanToWormOrthologs.getAllMappings().containsAll(sample))
			{
				throw new Exception("Sample input is neither fully worm nor human");
			}
		}
//		}
		
		//check if all genesForDisOrPheno inputs are from one organism
		boolean genesForDisOrPhenoIsHuman = this.humanToWormOrthologs.getAllGenes().containsAll(genesForDisOrPheno);
		if(!genesForDisOrPhenoIsHuman)
		{
			if(!this.humanToWormOrthologs.getAllMappings().containsAll(genesForDisOrPheno))
			{
				throw new Exception("GenesForDisOrPhenoIsHuman input is neither fully worm nor human");
			}
		}
		
		// if sample and genesForDisOrPhenoIsHuman are the same organism
		// we can just intersect to get the overlap
		if((sampleIsHuman && genesForDisOrPhenoIsHuman) || (!sampleIsHuman && !genesForDisOrPhenoIsHuman))
		{
			genesForDisOrPheno.retainAll(sample);
			return genesForDisOrPheno.size();
		}
		else
		{
				int overlapTotal = 0;
				for(String gene : sample)
				{
					//get the orthologs
					List<String> orthologs;
					if(sampleIsHuman)
					{
						orthologs = humanToWormOrthologs.getMapping(gene);
					}
					else
					{
						orthologs = humanToWormOrthologs.getGenes(gene);
					}
					
					//remove orthologs that are not in the disease/phenotype
					orthologs.retainAll(genesForDisOrPheno);
					
					//if there are still multiple orthologs for this disease/phenotype in the cross-organism, treat it as 1
					//we cannot test against one-to-many relations, because there is potentially more overlap than sample/draw size!
					overlapTotal += orthologs.size() > 1 ? 1 : orthologs.size();
				}
			return overlapTotal;
		}
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public List<String> genesForDisOrPheno(String disOrPheno, String source) throws Exception
	{
		if(this.humanSources.keySet().contains(source))
		{
			return humanSources.get(source).getGenes(disOrPheno);
		}
		else if(this.wormSources.keySet().contains(source))
		{
			return wormSources.get(source).getGenes(disOrPheno);
		}
		else
		{
			throw new Exception("Unknown source: " + source);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> allSources()
	{
		Set<String> allSrc = new HashSet<String>(this.humanSources.keySet());
		allSrc.addAll(this.wormSources.keySet());
		return allSrc;
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public List<String> humanDiseaseToHumanGenes(String disease, String sourceName)
	{
		return humanSources.get(sourceName).getGenes(disease);
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public Set<String> humanDiseasesToHumanGenes(List<String> diseases, String sourceName)
	{
		Set<String> genes = new HashSet<String>();
		for (String disease : diseases)
		{
			genes.addAll(humanSources.get(sourceName).getGenes(disease));
		}
		return genes;
	}

	/**
	 * 
	 * @param phenotypes
	 * @return
	 */
	public Set<String> wormPhenotypesToWormGenes(List<String> phenotypes, String sourceName)
	{
		Set<String> genes = new HashSet<String>();
		for (String phenotype : phenotypes)
		{
			genes.addAll(wormSources.get(sourceName).getGenes(phenotype));
		}
		return genes;
	}

	/**
	 * 
	 * @param disease
	 * @return
	 */
	public Set<String> humanDiseasesToHumanGenesWithOrthology(List<String> diseases, String sourceName)
	{
		Set<String> genes = humanDiseasesToHumanGenes(diseases, sourceName);
		genes.retainAll(humanToWormOrthologs.getAllGenes());
		return genes;
	}

	/**
	 * 
	 * @param phenotype
	 * @return
	 */
	public Set<String> wormPhenotypesToWormGenesWithOrthology(List<String> phenotypes, String sourceName)
	{
		Set<String> genes = wormPhenotypesToWormGenes(phenotypes, sourceName);
		genes.retainAll(humanToWormOrthologs.getAllMappings());
		return genes;
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
		return humanSources.get(sourceName).getGenes(disease);
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
