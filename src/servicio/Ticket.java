package servicio;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Servicio web que controla todas las acciones de tickets
 * @author Luis Roldan Chacon
 * @author Gilberth Arce Hernandez
 */
@WebService (targetNamespace ="http://service", name="ticket")
public class Ticket
{
	private static UserService users = UserService.getInstance();
	private static EventService events = EventService.getInstance();
	private static TicketService tickets = TicketService.getInstance();
	private static CustomerService customers = CustomerService.getInstance();
	private static LocationService locations = LocationService.getInstance();
	private static PromoterService promoters = PromoterService.getInstance();
	private static RolesByUserService rolesByUsers = RolesByUserService.getInstance();
	private static NumberedTicketService numberedTickets = NumberedTicketService.getInstance();

	/**
	 * Metodo que atiende una solicitud POST de un cliente y lo inserta
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:CustomersUpdate", operationName="CustomersUpdate")
	public void CustomersUpdate(@WebParam (partName = "id") String id,@WebParam (partName = "name") String name,@WebParam (partName = "address") String address,@WebParam (partName = "telephone") String telephone,@WebParam (partName = "cardnumber") String cardnumber,@WebParam (partName = "cardType") String cardType)
	{
		customers.put(id, new CustomerDTO(id, name, address, telephone, cardnumber, cardType));
	}
	
	/**
	 * Metodo que atiende una solicitud POST de un ticket y lo inserta
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:TicketsInsert", operationName="TicketsInsert")
	public void TicketsInsert(@WebParam (partName = "code") int code,@WebParam (partName = "locationType") int locationType,@WebParam (partName = "eventCode") int eventCode,@WebParam (partName = "customerId") String customerId,@WebParam (partName = "quantity") int quantity, @WebParam (partName = "numbered") int numbered)
	{
		TicketDTO ticket = new TicketDTO(code, locationType, eventCode, customerId, (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(Calendar.getInstance().getTime()), quantity, 0);
		LocationDTO location = locations.get(ticket.getLocationType() + "$" + ticket.getEventCode());
		if (ticket.getQuantity() + getNumberOfTicketsByEventAndLocation(ticket.getEventCode(), ticket.getLocationType()) <= location.getQuantity())
		{
			ticket.setAmount(ticket.getQuantity() * location.getPrice());
			tickets.put(ticket.getCode(), ticket);
			if (location.getNumbered())
			{
				int offset = numbered;
				if (offset > 1 && offset <= location.getQuantity())
				{
					int up = offset, down = offset - 1, needed = ticket.getQuantity();
					while (needed > 0)
					{
						if (up <= location.getQuantity())
						{
							if(!ticketExists(up, ticket.getEventCode(), ticket.getLocationType()))
							{
								numberedTickets.put(up + "$" + ticket.getCode(), new NumberedTicketDTO(up, ticket.getCode()));
								needed--;
							}
							up++;
							if (needed <= 0) break;
						}
						if (down > 0)
						{
							if (!ticketExists(down, ticket.getEventCode(), ticket.getLocationType()))
							{
								numberedTickets.put(down + "$" + ticket.getCode(), new NumberedTicketDTO(down, ticket.getCode()));
								needed--;
							}
							down--;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un ticket y lo devuelve segun su codigo
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:TicketsDetail", operationName="TicketsDetail")
	public TicketDTO TicketsDetail(@WebParam (partName = "ticketCode") int ticketCode)
	{
		return tickets.get(ticketCode);
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un cliente y lo retorna segun su usuario
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:CustomersDetail", operationName="CustomersDetail")
	public CustomerDTO CustomersDetail(@WebParam (partName = "username") String username)
	{
		return customers.get(username);
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un ticket numerado y retorna todos los tickets numerados asociados a un ticket
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:NumberedTicketsListByTicketCode", operationName="NumberedTicketsListByTicketCode")
	public ArrayList<NumberedTicketDTO> NumberedTicketsListByTicketCode(@WebParam (partName = "ticketCode") int ticketCode)
	{
		return numberedTickets.getByTicketCode(ticketCode);
	}
	
	/**
	 * Obtiene el numero de ticket's comprados para un evento y un tipo de localidad
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public int getNumberOfTicketsByEventAndLocation(int eventCode, int locationTypeId)
	{
		int numberOfTickets = 0;
		for (TicketDTO ticket : tickets.findByEventAndLocation(eventCode, locationTypeId))
		{
			numberOfTickets += ticket.getQuantity();
		}
		return numberOfTickets;
	}
	
	/**
	* Método que permite obtener la existencia de un ticket numerado
	* @return true si hay un ticket con la misma numeracion
	* @author Luis Roldan Chacon
	* @author Gilberth Arce Hernandez
	*/
	public boolean ticketExists(int locationNumber, int eventCode, int locationType)
	{
		for (TicketDTO ticket : tickets.findByEventAndLocation(eventCode, locationType))
		{
			if (numberedTickets.get(locationNumber + "$" + ticket.getCode()) != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Metodo que atiende una solicitud GET y confirma que un usuario tiene un rol especifico
	 * @return RoleByUser relacionado a los parametros establecidos
	 * @see NumberedTicketDTO
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:RolesByUsersDetail", operationName="RolesByUsersDetail")
	public RolesByUserDTO RolesByUsersDetail(@WebParam (partName = "username") String username, @WebParam (partName = "role") int role)
	{
		return rolesByUsers.get(username + "$" + role);
	}

	/**
	 * Metodo que atiende una solicitud GET y verifica que exista un usuario que coincida con los parametros proporcionados
	 * @return Usuario que coincide con el username y el password
	 * @see NumberedTicketDTO
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:UsersCredentials", operationName="UsersCredentials")
	public UserDTO UsersCredentials(@WebParam (partName = "username") String username, @WebParam (partName = "password") String password) throws Exception
	{
		UserDTO user = users.get(username);
		if (user != null && user.getPassword().equals(password))
		{
			return user;
		}
		throw new Exception("Not such object on this server!");
	}

	/**
	 * Metodo que atiende una solicitud GET y devuelve una lista de eventos
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:EventsList", operationName="EventsList")
	public ArrayList<EventDTO> EventsList()
	{
		ArrayList<EventDTO> values = new ArrayList<EventDTO>();
		values.addAll(events.values());
		return values;
	}
	
	/**
	 * Metodo que atiende una solicitud GET y devuelve una lista de eventos asiciados a un promotor
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:EventsListByPromoterCode", operationName="EventsListByPromoterCode")
	public ArrayList<EventDTO> EventsListByPromoterCode(@WebParam (partName = "promoterCode") int promoterCode)
	{
		return events.getByPromoterCode(promoterCode);
	}
	
	/**
	 * Metodo que atiende una solicitud GET y devuelve el detalle de un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:EventsDetail", operationName="EventsDetail")
	public EventDTO EventsDetail(@WebParam (partName = "eventCode") int eventCode)
	{
		return events.get(eventCode);
	}

	/**
	 * Metodo que atiende una solicitud GET y devuelve una lista de localidades asociadas a un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:LocationsListByEventCode", operationName="LocationsListByEventCode")
	public ArrayList<LocationDTO> LocationsListByEventCode(@WebParam (partName = "eventCode") int eventCode)
	{
		return locations.getByEventCode(eventCode);
	}
	
	/**
	 * Metodo que atiende una solicitud GET y obtiene los tickets asociados a un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:TicketsListByEventCode", operationName="TicketsListByEventCode")
	public ArrayList<TicketDTO> TicketsListByEventCode(@WebParam (partName = "eventCode") int eventCode)
	{
		return tickets.getByEventCode(eventCode);
	}

	/**
	 * Metodo que atiende una solicitud POST e inserta un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:TicketsInsert", operationName="TicketsInsert")
	public void TicketsInsert(@WebParam (partName = "code") int code,@WebParam (partName = "name") String name,@WebParam (partName = "eventType") int eventType,@WebParam (partName = "artists") String artists,@WebParam (partName = "date") String date,@WebParam (partName = "place") String place, @WebParam (partName = "promoterCode") int promoterCode)
	{
		events.put(code , new EventDTO(code, name, eventType, artists, date, place, promoterCode));
	}
	
	/**
	 * Metodo que atiende una solicitud POST e inserta una localidad
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:LocationsSave", operationName="LocationsSave")
	public void LocationsSave(@WebParam (partName = "locationType") int locationType, @WebParam (partName = "eventCode") int eventCode, @WebParam (partName = "price") int price, @WebParam (partName = "quantity") int quantity, @WebParam (partName = "numbered") boolean numbered)
	{
		locations.put(locationType + "$" + eventCode, new LocationDTO(locationType, eventCode, price, quantity, numbered));
	}
	
	/**
	 * Metodo que atiende una solicitud PUT y actualiza un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:EventsSave", operationName="EventsSave")
	public void EventsSave(@WebParam (partName = "code") int code,@WebParam (partName = "name") String name,@WebParam (partName = "eventType") int eventType,@WebParam (partName = "artists") String artists,@WebParam (partName = "date") String date,@WebParam (partName = "place") String place, @WebParam (partName = "promoterCode") int promoterCode)
	{
		events.put(code ,new EventDTO(code, name, eventType, artists, date, place, promoterCode));
	}
	
	/**
	 * Metodo que atiende una solicitud DELETE y elimina un evento y sus localidades
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:EventsDelete", operationName="EventsDelete")
	public void EventsDelete(@WebParam (partName = "code") int code) throws Exception
	{
		deleteLocationsByEvent(code);
		events.remove(code);
	}
	
	/**
	 * Metodo que atiende una solicitud DELETE y elimina las localidades de un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:LocationsDeleteByEventCode", operationName="LocationsDeleteByEventCode")
	public void LocationsDeleteByEventCode(@WebParam (partName = "eventCode") int eventCode)
	{
		deleteLocationsByEvent(eventCode);
	}
	
	/**
	 * Metodo que elimina todas las localidades asociadas a un evento
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	public void deleteLocationsByEvent(int eventCode)
	{
		ArrayList<String> keys = new ArrayList<String>();
		for (LocationDTO location : locations.values())
		{
			if (location.getEventCode() == eventCode)
			{
				keys.add(location.getLocationType() + "$" + eventCode);
			}
		}
		for (String key : keys)
		{
			locations.remove(key);
		}
	}

	/**
	 * Metodo que atiende una solicitud GET de un promotor y lista todos los promotores
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersList", operationName="PromotersList")
	public ArrayList<PromoterDTO> PromotersList()
	{
		ArrayList<PromoterDTO> values = new ArrayList<PromoterDTO>();
		values.addAll(promoters.values());
		return values;
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un promotor y obtiene su detalle segun su codigo
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersDetail", operationName="PromotersDetail")
	public PromoterDTO PromotersDetail(@WebParam (partName = "code") int code)
	{
		return promoters.get(code);
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un promotor y obtiene su detalle segun su usuario
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersDetailByUsername", operationName="PromotersDetailByUsername")
	public PromoterDTO PromotersDetailByUsername(@WebParam (partName = "username") String username)
	{
		return promoters.getByUsername(username);
	}
	
	/**
	 * Metodo que atiende una solicitud GET de un usuario y lo retoran segun su usuario
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:UsersDetail", operationName="UsersDetail")
	public UserDTO UsersDetail(@WebParam (partName = "username") String username)
	{
		return users.get(username);
	}
	
	/**
	 * Metodo que atiende una solicitud POST de un promotor y lo inserta
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersInsert", operationName="PromotersInsert")
	public void PromotersInsert(@WebParam (partName = "name") String name, @WebParam (partName = "address") String address, @WebParam (partName = "telephone") String telephone, @WebParam (partName = "bank") String bank, @WebParam (partName = "commision") int commision, @WebParam (partName = "username") String username, @WebParam (partName = "password") String password)
	{
		int code = (new Random()).nextInt();
		promoters.put(code, new PromoterDTO(code, name, address, telephone, bank, commision ,username));
		users.put(username, new UserDTO(username, password));
		rolesByUsers.put(username + "$2", new RolesByUserDTO(username, 2));
	}
	
	/**
	 * Metodo que atiende una solicitud PUT de un promotor y lo actualiza
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersUpdate", operationName="PromotersUpdate")
	public void PromotersUpdate(@WebParam (partName = "code") int code, @WebParam (partName = "name") String name, @WebParam (partName = "address") String address, @WebParam (partName = "telephone") String telephone, @WebParam (partName = "bank") String bank, @WebParam (partName = "commision") int commision, @WebParam (partName = "username") String username, @WebParam (partName = "password") String password)
	{
		promoters.put(code, new PromoterDTO(code, name, address, telephone, bank, commision ,username));
		users.put(username, new UserDTO(username, password));
		rolesByUsers.put(username + "$2", new RolesByUserDTO(username, 2));
	}
	
	/**
	 * Metodo que atiende una solicitud DELETE de un poromotor y lo elimina
	 * @author Luis Roldan Chacon
	 * @author Gilberth Arce Hernandez
	 */
	@WebMethod (action="urn:PromotersDelete", operationName="PromotersDelete")
	public void PromotersDelete(@WebParam (partName = "code") int code, @WebParam (partName = "username") String username)
	{
		promoters.remove(code);
		users.remove(username);
	}
}