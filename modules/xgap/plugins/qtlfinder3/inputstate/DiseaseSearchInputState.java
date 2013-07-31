package plugins.qtlfinder3.inputstate;

import java.util.List;

public class DiseaseSearchInputState
{
	// List of diseases that is filled with disease selections made by the user
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
