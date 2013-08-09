package org.molgenis.wormqtl.services;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.server.MolgenisContext;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.server.MolgenisResponse;
import org.molgenis.framework.server.MolgenisService;

import plugins.qtlfinder3.resources.GeneMappingDataSource;
import plugins.qtlfinder3.resources.HumanToWorm;
import app.DatabaseFactory;
import decorators.MolgenisFileHandler;

/**
 * A MolgenisService to clean the tmp dir every hour. Files older than 12 hours
 * are attempted to be deleted. The handleRequest of this service should never
 * be called, instead it is just initialized and uses a sleeping thread to clean
 * up once in a while. This job will be triggered every hour.
 * 
 * @author joerivandervelde
 * 
 */
public class WormQTLPreloadService implements MolgenisService
{
	Logger logger = Logger.getLogger(WormQTLPreloadService.class);

	public WormQTLPreloadService(MolgenisContext mc) throws Exception
	{
		System.out.println("Preloading humanworm..");
		Connection conn = mc.getDataSource().getConnection();
		//Database db = new app.JDBCDatabase(conn);
		Database db = DatabaseFactory.create(conn);	

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

		HumanToWorm h2w = new HumanToWorm(humanSources, wormSources, humanToWorm, db);
		
		mc.getServletContext().setAttribute("humantoworm", h2w);
		
		System.out.println("Preloading humanworm DONE");
	}

	@Override
	public void handleRequest(MolgenisRequest request, MolgenisResponse response) throws ParseException,
			DatabaseException, IOException
	{
		throw new IOException("This service does not accept requests.");
	}

}