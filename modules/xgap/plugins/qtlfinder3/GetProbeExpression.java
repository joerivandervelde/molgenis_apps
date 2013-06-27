package plugins.qtlfinder3;

import matrix.DataMatrixInstance;

import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.ScreenModel;

public class GetProbeExpression
{
	/**
	 * This method retrieves the highest scoring QTL from a certain probe and
	 * determines which marker that QTL is located on
	 * 
	 * @author Mark de Haan
	 * @param dm
	 * @param probe
	 * @param threshold
	 * @param screenmodel
	 * @return returns the name of the marker that has the highest scoring qtl
	 * @throws Exception
	 */
	public String getProbeExpression(DataMatrixInstance dm, String probe, double threshold, ScreenModel screenModel)
			throws Exception
	{
		// TODO: Change method of determining region inside QTL
		// Now: Highest scoring marker + / - 10.000 bp positions = Region

		String bestMarker = "";
		// Double flankLeft = 0.0;
		// Double flankRight = 0.0;
		Double highest = 0.0;
		Integer highestIdx = 0;
		Object[] myTraitQtlScore = dm.getRow(probe);

		for (int i = 0; i < myTraitQtlScore.length; i++)
		{
			if (highest == 0)
			{
				highest = (Double) myTraitQtlScore[i];
				highestIdx = i;
			}
			else
			{
				if (highest < (Double) myTraitQtlScore[i])
				{
					highest = (Double) myTraitQtlScore[i];
					highestIdx = i;
				}
				else
				{
					continue;
				}
			}
		}

		if (highest > threshold)
		{
			// flankLeft = (Double) myTraitQtlScore[(highestIdx - 1)];
			// flankRight = (Double) myTraitQtlScore[(highestIdx + 1)];

			// System.out.println("Flanking left: " + flankLeft +
			// " The highest QTL is: " + highest + " Flanking right: "
			// + flankRight);

			bestMarker = dm.getColNames().get(highestIdx);

		}
		else
		{
			screenModel.setMessages(new ScreenMessage(
					"There was no QTL for this probe / trait that scored above the submitted threshold", false));
		}
		return bestMarker;
	}
}
