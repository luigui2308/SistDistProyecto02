var parser = new X2JS();

function TicketsNew($, params) {
    $.customer = { id : '', name : '', address : '', telephone : '',
		cardnumber : '', cardType : ''};
    $global.customer = $.customer;
	
	$.ticket = {locationType:'', numbered:'', quantity:'', eventCode: params[0]};
	$global.ticket = $.ticket;
	
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		var json = parser.xml_str2json(data);
		$.locations = json.Envelope.Body.LocationsListByEventCodeResponse.return;
		framux.render();
	},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><LocationsListByEventCode xmlns="http://service"><eventCode>' + params[0] + '</eventCode></LocationsListByEventCode></soap:Body></soap:Envelope>','text/xml');
	
    framux.render();
};

function TicketsInsert($){
	$.customer = $global.customer;
	$.ticket = $global.ticket;
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		var ticketCode = Math.floor(Math.random() * 1000000);
		xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
			framux.go('tickets/'+ticketCode); 
		},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><TicketsInsert xmlns="http://service">'+TicketParams($, ticketCode)+'</TicketsInsert></soap:Body></soap:Envelope>','text/xml');
	},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><CustomersUpdate xmlns="http://service">'+CustomerParams($)+'</CustomersUpdate></soap:Body></soap:Envelope>','text/xml');
}

function TicketsDetail($, params)
{
	$.customer = $global.customer;
	var eventCode = $global.ticket.eventCode;
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		var json = parser.xml_str2json(data);
		$global.ticket = json.Envelope.Body.TicketsDetailResponse.return;
		$.ticket = $global.ticket;
		
		xhr('http://localhost:8089/axis2/services/TicketService',function(numberedData) {
			json = parser.xml_str2json(numberedData);
			$global.numeradas = json.Envelope.Body.NumberedTicketsListByTicketCodeResponse.return;
			$.numeradas = $global.numeradas;
			
			$.ticket.numeradas = formatNumeradas($.numeradas);
			
			xhr('http://localhost:8089/axis2/services/TicketService',function(eventData) {
				json = parser.xml_str2json(eventData);
				$global.event = json.Envelope.Body.EventsDetailResponse.return;
				$.event = $global.event;
				framux.render();
			},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsDetail xmlns="http://service"><eventCode>'+eventCode+'</eventCode></EventsDetail></soap:Body></soap:Envelope>','text/xml');
		},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><NumberedTicketsListByTicketCode xmlns="http://service"><ticketCode>'+params[0]+'</ticketCode></NumberedTicketsListByTicketCode></soap:Body></soap:Envelope>','text/xml');
	},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><TicketsDetail xmlns="http://service"><ticketCode>'+params[0]+'</ticketCode></TicketsDetail></soap:Body></soap:Envelope>','text/xml');
}

function formatNumeradas(array)
{
	var numeradas = "";
	for (var key in array) 
	{
		if (array.hasOwnProperty(key))
		{
			numeradas = array[key].locationNumber;
		}
	}
};

function CustomerParams($){
	var params = "";
	params += "<id>"+ $.customer.id + "</id>";
	params += "<name>"+$.customer.name+"</name>";
	params += "<address>"+$.customer.address+"</address>";
	params += "<telephone>"+$.customer.telephone+"</telephone>";
	params += "<cardnumber>"+$.customer.cardnumber+"</cardnumber>";
	params += "<cardType>"+$.customer.cardType+"</cardType>";
	return params;
}

function TicketParams($, ticket){
	var params = "<code>"+ ticket + "</code>";
	params += "<locationType>"+$.ticket.locationType+"</locationType>";
	params += "<eventCode>"+$.ticket.eventCode+"</eventCode>";
	params += "<customerId>"+$.customer.id+"</customerId>";
	params += "<quantity>"+$.ticket.quantity+"</quantity>";
	if($.ticket.numbered != '' || $.ticket.numbered != null)
		params += "<numbered>"+$.ticket.numbered+"</numbered>";
	else
		params += "<numbered>0</numbered>";
	return params;
}


