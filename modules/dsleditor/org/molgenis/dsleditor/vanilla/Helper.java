package org.molgenis.dsleditor.vanilla;

import java.util.ArrayList;
import java.util.List;

import org.molgenis.model.elements.Entity;
import org.molgenis.model.elements.Field;
import org.molgenis.model.elements.Model;
import org.molgenis.model.elements.Module;

public class Helper
{
	/**
	 * Rename for entity
	 * 
	 * @param moduleName
	 * @param model
	 * @return
	 */
	public static String renameIfDuplicateEntity(String entityName, Model model)
	{

		// 'copy' to avoid bug involving evil java pointers..
		List<Entity> entList = new ArrayList<Entity>();
		for (Entity e : model.getEntities())
		{
			entList.add(e);
		}

		for (Module m : model.getModules())
		{
			entList.addAll(m.getEntities());
		}
		List<String> entNames = new ArrayList<String>();
		for (Entity ent : entList)
		{
			entNames.add(ent.getName());
		}
		return renameIfDuplicate(entityName, entNames);
	}

	/**
	 * Rename for module names
	 * 
	 * @param moduleName
	 * @param model
	 * @return
	 */
	public static String renameIfDuplicateModule(String moduleName, Model model)
	{

		List<Module> modList = model.getModules();
		List<String> modNames = new ArrayList<String>();
		for (Module mod : modList)
		{
			modNames.add(mod.getName());
		}
		return renameIfDuplicate(moduleName, modNames);
	}

	/**
	 * Rename a single string
	 * 
	 * @param rename
	 * @param inputs
	 * @return
	 */
	public static String renameIfDuplicate(String rename, List<String> inputs)
	{
		if (inputs.contains(rename))
		{
			boolean highestDupFound = false;
			int dupNumber = 1;
			while (!highestDupFound)
			{
				if (inputs.contains(rename + "_" + dupNumber))
				{
					dupNumber++;
				}
				else
				{
					return rename + "_" + dupNumber;
				}
			}
		}
		return rename;
	}

	/**
	 * Rename duplicates by adding e.g. _1, or _2 if _1 is taken, and so on.
	 * 
	 * @param inputs
	 * @return
	 */
	public static ArrayList<String> renameDuplicates(ArrayList<String> inputs)
	{
		ArrayList<String> out = new ArrayList<String>();
		for (String s : inputs)
		{
			if (out.contains(s))
			{
				boolean highestDupFound = false;
				int dupNumber = 1;
				while (!highestDupFound)
				{
					if (out.contains(s + "_" + dupNumber))
					{
						dupNumber++;
					}
					else
					{
						out.add(s + "_" + (dupNumber));
						highestDupFound = true;
					}
				}
			}
			else
			{
				out.add(s);
			}
		}
		return out;
	}

	/**
	 * Make sure fields have a default unique name within an entity
	 * 
	 * @param string
	 * @param molgenisModel
	 * @return
	 */
	public static String renameIfDuplicateField(String name, String entityName, Model molgenisModel)
	{
		Entity e = molgenisModel.findEntity(entityName);

		List<String> fieldNames = new ArrayList<String>();
		for (Field f : e.getFields())
		{
			fieldNames.add(f.getName());
		}
		return renameIfDuplicate(name, fieldNames);
	}

}