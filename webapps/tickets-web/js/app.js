// ***** PROMOTERS ***** //
framux.route('promoters',
           {template:'partials/Promoter/promoters-list.html',
            controller:'PromotersList',
            selector:'#content'});
			
framux.route('promotersNew',
           {template:'partials/Promoter/promoters-new.html',
           controller:'PromotersNew',
           selector:'#content'});

framux.route('promotersInsert',
           {controller:'PromotersInsert'});

framux.route('promotersDelete/:promoterId',
           {controller:'PromotersDelete'});		   

framux.route('promoters/:promoterId',
           {template:'partials/Promoter/promoters-detail.html',
           controller:'PromotersDetail',
           selector:'#content'});

framux.route('promotersUpdate/:promoterId',
           {controller:'PromotersUpdate'});

// ***** EVENTS ***** //
framux.route('events',
           {template:'partials/Event/events-list.html',
            controller:'EventsList',
            selector:'#content'});
			
framux.route('promoterEvents',
           {template:'partials/Event/events-listByPromoter.html',
            controller:'EventsListByPromoterCode',
            selector:'#content'});
	
framux.route('eventsNew',
           {template:'partials/Event/events-new.html',
           controller:'EventsNew',
           selector:'#content'});

framux.route("eventsSave", {controller:"EventsSave"});

framux.route('eventsDelete/:eventId',
           {controller:'EventsDelete'});		   

framux.route('events/:eventId',
           {template:'partials/Event/events-detail.html',
           controller:'EventsDetail',
           selector:'#content'});
		   
// ***** TICKETS ***** //

framux.route('ticketsNew/:eventId',
           {template:'partials/Ticket/tickets-new.html',
           controller:'TicketsNew',
           selector:'#content'});

framux.route('ticketsInsert',
           {controller:'TicketsInsert'});		   

framux.route('tickets/:ticketId',
           {template:'partials/Ticket/tickets-detail.html',
           controller:'TicketsDetail',
           selector:'#content'});

// ***** LOGIN ***** //

framux.route('loginForm',
           {template:'partials/User/login.html',
           controller:'LoginForm',
           selector:'#content'});

framux.route('login', {controller:'Login'});

framux.route('logout', {controller:'Logout'});

framux.go('events');