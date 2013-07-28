package plugins.qtlfinder3.methods;


public class HumanDiseaseSearch
{

	// /**
	// * Disease Search
	// *
	// * User selects a disease from a dropdown list, genes that are associated
	// * with selected disease via ortholog matching are put in the shopping
	// cart
	// *
	// * @author Mark de Haan
	// * @param model
	// * @param db
	// * @throws Exception
	// */
	// public void diseaseSearch(QtlFinderHDModel model, Database db) throws
	// Exception
	// {
	// model.setHits(new HashMap<String, Entity>());
	// model.setProbeToGene(new HashMap<String, Gene>());
	//
	// List<Probe> probesInRegion = new ArrayList<Probe>();
	// List<String> wormGenes = new ArrayList<String>();
	//
	// if
	// (model.getHumanToWorm().humanSourceNames().contains(model.getDiseaseMapping()))
	// {
	// for (String disease : model.getDiseases())
	// {
	// wormGenes.addAll(model.getHumanToWorm().humanDiseaseToWormGenes(disease,
	// model.getDiseaseMapping()));
	// }
	// }
	// else
	// {
	// for (String disease : model.getDiseases())
	// {
	// System.out.println("diease: " + disease);
	// wormGenes.addAll(model.getHumanToWorm().wormPhenotypeToWormGenes(disease,
	// model.getDiseaseMapping()));
	// System.out.println("genes size: " + wormGenes.size());
	// }
	// }
	//
	// probesInRegion = db.find(Probe.class, new QueryRule(Probe.SYMBOL,
	// Operator.IN, wormGenes), new QueryRule(
	// Operator.OR), new QueryRule(Probe.REPORTSFOR_NAME, Operator.IN,
	// wormGenes));
	//
	// for (Probe p : probesInRegion)
	// {
	// model.getHits().put(p.getName(), p);
	// }
	// }
}
