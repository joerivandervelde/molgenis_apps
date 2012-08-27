/* Date:        March 22, 2011
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.3
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.dsleditor.vanilla;

import java.util.List;

import org.molgenis.model.elements.DBSchema;

public class DSLEditorModel
{

	DBSchema parent;

	String selectType;
	String selectName;

	String selectFieldEntity;
	Integer selectFieldIndex;

	List<String> fieldTypes;

	public DBSchema getParent()
	{
		return parent;
	}

	public void setParent(DBSchema parent)
	{
		this.parent = parent;
	}

	public String getSelectType()
	{
		return selectType;
	}

	public void setSelectType(String selectType)
	{
		this.selectType = selectType;
	}

	public String getSelectName()
	{
		return selectName;
	}

	public void setSelectName(String selectName)
	{
		this.selectName = selectName;
	}

	public String getSelectFieldEntity()
	{
		return selectFieldEntity;
	}

	public void setSelectFieldEntity(String selectFieldEntity)
	{
		this.selectFieldEntity = selectFieldEntity;
	}

	public Integer getSelectFieldIndex()
	{
		return selectFieldIndex;
	}

	public void setSelectFieldIndex(Integer selectFieldIndex)
	{
		this.selectFieldIndex = selectFieldIndex;
	}

	public List<String> getFieldTypes()
	{
		return fieldTypes;
	}

	public void setFieldTypes(List<String> fieldTypes)
	{
		this.fieldTypes = fieldTypes;
	}

}