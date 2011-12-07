package plugins.predictionModel;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.cxf.binding.corba.wsdl.Array;
import org.apache.poi.hssf.record.formula.Ptg;
import org.molgenis.core.Ontology;
import org.molgenis.core.OntologyTerm;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.Database.DatabaseAction;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.html.Table;
import org.molgenis.organization.Investigation;
import org.molgenis.pheno.Category;
import org.molgenis.pheno.Measurement;
import org.molgenis.pheno.ObservableFeature;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.pheno.ObservationTarget;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.Protocol;
import org.molgenis.util.Entity;
import org.molgenis.util.Tuple;

import com.sun.codemodel.ClassType;

import app.FillMetadata;
import app.servlet.RestApi.CategoryList;

import plugins.emptydb.emptyDatabase;




public class Prediction extends PluginModel<Entity>
{
	private String Status = "";

	private TableModel table;

	private static final long serialVersionUID = 6149846107377048848L;

	public Prediction(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public String getViewName()
	{
		return "plugins_predictionModel_Prediction";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/predictionModel/Prediction.ftl";
	}

	@Override
	public void handleRequest(Database db, Tuple request) throws Exception	{

		if ("ImportLifelineToPheno".equals(request.getAction())) {

			loadDataFromExcel(db, request, null);

		}

		if ("fillinDatabase".equals(request.getAction())) {

			new emptyDatabase(db, false);
			FillMetadata.fillMetadata(db, false);
			this.setStatus("The database is empty now");
		}

	}
	@SuppressWarnings("unchecked")
	public void loadDataFromExcel(Database db, Tuple request, Investigation inv) throws BiffException, IOException, DatabaseException{

		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		File file = new File(tmpDir+ "/DataShaperExcel.xls"); 

		if (file.exists()) {

			System.out.println("The excel file is being imported, please be patient");

			this.setStatus("The excel file is being imported, please be patient");

			Workbook workbook = Workbook.getWorkbook(file); 

			Sheet dictionaryCategory = workbook.getSheet(0);

			table = new TableModel (dictionaryCategory.getColumns(), db);
			
			int [] columnList = {0, 1, 2};
			
			int [] ignoreColumnList = {5,6};
			
			int [] observedValues = new int[16];
			
			for(int i = 0; i < observedValues.length; i++){
				observedValues[i] = i + 10;
			}
			
			table.addField(this, TableModel.PROTOCOL, columnList, TableField.VERTICAL);
			
			table.addField(this, TableModel.MEASUREMENT, 3, TableField.VERTICAL);
			
			table.addField(this, TableModel.MEASUREMENT_DESCRIPTION, 4, TableField.VERTICAL);
			
			table.addField(this,TableModel.IGNORE, ignoreColumnList,TableField.VERTICAL);
			
			table.addField(this, TableModel.MEASUREMENT, 7, TableField.HORIZONTAL);
			
			table.addField(this, TableModel.CATEGORY, 8, TableField.VERTICAL);
			
			table.addField(this, TableModel.CATEGORY, 9, TableField.VERTICAL);
			
			table.addField(this, TableModel.MEASUREMENT, observedValues, TableField.HORIZONTAL);
			
			table.setMissingCategoryIndex(9);
			
			table.setSubProtocolRelation(0, 1);
			
			table.setSubProtocolRelation(1, 2);
			
			table.setProtocolFeatureRelation(2,3, TableModel.PROTOCOL_FEATURE);
			
			int categoryList[] = {7,8};
			
			table.measurementSetting(4, 5, 6, categoryList);
			
			table.setTarget(3);
			
			table.convertIntoPheno(dictionaryCategory);
			
//			int [] columnList = {1,2};
//
//			table.setDataType("TEKST", TableModel.MEASUREMENT_STRING);
//
//			table.setDataType("NUMMER", TableModel.MEASUREMENT_INT);
//
//			table.setDataType("DATUM", TableModel.MEASUREMENT_DATE);
//
//			table.addField(this, TableModel.PROTOCOL, 0, TableField.VERTICAL);
//
//			table.addField(this, TableModel.MEASUREMENT, columnList, TableField.HORIZONTAL);
//
//			table.addField(this, TableModel.MEASUREMENT, 3, TableField.VERTICAL);
//
//			table.addField(this, TableModel.MEASUREMENT_DATATYPE, 4, TableField.VERTICAL);
//
//			table.addField(this, TableModel.MEASUREMENT_DESCRIPTION, 5, TableField.VERTICAL);
//
//			table.setTarget(3);
//
//			table.setProtocolFeatureRelation(0,3,TableModel.PROTOCOL_FEATURE);
//
//			table.convertIntoPheno(dictionaryCategory);
//
//			Sheet categoryInput = workbook.getSheet(1);
//
//			table = new TableModel (categoryInput.getColumns(), db);
//
//			table.addField(this, TableModel.PROTOCOL, 0, TableField.VERTICAL);
//
//			table.addField(this, TableModel.MEASUREMENT, 1, TableField.VERTICAL);
//
//			table.addField(this, TableModel.IGNORE, 2, TableField.VERTICAL);
//
//			table.addField(this, TableModel.CATEGORY, 3, TableField.VERTICAL);
//
//			table.addField(this, TableModel.CODE_STRING, 4, TableField.VERTICAL);
//
//			table.setProtocolFeatureRelation(0,1, TableModel.PROTOCOL_FEATURE);
//
//			table.setMeasurementCategoryRelation(1,3,TableModel.MEASUREMENT_CATEGORY);
//
//			table.convertIntoPheno(categoryInput);

			this.setStatus("finished!");

		} else {

			this.setStatus("The file should be in " + file );

		}
	}


	@Override
	public void reload(Database db)	{
	}


	public void setStatus(String status) {
		Status = status;
	}


	public String getStatus() {
		return Status;
	}
}