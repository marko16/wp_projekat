Vue.component("Profile", {

    data() {
        return {
            firstName: null,
            lastName: null,
            password: null,
            password2: null,
            gender: null,
            birthday: null,
            username: null
        }
    },

    mounted() {
        axios.get('/profile?username=' + window.localStorage.getItem("username"))
            .then(response => {
                const user = response.data
                this.firstName = user.firstName
                this.lastName = user.lastName
                this.username = user.username
                this.birthday = user.birthday.split("T")[0]
                if(user.gender === "MALE") this.gender = "male"
                else this.gender = "female"
            })

    },

    methods: {
        editProfile() {
            const role = localStorage.getItem("role")
            this.errors = null;
            if(!this.firstName || !this.lastName || !this.username || !this.gender){
                alert("Fill out all the fields")
            }else{
                axios.post('/editProfile', {}, { params: { firstName: this.firstName,
                    lastName: this.lastName,
                    username : this.username,
                    password: this.password,
                    gender : this.gender,
                    birthday : this.birthday,
                    role: role
                }}).then(response => (alert("You have successfully registered!")));
            }
        }
    },

    template: `
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header"  style="background-color:#333333; color:white">Profile</div>
          <div class="card-body">
            <div class="form-group row">
            <label
              for="username"
              class="col-md-4 col-form-label text-md-right"
              >Username: </label>
            <div class="col-md-6">
              <input
                type="text"
                id="username"
                class="form-control"
                name="username"
                v-model="username"
                readonly/>
            </div>
            </div>
           
            <div class="form-group row">
              <label
                for="firstName"
                class="col-md-4 col-form-label text-md-right"
                >First Name: </label>
              <div class="col-md-6">
                <input
                  type="text"
                  id="firstName"
                  class="form-control"
                  name="firstName"
                  v-model="firstName"/>
              </div>
            </div>
            <div class="form-group row">
              <label
                for="lastName"
                class="col-md-4 col-form-label text-md-right"
                >Last Name: </label>
              <div class="col-md-6">
                <input
                  type="text"
                  id="lastName"
                  class="form-control"
                  name="lastName"
                  v-model="lastName"/>
              </div>
            </div>
            <div class="form-group row">
            <label
              for="birthday"
              class="col-md-4 col-form-label text-md-right"
              >Date of birth: </label>
              <div class="col-md-6">
              <input 
              v-model="birthday" 
              class="form-control form-control-user"
              type="date" 
              name="birthday"
              required>
              </div>
              </div>
          <div class="form-group row">
              <label
                for="gender"
                class="col-md-4 col-form-label text-md-right"
                >Gender</label>
              <div class="form-check">
                <input class="form-check-input" type="radio" v-model="gender" value="male">
                          <label class="form-check-label" >
                            Male
                          </label>
                          </div>
              <label></label>
              <div class="form-check offset-md-1">
                  <input class="form-check-input" type="radio" v-model="gender" value="female" >
                  <label class="form-check-label">
                  Female
                </label>
            </div>
            </div>
            <div class="buttons col-md-1 offset-md-4">
              <button class="btn" style="margin: 1px; background-color:#0069d9; color:white" @click="editProfile">
                Edit profile
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    `
});