Vue.component('AddEvent', {

    data() {
        return {
            event: {},
            name: "",
            type: "",
            capacity: "",
            startTime: "",
            price: "",
            poster: "",
            street: "",
            number: "",
            city: "",
            zipcode: "",
            location: {}
        }
    },

    methods: {
        async addEvent(e) {
            e.preventDefault()
            this.event.name = this.name
            this.event.capacity = parseInt(this.capacity)
            this.event.regularPrice = parseFloat(this.regularPrice)
            this.event.startTime = this.startTime
            this.event.salesman = localStorage.getItem("username")

            await this.getLocation();
            console.log(this.event)
            console.log(this.location)
            axios.post("/addEvent", {
                event: this.event,
                location: this.location
            })
                .then(response => {
                    console.log(response.data)
                    if(response.data) alert("You have successfully added this event!")
                    else alert("This location is not available at the desired time!")
                })
        },

        addImage(event) {
            const file = event.target.files[0];
            const reader = new FileReader();
            reader.onload = (e) => {
                this.poster = e.target.result;
                console.log(this.poster);
            }
            reader.readAsDataURL(file);
        },

        getLocation() {
            const address = `${this.street} ${this.number} ${this.city} ${this.zipcode}`
            axios.get("https://nominatim.openstreetmap.org/search.php?q=" + address + "&polygon_geojson=1&format=jsonv2")
                .then(response => {
                    console.log(response.data)
                    if(response.data.length === 0) {
                        alert("Location not found")
                    } else {
                        this.location.latitude = parseFloat(response.data[0].lat);
                        this.location.longitude = parseFloat(response.data[0].lon);
                        this.location.street = this.street
                        this.location.number = this.number
                        this.location.city = this.city
                        this.location.zipcode = parseInt(this.zipcode)
                    }
                });
        },

    },

    template: `
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header" style="background-color:#333333; color:white">Add event</div>
          <div class="card-body">
            <form name="myform" @submit="addEvent">
            <div class="form-group row">
            <label
              for="name"
              class="col-md-4 col-form-label text-md-right"
              >Event name: </label>
            <div class="col-md-6">
              <input
                type="text"
                id=""
                class="form-control"
                name="name"
                v-model="name"
                required/>
            </div>
            </div>
            <div class="form-group row">
            <label
              for="type"
              class="col-md-4 col-form-label text-md-right">Event type: </label>
            <div class="col-md-6">
              <input
                type="type"
                id="type"
                class="form-control"
                name="type"
                v-model="type"
                required/>
            </div>
            </div>
            <div class="form-group row">
              <label
                for="capacity"
                class="col-md-4 col-form-label text-md-right"
                >Capacity: </label>
              <div class="col-md-6">
                <input
                  type="number"
                  min="1"
                  id="capacity"
                  class="form-control"
                  name="capacity"
                  v-model="capacity"
                  required/>
              </div>
            </div>
            <div class="form-group row">
              <label
                for="startTime"
                class="col-md-4 col-form-label text-md-right"
                >Start time: </label>
              <div class="col-md-6">
                <input
                  type="datetime-local" 
                  id="startTime"
                  class="form-control"
                  name="startTime"
                  v-model="startTime"
                  required/>
              </div>
            </div>
            <div class="form-group row">
              <label
                for="price"
                class="col-md-4 col-form-label text-md-right"
                >Regular price: </label>
              <div class="col-md-6">
                <input
                  type="number"
                  min="1"
                  id="price"
                  class="form-control"
                  name="price"
                  v-model="price"
                  required/>
              </div>
            </div>
            <br>
            <div class="form-group row">
            <label
              for="street"
              class="col-md-4 col-form-label text-md-right"
              >Street: </label>
              <div class="col-md-6">
              <input 
              v-model="street" 
              class="form-control form-control-user"
              type="text" 
              name="street"
              required>
              </div>
              </div>
              <div class="form-group row">
            <label
              for="number"
              class="col-md-4 col-form-label text-md-right"
              >Number: </label>
              <div class="col-md-6">
              <input 
              v-model="number" 
              class="form-control form-control-user"
              type="text" 
              name="number"
              required>
              </div>
              </div>
              <div class="form-group row">
            <label
              for="city"
              class="col-md-4 col-form-label text-md-right"
              >City: </label>
              <div class="col-md-6">
              <input 
              v-model="city" 
              class="form-control form-control-user"
              type="text" 
              name="city"
              required>
              </div>
              
              </div>
              <div class="form-group row">
            <label
              for="zipcode"
              class="col-md-4 col-form-label text-md-right"
              >Zipcode: </label>
              <div class="col-md-6">
              <input 
              v-model="zipcode" 
              class="form-control form-control-user"
              type="number" 
              name="zipcode"
              required>
              </div>
              </div>
          <div class="form-group row">
              <label
                for="poster"
                class="col-md-4 col-form-label text-md-right"
                >Poster</label>
                <input type="file" class="ml-3" @change="addImage">
            </div>
            <div class="buttons col-md-1 offset-md-4">
              <button class="btn" style="margin: 1px; background-color:#0069d9; color:white" >
                Add
              </button>
            </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    `
});