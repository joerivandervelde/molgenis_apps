package plugins.qtlfinder3.results;

import java.util.Map;

import org.molgenis.util.Entity;

import plugins.qtlfinder.QTLMultiPlotResult;

public class DiseaseSearchResults
{
	// current hits for disease search, these will be reloaded to the global
	// hits map when disease search function is selected. Only set when search
	// changes
	private Map<String, Entity> diseaseSearchHits;

	private QTLMultiPlotResult multiplot;

	public Map<String, Entity> getDiseaseSearchHits()
	{
		return diseaseSearchHits;
	}

	public void setDiseaseSearchHits(Map<String, Entity> diseaseSearchHits)
	{
		this.diseaseSearchHits = diseaseSearchHits;
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
