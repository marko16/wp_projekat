Vue.component("Login", {
    template: `
    <div class="text-center center-login" @submit.prevent="login">
        <form class="form-signin">
          <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
          <h1 class="h3 mb-4 font-weight-normal">Please sign in</h1>
          <label for="inputEmail" class="sr-only">Email address</label>
          <input type="email" id="inputEmail" class="form-control mb-2" placeholder="Email address" required="" autofocus="" v-model="email">
          <label for="inputPassword" class="sr-only">Password</label>
          <input type="password" id="inputPassword" class="form-control" placeholder="Password" required="" v-model="password">
          
          <button class="btn btn-lg btn-primary btn-block mt-4" type="submit" formmethod="post">Log in</button>
          <button class="btn btn-lg btn-outline-primary btn-block mt-2" type="button">Sign up</button>
        </form>
    </div>
    `,
    data() {
        return {
            email: "",
            password: "",
            showFailedLogin: false,
        }
    },

    methods: {
        login() {
            axios.post('auth/login', {
                username: this.email,
                password: this.password
            }).then(response => {
                console.log(response);
                localStorage.setItem("token", response);
                this.findUserRole();
            }).catch(err => {
                console.log(err);
                this.showFailedLogin = true;
            });
        },

        findUserRole() {
            let token = JSON.parse(atob(localStorage.getItem("token").split(".")[1]));
            let role = token.role;
            let activeUser = token.active;

            role = "USER";
            activeUser = true;
            console.log("Smth");

            if(active === false) {
                this.$router.push("login");
            }
            if(role === "USER") {
                this.$router.push("user");
            }
            if(role === "ADMIN") {
                this.$router.push("admin");
            }
            if(role === "SALESMAN") {
                this.$router.push("salesman");
            }
        }
    },
})