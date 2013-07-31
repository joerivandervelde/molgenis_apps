package plugins.qtlfinder3.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparePhenotypesResult
{
	private String sampleSource;
	private List<String> samplePhenotypesOrGenes;
	private int sampleSize;
	private int sampleSizePruned;
	
	private double baseThreshold;
	private Map<String, Double> sourceToBonferroniThreshold;

	private Map<String, Integer> sourceToPopulationSize;
	private Map<String, Integer> sourceToPopulationSizePruned;
	
	private Map<String, Map<String, Integer>> sourceToPhenoToSuccessStates;
	private Map<String, Map<String, Integer>> sourceToPhenoToSuccessStatesPruned;
	
	private Map<String, Map<String, Integer>> sourceToPhenoToSuccesses;
	private Map<String, Map<String, Double>> sourceToPhenoToPval;
	
	public ComparePhenotypesResult()
	{
		this.samplePhenotypesOrGenes = new ArrayList<String>();
		this.sourceToBonferroniThreshold = new HashMap<String, Double>();
		
		this.sourceToPopulationSize = new HashMap<String, Integer>();
		this.sourceToPopulationSizePruned = new HashMap<String, Integer>();
		
		this.sourceToPhenoToSuccessStates = new HashMap<String, Map<String, Integer>>();
		this.sourceToPhenoToSuccessStatesPruned = new HashMap<String, Map<String, Integer>>();
		
		this.sourceToPhenoToSuccesses = new HashMap<String, Map<String, Integer>>();
		this.sourceToPhenoToPval = new HashMap<String, Map<String, Double>>();
		
	}
	
	



	public String getSampleSource()
	{
		return sampleSource;
	}





	public void setSampleSource(String sampleSource)
	{
		this.sampleSource = sampleSource;
	}





	public void setSamplePhenotypesOrGenes(List<String> samplePhenotypesOrGenes)
	{
		this.samplePhenotypesOrGenes = samplePhenotypesOrGenes;
	}





	public List<String> getSamplePhenotypesOrGenes()
	{
		return samplePhenotypesOrGenes;
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


	public int getSampleSize()
	{
		return sampleSize;
	}
	public void setSampleSize(int sampleSize)
	{
		this.sampleSize = sampleSize;
	}
	public int getSampleSizePruned()
	{
		return sampleSizePruned;
	}
	public void setSampleSizePruned(int sampleSizePruned)
	{
		this.sampleSizePruned = sampleSizePruned;
	}
	public Map<String, Integer> getSourceToPopulationSize()
	{
		return sourceToPopulationSize;
	}
	public Map<String, Integer> getSourceToPopulationSizePruned()
	{
		return sourceToPopulationSizePruned;
	}

	public Map<String, Map<String, Integer>> getSourceToPhenoToSuccessStates()
	{
		return sourceToPhenoToSuccessStates;
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
