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

	/**
	 * Called once in 'FrontController' on app startup, fails unless the
	 * database has been populated. So it fails on first startup, but works on
	 * subsequent startups. (cought by plugin that initialized the data)
	 * Preloads a bunch of data to make browsing fast.
	 * 
	 * @param mc
	 * @throws Exception
	 */
	public WormQTLPreloadService(MolgenisContext mc) throws Exception
	{
		System.out.println("Preloading human2worm..");
		Connection conn = mc.getDataSource().getConnection();
		Database db = DatabaseFactory.create(conn);
		try
		{
			HumanToWorm h2w = createHumanToWorm(db);
			mc.getServletContext().setAttribute("humantoworm", h2w);
			System.out.println("Preloading human2worm SUCCESFUL!");
		}
		catch (Exception e)
		{
			System.out.println("Preloading human2worm FAILED!");
		}
	}

	/**
	 * Create humanToWorm object, provided database has been setup & populated.
	 * 
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public static HumanToWorm createHumanToWorm(Database db) throws Exception
	{
		MolgenisFileHandler filehandle = new MolgenisFileHandler(db);
		File storage = null;

		try
		{
			storage = filehandle.getFileStorage(true, db);
		}
		catch (Exception e)
		{
			System.out
					.println("FAILED: Could not preload human2worm, please FIRST populate the database and setup the file storage location, THEN restart the application to preload!");
			throw e;
		}

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

		return h2w;

	}

	@Override
	public void handleRequest(MolgenisRequest request, MolgenisResponse response) throws ParseException,
			DatabaseException, IOException
	{
		throw new IOException("This service does not accept requests.");
	}

}