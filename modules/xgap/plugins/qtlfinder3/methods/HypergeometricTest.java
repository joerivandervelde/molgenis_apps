/**
 * 
 */
package plugins.qtlfinder3.methods;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

/**
 * @author Mark de Haan
 * @version 1.0
 * @since 13/05/2013
 */
public class HypergeometricTest
{

	/**
	 * This method performs a hyper geometric test. A distribution is created
	 * based on the total number of proteins present in the population, the
	 * total number of orthologs found between two species and the total number
	 * of proteins that are found to be associated with disease of interest. A
	 * propability is then calculated using the number of proteins are
	 * associated with the disease AND are orthologs
	 * 
	 * <br>
	 * <br>
	 * HypergeometricDistribution <br>
	 * <b>populationSize</b>: total (unique?) number of human genes in a data
	 * source <br>
	 * <b>numberOfSuccesses</b>: for 1 disease, the subsection of genes
	 * associated (-> multiple testing correction applies here because we test
	 * many of these subsections) <br>
	 * <b>sampleSize</b>: the number of genes from the collection we're testing
	 * against. This collection is usually derived from worm orthologs, and can
	 * be predefined as a 'worm phenotype' or come from region/QTL searches. <br>
	 * <b>upperCumulativeProbability(int x)</b>: the number of overlapping genes
	 * between numberOfSuccesses and sampleSize
	 * 
	 * @param nGenes
	 * @param nOrthologs
	 * @param nPerDisease
	 * @param nFound
	 * @return a double value representing the result of the hyper geometric
	 *         test
	 */
	public double hyperGeometricTest(int nGenes, int nOrthologs, int nPerDisease, int nFound)
	{
		HypergeometricDistribution h = new HypergeometricDistribution(nGenes, nOrthologs, nPerDisease);
		return h.upperCumulativeProbability(nFound);
	}
}
