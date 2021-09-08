const Login = { template: "<Login></Login>" };
const EventListView = { template: '<event-list-view></event-list-view>' };
const EventView = { template: '<EventView></EventView>' };
const Register = { template: '<Register></Register>' };
const ChangePassword = { template: `<ChangePassword></ChangePassword>` }

const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/login', component: Login },
        { path: '/events', component: EventListView },
        { path: '/event/:id', name: 'event', component: EventView, props: true },
        { path: '/reg', component: Register },
        { path: `/changePassword`, component: ChangePassword }
    ]
});

const app = new Vue({
    router,
    el: '#app'
});
