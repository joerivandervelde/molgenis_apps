/**
 * 
 */
package plugins.qtlfinder3;

import plugins.qtlfinder2.QtlFinderModel2;
import plugins.qtlfinder3.inputstate.DiseaseSearchInputState;
import plugins.qtlfinder3.inputstate.PhenoCompareInputState;
import plugins.qtlfinder3.inputstate.QtlSearchInputState;
import plugins.qtlfinder3.inputstate.RegionSearchInputState;
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

	private RegionSearchResults regionSearchResults;
	private QtlSearchResults qtlSearchResults;
	private DiseaseSearchResults diseaseSearchResults;
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

		this.regionSearchResults = new RegionSearchResults();
		this.qtlSearchResults = new QtlSearchResults();
		this.diseaseSearchResults = new DiseaseSearchResults();
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

	// If true, the user can see multiplot, shoppingcart, cartview, overlap
	// table
	private String showAnyResultToUser;

	// String that says if OMIM, DGA, gwascentral, gwascatalog or wormbase worm
	// phenotype is set
	private String diseaseMapping;

	// With example mode on, freemarker can use the boolean to fill in example
	// values
	private Boolean exampleMode;

	public HumanToWorm getHumanToWorm()
	{
		return humanToWorm;
	}

	public void setHumanToWorm(HumanToWorm humanToWorm)
	{
		this.humanToWorm = humanToWorm;
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

	public Boolean getExampleMode()
	{
		return exampleMode;
	}

	public void setExampleMode(Boolean exampleMode)
	{
		this.exampleMode = exampleMode;
	}
}
