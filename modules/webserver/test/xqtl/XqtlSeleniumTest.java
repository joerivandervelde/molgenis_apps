package test.xqtl;

import java.io.File;

import org.molgenis.util.DetectOS;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.Helper;
import app.servlet.MolgenisServlet;
import boot.RunStandalone;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.Selenium;

/**
 * 
 * The complete xQTL Selenium web test.
 * Has a section for init/helpers and section for the real tests.
 * 
 */
public class XqtlSeleniumTest
{
	
	/**
	 *******************************************************************
	 *************************  Init and helpers  **********************
	 *******************************************************************
	 */

	Selenium selenium;
	String pageLoadTimeout = "30000";
	boolean tomcat = false;
	String appName;

	/**
	 * Configure Selenium server and delete the database
	 */
	@BeforeClass
	public void start() throws Exception
	{
		appName = MolgenisServlet.getMolgenisVariantID();
		int webserverPort = 8080;
		if (!tomcat) webserverPort = Helper.getAvailablePort(11000, 100);

		String seleniumUrl = "http://localhost:" + webserverPort + "/";
		String seleniumHost = "localhost";
		String seleniumBrowser = "firefox";
		int seleniumPort = Helper.getAvailablePort(9080, 100);

		RemoteControlConfiguration rcc = new RemoteControlConfiguration();
		rcc.setSingleWindow(true);
		rcc.setPort(seleniumPort);

		try
		{
			SeleniumServer server = new SeleniumServer(false, rcc);
			server.boot();
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Cannot start selenium server: ", e);
		}

		HttpCommandProcessor proc = new HttpCommandProcessor(seleniumHost, seleniumPort, seleniumBrowser, seleniumUrl);
		selenium = new DefaultSelenium(proc);
		selenium.start();

		Helper.deleteDatabase();

		if (!tomcat) new RunStandalone(webserverPort);
	}

	/**
	 * Stop Selenium server and remove files
	 */
	@AfterClass
	public void stop() throws Exception
	{
		selenium.stop();
		Helper.deleteStorage();
	}

	/**
	 * Start the app and verify home page
	 */
	@Test
	public void startup() throws InterruptedException
	{
		selenium.open("/" + appName + "/molgenis.do");
		selenium.waitForPageToLoad(pageLoadTimeout);
		Assert.assertEquals(selenium.getTitle(), "xQTL workbench");
		Assert.assertTrue(selenium.isTextPresent("Welcome"));
		Assert.assertEquals(selenium.getText("link=R api"), "R api");
	}

	/**
	 * Login as admin and redirect
	 */
	@Test(dependsOnMethods =
	{ "startup" })
	public void login() throws InterruptedException
	{
		clickAndWait("id=UserLogin_tab_button");
		Assert.assertEquals(selenium.getText("link=Register"), "Register");
		selenium.type("id=username", "admin");
		selenium.type("id=password", "admin");
		clickAndWait("id=Login");
		// note: page now redirects to the Home screen ('auth_redirect' in
		// properties)
		Assert.assertTrue(selenium
				.isTextPresent("You are logged in as admin, and the database does not contain any investigations or other users."));
	}

	/**
	 * Press 'Load' example data
	 */
	@Test(dependsOnMethods =
	{ "login" })
	public void loadExampleData() throws InterruptedException
	{
		selenium.type("id=inputBox", storagePath());
		clickAndWait("id=loadExamples");
		Assert.assertTrue(selenium.isTextPresent("File path '" + storagePath()
				+ "' was validated and the dataloader succeeded"));
	}

	/**
	 * Function that sets the application to a 'default' state after setting up.
	 * All downstream test functions require this to be done.
	 */
	@Test(dependsOnMethods =
	{ "loadExampleData" })
	public void returnHome() throws InterruptedException
	{
		clickAndWait("id=ClusterDemo_tab_button");
	}

	/**
	 * Helper function. Click a target and wait.
	 */
	public void clickAndWait(String target)
	{
		selenium.click(target);
		selenium.waitForPageToLoad(pageLoadTimeout);
	}

	/**
	 * Helper function. Get DOM property using JavaScript.
	 */
	public String propertyScript(String element, String property)
	{
		return "var x = window.document.getElementById('" + element
				+ "'); window.document.defaultView.getComputedStyle(x,null).getPropertyValue('" + property + "');";
	}

	/**
	 * Helper function. Get the storage path to use in test.
	 */
	public String storagePath()
	{
		String storagePath = new File(".").getAbsolutePath() + File.separator + "tmp_selenium_test_data";
		if (DetectOS.getOS().startsWith("windows"))
		{
			return storagePath.replace("\\", "/");
		}
		else
		{
			return storagePath;
		}
	}
	
	
	
