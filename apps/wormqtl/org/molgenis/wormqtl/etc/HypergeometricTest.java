/**
 * 
 */
package org.molgenis.wormqtl.etc;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

/**
 * @author Mark de Haan
 * @version 1.0
 * @since 13/05/2013
 */
public class HypergeometricTest
{
	public double hyperGeometricTest(int nGenes, int nOrthologs, int nPerDisease, int nFound)
	{
		HypergeometricDistribution h = new HypergeometricDistribution(nGenes, nOrthologs, nPerDisease);
		return h.upperCumulativeProbability(nFound);
	}
}
