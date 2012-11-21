var parser = new X2JS();

function EventsListByPromoterCode($)
{
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		$.events = parser.xml_str2json(data).Envelope.Body.EventsListByPromoterCodeResponse.return;
		changeEventTypes($.events);
		$global.events = $.events;
		framux.render();
	}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsListByPromoterCode xmlns="http://service"><promoterCode>' + gsPromoterCode + '</promoterCode></EventsListByPromoterCode></soap:Body></soap:Envelope>', 'text/xml');
};

function EventsList($)
{
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		$.events = parser.xml_str2json(data).Envelope.Body.EventsListResponse.return;
		changeEventTypes($.events);
		$global.events = $.events;
		framux.render();
	}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsList xmlns="http://service"></EventsList></soap:Body></soap:Envelope>', 'text/xml');
};

function EventsDetail($, params)
{
	xhr('http://localhost:8089/axis2/services/TicketService',function(eventData) {
		$.event = parser.xml_str2json(eventData).Envelope.Body.EventsDetailResponse.return;
		$global.event = $.event;
		xhr('http://localhost:8089/axis2/services/TicketService',function(locationsData) {
			var locationsJson = parser.xml_str2json(locationsData).Envelope.Body.LocationsListByEventCodeResponse.return;
			var found, price, quantity, numbered;
			$.locations = [];
			for (a = 0; a < gaLocationTypes.length; a++)
			{
				price = "";
				quantity = "";
				numbered = false;
				found = false;
				for (var key in locationsJson) 
				{
					if (locationsJson.hasOwnProperty(key) && locationsJson[key].locationType == a)
					{
						price = locationsJson[key].price;
						quantity = locationsJson[key].quantity;
						numbered = locationsJson[key].numbered == "true";
						found = true;
					}
				}
				$.locations.push({"found" : found, "locationType" : a, "locationTypeDescription" : gaLocationTypes[a], "eventCode" : $.event.code, "price" : price, "quantity" : quantity, "numbered" : numbered});
			}
			$global.locations = $.locations;
			setEventTypesJson($);
			framux.render();
		}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><LocationsListByEventCode xmlns="http://service"><eventCode>' + params[0] + '</eventCode></LocationsListByEventCode></soap:Body></soap:Envelope>', 'text/xml');
	}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsDetail xmlns="http://service"><eventCode>' + params[0] + '</eventCode></EventsDetail></soap:Body></soap:Envelope>', 'text/xml');
};

function EventsNew($)
{
	$global.event = $.event =
	{
		"code" : Math.floor(Math.random() * 1000)
		, "name" : ""
		, "eventType" : ""
		, "artists" : ""
		, "date" : ""
		, "place" : ""
		, "promoterCode" : gsPromoterCode
	}
	$.locations = [];
	for (a = 0; a < gaLocationTypes.length; a++)
	{
		$.locations.push({"found" : false, "locationType" : a, "locationTypeDescription" : gaLocationTypes[a], "eventCode" : $.event.code, "price" : "", "quantity" : "", "numbered" : false});
	}
	$global.locations = $.locations;
	setEventTypesJson($);
	framux.render();
};

function EventsSave($)
{
	$.event = $global.event;
	$.locations = $global.locations;
	var eventXML = "<code>" + $.event.code + "</code>"
		+ "<name>" + $.event.name +"</name>"
		+ "<eventType>" + $.event.eventType + "</eventType>"
		+ "<artists>" + $.event.artists + "</artists>"
		+ "<date>" + $.event.date + "</date>"
		+ "<place>" + $.event.place + "</place>"
		+ "<promoterCode>" + $.event.promoterCode + "</promoterCode>";
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		for (var key in $.locations) 
		{
			if ($.locations.hasOwnProperty(key) && $.locations[key].found)
			{
				var locationXML = "<locationType>" + $.locations[key].locationType + "</locationType>"
					+ "<eventCode>" + $.locations[key].eventCode + "</eventCode>"
					+ "<price>" + $.locations[key].price + "</price>"
					+ "<quantity>" + $.locations[key].quantity + "</quantity>"
					+ "<numbered>" + $.locations[key].numbered + "</numbered>";
				xhr('http://localhost:8089/axis2/services/TicketService', function(data) { }, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><LocationsSave xmlns="http://service">' + locationXML +'</LocationsSave></soap:Body></soap:Envelope>', 'text/xml');
			}
		}
		framux.go('events');
	}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsSave xmlns="http://service">' + eventXML +'</EventsSave></soap:Body></soap:Envelope>', 'text/xml');
};

function EventsDelete($, params)
{
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		framux.go('events');
	}, 'POST', '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><EventsDelete xmlns="http://service"><code>' + params[0] + '</code></EventsDelete></soap:Body></soap:Envelope>', 'text/xml');
};

function setEventTypesJson($)
{
	$.eventTypes = [];
	for (a = 0; a < gaEventTypes.length; a++)
	{
		$.eventTypes.push({
			"value" : a
			, "description" : gaEventTypes[a]
		});
	}
	$global.eventTypes = $.eventTypes;
}

function changeEventTypes(array)
{
	for (var key in array) 
	{
		if (array.hasOwnProperty(key))
		{
			array[key].eventType = gaEventTypes[array[key].eventType];
		}
	}
};