package plugins.qtlfinder3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.util.Entity;

public class PlotOverlap
{
	public void plotOverlap(QtlFinderHDModel model, Database db)
	{
		Set<String> set = new HashSet<String>();

		for (Entity h : model.getShoppingCart().values())
		{
			if (h.getValues().get("symbol").toString().startsWith("A_"))
			{
				set.add(h.getValues().get("reportsFor_name").toString());
			}
			else
			{
				set.add(h.getValues().get("symbol").toString());
			}
		}

		model.setGenes(set);
	}

	public void plotOverlap(QtlFinderHDModel model, ScreenModel screenModel, List<Entity> cart) throws Exception
	{

	}
}
