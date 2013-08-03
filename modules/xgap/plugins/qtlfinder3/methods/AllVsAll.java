package plugins.qtlfinder3.methods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.molgenis.auth.DatabaseLogin;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.security.Login;
import org.molgenis.framework.server.TokenFactory;
import org.molgenis.util.HandleRequestDelegationException;

import plugins.qtlfinder3.InitQtlFinderHDModel;
import plugins.qtlfinder3.QtlFinderHDModel;
import plugins.qtlfinder3.resources.HumanToWorm;
import app.DatabaseFactory;

public class AllVsAll
{
	public AllVsAll(String usr, String pwd) throws HandleRequestDelegationException, Exception
	{
		boolean toFile = false;
		
		
		Database db = getDb(usr, pwd);
		QtlFinderHDModel model = InitQtlFinderHDModel.init(db);
		HumanToWorm h2w = model.getHumanToWorm();
		
		
		File out = new File("all_vs_all.tsv");
		System.out.println("writing to: " + out.getAbsolutePath());
		// if file doesnt exists, then create it
//		if (!out.exists()) {
//			out.createNewFile();
//		}
		FileWriter fw = new FileWriter(out.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//header
		if(toFile){
			for (String sampleSource : h2w.allSources())
			{
				for (String sampleDisOrPheno : h2w.disOrPhenoWithOrthologyFromSource(sampleSource))
				{
					bw.write("\t\"" + sampleSource + "_" + sampleDisOrPheno+"\"");
				}
			}
		
			bw.write("\n");
		}
		
		int populationSize = h2w.numberOfOrthologsBetweenHumanAndWorm();
		
		System.out.println("Population size = " + h2w.numberOfOrthologsBetweenHumanAndWorm());
		
		//print bonferroni
		for (String sampleSource : h2w.allSources())
		{	
			for (String source : h2w.allSources())
			{
				double bonferroniThreshold = 0.05 / (Math.max(h2w.disOrPhenoWithOrthologyFromSource(sampleSource).size(), h2w.disOrPhenoWithOrthologyFromSource(source).size()));
				System.out.println("Bonferroni for " + sampleSource + " vs " + source + ": 0.05 / (Math.max(" +h2w.disOrPhenoWithOrthologyFromSource(sampleSource).size() +", "+ h2w.disOrPhenoWithOrthologyFromSource(source).size() + ") = "+ (Math.max(h2w.disOrPhenoWithOrthologyFromSource(sampleSource).size(), h2w.disOrPhenoWithOrthologyFromSource(source).size())) + ")  = " + bonferroniThreshold);

			}
		}
		
		System.out.println("Phenotype1 (Ce)" + "\t" + "Phenotype2 (Hs)" + "\t" + "n1" + "\t" + "n2" + "\t" + "k" + "\t" + "P value");
		
		TreeMap<Double, String> sortedResults = new TreeMap<Double, String>();
		
		List<String> combinationsAlreadySeen = new ArrayList<String>();
		
		//draw samples from 
		for (String sampleSource : h2w.allSources())
		{
			for (String sampleDisOrPheno : h2w.disOrPhenoWithOrthologyFromSource(sampleSource))
			{
			
				Set<String> sampleGenes = new HashSet<String>(h2w.genesForDisOrPheno(sampleDisOrPheno, sampleSource));
				sampleGenes.retainAll(h2w.allGenesInOrthologs());
				int sampleSize = sampleGenes.size();

				if(toFile){bw.write("\"" + sampleSource + "_" + sampleDisOrPheno + "\"");}
				
				for (String source : h2w.allSources())
				{
					
					double bonferroniThreshold = 0.05 / (Math.max(h2w.disOrPhenoWithOrthologyFromSource(sampleSource).size(), h2w.disOrPhenoWithOrthologyFromSource(source).size()));
					
					for (String disOrPheno : h2w.disOrPhenoWithOrthologyFromSource(source))
					{
						
						Set<String> genesForDisOrPheno = new HashSet<String>(h2w.genesForDisOrPheno(disOrPheno, source));
						genesForDisOrPheno.retainAll(h2w.allGenesInOrthologs());
						int successStates = genesForDisOrPheno.size();
						
						int overlap = h2w.overlap(sampleGenes, genesForDisOrPheno).keySet().size();
						try
						{
							HypergeometricDistribution h = new HypergeometricDistribution(populationSize, successStates, sampleSize);
							double pval = h.upperCumulativeProbability(overlap);
							
							
							
							if(pval < 0.001)
							{
								boolean humanSampleSource = h2w.humanSourceNames().contains(sampleSource);
								boolean humanDisOrPhenSource = h2w.humanSourceNames().contains(source);
								
								if((!humanSampleSource && humanDisOrPhenSource) || (humanSampleSource && !humanDisOrPhenSource))
								{
									String sampleName = "\""+sampleSource + " " + sampleDisOrPheno+"\"";
									String vsDisOrPheno = "\""+source + " " + disOrPheno+"\"";
									
									
									
									if(!sampleName.equals(vsDisOrPheno) && !combinationsAlreadySeen.contains(vsDisOrPheno+sampleName)){
										
										if(sampleSource.equals("WormBase"))
										{
											sortedResults.put(pval, sampleDisOrPheno + "\t" + disOrPheno + " ["+source+"]" + "\t" + sampleSize + "\t" + successStates + "\t" + overlap + "\t" + pval);
										}
										else
										{
											sortedResults.put(pval, disOrPheno + "\t" + sampleDisOrPheno +  " ["+sampleSource+"]" + "\t" + successStates + "\t" + sampleSize + "\t" + overlap + "\t" + pval);
										}
										
										//String res = sampleName + "\t" + vsDisOrPheno + "\t" + pval + "\t" + overlap + "\t" + successStates + "\t" + sampleSize;
										//System.out.println(res);
										combinationsAlreadySeen.add(sampleName+vsDisOrPheno);
									
									}
								}
								
								
								
							}
							
							if(toFile){ bw.write("\t" + pval); }
							
							
						}
						catch(Exception e)
						{
							System.out.println("ERROR FOR " + "\""+sampleSource + " " + sampleDisOrPheno+"\"" + " vs " + "\""+source + " " + disOrPheno+"\"" + " " + e.getMessage());
						}
						
						
						
					}
				}
				
				if(toFile){
					bw.write("\n");
					bw.flush();
				}

				
			}
		}

		bw.close();
		
		for(String res : sortedResults.values())
		{
			System.out.println(res);
		}
		
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

	/**
	 * @param args
	 * @throws Exception
	 * @throws HandleRequestDelegationException
	 */
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

		new AllVsAll(args[0], args[1]);
	}

}
