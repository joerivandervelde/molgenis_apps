package plugins.qtlfinder3;

import java.util.ArrayList;

import org.molgenis.framework.db.Database;
import org.molgenis.util.Entity;

public class PlotOverlap
{
	public void plotOverlap(QtlFinderHDModel model, Database db)
	{
		model.setGenes(new ArrayList<String>());

		for (Entity h : model.getHits().values())
		{
			if (h.getValues().get("symbol").toString().startsWith("A_"))
			{
				model.getGenes().add(h.getValues().get("reportsFor_name").toString());
			}
			else
			{
				model.getGenes().add(h.getValues().get("symbol").toString());
			}

		}
	}
}
