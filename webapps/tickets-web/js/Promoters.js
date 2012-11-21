var parser = new X2JS();

function PromotersList($) {
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		var json = parser.xml_str2json(data);
		$.promoters = json.Envelope.Body.PromotersListResponse.return;
		framux.render();
	},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersList xmlns="http://service"></PromotersList></soap:Body></soap:Envelope>','text/xml');
};

function PromotersDetail($,params) {
  xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
    var json = parser.xml_str2json(data);
    $global.promoter = json.Envelope.Body.PromotersDetailResponse.return;
    $.promoter = $global.promoter;
    framux.render();
  },'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersDetail xmlns="http://service"><code>'+params[0]+'</code></PromotersDetail></soap:Body></soap:Envelope>','text/xml');
};

function PromotersNew($) {
    $.promoter = { code : '', name : '', address : '', telephone : '',
		bank : '', commision : '', username : '', password : ''};
    $global.promoter = $.promoter;
    framux.render();
};

function PromotersUpdate($,params) {
  $.promoter = $global.promoter;
  xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
    framux.go('promoters');
  },'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersUpdate xmlns="http://service">'+CreateParams($, "Update")+'</PromotersUpdate></soap:Body></soap:Envelope>','text/xml');
};

function PromotersInsert($,params) {
  $.promoter = $global.promoter;
  xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
    framux.go('promoters');
  },'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersInsert xmlns="http://service">'+CreateParams($, "Insert")+'</PromotersInsert></soap:Body></soap:Envelope>','text/xml');
};

function PromotersDelete($,params) {
  $.promoter = $global.promoter;
  xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
    framux.go('promoters');
  },'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersDelete xmlns="http://service"><code>'+$.promoter.code+'</code></PromotersDelete></soap:Body></soap:Envelope>','text/xml');
};

function CreateParams($, modo){
	var params = "";
	if (modo == "Update")
		params += "<code>"+$.promoter.code+"</code>";
	params += "<name>"+$.promoter.name+"</name>";
	params += "<address>"+$.promoter.address+"</address>";
	params += "<telephone>"+$.promoter.telephone+"</telephone>";
	params += "<bank>"+$.promoter.bank+"</bank>";
	params += "<commision>"+$.promoter.commision+"</commision>";
	params += "<username>"+$.promoter.username+"</username>";
	params += "<password>"+$.promoter.password+"</password>";
	return params;
}