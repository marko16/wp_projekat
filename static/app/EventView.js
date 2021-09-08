Vue.component('EventView', {
    props: {
    },

    data() {
        return {
            id: "",
            event: "",
            firstOpen: true,
            amount: 1,
            coef: 1,
            entirePrice: 0,
            ticketType: ""
        }
    },

    created() {
        $(document).on('show.bs.modal', '#map-modal', () => {
            if(this.firstOpen) {
                this.firstOpen = false;
                setTimeout(() => { this.map.updateSize(); }, 300);
            }
        });

        this.id = this.$route.params.id;
        this.loadEvent();
    },

    methods: {
        loadEvent() {
            axios.get("/event", { params: { id: this.id }}).
                then(response => {
                    this.event = response.data
                    this.loadMap();
            })
        },
        isActive() {
            if(this.event.isActive) {
                return "Active"
            }
            return "Not active"
        },
        parseLocation() {
            return `${this.event.location.street} ${this.event.location.number}, ${this.event.location.city} ${this.event.location.zipcode}`
        },

        purchase() {
            const username = window.localStorage.getItem("username");

            axios.post("/reserve", {}, { params: { username: "c1", eventId: this.id, ticketType: this.ticketType, amount: this.amount}})
                .then(response => {
                    if(response.data)
                        alert("Tickets purchased successfully!")
                    else
                        alert("Not enough available tickets!")
                })
        },

        calcPrice(coef) {
            console.log(this.amount)
            this.entirePrice = this.amount * this.event.regularPrice * coef;
            if(coef === 1) this.ticketType = "REGULAR"
            else if(coef === 2) this.ticketType = "FANPIT"
            else this.ticketType = "VIP"
        },

        loadMap() {
                this.map = new ol.Map({
                    target: this.$refs["map-root"],
                    layers: [
                        new ol.layer.Tile({
                            source: new ol.source.OSM()
                        })
                    ],
                    view: new ol.View({
                        center: ol.proj.fromLonLat([this.event.location.longitude, this.event.location.latitude]),
                        zoom: 18
                    })
                });
                const markers = new ol.layer.Vector({
                    source: new ol.source.Vector(),
                    style: new ol.style.Style({
                        image: new ol.style.Icon({
                            anchor: [0.5, 1],
                            src: "marker.png",
                        }),
                    }),
                });
                this.map.addLayer(markers);

                const marker = new ol.Feature(
                    new ol.geom.Point(ol.proj.fromLonLat([this.event.location.longitude, this.event.location.latitude]))
                );
                markers.getSource().addFeature(marker);

        },
    },

    template:
    `
    <div>
        <div class="container px-4 px-lg-5 my-5">
            <div class="row gx-4 gx-lg-5 align-items-center">
                <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" :src="event.poster" alt="..." /></div>
                <div class="col-md-6">
                    <div class="small mb-1 average-rating">Average event rating: 3.6</div>
                    <div class="col rate">
                          <input type="radio" id="star52" name="rate" value="5"/>
                          <label for="star52" title="text"></label>
                          <input type="radio" id="star42" name="rate" value="4"/>
                          <label for="star42" title="text"></label>
                          <input type="radio" id="star32" name="rate" value="3"/>
                          <label for="star32" title="text"></label>
                          <input type="radio" id="star22" name="rate" value="2"/>
                          <label for="star22" title="text"></label>
                          <input type="radio" id="star12" name="rate" value="1"/>
                          <label for="star12" title="text"></label> 
                    </div>
                    <h1 class="display-5 fw-bolder">{{event.name}}</h1>
                    <div class="fs-3 mb-3">
                        <span class="text-decoration-line-through">Ordinary card price: {{event.regularPrice}}</span>
                    </div>
                    <div class="mb-2">
                        <h5>Event type: {{event.eventType}}</h5> 
                    </div>
                    <h5 class="mb-2">Capacity: {{event.capacity}}, Available: {{event.availableTickets}}</h5>
                    <h5 class="mb-2">Start date and time: {{event.startTime}}</h5>
                    <p>Status: {{isActive()}}</p>
                    <a type="button" @click="loadMap()" style="cursor: pointer; position: relative; right: 20px" data-toggle="modal" data-target="#map-modal">
                        <h5 class="address"><i class="fa fa-map-marker location-pin" aria-hidden="true"></i>{{parseLocation()}}</h5></a>
                    <div class="d-flex">
                        <input class="form-control text-center me-3 mr-2" id="inputQuantity" type="number" value="1" min="1" style="max-width: 65px" v-model="amount"/>
                        <button @click="calcPrice(1)" class="btn btn-outline-dark flex-shrink-0" type="button" data-toggle="modal" data-target="#purchase-modal">
                            <i class="bi-cart-fill me-1"></i>
                            Purchase
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        
        
            <div id="map-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Map Preview</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div id="map" ref="map-root" class="map">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>
    </div>
    
    <div id="purchase-modal" class="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Purchase tickets</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="mb-3">Price: {{entirePrice}}</h5>
        <div class="btn-group" role="group" aria-label="Basic example">
          <button type="button" class="btn btn-secondary" @click="calcPrice(1)">REGULAR</button>
          <button type="button" class="btn btn-info" @click="calcPrice(2)">FANPIT</button>
          <button type="button" class="btn btn-dark" @click="calcPrice(4)">VIP</button>
        </div>
</div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="purchase()">Purchase</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
            </div>

    </div>
  </div>
</div>
    
    </div>
    </div>
    `
});