	/**
	 *******************************************************************
	 **************************  Assorted tests  ***********************
	 *******************************************************************
	 */
	
	
	
	@Test(dependsOnMethods =
		{ "returnHome" })
		public void exploreExampleData() throws InterruptedException
		{
			// browse to Overview and check links
			clickAndWait("id=Investigations_tab_button");
			clickAndWait("id=Overview_tab_button");
			Assert.assertEquals(selenium.getText("link=Marker (117)"), "Marker (117)");

			// click link to a matrix and check values
			clickAndWait("link=metaboliteexpression");
			Assert.assertTrue(selenium.isTextPresent("942") && selenium.isTextPresent("4857")
					&& selenium.isTextPresent("20716"));
		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void compactView() throws InterruptedException
		{
			// browse to Data tab
			clickAndWait("id=Investigations_tab_button");
			clickAndWait("id=Datas_tab_button");

			// assert if the hide investigation and data table rows are hidden
			Assert.assertEquals(selenium.getEval(propertyScript("Investigations_collapse_tr_id", "display")), "none");
			Assert.assertEquals(selenium.getEval(propertyScript("Datas_collapse_tr_id", "display")), "none");

			// click both unhide buttons
			selenium.click("id=Investigations_collapse_button_id");
			selenium.click("id=Datas_collapse_button_id");

			// assert if the hide investigation and data table rows are exposed
			Assert.assertEquals(selenium.getEval(propertyScript("Investigations_collapse_tr_id", "display")), "table-row");
			Assert.assertEquals(selenium.getEval(propertyScript("Datas_collapse_tr_id", "display")), "table-row");

			// click both unhide buttons
			selenium.click("id=Investigations_collapse_button_id");
			selenium.click("id=Datas_collapse_button_id");

			// assert if the hide investigation and data table rows are hidden again
			Assert.assertEquals(selenium.getEval(propertyScript("Investigations_collapse_tr_id", "display")), "none");
			Assert.assertEquals(selenium.getEval(propertyScript("Datas_collapse_tr_id", "display")), "none");

		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void individualForms() throws Exception
		{
			// browse to basic annotations, individuals is the first form
			clickAndWait("id=Investigations_tab_button");
			clickAndWait("id=BasicAnnotations_tab_button");

			// check some values here
			Assert.assertEquals(selenium.getText("//form[@id='Individuals_form']/div[3]/div/div/table/tbody/tr[7]/td[4]"),
					"X7");
			Assert.assertTrue(selenium.isTextPresent("xgap_rqtl_straintype_riself"));

			// switch to edit view and check some values there too
			clickAndWait("id=Individuals_editview");
			Assert.assertEquals(selenium.getValue("id=Individual_name"), "X1");
			Assert.assertEquals(selenium.getText("css=#Individual_ontologyReference_chzn > a.chzn-single > span"),
					"xgap_rqtl_straintype_riself");
			Assert.assertEquals(
					selenium.getText("//img[@onclick=\"if ($('#Individuals_form').valid() && validateForm(document.forms.Individuals_form,new Array())) {setInput('Individuals_form','_self','','Individuals','update','iframe'); document.forms.Individuals_form.submit();}\"]"),
					"");

			// click add new, wait for popup, and select it
			selenium.click("id=Individuals_edit_new");
			selenium.waitForPopUp("molgenis_edit_new", "30000");
			selenium.selectWindow("name=molgenis_edit_new");

			// fill in the form and click add
			selenium.type("id=Individual_name", "testindv");
			clickAndWait("id=Add");

			// select main window and check if add was successful
			selenium.selectWindow("title=xQTL workbench");
			Assert.assertTrue(selenium.isTextPresent("ADD SUCCESS: affected 1"));

			// page back and forth and check values
			clickAndWait("id=prev_Individuals");
			Assert.assertEquals(selenium.getValue("id=Individual_name"), "X193");
			clickAndWait("id=last_Individuals");
			Assert.assertEquals(selenium.getValue("id=Individual_name"), "testindv");

			// delete the test individual and check if it happened
			selenium.click("id=delete_Individuals");
			Assert.assertEquals(selenium.getConfirmation(),
					"You are about to delete a record. If you click [yes] you won't be able to undo this operation.");
			selenium.waitForPageToLoad(pageLoadTimeout);
			Assert.assertTrue(selenium.isTextPresent("REMOVE SUCCESS: affected 1"));

		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void enumInput() throws Exception
		{
			// browse to 'Data' view and expand compact view
			clickAndWait("id=Investigations_tab_button");
			clickAndWait("id=Datas_tab_button");
			selenium.click("id=Datas_collapse_button_id");

			// assert content of enum fields
			Assert.assertEquals(
					selenium.getTable("css=#Datas_form > div.screenbody > div.screenpadding > table > tbody > tr > td > table.5.1"),
					"Individual\n\nChromosomeDerivedTraitEnvironmentalFactorGeneIndividualMarkerMassPeakMeasurementMetabolitePanelProbeSampleSpot");
			Assert.assertEquals(
					selenium.getTable("css=#Datas_form > div.screenbody > div.screenpadding > table > tbody > tr > td > table.6.1"),
					"Metabolite\n\nChromosomeDerivedTraitEnvironmentalFactorGeneIndividualMarkerMassPeakMeasurementMetabolitePanelProbeSampleSpot");

			// change Individual to Gene and save
			selenium.click("css=#Data_FeatureType_chzn > a.chzn-single > span");
			selenium.click("id=Data_FeatureType_chzn_o_3");
			selenium.click("id=save_Datas");
			selenium.waitForPageToLoad(pageLoadTimeout);
			Assert.assertTrue(selenium.isTextPresent("UPDATE SUCCESS: affected 1"));

			// expand compact view again and check value has changed
			selenium.click("id=Datas_collapse_button_id");
			Assert.assertEquals(
					selenium.getTable("css=#Datas_form > div.screenbody > div.screenpadding > table > tbody > tr > td > table.5.1"),
					"Gene\n\nChromosomeDerivedTraitEnvironmentalFactorGeneIndividualMarkerMassPeakMeasurementMetabolitePanelProbeSampleSpot");

			// change back to Individual and save
			selenium.click("css=#Data_FeatureType_chzn > a.chzn-single > span");
			selenium.click("id=Data_FeatureType_chzn_o_4");
			clickAndWait("id=save_Datas");
			Assert.assertTrue(selenium.isTextPresent("UPDATE SUCCESS: affected 1"));

			// expand compact view again and check value is back to normal again
			selenium.click("id=Datas_collapse_button_id");
			Assert.assertEquals(
					selenium.getTable("css=#Datas_form > div.screenbody > div.screenpadding > table > tbody > tr > td > table.5.1"),
					"Individual\n\nChromosomeDerivedTraitEnvironmentalFactorGeneIndividualMarkerMassPeakMeasurementMetabolitePanelProbeSampleSpot");

		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void qtlImporter() throws Exception
		{
			// not much to test here, cannot upload actual files
			// could only be tested on an API level, or manually

			// go to Import data and check the screen contents of the QTL importer
			clickAndWait("id=ImportDataMenu_tab_button");
			clickAndWait("id=QTLWizard_tab_button");
			
			Assert.assertTrue(selenium.isTextPresent("Again, your individuals must be in the first line."));
			Assert.assertEquals(selenium.getText("name=invSelect"), "ClusterDemo");
			Assert.assertEquals(
					selenium.getText("name=cross"),
					"xgap_rqtl_straintype_f2xgap_rqtl_straintype_bcxgap_rqtl_straintype_riselfxgap_rqtl_straintype_risibxgap_rqtl_straintype_4wayxgap_rqtl_straintype_dhxgap_rqtl_straintype_specialxgap_rqtl_straintype_naturalxgap_rqtl_straintype_parentalxgap_rqtl_straintype_f1xgap_rqtl_straintype_rccxgap_rqtl_straintype_cssxgap_rqtl_straintype_unknownxgap_rqtl_straintype_other");
			Assert.assertEquals(selenium.getText("name=trait"),
					"MeasurementDerivedTraitEnvironmentalFactorGeneMarkerMassPeakMetaboliteProbe");

			// try pressing a button and see if the error pops up
			clickAndWait("id=upload_genotypes");
			Assert.assertTrue(selenium.isTextPresent("No file selected"));
		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void runMapping() throws Exception
		{
			// we're not going to test actual execution, just page around

			// go to Run QTL mapping
			clickAndWait("id=Cluster_tab_button");
			Assert.assertTrue(selenium.isTextPresent("This is the main menu for starting a new analysis"));

			// page from starting page to Step 2
			clickAndWait("id=start_new_analysis");
			Assert.assertEquals(selenium.getValue("name=outputDataName"), "MyOutput");
			clickAndWait("id=toStep2");
			Assert.assertTrue(selenium.isTextPresent("You have selected: Rqtl_analysis"));
			Assert.assertEquals(selenium.getText("name=phenotypes"), "Fu_LCMS_data");

			// go back to starting page
			clickAndWait("id=toStep1");
			clickAndWait("id=toStep0");

			// go to job manager
			clickAndWait("id=view_running_analysis");
			Assert.assertTrue(selenium.isTextPresent("No running analysis to display."));

			// go back to starting page
			clickAndWait("id=back_to_start");
			Assert.assertTrue(selenium.isTextPresent("No settings saved."));

		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void configureAnalysis() throws Exception
		{
			// browse to Configure Analysis and check values
			clickAndWait("id=AnalysisSettings_tab_button");
			Assert.assertEquals(selenium.getTable("css=table.listtable.1.3"), "Rqtl_analysis");
			Assert.assertEquals(selenium.getTable("css=table.listtable.1.4"),
					"This is a basic QTL analysis performed in the R environment for statistical computing, powered by th...");

			// browse to R scripts and add a script
			clickAndWait("id=RScripts_tab_button");
			selenium.click("id=RScripts_edit_new");
			selenium.waitForPopUp("molgenis_edit_new", "30000");
			selenium.selectWindow("name=molgenis_edit_new");
			selenium.type("id=RScript_name", "test");
			selenium.type("id=RScript_Extension", "r");
			selenium.click("css=span");
			//from: http://blog.browsermob.com/2011/03/selenium-tips-wait-with-waitforcondition/
			//"Now for the killer part, for sites that use jQuery, if all you need is to confirm there aren�t any active asynchronous requests, then the following does the trick:"
			selenium.waitForCondition("selenium.browserbot.getUserWindow().$.active == 0", "10000");
			selenium.click("css=span");
			clickAndWait("id=Add");

			// add content and save
			selenium.selectWindow("title=xQTL workbench");
			Assert.assertTrue(selenium.isTextPresent("ADD SUCCESS: affected 1"))
			;
			Assert.assertTrue(selenium.isTextPresent("No file found. Please upload it here."));
			selenium.type("name=inputTextArea", "content");
			clickAndWait("id=uploadTextArea");
			Assert.assertFalse(selenium.isTextPresent("No file found. Please upload it here."));

			// delete script and content
			selenium.click("id=delete_RScripts");
			Assert.assertEquals(selenium.getConfirmation(),
					"You are about to delete a record. If you click [yes] you won't be able to undo this operation.");
			selenium.waitForPageToLoad(pageLoadTimeout);
			Assert.assertTrue(selenium.isTextPresent("REMOVE SUCCESS: affected 1"));

			// browse to Tag data and click the hide/show buttons
			clickAndWait("id=MatrixWizard_tab_button");
			Assert.assertTrue(selenium
					.isTextPresent("This screen provides an overview of all data matrices stored in your database."));
			Assert.assertTrue(selenium.isTextPresent("ClusterDemo / genotypes"));
			Assert.assertTrue(selenium.isTextPresent("Rqtl_data -> genotypes"));
			clickAndWait("id=hide_verified");
			Assert.assertTrue(selenium.isTextPresent("Nothing to display"));
			clickAndWait("id=show_verified");
			Assert.assertTrue(selenium.isTextPresent("Binary"));
		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void search() throws Exception
		{
			// browse to Search
			clickAndWait("id=SimpleDbSearch_tab_button");
			selenium.type("name=searchThis", "ee");
			clickAndWait("id=simple_search");

			Assert.assertTrue(selenium.isTextPresent("Found 2 result(s)"));
			Assert.assertTrue(selenium.isTextPresent("Type: BinaryDataMatrix"));

			selenium.type("name=searchThis", "e");
			clickAndWait("id=simple_search");
			Assert.assertTrue(selenium.isTextPresent("Found 339 result(s)"));
			Assert.assertTrue(selenium.isTextPresent("bioinformatician"));

		}

		@Test(dependsOnMethods =
		{ "returnHome" })
		public void molgenisFileMenu() throws Exception
		{
			// browse to chromosomes and click 'Update selected' in File
			clickAndWait("id=Investigations_tab_button");
			clickAndWait("id=BasicAnnotations_tab_button");
			clickAndWait("Chromosomes_tab_button");
			selenium.click("id=Chromosomes_menu_Edit");
			selenium.click("id=Chromosomes_edit_update_selected_submenuitem");

			// select the popup and verify the message
			selenium.waitForPopUp("molgenis_edit_update_selected", "5000");
			selenium.selectWindow("name=molgenis_edit_update_selected");
			Assert.assertTrue(selenium.isTextPresent("No records were selected for updating."));

			// cancel and select main window
			selenium.click("id=Cancel");
			selenium.selectWindow("title=xQTL workbench");

			// TODO: select records, update 2+, verify values changed, change them
			// back, verify values changed back

			// TODO: Add in batch/upload CSV(as best as possible w/o uploading)

			// TODO: Upload CSV file (as best as possible w/o uploading)
		}

}
