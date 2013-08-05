package plugins.qtlfinder3.methods;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComparePhenotypesResult
{
	private String sampleSource;
	private Set<String> sampleInputs;
	private Set<String> sampleGenes;
	private int sampleSizePruned;
	private double baseThreshold;
	
	private Map<String, Double> sourceToBonferroniThreshold;
	private Map<String, Integer> sourceToPopulationSizePruned;
	private Map<String, Map<String, Integer>> sourceToPhenoToSuccessStatesPruned;
	private Map<String, Map<String, Integer>> sourceToPhenoToSuccesses;
	private Map<String, Map<String, Double>> sourceToPhenoToPval;
	private Map<String, Map<String, Set<String>>> sourceToPhenoToGenes;
	private Map<String, Map<String, Map<String, Set<String>>>> sourceToPhenoToOverlappingGenes;
	private Map<String, Map<String, Set<String>>> sourceToPhenoToDetails;

	public ComparePhenotypesResult()
	{
		this.sourceToBonferroniThreshold = new HashMap<String, Double>();
		this.sourceToPopulationSizePruned = new HashMap<String, Integer>();
		this.sourceToPhenoToSuccessStatesPruned = new HashMap<String, Map<String, Integer>>();
		this.sourceToPhenoToSuccesses = new HashMap<String, Map<String, Integer>>();
		this.sourceToPhenoToPval = new HashMap<String, Map<String, Double>>();
		this.sourceToPhenoToGenes = new HashMap<String, Map<String, Set<String>>>();
		this.sourceToPhenoToOverlappingGenes = new HashMap<String, Map<String, Map<String, Set<String>>>>();
		this.sourceToPhenoToDetails = new HashMap<String, Map<String, Set<String>>>();
	}
	
	
	
	

	public Map<String, Map<String, Map<String, Set<String>>>> getSourceToPhenoToOverlappingGenes()
	{
		return sourceToPhenoToOverlappingGenes;
	}





	public Set<String> getSampleGenes()
	{
		return sampleGenes;
	}





	public void setSampleGenes(Set<String> sampleGenes)
	{
		this.sampleGenes = sampleGenes;
	}





	public Map<String, Map<String, Set<String>>> getSourceToPhenoToGenes()
	{
		return sourceToPhenoToGenes;
	}





	public Map<String, Map<String, Set<String>>> getSourceToPhenoToDetails()
	{
		return sourceToPhenoToDetails;
	}

	public String getSampleSource()
	{
		return sampleSource;
	}

	public void setSampleSource(String sampleSource)
	{
		this.sampleSource = sampleSource;
	}

	

	public Set<String> getSampleInputs()
	{
		return sampleInputs;
	}





	public void setSampleInputs(Set<String> sampleInputs)
	{
		this.sampleInputs = sampleInputs;
	}





	public double getBaseThreshold()
	{
		return baseThreshold;
	}

	public void setBaseThreshold(double baseThreshold)
	{
		this.baseThreshold = baseThreshold;
	}

	public Map<String, Double> getSourceToBonferroniThreshold()
	{
		return sourceToBonferroniThreshold;
	}


	public int getSampleSizePruned()
	{
		return sampleSizePruned;
	}

	public void setSampleSizePruned(int sampleSizePruned)
	{
		this.sampleSizePruned = sampleSizePruned;
	}


	public Map<String, Integer> getSourceToPopulationSizePruned()
	{
		return sourceToPopulationSizePruned;
	}



	public Map<String, Map<String, Integer>> getSourceToPhenoToSuccessStatesPruned()
	{
		return sourceToPhenoToSuccessStatesPruned;
	}

	public Map<String, Map<String, Integer>> getSourceToPhenoToSuccesses()
	{
		return sourceToPhenoToSuccesses;
	}

	public Map<String, Map<String, Double>> getSourceToPhenoToPval()
	{
		return sourceToPhenoToPval;
	}

}
