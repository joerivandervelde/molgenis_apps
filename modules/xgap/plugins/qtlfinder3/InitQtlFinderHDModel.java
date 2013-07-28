package plugins.qtlfinder3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.molgenis.cluster.DataValue;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.util.Entity;

import plugins.qtlfinder3.resources.GeneMappingDataSource;
import plugins.qtlfinder3.resources.HumanToWorm;
import decorators.MolgenisFileHandler;

public class InitQtlFinderHDModel
{

	public static QtlFinderHDModel init(Database db) throws Exception
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

		/**
		 * Pre-loads the hashmaps used by the HumanToWorm class by reading in
		 * files
		 * 
		 * @author Mark de Haans
		 */

		MolgenisFileHandler filehandle = new MolgenisFileHandler(db);
		File storage = filehandle.getFileStorage(true, db);

		GeneMappingDataSource omim = new GeneMappingDataSource(new File(storage, "human_disease_OMIM.csv"), "OMIM");
		GeneMappingDataSource dga = new GeneMappingDataSource(new File(storage, "human_disease_DGA.csv"), "DGA");

		GeneMappingDataSource gwascentral = new GeneMappingDataSource(
				new File(storage, "human_disease_GWASCENTRAL.csv"), "GWAS Central");
		GeneMappingDataSource gwascatalog = new GeneMappingDataSource(
				new File(storage, "human_disease_GWASCATALOG.csv"), "GWAS Catalog");
		GeneMappingDataSource wormPheno = new GeneMappingDataSource(new File(storage, "worm_disease.csv"), "WormBase");
		GeneMappingDataSource humanToWorm = new GeneMappingDataSource(new File(storage, "orthologs.csv"), "INPARANOID");

		List<GeneMappingDataSource> humanSources = new ArrayList<GeneMappingDataSource>();
		humanSources.add(omim);
		humanSources.add(dga);
		humanSources.add(gwascentral);
		humanSources.add(gwascatalog);

		List<GeneMappingDataSource> wormSources = new ArrayList<GeneMappingDataSource>();
		wormSources.add(wormPheno);

		HumanToWorm h2w2 = new HumanToWorm(humanSources, wormSources, humanToWorm, db);

		newModel.setHumanToWorm(h2w2);

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

		return newModel;

	}

}
