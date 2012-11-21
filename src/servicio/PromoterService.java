package servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los Promotores
 * @see PromoterDTO
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class PromoterService extends HashMap<Integer, PromoterDTO>
{
	
	private String[][] data =
	{
		{"1668757130","root","x","x","x","100","root"}
		, {"1753281862","Luis Roldan","Barva de Heredia","22573987","Banco de Costa Rica","10","luis"}
		, {"1948347202","Gilberth Arce","Barva de Heredia","22630736","Banco Nacional","13","gilberth"}
	};
	
	private static PromoterService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static PromoterService getInstance()
	{
		if (instance == null)
		{
			instance = new PromoterService();
		}
		return instance;
	}

	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private PromoterService()
	{
		for (int a = 0; a < data.length; a++)
		{
			PromoterDTO instance = new PromoterDTO();
			instance.setCode(Integer.parseInt(data[a][0]));
			instance.setName(data[a][1]);
			instance.setAddress(data[a][2]);
			instance.setTelephone(data[a][3]);
			instance.setBank(data[a][4]);
			instance.setCommision(Integer.parseInt(data[a][5]));
			instance.setUsername(data[a][6]);
			super.put(instance.getCode(), instance);
		}
	}
	
	/**
	 * Obtiene un listado de los promotores que coincidan con un nombre de usuario
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public PromoterDTO getByUsername(String username)
	{
		for (PromoterDTO PromoterDTO : super.values())
		{
			if (PromoterDTO.getUsername().equals(username))
			{
				return PromoterDTO;
			}
		}
		return null;
	}
}