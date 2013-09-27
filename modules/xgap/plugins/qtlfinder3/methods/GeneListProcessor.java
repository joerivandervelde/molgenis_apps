package plugins.qtlfinder3.methods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.molgenis.auth.DatabaseLogin;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.security.Login;
import org.molgenis.framework.server.TokenFactory;
import org.molgenis.util.HandleRequestDelegationException;
import org.molgenis.xgap.Probe;

import plugins.qtlfinder3.resources.GeneMappingDataSource;
import plugins.qtlfinder3.resources.HumanToWorm;
import app.DatabaseFactory;
import decorators.MolgenisFileHandler;

public class GeneListProcessor
{

	public GeneListProcessor(String usr, String pwd) throws HandleRequestDelegationException, Exception
	{
		Database db = getDb(usr, pwd);

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

		File out = new File("processedGeneList.txt");
		System.out.println("writing to: " + out.getAbsolutePath());
		FileWriter fw = new FileWriter(out.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		Map<String, String> geneConversion = generateWBGeneIds(new File(storage, "geneListForYang_short.txt"), db);

		for (String gene : geneConversion.keySet())
		{
			bw.write(gene + "\t");

			if (gene == null)
			{
				System.out.println("no gene at all, skipping to next gene...");
				bw.write("not in the WormQTL database\n");
				continue;
			}

			bw.write(h2w.wormGeneToHumanGene(geneConversion.get(gene)) + "\t");

			if (h2w.wormGeneToHumanGene(geneConversion.get(gene)) != null)
			{
				String ortholog = h2w.wormGeneToHumanGene(geneConversion.get(gene));
				for (String source : h2w.allSources())
				{
					if (source.equals("WormBase"))
					{
						bw.write(source + ": " + h2w.wormGeneToWormPhenotypes(geneConversion.get(gene), source) + "\n");
					}
					else
					{
						bw.write(source + ": " + h2w.humanGeneToHumanDisease(ortholog, source) + "\n");
					}
				}
			}
			else
			{
				bw.write("WormBase: " + h2w.wormGeneToWormPhenotypes(geneConversion.get(gene), "WormBase") + "\n");
				continue;
			}
		}

		bw.close();

	}

	private Map<String, String> generateWBGeneIds(File file, Database db) throws FileNotFoundException,
			DatabaseException
	{

		Map<String, String> geneConversion = new HashMap<String, String>();
		Scanner openFile = new Scanner(file);

		List<String> geneList = new ArrayList<String>();

		while (openFile.hasNext())
		{
			String line = openFile.nextLine();
			geneList.add(line);
		}

		for (String gene : geneList)
		{
			List<Probe> probe = db.find(Probe.class, new QueryRule(Probe.LABEL, Operator.EQUALS, gene.trim()));
			if (probe.isEmpty())
			{
				geneConversion.put(gene, "NULL");
			}
			else
			{
				geneConversion.put(gene, probe.get(0).getReportsFor_Name());
			}
		}

		return geneConversion;
	}

	private Database getDb(String usr, String pwd) throws HandleRequestDelegationException, Exception
	{
		/*
		 * minEvictableIdleTimeMillis="4000"
		 * timeBetweenEvictionRunsMillis="5000" maxActive="20" minIdle="4"
		 * maxIdle="8"
		 */
		// create db
		BasicDataSource data_src = new BasicDataSource();
		data_src.setDriverClassName("org.hsqldb.jdbcDriver");
		data_src.setUsername("sa");
		data_src.setPassword("");
		data_src.setUrl("jdbc:hsqldb:file:hsqldb/molgenisdb;shutdown=true");
		data_src.setInitialSize(10);
		data_src.setTestOnBorrow(true);
		data_src.setMinEvictableIdleTimeMillis(4000);
		data_src.setTimeBetweenEvictionRunsMillis(5000);
		data_src.setMaxActive(20);
		data_src.setMinIdle(4);
		data_src.setMaxIdle(8);
		DataSource dataSource = (DataSource) data_src;
		Connection conn = dataSource.getConnection();
		conn = dataSource.getConnection();
		conn = dataSource.getConnection();
		// Database db = new app.JDBCDatabase(conn);
		Database db = DatabaseFactory.create(conn);

		// login
		Login login = new DatabaseLogin(new TokenFactory());
		login.login(db, usr, pwd);
		db.setLogin(login);
		System.out.println("logged in as: " + db.getLogin().getUserName());
		return db;
	}

	public static void main(String[] args) throws HandleRequestDelegationException, Exception
	{
		// arg checking
		if (args.length != 2 || args[0].length() == 0 || args[1].length() == 0)
		{
			throw new IllegalArgumentException(
					"You must supply username and password. Add e.g. 'admin admin' to program arguments. Add '-Xmx2g' to VM arguments to make it fast.");
		}

		System.out.println("starting ExampleQueries, arguments ok");
		System.out.println("user: " + args[0]);
		char[] stars = new char[args[1].length()];
		Arrays.fill(stars, '*');
		String starString = new String(stars);
		System.out.println("pass: " + starString);

		new GeneListProcessor(args[0], args[1]);
	}

}
