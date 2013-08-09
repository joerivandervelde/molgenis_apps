package plugins.qtlfinder3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.molgenis.cluster.DataValue;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Chromosome;

import plugins.qtlfinder3.resources.HumanToWorm;

public class InitQtlFinderHDModel
{

	public static QtlFinderHDModel init(Database db, HumanToWorm h2w) throws Exception
	{
		QtlFinderHDModel newModel = new QtlFinderHDModel();

		/**
		 * @author Mark de Haan
		 * 
		 *         Generates a list of datasets that contain QTL lod scores.
		 *         Used by searching algorithms that set thresholds for QTL
		 *         values
		 */

		// give user dropdown of datasets that contain LOD scores
		List<DataValue> dvList = db.find(DataValue.class, new QueryRule(DataValue.DATANAME_NAME, Operator.EQUALS,
				"LOD_score"));

		List<String> dataNames = new ArrayList<String>();

		for (DataValue dv : dvList)
		{
			dataNames.add(dv.getValue_Name());
		}

		// list with datasets to be shown in dropdown menu
		newModel.getQtlSearchInputState().setDataSets(dataNames);

		newModel.setHumanToWorm(h2w);

		//
		newModel.setDiseaseMapping(newModel.getHumanToWorm().humanSourceNames().toArray()[0].toString());

		//
		newModel.getQtlSearchInputState().setSelectedDataSet(newModel.getQtlSearchInputState().getDataSets().get(0));

		//
		newModel.setShoppingCart(new HashMap<String, Entity>());

		//
		newModel.setShowResults(false);

		//
		newModel.setScreenType("humanDisease");

		//
		newModel.setCartView(false);

		// setup human disease search
		List<String> diseases = new ArrayList<String>();
		newModel.getDiseaseSearchInputState().setSelectedDiseases(diseases);

		// setup region search
		freshRegionSearch(newModel, db);

		return newModel;

	}

	public static void freshRegionSearch(QtlFinderHDModel model, Database db) throws DatabaseException
	{
		List<Chromosome> chromosomes = db.find(Chromosome.class);
		LinkedHashMap<String, Chromosome> chrs = new LinkedHashMap<String, Chromosome>();
		for (Chromosome chr : chromosomes)
		{
			chrs.put(chr.getName(), chr);
		}
		model.getRegionSearchInputState().setChromosomes(chrs);
		model.getRegionSearchInputState().setSelectedChromosome(chromosomes.get(0).getName());
		int startBp = (int) (((double) chromosomes.get(0).getBpLength()) / 10.0);
		int endBp = (int) (((double) chromosomes.get(0).getBpLength()) / 6.0);
		model.getRegionSearchInputState().setSelectedStartBp(startBp);
		model.getRegionSearchInputState().setSelectedEndBp(endBp);
	}

}
