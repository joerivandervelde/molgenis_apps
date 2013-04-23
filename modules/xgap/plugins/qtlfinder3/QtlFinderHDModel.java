/**
 * 
 */
package plugins.qtlfinder3;

import org.molgenis.wormqtl.etc.HumanToWorm;

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
	private HumanToWorm humanToWorm;

	// The disease that is selected by the user
	private String disease;

	// User specified location to start on in region search
	private int regionStartLocation;

	// User specified location to end on in region search
	private int regionEndLocation;

	// User specified chromosome to look at in region search
	private int regionChromosome;

	// The selected search makes it possible to switch between different kinds
	// of searching
	private String selectedSearch;

	public int getRegionStartLocation()
	{
		return regionStartLocation;
	}

	public void setRegionStartLocation(int regionStartLocation)
	{
		this.regionStartLocation = regionStartLocation;
	}

	public int getRegionEndLocation()
	{
		return regionEndLocation;
	}

	public void setRegionEndLocation(int regionEndLocation)
	{
		this.regionEndLocation = regionEndLocation;
	}

	public int getRegionChromosome()
	{
		return regionChromosome;
	}

	public void setRegionChromosome(int regionChromosome)
	{
		this.regionChromosome = regionChromosome;
	}

	public String getDisease()
	{
		return disease;
	}

	public void setDisease(String disease)
	{
		this.disease = disease;
	}

	public HumanToWorm getHumanToWorm()
	{
		return humanToWorm;
	}

	public void setHumanToWorm(HumanToWorm humanToWorm)
	{
		this.humanToWorm = humanToWorm;
	}

	public String getSelectedSearch()
	{
		return selectedSearch;
	}

	public void setSelectedSearch(String selectedSearch)
	{
		this.selectedSearch = selectedSearch;
	}

}
