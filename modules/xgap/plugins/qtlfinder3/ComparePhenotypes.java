package plugins.qtlfinder3;

import java.util.List;

import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.wormqtl.etc.HypergeometricTest;

public class ComparePhenotypes
{

	/**
	 * Compare Phenotypes
	 * 
	 * This function takes a human and worm phenotype, and calculates on the fly
	 * what the exact gene overlap is between these two disease phenotypes. It
	 * will call R scripts for
	 * 
	 * @author Mark de Haan
	 * @param model
	 * @param screenmodel
	 * @param humanphenotype
	 * @param wormphenotype
	 * @throws Exception
	 * */
	public void comparePhenotypes(QtlFinderHDModel model, ScreenModel screenModel, String humanPhenotype,
			String wormPhenotype) throws Exception
	{
		Integer wormPhenoGeneNumber = model.getHumanToWorm().getWormToPhenotype().get(wormPhenotype).size();
		Integer humanPhenoGeneNumber = model.getHumanToWorm().retrieve(humanPhenotype);
		Integer numberOfOrthologs = 4988;
		Integer numberOfOverlappingGenes = 0;

		List<String> wormGenesForThisWormPhenotype = model.getHumanToWorm().getWormToPhenotype().get(wormPhenotype);
		List<String> wormGenesForThisHumanPhenotype = model.getHumanToWorm().convert(humanPhenotype);

		for (String gene : wormGenesForThisWormPhenotype)
		{
			if (wormGenesForThisHumanPhenotype.contains(gene))
			{
				numberOfOverlappingGenes++;
			}
		}

		HypergeometricTest hg = new HypergeometricTest();

		model.setHyperTestProbability(hg.hyperGeometricTest(numberOfOrthologs - wormPhenoGeneNumber, numberOfOrthologs,
				humanPhenoGeneNumber, numberOfOverlappingGenes));

		System.out.println(numberOfOverlappingGenes);

	}
}
