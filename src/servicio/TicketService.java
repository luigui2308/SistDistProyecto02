package servicio;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los TicketService
 * @see TicketDTO
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class TicketService extends HashMap<Integer, TicketDTO>
{
	private static String[][] data = {};

	private static TicketService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static TicketService getInstance()
	{
		if (instance == null)
		{
			instance = new TicketService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private TicketService()
	{
		for (int a = 0; a < data.length; a++)
		{
			TicketDTO instance = new TicketDTO();
			instance.setCode(Integer.parseInt(data[a][0]));
			instance.setLocationType(Integer.parseInt(data[a][1]));
			instance.setEventCode(Integer.parseInt(data[a][2]));
			instance.setCustomerId(data[a][3]);
			instance.setDate(data[a][4]);
			instance.setQuantity(Integer.parseInt(data[a][5]));
			instance.setAmount(Integer.parseInt(data[a][6]));
			super.put(instance.getCode(), instance);
		}
	}
	
	/**
	 * Obtiene todos los TicketService que coincidan con un codigo de evento especifico
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public ArrayList<TicketDTO> getByEventCode(int eventCode)
	{
		ArrayList<TicketDTO> values = new ArrayList<TicketDTO>();
		for (TicketDTO TicketDTO : super.values())
		{
			if(TicketDTO.getEventCode() == eventCode)
			{
				values.add(TicketDTO);
			}
		}
		return values;
	}
	
	/**
	 * Obtiene los TicketService que coincidan con un codigo de evento y un tipo de localidad especifica
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public ArrayList<TicketDTO> findByEventAndLocation(int eventCode, int locationTypeId)
	{
		ArrayList<TicketDTO> values = new ArrayList<TicketDTO>();
		for (TicketDTO TicketDTO : super.values())
		{
			if(TicketDTO.getEventCode() == eventCode && TicketDTO.getLocationType() == locationTypeId)
			{
				values.add(TicketDTO);
			}
		}
		return values;
	}
}