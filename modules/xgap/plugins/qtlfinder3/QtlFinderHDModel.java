/**
 * 
 */
package plugins.qtlfinder3;

import java.util.List;
import java.util.Map;

import plugins.qtlfinder2.QtlFinderModel2;
import plugins.qtlfinder3.inputstate.DiseaseSearchInputState;
import plugins.qtlfinder3.inputstate.PhenoCompareInputState;
import plugins.qtlfinder3.inputstate.QtlSearchInputState;
import plugins.qtlfinder3.inputstate.RegionSearchInputState;
import plugins.qtlfinder3.methods.HypergeometricTest;
import plugins.qtlfinder3.resources.HumanToWorm;
import plugins.qtlfinder3.results.DiseaseSearchResults;
import plugins.qtlfinder3.results.PhenoCompareResults;
import plugins.qtlfinder3.results.QtlSearchResults;
import plugins.qtlfinder3.results.RegionSearchResults;

/**
 * @author mark
 * 
 */
public class QtlFinderHDModel extends QtlFinderModel2
{

	private RegionSearchInputState regionSearchInputState;
	private QtlSearchInputState qtlSearchInputState;
	private DiseaseSearchInputState diseaseSearchInputState;
	private PhenoCompareInputState phenoCompareInputState;

	private DiseaseSearchResults diseaseSearchResults;
	private QtlSearchResults qtlSearchResults;
	private RegionSearchResults regionSearchResults;
	private PhenoCompareResults phenoCompareResults;

	public QtlFinderHDModel()
	{
		this.regionSearchInputState = new RegionSearchInputState();
		this.qtlSearchInputState = new QtlSearchInputState();
		this.diseaseSearchInputState = new DiseaseSearchInputState();
		this.phenoCompareInputState = new PhenoCompareInputState();
		this.diseaseSearchResults = new DiseaseSearchResults();
		this.qtlSearchResults = new QtlSearchResults();
		this.regionSearchResults = new RegionSearchResults();
		this.phenoCompareResults = new PhenoCompareResults();
	}

	public DiseaseSearchInputState getDiseaseSearchInputState()
	{
		return diseaseSearchInputState;
	}

	public PhenoCompareInputState getPhenoCompareInputState()
	{
		return phenoCompareInputState;
	}

	public RegionSearchInputState getRegionSearchInputState()
	{
		return regionSearchInputState;
	}

	public QtlSearchInputState getQtlSearchInputState()
	{
		return qtlSearchInputState;
	}

	public DiseaseSearchResults getDiseaseSearchResults()
	{
		return diseaseSearchResults;
	}

	public QtlSearchResults getQtlSearchResults()
	{
		return qtlSearchResults;
	}

	public RegionSearchResults getRegionSearchResults()
	{
		return regionSearchResults;
	}

	public PhenoCompareResults getPhenoCompareResults()
	{
		return phenoCompareResults;
	}

	// Prefix for actions coming from the human worm association form
	public final String prefix = "__qtlfinderhd__";

	// Class for building the translation table hashMaps
	private HumanToWorm humanToWorm;

	// Class for calculating the significance of a disease - worm gene
	// association
	private HypergeometricTest hyperGeometric;

	// If true, the user can see multiplot, shoppingcart, cartview, overlap
	// table
	private String showAnyResultToUser;

	// User specified (and parsed) list of ENPS ids
	private List<String> humanGeneQuery;

	// A double containing the result of the hyper geometric test
	private double hyperTestProbability;

	// Hash of human phenotypes and number of genes overlapping with the
	// selected worm phenotype
	private Map<String, Map<String, Integer>> allOverlaps;

	// Hash of disease phenotype found and the p value after hyper testing
	// (Compare phenotypes)
	private Map<String, Map<String, Double>> allProbabilities;

	// String that says if OMIM, DGA, gwascentral, gwascatalog or wormbase worm
	// phenotype is set
	private String diseaseMapping;

	public HumanToWorm getHumanToWorm()
	{
		return humanToWorm;
	}

	public void setHumanToWorm(HumanToWorm humanToWorm)
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

	public String getDiseaseMapping()
	{
		return diseaseMapping;
	}

	public void setDiseaseMapping(String diseaseMapping)
	{
		this.diseaseMapping = diseaseMapping;
	}

	public String getShowAnyResultToUser()
	{
		return showAnyResultToUser;
	}

	public void setShowAnyResultToUser(String showAnyResultToUser)
	{
		this.showAnyResultToUser = showAnyResultToUser;
	}
}
