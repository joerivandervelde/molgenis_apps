package plugins.qtlfinder3;

import org.molgenis.framework.ui.ScreenController;

import plugins.qtlfinder2.QtlFinder2;

public class ShoppingCart extends QtlFinder2
{
	private static final long serialVersionUID = 1L;

	public ShoppingCart(String name, ScreenController<?> parent)
	{
		super(name, parent);
		// TODO Auto-generated constructor stub
	}

	public String getViewName()
	{
		return "plugins_qtlfinder3_ShoppingCart";
	}

	public String getViewTemplate()
	{
		return "plugins/qtlfinder3/ShoppingCart.ftl";
	}

}
