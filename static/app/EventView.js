Vue.component('EventView', {
    props: {
    },

    data() {
        return {
            id: "",
            event: "",
            firstOpen: true
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
                <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://citypal.me/wp-content/uploads/2016/04/01-e1462017909580.jpg" alt="..." /></div>
                <div class="col-md-6">
                    <div class="small mb-1">Average event rating: 3.6</div>
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
                        <input class="form-control text-center me-3 mr-2" id="inputQuantity" type="number" value="1" min="1" style="max-width: 65px" />
                        <button class="btn btn-outline-dark flex-shrink-0" type="button">
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
    </div>
    `
});