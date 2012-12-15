package org.molgenis.omx.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.tupletable.AbstractFilterableTupleTable;
import org.molgenis.framework.tupletable.DatabaseTupleTable;
import org.molgenis.framework.tupletable.TableException;
import org.molgenis.model.elements.Field;
import org.molgenis.omx.core.DataSet;
import org.molgenis.omx.core.ObservationSet;
import org.molgenis.omx.core.ObservedValue;
import org.molgenis.omx.core.StringObservedValue;
import org.molgenis.util.SimpleTuple;
import org.molgenis.util.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * DataSetTable
 * 
 * If this table is too slow consider creating database an index on the
 * ObservedValue table : One on the fields Feature-Value and one on
 * ObservationSet-Feature-Value
 * 
 */
public class DataSetTable extends AbstractFilterableTupleTable implements DatabaseTupleTable
{
	private static Logger logger = Logger.getLogger(DataSetTable.class);
	private DataSet dataSet;
	private Database db;
	private List<Field> columns;

	public DataSetTable(DataSet set, Database db) throws TableException
	{
		if (set == null) throw new TableException("DataSet cannot be null");
		this.dataSet = set;
		if (db == null) throw new TableException("db cannot be null");
		setDb(db);
		setFirstColumnFixed(true);
	}

	@Override
	public Database getDb()
	{
		return db;
	}

	@Override
	public void setDb(Database db)
	{
		this.db = db;
	}

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}

	@Override
	public List<Field> getAllColumns() throws TableException
	{
		if (columns == null) initColumnsFromDb();
		return Collections.unmodifiableList(columns);
	}

	private void initColumnsFromDb() throws TableException
	{
		try
		{
			// instead ask for protocol.features?

			String sql = "SELECT DISTINCT Characteristic.identifier as name, Characteristic.name as label FROM Characteristic, ObservedValue, ObservationSet WHERE ObservationSet.partOfDataSet="
					+ dataSet.getId()
					+ " AND ObservedValue.ObservationSet=ObservationSet.id AND Characteristic.id = ObservedValue.feature";

			columns = new ArrayList<Field>();
			Field targetField = new Field("target");
			targetField.setLabel("target");
			columns.add(targetField);

			for (Tuple t : getDb().sql(sql))
			{
				Field f = new Field(t.getString("name"));
				f.setLabel(t.getString("label"));
				columns.add(f);
			}
		}
		catch (Exception e)
		{
			throw new TableException(e);
		}
	}

	@Override
	public Iterator<Tuple> iterator()
	{
		try
		{
			return getRows().iterator();
		}
		catch (TableException e)
		{
			logger.error("Exception getting iterator", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Tuple> getRows() throws TableException
	{
		try
		{
			List<Tuple> result = new ArrayList<Tuple>();

			Query<ObservationSet> query = createQuery();

			if (query == null)
			{
				return new ArrayList<Tuple>();
			}

			// Limit the nr of rows
			if (getLimit() > 0)
			{
				query.limit(getLimit());
			}

			if (getOffset() > 0)
			{
				query.offset(getOffset());
			}

			for (ObservationSet os : query.find())
			{

				Tuple t = new SimpleTuple();
				// FIXME: no more target! problem?
				// Concept c = db.findById(Feature.class, os.getTarget_Id());
				// t.set("target", c.getName());

				// FIXME: typing! not String only!!!
				Query<StringObservedValue> queryObservedValue = getDb().query(StringObservedValue.class);

				List<Field> columns = getColumns();
				if (isFirstColumnFixed())
				{
					columns.remove(0);
				}

				// Only retrieve the visible columns
				Collection<String> fieldNames = Collections2.transform(columns, new Function<Field, String>()
				{
					@Override
					public String apply(final Field field)
					{
						return field.getName();
					}
				});

				// TODO: String values only!?!?
				for (StringObservedValue v : queryObservedValue.eq(StringObservedValue.OBSERVATIONSET, os.getId())
						.in(StringObservedValue.METHOD_ID, new ArrayList<String>(fieldNames)).find())
				{
					t.set(v.getMethod_Id(), v.getValue());
				}

				result.add(t);
			}

			return result;

		}
		catch (Exception e)
		{
			logger.error("Exception getRows", e);
			throw new TableException(e);
		}

	}

	@Override
	public int getCount() throws TableException
	{
		try
		{
			Query<ObservationSet> query = createQuery();
			return query == null ? 0 : query.count();
		}
		catch (DatabaseException e)
		{
			logger.error("DatabaseException getCount", e);
			throw new TableException(e);
		}

	}

	// Creates the query based on the provided filters
	// Returns null if we already now there wil be no results
	private Query<ObservationSet> createQuery() throws TableException, DatabaseException
	{

		Query<ObservationSet> query;

		if (getFilters().isEmpty())
		{
			query = getDb().query(ObservationSet.class).eq(ObservationSet.PARTOFDATASET, dataSet.getId());
		}
		else
		{
			// For now only single simple queries are supported
			List<QueryRule> queryRules = new ArrayList<QueryRule>();

			for (QueryRule filter : getFilters())
			{
				if ((filter.getOperator() != Operator.EQUALS) && (filter.getOperator() != Operator.LIKE))
				{
					// value is always a String so LESS etc. can't be
					// supported, NOT queries are not supported yet
					throw new NotImplementedException("Operator [" + filter.getOperator()
							+ "] not yet implemented, only EQUALS and LIKE are supported.");

				}

				// Null values come to us as String 'null'
				if ((filter.getValue() != null) && (filter.getValue() instanceof String)
						&& ((String) filter.getValue()).equalsIgnoreCase("null"))
				{
					filter.setValue(null);
				}

				// FIXME: String only!!
				queryRules.add(new QueryRule(StringObservedValue.METHOD_ID, Operator.EQUALS, filter.getField()));
				queryRules.add(new QueryRule(StringObservedValue.VALUE, filter.getOperator(), filter.getValue()));

			}

			List<ObservedValue> observedValues = getDb().find(ObservedValue.class,
					queryRules.toArray(new QueryRule[queryRules.size()]));

			// No results
			if (observedValues.isEmpty())
			{
				return null;
			}

			List<Integer> observationSetIds = new ArrayList<Integer>();
			for (ObservedValue observedValue : observedValues)
			{
				if (!observationSetIds.contains(observedValue.getObservationSet_Id()))
				{
					observationSetIds.add(observedValue.getObservationSet_Id());
				}
			}

			query = getDb().query(ObservationSet.class).eq(ObservationSet.PARTOFDATASET, dataSet.getId())
					.in(ObservationSet.ID, observationSetIds);
		}

		return query;

	}
}
