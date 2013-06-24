/**
 * 
 */
package plugins.qtlfinder3;

import java.util.LinkedHashMap;
import java.util.List;

import org.molgenis.wormqtl.etc.HumanToWorm;
import org.molgenis.wormqtl.etc.HypergeometricTest;

import plugins.qtlfinder2.QtlFinderModel2;

/**
 * @author mark
 * 
 */
public class QtlFinderHDModel extends QtlFinderModel2
{
	// Prefix for actions coming from the human worm association form
	public final String prefix = "__qtlfinderhd__";

	// Class for building the translation table hashMaps
	private HumanToWorm humanToWorm;

	// The disease that is selected by the user
	private String disease;

	// The phenotype that is selected by the user
	private String phenotype;

	// List with data sets from the database that has Lod scores for QTLs
	private List<String> dataSets;

	// Data set that is selected for the QTL search
	private String dataSet;

	// User specified (and parsed) list of ENPS ids
	private List<String> humanGeneQuery;

	// A double containing the result of the hyper geometric test
	private double hyperTestProbability;

	// Class for calculating the significance of a disease - worm gene
	// association
	private HypergeometricTest hyperGeometric;

	// List with diseases that are linked to the probes found in the
	// qtl hotspot (QTL region search)
	private LinkedHashMap<String, List<String>> geneAssociatedDiseases;

	// If true, then result screen will show a table with genes in the region +
	// diseases mapped to them
	private Boolean showTable;

	// Worm phenotype that is selected for phenotype comparison
	private List<String> selectedPhenotype;

	public String getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(String dataSet)
	{
		this.dataSet = dataSet;
	}

	public List<String> getDataSets()
	{
		return dataSets;
	}

	public void setDataSets(List<String> dataSets)
	{
		this.dataSets = dataSets;
	}

	public String getDisease()
	{
		return disease;
	}

	public void setDisease(String disease)
	{
		this.disease = disease;
	}

	public HumanToWorm getHumanToWorm()
	{
		return humanToWorm;
	}

	public void setHumanToWorm(HumanToWorm humanToWorm)
	{
		this.humanToWorm = humanToWorm;
	}

	public List<String> getHumanGeneQuery()
	{
		return humanGeneQuery;
	}

	public void setHumanGeneQuery(List<String> humanGeneQuery)
	{
		this.humanGeneQuery = humanGeneQuery;
	}

	public double getHyperTestProbability()
	{
		return hyperTestProbability;
	}

	public void setHyperTestProbability(double hyperTestProbability)
	{
		this.hyperTestProbability = hyperTestProbability;
	}

	public HypergeometricTest getHypergeometricTest()
	{
		return hyperGeometric;
	}

	public void setHypergeometricTest(HypergeometricTest hyperGeometric)
	{
		this.hyperGeometric = hyperGeometric;
	}

	public LinkedHashMap<String, List<String>> getGeneAssociatedDiseases()
	{
		return geneAssociatedDiseases;
	}

	public void setGeneAssociatedDiseases(LinkedHashMap<String, List<String>> geneAssociatedDiseases)
	{
		this.geneAssociatedDiseases = geneAssociatedDiseases;
	}

	public Boolean getShowTable()
	{
		return showTable;
	}

	public void setShowTable(Boolean showTable)
	{
		this.showTable = showTable;
	}

	public List<String> getSelectedPhenotype()
	{
		return selectedPhenotype;
	}

	public void setSelectedPhenotype(List<String> selectedPhenotype)
	{
		this.selectedPhenotype = selectedPhenotype;
	}

	public String getPhenotype()
	{
		return phenotype;
	}

	public void setPhenotype(String phenotype)
	{
		this.phenotype = phenotype;
	}
}
