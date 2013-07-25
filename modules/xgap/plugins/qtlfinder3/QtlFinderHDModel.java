/**
 * 
 */
package plugins.qtlfinder3;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.wormqtl.etc.HumanToWorm2;
import org.molgenis.wormqtl.etc.HypergeometricTest;

import plugins.qtlfinder2.QtlFinderModel2;

/**
 * @author mark
 * 
 */
public class QtlFinderHDModel extends QtlFinderModel2
{
	// Prefix for actions coming from the human worm association form
	public final String prefix = "__qtlfinderhd__";

	// Class for building the translation table hashMaps
	private HumanToWorm2 humanToWorm;

	// The disease that is selected by the user
	private String disease;

	// List of diseases selected when user uses OMIM
	private List<String> diseases;

	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String dataSet;

	// User specified (and parsed) list of ENPS ids
	private List<String> humanGeneQuery;

	// A double containing the result of the hyper geometric test
	private double hyperTestProbability;

	// Class for calculating the significance of a disease - worm gene
	// association
	private HypergeometricTest hyperGeometric;

	// List with diseases that are linked to the probes found in the
	// qtl hotspot (QTL region search)
	private LinkedHashMap<String, List<String>> geneAssociatedDiseases;

	// If true, then result screen will show a table with genes in the region +
	// diseases mapped to them
	private Boolean showTable;

	// Worm phenotype that is selected for phenotype comparison
	private String selectedWormPhenotype;

	// Human phenotype that is selected for phenotype comparison
	private String selectedHumanPhenotype;

	// Hash of human phenotypes and number of genes overlapping with the
	// selected worm phenotype
	private Map<String, Map<String, Integer>> allOverlaps;

	// Hash of human phenotype found and the p value after hyper testing
	// (Compare phenotypes)
	private Map<String, Map<String, Double>> allProbabilities;

	// String that says if OMIM or DGA is set
	private String diseaseMapping;

	// String that says which worm mapping is selected (datasource coupled to
	// WormGene ID's)
	// similar to private String diseaseMapping except it's on the worm side of
	// things
	private String phenotypeMapping;

	// Show human or worm phenotype comparison
	private Boolean showWorm;

	// public String getPhenotypeMapping()
	// {
	// return phenotypeMapping;
	// }
	//
	// public void setPhenotypeMapping(String phenotypeMapping)
	// {
	// this.phenotypeMapping = phenotypeMapping;
	// }

	public String getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(String dataSet)
	{
		this.dataSet = dataSet;
	}

	public List<String> getDataSets()
	{
		return dataSets;
	}

	public void setDataSets(List<String> dataSets)
	{
		this.dataSets = dataSets;
	}

	public String getDisease()
	{
		return disease;
	}

	public void setDisease(String disease)
	{
		this.disease = disease;
	}

	public HumanToWorm2 getHumanToWorm()
	{
		return humanToWorm;
	}

	public void setHumanToWorm(HumanToWorm2 humanToWorm)
	{
		this.humanToWorm = humanToWorm;
	}

	public List<String> getHumanGeneQuery()
	{
		return humanGeneQuery;
	}

	public void setHumanGeneQuery(List<String> humanGeneQuery)
	{
		this.humanGeneQuery = humanGeneQuery;
	}

	public double getHyperTestProbability()
	{
		return hyperTestProbability;
	}

	public void setHyperTestProbability(double hyperTestProbability)
	{
		this.hyperTestProbability = hyperTestProbability;
	}

	public HypergeometricTest getHypergeometricTest()
	{
		return hyperGeometric;
	}

	public void setHypergeometricTest(HypergeometricTest hyperGeometric)
	{
		this.hyperGeometric = hyperGeometric;
	}

	public LinkedHashMap<String, List<String>> getGeneAssociatedDiseases()
	{
		return geneAssociatedDiseases;
	}

	public void setGeneAssociatedDiseases(LinkedHashMap<String, List<String>> geneAssociatedDiseases)
	{
		this.geneAssociatedDiseases = geneAssociatedDiseases;
	}

	public Boolean getShowTable()
	{
		return showTable;
	}

	public void setShowTable(Boolean showTable)
	{
		this.showTable = showTable;
	}

	public String getSelectedPhenotype()
	{
		return selectedWormPhenotype;
	}

	public void setSelectedWormPhenotype(String selectedWormPhenotype)
	{
		this.selectedWormPhenotype = selectedWormPhenotype;
	}

	public Map<String, Map<String, Integer>> getAllOverlaps()
	{
		return allOverlaps;
	}

	public void setAllOverlaps(Map<String, Map<String, Integer>> overlapPerDiseasePerSource)
	{
		this.allOverlaps = overlapPerDiseasePerSource;
	}

	public Map<String, Map<String, Double>> getAllProbabilities()
	{
		return allProbabilities;
	}

	public void setAllProbabilities(Map<String, Map<String, Double>> pvalsPerDiseasePerSource)
	{
		this.allProbabilities = pvalsPerDiseasePerSource;
	}

	public String getSelectedHumanPhenotype()
	{
		return selectedHumanPhenotype;
	}

	public void setSelectedHumanPhenotype(String selectedHumanPhenotype)
	{
		this.selectedHumanPhenotype = selectedHumanPhenotype;
	}

	public String getDiseaseMapping()
	{
		return diseaseMapping;
	}

	public void setDiseaseMapping(String diseaseMapping)
	{
		this.diseaseMapping = diseaseMapping;
	}

	public List<String> getDiseases()
	{
		return diseases;
	}

	public void setDiseases(List<String> diseases)
	{
		this.diseases = diseases;
	}

	public Boolean getShowWorm()
	{
		return showWorm;
	}

	public void setShowWorm(Boolean showWorm)
	{
		this.showWorm = showWorm;
	}

}
