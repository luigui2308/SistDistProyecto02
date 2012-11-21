package servicio;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los tickets numerados
 * @see Location
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class NumberedTicketService extends HashMap<String, NumberedTicketDTO>
{
	private static String[][] data = {};

	private static NumberedTicketService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static NumberedTicketService getInstance()
	{
		if (instance == null)
		{
			instance = new NumberedTicketService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private NumberedTicketService()
	{
		for (int a = 0; a < data.length; a++)
		{
			NumberedTicketDTO instance = new NumberedTicketDTO();
			instance.setLocationNumber(Integer.parseInt(data[a][0]));
			instance.setTicketCode(Integer.parseInt(data[a][1]));
			super.put(instance.getLocationNumber()+ "$" + instance.getTicketCode(), instance);
		}
	}
	
	/**
	 * Obtiene todos los ticketes numerados que coincidan con un numero de ticket especifico
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public ArrayList<NumberedTicketDTO> getByTicketCode(int ticketCode)
	{
		ArrayList<NumberedTicketDTO> values = new ArrayList<NumberedTicketDTO>();
		for (NumberedTicketDTO NumberedTicketDTO : super.values())
		{
			if (NumberedTicketDTO.getTicketCode() == ticketCode)
			{
				values.add(NumberedTicketDTO);
			}
		}
		return values;
	}
}