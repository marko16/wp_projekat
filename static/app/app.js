const Login = { template: "<Login></Login>" };
const EventListView = { template: '<event-list-view></event-list-view>' };
const EventView = { template: '<EventView></EventView>' };
const Register = { template: '<Register></Register>' };
const ChangePassword = { template: '<ChangePassword></ChangePassword>' }
const SalesmanRegister = { template: '<SalesmanRegister></SalesmanRegister>' }
const AddEvent = { template: '<AddEvent></AddEvent>' }
const EventSalesman = { template: '<EventSalesman></EventSalesman>' }
const EditEvent = { template: '<EditEvent></EditEvent>' }
const SalesmanTickets = { template: '<SalesmanTickets></SalesmanTickets>' }
const UserTickets = { template: '<UserTickets></UserTickets>' }
const Profile = { template: '<Profile></Profile>' }

const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/login', component: Login },
        { path: '/events', component: EventListView },
        { path: '/event/:id', name: 'event', component: EventView, props: true },
        { path: '/editEvent/:id', name: 'editEvent', component: EditEvent, props: true },
        { path: '/reg', component: Register },
        { path: '/changePassword', component: ChangePassword },
        { path: '/salesmanRegister', component: SalesmanRegister },
        { path: '/addEvent', component: AddEvent },
        { path: '/eventSalesman', component: EventSalesman },
        { path: '/salesmanTickets', component: SalesmanTickets },
        { path: '/ticketUser', component: UserTickets },
        { path: '/profie', component: Profile }
    ]
});

const app = new Vue({
    router,
    el: '#app'
});
