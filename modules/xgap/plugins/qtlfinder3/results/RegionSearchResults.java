package plugins.qtlfinder3.results;

import java.util.Map;

import org.molgenis.util.Entity;

import plugins.qtlfinder.QTLMultiPlotResult;
import plugins.qtlfinder3.methods.ComparePhenotypesResult;

public class RegionSearchResults
{
	private ComparePhenotypesResult results;
	private Map<String, Entity> regionSearchHits;
	private QTLMultiPlotResult multiplot;

	public ComparePhenotypesResult getResults()
	{
		return results;
	}

	public void setResults(ComparePhenotypesResult results)
	{
		this.results = results;
	}

	public Map<String, Entity> getRegionSearchHits()
	{
		return regionSearchHits;
	}

	public void setRegionSearchHits(Map<String, Entity> regionSearchHits)
	{
		this.regionSearchHits = regionSearchHits;
	}

	public QTLMultiPlotResult getMultiplot()
	{
		return multiplot;
	}

	public void setMultiplot(QTLMultiPlotResult multiplot)
	{
		this.multiplot = multiplot;
	}
}
