/**
 * 
 */
package plugins.qtlfinder3;

import java.util.List;

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

	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String dataSet;

	// Lod threshold for the QTL searching
	private double lodThreshold;

	// User specified location to start on in QTL region search
	private int QtlRegionStartLocation;

	// User specified location to end on in QTL region search
	private int QtlRegionEndLocation;

	// User specified chromosome to look at in region search
	private int QtlRegionChromosome;

	// User specified (and parsed) list of ENPS ids
	private List<String> humanGeneQuery;

	public int getQtlRegionStartLocation()
	{
		return QtlRegionStartLocation;
	}

	public void setQtlRegionStartLocation(int qtlRegionStartLocation)
	{
		QtlRegionStartLocation = qtlRegionStartLocation;
	}

	public int getQtlRegionEndLocation()
	{
		return QtlRegionEndLocation;
	}

	public void setQtlRegionEndLocation(int qtlRegionEndLocation)
	{
		QtlRegionEndLocation = qtlRegionEndLocation;
	}

	public int getQtlRegionChromosome()
	{
		return QtlRegionChromosome;
	}

	public void setQtlRegionChromosome(int qtlRegionChromosome)
	{
		QtlRegionChromosome = qtlRegionChromosome;
	}

	public double getLodThreshold()
	{
		return lodThreshold;
	}

	public void setLodThreshold(double lodThreshold)
	{
		this.lodThreshold = lodThreshold;
	}

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

	public List<String> getHumanGeneQuery()
	{
		return humanGeneQuery;
	}

	public void setHumanGeneQuery(List<String> humanGeneQuery)
	{
		this.humanGeneQuery = humanGeneQuery;
	}
}
