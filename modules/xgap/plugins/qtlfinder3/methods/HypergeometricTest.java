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
