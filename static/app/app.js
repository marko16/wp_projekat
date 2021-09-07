const Login = { template: "<Login></Login>" };
const EventListView = { template: '<event-list-view></event-list-view>'};
const EventView = { template: '<EventView></EventView>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/login', component: Login },
        { path: '/events', component: EventListView },
        { path: '/event/:id', name: 'event', component: EventView, props: true }
    ]
});

const app = new Vue({
    router,
    el: '#app'
});
