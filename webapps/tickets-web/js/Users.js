var gbPromoter = false;
var gbAdministrator = false;
var gsPromoterCode = -1;
var gsUsername = "";

var gaEventTypes = ["Concierto", "Recital", "Obra de teatro", "Danza", "Otro"];
var gaLocationTypes = ["Sol", "Sombre", "Platea", "Palco", "Preferencial", "VIP"];

function LoginForm($)
{
	$global.user = $.user = {"username":"", "password":""};
	framux.render();
}

function Login($)
{
	$.user = $global.user;
	xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
		if (parser.xml_str2json(data).Envelope.Body.UsersCredentialsResponse.return.username != null)
		{
			gsUsername = $.user.username;
			xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
				if (parser.xml_str2json(data).Envelope.Body.RolesByUsersDetailResponse.return.role != null)
				{
					gbAdministrator = true;
				}
				xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
					if (parser.xml_str2json(data).Envelope.Body.RolesByUsersDetailResponse.return.role != null)
					{
						gbPromoter = true;
						xhr('http://localhost:8089/axis2/services/TicketService',function(data) {
							var promoter = parser.xml_str2json(data).Envelope.Body.PromotersDetailByUsernameResponse.return;
							if (promoter.code != null)
							{
								gsPromoterCode = promoter.code;
							}
							setNavigationTabs();
							framux.go('events');
						},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><PromotersDetailByUsername xmlns="http://service"><username>' + $.user.username + '</username></PromotersDetailByUsername></soap:Body></soap:Envelope>','text/xml');
					}
					else
					{
						setNavigationTabs();
						framux.go('events');
					}
				},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><RolesByUsersDetail xmlns="http://service"><username>' + $.user.username + '</username><role>2</role></RolesByUsersDetail></soap:Body></soap:Envelope>','text/xml');
			},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><RolesByUsersDetail xmlns="http://service"><username>' + $.user.username + '</username><role>1</role></RolesByUsersDetail></soap:Body></soap:Envelope>','text/xml');
		}
		else
		{
			setNavigationTabs();
			framux.go('events');
		}
	},'POST','<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><UsersCredentials xmlns="http://service"><username>' + $.user.username + '</username><password>' + $.user.password + '</password></UsersCredentials></soap:Body></soap:Envelope>','text/xml');
}

function Logout()
{
	gbPromoter = false;
	gbAdministrator = false;
	gsUsername = "";
	gsPromoterCode = -1;
	setNavigationTabs();
	framux.go('events');
}

function setNavigationTabs()
{
	var navigation = d3.select("#navigation");
	navigation.selectAll("ul").remove();
	var ul = navigation.append("ul");
	ul.append("li").append("a").attr("href", "#events").text("Compra");
	if (gbAdministrator)
	{
		ul.append("li").append("a").attr("href", "#promoters").text("Promotores");
	}
	if (gbPromoter)
	{
		ul.append("li").append("a").attr("href", "#promoterEvents").text("Eventos");
	}
	if (gsUsername != "")
	{
		ul.append("li").append("a").attr("href", "#logout").text("Salir");
	}
	else
	{
		ul.append("li").append("a").attr("href", "#loginForm").text("Entrar");
	}
}