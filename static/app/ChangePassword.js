Vue.component("ChangePassword", {

    data() {
        return {
            user: null,
            username: "",
            newPassword: "",
            newPassword2: "",
            currentInput: ""
        }
    },

    mounted() {
        this.username = window.localStorage.getItem("username");
        axios.get('/getUser?username=' + this.username)
            .then(response => {
                 console.log(response.data);
                 this.user = response.data;
        });
    },

    methods: {
        changePassword() {
            if(this.newPassword === this.newPassword2 && this.newPassword !== "") {
                if (this.user.password !== this.currentInput)
                    alert("You did not enter your current password correctly!");
                else {
                    console.log(this.username);
                    axios.post('/changePassword',{}, { params: { password: this.newPassword, username: this.username }})
                    .then(response => {
                        if(response.data === true) {
                            alert("Password successfully changed!");
                            this.$router.push("/events")
                        }
                        else alert("wtf")
                    }).catch(error => alert(error.data));
                }
            } else {
                alert("Passwords do not match!")
            }
        }
    },

    template: `
    <div class="card card-outline-secondary container mt-5" style="width: 50%">
        <div class="card-header">
            <h3 class="mb-0">Change Password</h3>
        </div>
        <div class="card-body">
            <form class="form" role="form" autocomplete="off">
                <div class="form-group">
                    <label for="inputPasswordOld">Current Password</label>
                    <input type="password" class="form-control" id="inputPasswordOld" required="" v-model="currentInput">
                </div>
                <div class="form-group">
                    <label for="inputPasswordNew">New Password</label>
                    <input type="password" class="form-control" id="inputPasswordNew" required="" v-model="newPassword">
                    
                </div>
                <div class="form-group">
                    <label for="inputPasswordNewVerify">Verify</label>
                    <input type="password" class="form-control" id="inputPasswordNewVerify" required="" v-model="newPassword2">
                    <span class="form-text small text-muted">
                            To confirm, type the new password again.
                        </span>
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-lg float-right" @click="changePassword" style="background-color:#0069d9;">Save</button>
                </div>
            </form>
        </div>
    </div>
    `
});