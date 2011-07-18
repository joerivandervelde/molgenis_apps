/* Date:        February 10, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package plugins.protocol;

import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.util.Entity;
import org.molgenis.util.Tuple;

import commonservice.CommonService;

public class EventViewerPlugin extends PluginModel<Entity>
{
	private static final long serialVersionUID = 8804579908239186037L;
	private List<Integer> targetIdList;
	private CommonService ct = CommonService.getInstance();
	
	public EventViewerPlugin(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}
	
	public String getCustomHtmlHeaders()
    {
        return "<script src=\"res/scripts/custom/addingajax.js\" language=\"javascript\"></script>\n" +
 	   		   "<script src=\"res/scripts/custom/viewevents.js\" language=\"javascript\"></script>\n" +
 	   		   "<link rel=\"stylesheet\" style=\"text/css\" href=\"res/css/animaldb.css\">";
    }

	@Override
	public String getViewName()
	{
		return "plugins_protocol_EventViewerPlugin";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/protocol/EventViewerPlugin.ftl";
	}
	
	// Target related methods:
	public List<Integer> getTargetIdList()
	{
		return targetIdList;
	}

	public void setTargetIdList(List<Integer> targetIdList)
	{
		this.targetIdList = targetIdList;
	}
	
	public String getTargetName(Integer id) {
		try {
			return ct.getObservationTargetLabel(id);
		} catch (Exception e) {
			return id.toString();
		}
	}

	public void handleRequest(Database db, Tuple request)
	{
		/*this.setSuccess(-1);
		try {
			String action = request.getString("__action");
			if (action.equals("viewEvents")) {
				Query q = db.query(Value.class);
				q.addRules(new QueryRule("targetid", Operator.EQUALS, request.getInt("animal")));
				List<Value> valueList = q.find();
				for (Value val : valueList) {
					
				}
			}
		} catch (Exception e) {
			this.setSuccess(-1);
			e.printStackTrace();
		}*/
	}

	public void reload(Database db)
	{
		ct.setDatabase(db);
		ct.makeObservationTargetNameMap(this.getLogin().getUserId(), false);
		
		// Populate target list
		List<Integer> idList = null;
		try {
			List<Integer> investigationIds = ct.getAllUserInvestigationIds(this.getLogin().getUserId());
			idList = ct.getAllObservationTargetIds(null, false, investigationIds);
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessages(new ScreenMessage("Something went wrong while loading target list", false));
		}
		if (idList != null && idList.size() > 0) {
			this.setTargetIdList(idList);
		} else {
			this.setMessages(new ScreenMessage("No targets found in database", false));
		}
	}
	
}
