package servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los Clientes
 * @see CustomerDTO
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class CustomerService extends HashMap<String, CustomerDTO>
{
	private static String[][] data = {};
	
	private static CustomerService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static CustomerService getInstance()
	{
		if (instance == null)
		{
			instance = new CustomerService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private CustomerService()
	{
		for (int a = 0; a < data.length; a++)
		{
			CustomerDTO instance = new CustomerDTO();
			instance.setId(data[a][0]);
			instance.setName(data[a][1]);
			instance.setAddress(data[a][2]);
			instance.setTelephone(data[a][3]);
			instance.setCardnumber(data[a][4]);
			instance.setCardType(data[a][5]);
			super.put(instance.getId(), instance);
		}
	}
}