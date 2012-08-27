package org.molgenis.dsleditor.vanilla;

import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.model.elements.Entity;
import org.molgenis.model.elements.Field;
import org.molgenis.util.Tuple;

public class HandleRequest_Remove
{
	public static ScreenMessage handle(DSLEditorModel model, Tuple request) throws Exception
	{
		String action = request.getString("__action");

		if (action.equals("removeModuleFromMolgenis"))
		{
			Integer previousIndex = model.getParent().getModel().removeModule(request.getString("__selectName"));

			if (previousIndex != null)
			{
				model.setSelectType("module");
				model.setSelectName(model.getParent().getModel().getModules().get(previousIndex).getName());
			}
			else
			{
				model.setSelectType("molgenis");
				model.setSelectName(model.getParent().getModel().getName());
			}

			return new ScreenMessage("module '" + request.getString("__selectName") + "' removed", true);
		}

		if (action.equals("removeEntity"))
		{
			String entityName = request.getString("__selectName");

			if (model.getParent().getModel().getEntity(entityName) == null)
			{
				model.setSelectName(model.getParent().getModel().findModuleNameForEntity(entityName));
				System.out.println("**** SETTING MODULE TO : "
						+ model.getParent().getModel().findModuleNameForEntity(entityName));
				model.setSelectType("module");
			}
			else
			{
				model.setSelectName(model.getParent().getModel().getName());
				model.setSelectType("molgenis");
			}

			String jumpToName = model.getParent().getModel().findRemoveEntity(entityName);

			if (jumpToName != null)
			{
				model.setSelectType("entity");
				model.setSelectName(jumpToName);
			}

			return new ScreenMessage("entity '" + request.getString("__selectName") + "' removed", true);
		}

		if (action.equals("removeField"))
		{
			int indexToBeRemoved = request.getInt("__selectFieldIndex");
			String parentEntity = request.getString("__selectFieldEntity");

			Entity e = model.getParent().getModel().findEntity(parentEntity);
			e.removeField(indexToBeRemoved);

			if (e.getFields().size() > 0)
			{
				model.setSelectType("field");
				if (indexToBeRemoved == 0)
				{
					indexToBeRemoved = 1;
				}
				Field previousField = e.getFields().get(indexToBeRemoved - 1);
				model.setSelectName(previousField.getName());
				model.setSelectFieldEntity(e.getName());
				model.setSelectFieldIndex(indexToBeRemoved - 1);
			}
			else
			{
				model.setSelectType("entity");
				model.setSelectName(e.getName());
			}

			return new ScreenMessage("field removed", true);
		}

		return new ScreenMessage("action '" + action + "'not reckognized", false);
	}
}