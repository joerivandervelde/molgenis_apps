package plugins.qtlfinder3.inputstate;

import java.util.List;

public class DiseaseSearchInputState
{
	// List of diseases selected when user uses OMIM
	private List<String> selectedDiseases;

	public List<String> getSelectedDiseases()
	{
		return selectedDiseases;
	}

	public void setSelectedDiseases(List<String> diseases)
	{
		this.selectedDiseases = diseases;
	}
}
