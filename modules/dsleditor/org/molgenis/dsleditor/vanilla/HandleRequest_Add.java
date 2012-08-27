package org.molgenis.dsleditor.vanilla;

import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.model.elements.Entity;
import org.molgenis.model.elements.Field;
import org.molgenis.model.elements.Module;
import org.molgenis.util.Tuple;

public class HandleRequest_Add
{
	public static ScreenMessage handle(DSLEditorModel model, Tuple request) throws Exception
	{
		String action = request.getString("__action");

		if (action.equals("addModuleToMolgenis"))
		{
			Module m = new Module(model.getParent().getModel());
			m.setName(Helper.renameIfDuplicateModule("newModule", model.getParent().getModel()));
			// model.getParent().getModel().addModule(m);

			model.setSelectType("module");
			model.setSelectName(m.getName());

			return new ScreenMessage("added module to molgenis '" + model.getParent().getModel().getName() + "'", true);
		}

		if (action.equals("addEntityToMolgenis"))
		{
			Entity e = new Entity("unnamed_entity", model.getParent());
			e.setName(Helper.renameIfDuplicateEntity("newEntity", model.getParent().getModel()));
			model.getParent().getModel().addEntity(e);

			model.setSelectType("entity");
			model.setSelectName(e.getName());

			return new ScreenMessage("added entity to molgenis '" + model.getParent().getModel().getName() + "'", true);
		}

		if (action.equals("addEntityToModule"))
		{
			Entity e = new Entity("unnamed_entity", model.getParent());
			e.setName(Helper.renameIfDuplicateEntity("newEntity", model.getParent().getModel()));
			model.getParent().getModel().getModule(request.getString("__selectName")).addEntity(e);

			model.setSelectType("entity");
			model.setSelectName(e.getName());

			return new ScreenMessage("added entity to module '" + request.getString("__selectName") + "'", true);
		}

		if (action.equals("addFieldToEntity"))
		{
			String entityName = request.getString("__selectName");
			Field f = new Field();
			f.setName(Helper.renameIfDuplicateField("newField", entityName, model.getParent().getModel()));
			model.getParent().getModel().findEntity(entityName).addField(f);
			model.setSelectType("field");
			model.setSelectName(f.getName());
			model.setSelectFieldEntity(entityName);
			model.setSelectFieldIndex(model.getParent().getModel().findEntity(entityName).getFields().indexOf(f));

			return new ScreenMessage("added field to entity '" + entityName + "'", true);
		}

		return new ScreenMessage("action '" + action + "'not reckognized", false);
	}

}