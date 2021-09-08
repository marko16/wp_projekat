Vue.component("Login", {
    template: `
    <div class="text-center center-login" >
        <form class="form-signin">
          <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
          <h1 class="h3 mb-4 font-weight-normal">Please sign in</h1>
          <label for="inputUsername" class="sr-only">Username</label>
          <input type="text" id="inputUsername" class="form-control mb-2" placeholder="Username" required="" autofocus="" v-model="username">
          <label for="inputPassword" class="sr-only">Password</label>
          <input type="password" id="inputPassword" class="form-control" placeholder="Password" required="" v-model="password">
          
          <button class="btn btn-lg btn-primary btn-block mt-4" type="button" @click="login">Log in</button>
          <button class="btn btn-lg btn-outline-primary btn-block mt-2" type="button" @click="register()">Sign up</button>
        </form>
    </div>
    `,
    data() {
        return {
            username: "",
            password: "",
            showFailedLogin: false,
        }
    },

    methods: {
        register() {
            this.$router.push("/reg");
        },
        login() {
            axios.post('/login', {}, {
                params: {username: this.username, password: this.password}
            }).then(response => {
                console.log(response);
                if (JSON.parse(JSON.stringify(response.data))[0] === " ") {
                    alert("Wrong username or password");
                } else if (JSON.parse(JSON.stringify(response.data))[0] === "blocked") {
                    alert("Your account is blocked");
                } else {
                    localStorage.setItem("username", JSON.parse(JSON.stringify(response.data))[0]);
                    localStorage.setItem("role", JSON.parse(JSON.stringify(response.data))[1]);
                    this.$router.push("events")
                }
            }).catch(err => {
                console.log(err);
                this.showFailedLogin = true;
            });
        },
    }
})