package plugins.qtlfinder3.methods;

import java.util.List;

import matrix.DataMatrixInstance;
import matrix.general.DataMatrixHandler;

import org.molgenis.data.Data;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Marker;

import plugins.qtlfinder3.QtlFinderHDModel;
import plugins.qtlfinder3.SetRegion;
import plugins.qtlfinder3.resources.GetProbeExpression;

public class TraitRegionSearch
{

	/**
	 * Probe QTL region search
	 * 
	 * User fills in a probe / trait name, a lod threshold and a dataset to
	 * search in. The algorithm then uses these parameters to determine the
	 * highest QTL score present for the submitted probe. Then based on the
	 * position of the marker containing this QTL, the surrounding region is
	 * taken and a region search is performed. This determines what probes are
	 * located within the QTL peak region
	 * 
	 * @author Mark de Haan
	 * @param db
	 * @param model
	 * @param threshold
	 * @param dataset
	 * @param trait
	 * @param screenModel
	 * @throws Exception
	 */

	public void traitRegionSearch(String trait, String dataset, double threshold, QtlFinderHDModel model, Database db,
			ScreenModel screenModel) throws Exception
	{

		DataMatrixHandler dmh = new DataMatrixHandler(db);

		Data selectDataset = db.find(Data.class, new QueryRule(Data.NAME, Operator.EQUALS, dataset)).get(0);
		DataMatrixInstance dataMatrix = dmh.createInstance(selectDataset, db);

		GetProbeExpression gpe = new GetProbeExpression();
		String highestMarker = gpe.getProbeExpression(dataMatrix, trait, threshold, screenModel);

		List<Marker> highestProbe = db.find(Marker.class, new QueryRule(Marker.NAME, Operator.EQUALS, highestMarker));

		List<Chromosome> chromosomes = db.find(Chromosome.class, new QueryRule(Chromosome.NAME, Operator.EQUALS,
				highestProbe.get(0).getChromosome_Name()));

		// Once the marker has been determined, the region
		// within the Qtl is determines by taking the
		// starting position, and adding or retracting
		// 10.000 (temporary solution)
		Integer start = (int) (highestProbe.get(0).getBpStart() - 10000);
		Integer end = (int) (highestProbe.get(0).getBpStart() + 10000);
		Integer chromosome = chromosomes.get(0).getOrderNr();

		SetRegion sr = new SetRegion();
		sr.setRegion(start, end, chromosome, db, false, model);
	}

}
