package plugins.qtlfinder3;

import java.util.List;

import matrix.DataMatrixInstance;
import matrix.general.DataMatrixHandler;

import org.molgenis.data.Data;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Marker;

public class QtlSearch
{

	/**
	 * QTL Search User can submit a region to start and end, a chromosome to
	 * look at, a dataset to search in and a QTL lod score threshold. The
	 * algorithm then looks into the selected region and returns probes if there
	 * is a 'QTL hotspot' present in this region. This hotspot is located at the
	 * position where a QTL peak is above the pre determined threshold
	 * 
	 * TODO: Determine which disease can be mapped to found hotspot
	 * 
	 * @author Joeri van der Velde, Mark de Haan
	 * @param dataset
	 * @param start
	 * @param end
	 * @param chromosome
	 * @param threshold
	 * @param model
	 * @param Database
	 * @param screenmodel
	 * @throws Exception
	 * 
	 */
	public void qtlSearch(String dataset, Integer start, Integer end, Integer chromosome, Integer threshold,
			QtlFinderHDModel model, Database db, ScreenModel screenModel) throws Exception
	{
		List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR, Operator.LESS,
				chromosome));

		for (Chromosome chr : chrNeeded)
		{
			start = start + chr.getBpLength();
			end = end + chr.getBpLength();
		}

		DataMatrixHandler dmh = new DataMatrixHandler(db);
		Data selectDataset = db.find(Data.class, new QueryRule(Data.NAME, Operator.EQUALS, dataset)).get(0);

		DataMatrixInstance dataMatrix = dmh.createInstance(selectDataset, db);

		List<String> markers = selectDataset.getFeatureType().equals(Marker.class.getSimpleName()) ? dataMatrix
				.getColNames() : dataMatrix.getRowNames();

		Query<Marker> q = db.query(Marker.class);
		// Get markers used in dataset name
		q.addRules(new QueryRule(Marker.NAME, Operator.IN, markers));
		// Get markers in specific region
		q.addRules(new QueryRule(Marker.BPSTART, Operator.GREATER_EQUAL, start));
		q.addRules(new QueryRule(Marker.BPSTART, Operator.LESS_EQUAL, end));
		// Save markers selected from region
		List<Marker> regionMarkers = q.find();

		if (regionMarkers.size() == 0)
		{
			screenModel.setMessages(new ScreenMessage("No markers where found within this region of chromosome "
					+ chromosome, false));
		}
		else
		{
			// Get lowest and highest BP number
			Marker lowest = regionMarkers.get(0);
			Marker highest = regionMarkers.get(regionMarkers.size() - 1);
			for (Marker m : regionMarkers)
			{
				if (m.getBpStart().doubleValue() < lowest.getBpStart().doubleValue())
				{
					lowest = m;
				}
				else if (m.getBpStart().doubleValue() > highest.getBpStart().doubleValue())
				{
					highest = m;
				}
			}

			// Slice selected region from datamatrix
			if (selectDataset.getFeatureType().equals(Marker.class.getSimpleName()))
			{
				int colStart = dataMatrix.getColIndexForName(lowest.getName());
				int colStop = dataMatrix.getColIndexForName(highest.getName());

				// cut out slice with our flanking
				// markers(start, stop)
				DataMatrixInstance slice = dataMatrix.getSubMatrixByOffset(0, dataMatrix.getNumberOfRows(), colStart,
						colStop - colStart);

				// we want "1" value per row (trait)
				// with a value GREATER than THRESHOLD
				QueryRule findAboveThreshold = new QueryRule("1", Operator.GREATER, threshold);

				// apply filter and get result: number
				// of rows (traits) are now reduced

				DataMatrixInstance traitsAboveThreshold = slice.getSubMatrix2DFilterByRow(findAboveThreshold);

				List<String> rowNames = traitsAboveThreshold.getRowNames();

				Class<? extends Entity> traitClass = db.getClassForName(selectDataset.getTargetType());
				List<? extends Entity> traits = db.find(traitClass, new QueryRule(ObservationElement.NAME, Operator.IN,
						rowNames));

				String wbGene;

				for (Entity t : traits)
				{
					model.getHits().put(t.get(ObservationElement.NAME).toString(), t);

					if (t.get("symbol") == null)
					{
						continue;
					}

					wbGene = t.get("symbol").toString();

					List<String> myList = model.getHumanToWorm().wormGeneToHumanDiseases(wbGene,
							model.getDiseaseMapping());

					model.getGeneAssociatedDiseases().put(myList.get(0), myList.subList(1, myList.size()));
				}

				model.setShowResults(true);
			}
			else
			{
				// TODO: Do something that is different
			}
		}
	}
}
