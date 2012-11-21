package servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todas las relaciones entre roles y usuarios
 * @see RoleByUser
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class RolesByUserService extends HashMap<String, RolesByUserDTO>
{
	private static String[][] data =
	{
		{"admin","1"}
		, {"luis","2"}
		, {"gilberth","2"}
		, {"root","2"}
		, {"root","1"}
	};

	private static RolesByUserService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static RolesByUserService getInstance()
	{
		if (instance == null)
		{
			instance = new RolesByUserService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private RolesByUserService()
	{
		for (int a = 0; a < data.length; a++)
		{
			RolesByUserDTO instance = new RolesByUserDTO();
			instance.setUsername(data[a][0]);
			instance.setRole(Integer.parseInt(data[a][1]));
			super.put(instance.getUsername() + "$" + instance.getRole(), instance);
		}
	}
}