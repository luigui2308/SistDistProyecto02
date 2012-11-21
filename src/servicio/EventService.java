package servicio;
import servicio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Almacen de datos donde se guardan todos los Eventos
 * @see EventDTO
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
public class EventService extends HashMap<Integer, EventDTO>
{
	private static String[][] data =
	{
		{"1808835074","Le canta la gallina","3","Por asignar","20 Dic 2012","Universidad Nacional","1948347202"}
		, {"586998177","X-Knights","1","Conductores","17 Nov 2012","Estadio Ricardo Saprissa, Tibas","1753281862"}
		, {"1589262537","Concierto de Evanescence","1","Amy Lee","02 Nov 2012","Palacio de los deportes, Heredia","1753281862"}
	};

	private static EventService instance;
	/**
	 * Singleton
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public static EventService getInstance()
	{
		if (instance == null)
		{
			instance = new EventService();
		}
		return instance;
	}
	
	/**
	 * Constructor que inicializa el almacen de datos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	private EventService()
	{
		for (int a = 0; a < data.length; a++)
		{
			EventDTO instance = new EventDTO();
			instance.setCode(Integer.parseInt(data[a][0]));
			instance.setName(data[a][1]);
			instance.setEventType(Integer.parseInt(data[a][2]));
			instance.setArtists(data[a][3]);
			instance.setDate(data[a][4]);
			instance.setPlace(data[a][5]);
			instance.setPromoterCode(Integer.parseInt(data[a][6]));
			super.put(instance.getCode(), instance);
		}
	}
	
	/**
	 * Obtiene todos los promotores que coincidan con un codigo de promotor
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public ArrayList<EventDTO> getByPromoterCode(int code)
	{
		ArrayList<EventDTO> values = new ArrayList<EventDTO>();
		for (EventDTO EventDTO : super.values())
		{
			if (EventDTO.getPromoterCode() == code)
			{
				values.add(EventDTO);
			}
		}
		return values;
	}
}