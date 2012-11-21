package servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los usuarios
 * @see Usuario
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class UserService extends HashMap<String, UserDTO>
{
	private static String[][] data = 
	{
		{"admin","pass"}
		, {"luis","pass"}
		, {"root","pass"}
		, {"gilberth","pass"}
	};

	private static UserService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static UserService getInstance()
	{
		if (instance == null)
		{
			instance = new UserService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private UserService()
	{
		for (int a = 0; a < data.length; a++)
		{
			UserDTO instance = new UserDTO();
			instance.setUsername(data[a][0]);
			instance.setPassword(data[a][1]);
			super.put(instance.getUsername(), instance);
		}
	}
}