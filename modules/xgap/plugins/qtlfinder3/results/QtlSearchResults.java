package plugins.qtlfinder3.results;

import java.util.Map;

import org.molgenis.util.Entity;

import plugins.qtlfinder.QTLMultiPlotResult;
import plugins.qtlfinder3.methods.ComparePhenotypesResult;

public class QtlSearchResults
{
	private ComparePhenotypesResult results;
	private Map<String, Entity> qtlSearchHits;
	private QTLMultiPlotResult multiplot;

	public ComparePhenotypesResult getResults()
	{
		return results;
	}

	public void setResults(ComparePhenotypesResult results)
	{
		this.results = results;
	}

	public Map<String, Entity> getQtlSearchHits()
	{
		return qtlSearchHits;
	}

	public void setQtlSearchHits(Map<String, Entity> qtlSearchHits)
	{
		this.qtlSearchHits = qtlSearchHits;
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
