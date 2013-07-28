package plugins.qtlfinder3.inputstate;

import java.util.List;

public class QtlSearchInputState
{
	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String selectedDataSet;

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

}
