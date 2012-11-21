::PATH=D:\Program Files\Java\jdk1.7.0_07\bin
PATH = C:\Program Files\Java\jdk1.7.0_09\bin

:: Compilar el Servicio
javac -cp "tickets";"tickets\lib\*" -d tickets src\servicio\CustomerDTO.java src\servicio\CustomerService.java src\servicio\EventDTO.java src\servicio\EventService.java src\servicio\LocationDTO.java src\servicio\LocationService.java src\servicio\NumberedTicketDTO.java src\servicio\NumberedTicketService.java src\servicio\PromoterDTO.java src\servicio\PromoterService.java src\servicio\RolesByUserDTO.java src\servicio\RolesByUserService.java src\servicio\Ticket.java src\servicio\TicketDTO.java src\servicio\TicketService.java src\servicio\UserDTO.java src\servicio\UserService.java

:: Crear el .Aar
cd tickets
jar cvf ../tickets.aar .

pause