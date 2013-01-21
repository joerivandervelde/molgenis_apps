package plugins.xml;

import java.io.File;

import org.molgenis.cbm.CbmXmlParser;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.util.Entity;

public class ImportCbmData extends PluginModel<Entity>
{

	private File currentFile;

	private static final long serialVersionUID = -6143910771849972946L;

	public ImportCbmData(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public String getViewName()
	{
		return "plugins_xml_ImportCbmData";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/xml/ImportCbmData.ftl";
	}

	@Override
	public void handleRequest(Database db, MolgenisRequest request) throws Exception
	{
		if (request.getString("__action").equals("upload"))
		{

			// get uploaded file and do checks
			File file = request.getFile("upload");
			if (file == null)
			{
				throw new Exception("No file selected.");
			}
			else if (!file.getName().endsWith(".xml"))
			{
				throw new Exception("File does not end with '.xml', other formats are not supported.");
			}

			// if no error, set file, and continue
			this.setCurrentFile(file);

			System.out.println("current file : " + this.getCurrentFile());

			// Parsing a document with JAXP

			CbmXmlParser cbmXmlParser = new CbmXmlParser();

			File currentXsdfile = new File("/Users/despoina/Documents/__CTMM_project/CBM/CBM.xsd");
			cbmXmlParser.load(currentFile, currentXsdfile);

		}

	}

	private void setCurrentFile(File file)
	{
		this.currentFile = file;

	}

	private File getCurrentFile()
	{
		return this.currentFile;

	}

	@Override
	public void reload(Database db)
	{
		// try
		// {
		// Database db = this.getDatabase();
		// Query q = db.query(Experiment.class);
		// q.like("name", "test");
		// List<Experiment> recentExperiments = q.find();
		//
		// //do something
		// }
		// catch(Exception e)
		// {
		// //...
		// }
	}

	@Override
	public boolean isVisible()
	{
		// you can use this to hide this plugin, e.g. based on user rights.
		// e.g.
		// if(!this.getLogin().hasEditPermission(myEntity)) return false;
		return true;
	}
}
