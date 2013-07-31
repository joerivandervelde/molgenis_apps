package plugins.qtlfinder3.results;

import java.util.Map;

import org.molgenis.util.Entity;

import plugins.qtlfinder3.methods.ComparePhenotypesResult;

public class QtlSearchResults
{
	private ComparePhenotypesResult results;
	private Map<String, Entity> qtlSearchHits;

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
}
