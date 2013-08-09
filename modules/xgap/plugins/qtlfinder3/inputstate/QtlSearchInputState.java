package plugins.qtlfinder3.inputstate;

import java.util.List;

public class QtlSearchInputState
{
	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String selectedDataSet;

	// threshold that is set for the qtl search
	private Integer lodThreshold;

	// trait or probe name that was put in
	private String traitInput;

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

	public Integer getLodThreshold()
	{
		return lodThreshold;
	}

	public void setLodThreshold(Integer lodThreshold)
	{
		this.lodThreshold = lodThreshold;
	}

	public String getTraitInput()
	{
		return traitInput;
	}

	public void setTraitInput(String traitInput)
	{
		this.traitInput = traitInput;
	}
}
