package org.molgenis.omicsconnect.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.omx.core.DataSet;
import org.molgenis.omx.core.Feature;
import org.molgenis.omx.core.Individual;
import org.molgenis.omx.core.ObservationSet;
import org.molgenis.omx.core.ObservedValue;
import org.molgenis.omx.core.Protocol;

import app.DatabaseFactory;

public class GenerateExampleData
{
	public static void main(String[] args) throws DatabaseException
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);

		int noColumns = 1000;

		int noRows = 1000;

		Database db = DatabaseFactory.create();
		try
		{
			db.beginTx();

			// create protocol
			Protocol p = new Protocol();
			p.setIdentifier("p2test");
			p.setName("dit is een test");

			db.add(p);

			// create features
			List<Feature> fList = new ArrayList<Feature>();
			for (int i = 0; i < noColumns; i++)
			{
				Feature f = new Feature();
				f.setIdentifier("f" + i + "test");
				f.setName("test feature " + i);
				fList.add(f);
			}
			db.add(fList);

			// add features to protocol
			List<Integer> fIds = new ArrayList<Integer>();
			for (Feature f : fList)
			{
				fIds.add(f.getId());
			}
			p.setFeatures_Id(fIds);
			db.update(p);

			// create targets
			List<Individual> iList = new ArrayList<Individual>();
			for (int i = 0; i < noRows; i++)
			{
				Individual f = new Individual();
				f.setIdentifier("i" + i + "test");
				f.setName("feature " + i);
				iList.add(f);
			}
			db.add(iList);

			// create a data set
			DataSet ds = new DataSet();
			ds.setIdentifier("ds1");
			ds.setName("test data set");
			ds.setProtocolUsed(p.getId());
			db.add(ds);

			// get values
			List<ObservedValue> vList = new ArrayList<ObservedValue>();
			for (Individual i : iList)
			{
				ObservationSet os = new ObservationSet();
				os.setTarget_Id(i.getId());
				os.setPartOfDataSet(ds.getId());

				db.add(os);

				for (Feature of : fList)
				{
					ObservedValue v = new ObservedValue();
					v.setFeature(of);
					v.setObservationSet(os.getId());
					v.setValue(Double.toString(Math.random()));
					vList.add(v);
				}
			}
			db.add(vList);

			db.commitTx();
		}
		catch (Exception e)
		{
			db.rollbackTx();
		}

	}
}
