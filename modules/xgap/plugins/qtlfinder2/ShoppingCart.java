package plugins.qtlfinder2;

import java.util.HashMap;
import java.util.Map;

import org.molgenis.util.Entity;

public class ShoppingCart
{
	Map<String, Entity> shoppingCart = new HashMap<String, Entity>();

	public Map<String, Entity> getShoppingCart()
	{
		return shoppingCart;
	}

	public void setShoppingCart(Map<String, Entity> shoppingCart)
	{
		this.shoppingCart = shoppingCart;
	}

}
