package plugins.qtlfinder3.inputstate;

import java.util.List;
import java.util.Map;

public class QtlSearchInputState
{
	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String selectedDataSet;

	// threshold that is set for the qtl search
	private Double lodThreshold;

	// trait or probe name that was put in
	private String traitInput;

	private Map<String, List<String>> probesForSelectedDataset;

	public List<String> getDataSets()
	{
		return dataSets;
	}

	public void setDataSets(List<String> dataSets)
	{
		this.dataSets = dataSets;
	}

	public String getSelectedDataSet()
	{
		return selectedDataSet;
	}

	public void setSelectedDataSet(String selectedDataSet)
	{
		this.selectedDataSet = selectedDataSet;
	}

	public Double getLodThreshold()
	{
		return lodThreshold;
	}

	public void setLodThreshold(Double double1)
	{
		this.lodThreshold = double1;
	}

	public String getTraitInput()
	{
		return traitInput;
	}

	public void setTraitInput(String traitInput)
	{
		this.traitInput = traitInput;
	}

	public Map<String, List<String>> getProbesForSelectedDataset()
	{
		return probesForSelectedDataset;
	}

	public void setProbesForSelectedDataset(Map<String, List<String>> probesPerDataset)
	{
		this.probesForSelectedDataset = probesPerDataset;
	}
}
