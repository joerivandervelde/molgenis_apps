package plugins.qtlfinder3.inputstate;

import java.util.LinkedHashMap;

import org.molgenis.xgap.Chromosome;

public class RegionSearchInputState
{
	private Integer selectedStartBp;
	private Integer selectedEndBp;
	private String selectedChromosome;
	private String inputGene;

	private LinkedHashMap<String, Chromosome> chromosomes;

	public Integer getSelectedStartBp()
	{
		return selectedStartBp;
	}

	public void setSelectedStartBp(Integer selectedStartBp)
	{
		this.selectedStartBp = selectedStartBp;
	}

	public Integer getSelectedEndBp()
	{
		return selectedEndBp;
	}

	public void setSelectedEndBp(Integer selectedEndBp)
	{
		this.selectedEndBp = selectedEndBp;
	}

	public String getSelectedChromosome()
	{
		return selectedChromosome;
	}

	public void setSelectedChromosome(String selectedChromosome)
	{
		this.selectedChromosome = selectedChromosome;
	}

	public LinkedHashMap<String, Chromosome> getChromosomes()
	{
		return chromosomes;
	}

	public String getInputGene()
	{
		return inputGene;
	}

	public void setInputGene(String inputGene)
	{
		this.inputGene = inputGene;
	}

	public void setChromosomes(LinkedHashMap<String, Chromosome> chromosomes)
	{
		this.chromosomes = chromosomes;
	}

}
