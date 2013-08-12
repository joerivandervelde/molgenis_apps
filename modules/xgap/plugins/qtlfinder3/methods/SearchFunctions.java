package plugins.qtlfinder3.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import matrix.DataMatrixInstance;
import matrix.general.DataMatrixHandler;

import org.molgenis.data.Data;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.util.Entity;
import org.molgenis.xgap.Chromosome;
import org.molgenis.xgap.Marker;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder3.resources.HumanToWorm;

public class SearchFunctions
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

	public static List<Probe> qtlRegionSearch(String trait, String dataset, double threshold, Database db)
			throws Exception
	{

		DataMatrixHandler dmh = new DataMatrixHandler(db);

		Data selectDataset = db.find(Data.class, new QueryRule(Data.NAME, Operator.EQUALS, dataset)).get(0);
		DataMatrixInstance dataMatrix = dmh.createInstance(selectDataset, db);
		String highestMarkerName = getPeakMarker(dataMatrix, trait, threshold);

		Marker highestMarker = db.find(Marker.class, new QueryRule(Marker.NAME, Operator.EQUALS, highestMarkerName))
				.get(0);

		Query<Probe> fiftyBefore = db.query(Probe.class);
		fiftyBefore.equals(Probe.CHROMOSOME_NAME, highestMarker.getChromosome_Name());
		fiftyBefore.lessOrEqual(Probe.BPSTART, highestMarker.getBpStart());
		fiftyBefore.sortDESC(Probe.BPSTART);
		fiftyBefore.limit(50);
		List<Probe> probes1 = fiftyBefore.find();

		Query<Probe> fiftyAfter = db.query(Probe.class);
		fiftyAfter.equals(Probe.CHROMOSOME_NAME, highestMarker.getChromosome_Name());
		fiftyAfter.greaterOrEqual(Probe.BPSTART, highestMarker.getBpStart());
		fiftyAfter.sortASC(Probe.BPSTART);
		fiftyAfter.limit(50);
		List<Probe> probes2 = fiftyAfter.find();

		probes1.addAll(probes2);

		return probes1;
	}

	/**
	 * This method determines what probes and genes are inside the region that
	 * was selected, either by qtl search or by region search.
	 * 
	 * Calls the HumanToWorm class to determine which diseases are mapped to the
	 * selected region. Sets table view to true to show gene - disease -
	 * probability as a result
	 * 
	 * @author Mark de Haan
	 * @param start
	 * @param end
	 * @param chromosome
	 * @param db
	 * @param search
	 * @throws Exception
	 */

	public static List<Probe> regionSearch(Integer start, Integer end, Integer chromosome, Database db, boolean search)
			throws Exception
	{
		List<Probe> probesInRegion = new ArrayList<Probe>();
		List<Chromosome> chrNeeded = db.find(Chromosome.class, new QueryRule(Chromosome.ORDERNR, Operator.LESS,
				chromosome));

		if (search)
		{
			for (Chromosome chr : chrNeeded)
			{
				start = start + chr.getBpLength();
				end = end + chr.getBpLength();
			}
		}

		probesInRegion = db.find(Probe.class, new QueryRule(Probe.BPSTART, Operator.GREATER_EQUAL, start),
				new QueryRule(Probe.BPSTART, Operator.LESS_EQUAL, end));

		return probesInRegion;

	}

	/**
	 * This method retrieves the highest scoring QTL from a certain probe and
	 * determines which marker that QTL is located on
	 * 
	 * @author Mark de Haan
	 * @param dm
	 * @param trait
	 * @param threshold
	 * @param screenmodel
	 * @return returns the name of the marker that has the highest scoring qtl
	 * @throws Exception
	 */
	private static String getPeakMarker(DataMatrixInstance dm, String trait, double threshold) throws Exception
	{
		// TODO: Change method of determining region inside QTL
		// Now: Highest scoring marker + / - 10.000 bp positions = Region

		String bestMarker;
		// Double flankLeft = 0.0;
		// Double flankRight = 0.0;
		Double highest = 0.0;
		Integer highestIdx = 0;
		
		Object[] myTraitQtlScore;
		if(dm.getRowNames().contains(trait))
		{
			myTraitQtlScore = dm.getRow(trait);
		}
		else if(dm.getColNames().contains(trait))
		{
			myTraitQtlScore = dm.getCol(trait);
		}
		else
		{
			throw new Exception("There is no probe/trait named '" + trait+ "' in dataset '" + dm.getData().getName() + "'");
		}

		for (int i = 0; i < myTraitQtlScore.length; i++)
		{
			if(myTraitQtlScore[i] == null)
			{
				continue;
			}
			
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
			
			if(dm.getRowNames().contains(trait))
			{
				bestMarker = dm.getColNames().get(highestIdx);
			}
			else
			{
				bestMarker = dm.getRowNames().get(highestIdx);
			}

		}
		else
		{
			throw new Exception("There was no QTL for '" + trait+ "' above the LOD threshold of " + threshold);
		}
		return bestMarker;
	}

	/**
	 * Disease Search
	 * 
	 * User selects a disease from a dropdown list, genes that are associated
	 * with selected disease via ortholog matching are put in the shopping cart
	 * 
	 * @author Mark de Haan
	 * @param model
	 * @param db
	 * @throws Exception
	 */
	public static List<Probe> diseaseSearch(Database db, String dataSource, List<String> diseases, HumanToWorm h2w)
			throws Exception
	{

		List<String> wormGenes = new ArrayList<String>();

		if (h2w.humanSourceNames().contains(dataSource))
		{
			for (String disease : diseases)
			{
				wormGenes.addAll(h2w.humanDiseaseToWormGenes(disease, dataSource));
			}
		}
		else
		{
			for (String disease : diseases)
			{
				wormGenes.addAll(h2w.wormPhenotypeToWormGenes(disease, dataSource));
			}
		}

		List<Probe> probes = db.find(Probe.class, new QueryRule(Probe.SYMBOL, Operator.IN, wormGenes), new QueryRule(
				Operator.OR), new QueryRule(Probe.REPORTSFOR_NAME, Operator.IN, wormGenes));

		return probes;

	}

	/**
	 * Ortholog Search
	 * 
	 * User can submit human genes. The algorithm will then determine the
	 * ortholog genes belonging to these human genes.
	 * 
	 * TODO: Greatly increase the input possibilities
	 * 
	 * @author Mark de Haan
	 * @param db
	 * @param model
	 * @param humanGeneQuery
	 */

	public static List<Probe> orthologSearch(String[] humanGeneQuery, HumanToWorm h2w, Database db) throws Exception
	{
		List<String> enpsIDs = new ArrayList<String>(Arrays.asList(humanGeneQuery));
		List<String> orthologs = new ArrayList<String>();
		for (String enpsID : enpsIDs)
		{
			String ortholog = h2w.humanGeneToWormGene(enpsID);
			if (ortholog == null)
			{
				continue;
			}
			orthologs.add(ortholog);
		}

		List<Probe> probes = db.find(Probe.class, new QueryRule(Probe.SYMBOL, Operator.IN, orthologs));

		return probes;

	}

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
	public static List<? extends Entity> qtlSearch(String dataset, Integer start, Integer end, Integer chromosome,
			Integer threshold, Database db) throws Exception
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
			throw new Exception("No markers where found within this region of chromosome " + chromosome);
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

				return traits;
			}
			else
			{
				// FIXME unimplemented!
				throw new Exception("UNIMPLEMENTED qtlSearch");
			}
		}
	}
}
