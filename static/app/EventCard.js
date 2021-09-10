Vue.component("EventCard", {
    props: {
        event: Object
    },

    data() {
        return {
            amount:1,
            ticketType: "REGULAR",
            entirePrice: ""
        }
    },

    // created() {
    //     console.log(event);
    //     this.event = {
    //         name: "event1",
    //         eventType: "tip1",
    //         description: "Nunc tincidunt volutpat hendrerit. Praesent lacinia arcu in dui imperdiet, ac mollis neque tristique. Maecenas id neque non sem feugiat efficitur. Fusce suscipit turpis nisi, at pretium dui commodo varius. Sed hendrerit et leo a rhoncus. Vivamus lobortis blandit turpis, non aliquet elit dignissim et. Integer quis iaculis ante."
    //     }
    // },

    methods: {
        calcPrice(coef) {
            console.log(this.event)
            this.entirePrice = this.amount * this.event.regularPrice * coef;
            if(coef === 1) this.ticketType = "REGULAR"
            else if(coef === 2) this.ticketType = "FANPIT"
            else this.ticketType = "VIP"
        },
        purchase() {
            const username = window.localStorage.getItem("username");

            axios.post("/reserve", {}, { params: { username: "c1", eventId: this.event.id, ticketType: this.ticketType, amount: this.amount}})
                .then(response => {
                    if(response.data)
                        alert("Tickets purchased successfully!")
                    else
                        alert("Not enough available tickets!")
                })
        },
        parseLocation() {
            return `${this.event.location.street} ${this.event.location.number}, ${this.event.location.city} ${this.event.location.zipcode}`
        },
        dateParse(date) {
            return date.split("T").join(", ")
        },
        redirectEvent() {
            this.router.push(`/${this.event.id}`)
        },
        seeMore() {
            this.$emit("seeMoreClicked", this.event);
            this.$router.push({path: "/event/" + this.event.id, name: "event", params: {id: this.event.id}});
        },

    },

    template:
    `
    <div>
    <div id="container" class="event-container">    
    
    <div class="product-details">
    <a @click="seeMore()">    
    <h1 class="event-title mb-5">{{event.name}}</h1>
    </a>
    
        
        
    <ul class="information">
        <li><strong>Event type : </strong> {{event.eventType}} </li>
        <li><strong>Start : </strong>{{dateParse(event.startTime)}}</li>
        <li><strong>Average rating : </strong>4.0</li>
    </ul>    
<!--    <p class="information">" Let's spread the joy , here is Christmas , the most awaited day of the year.Christmas Tree is what one need the most. Here is the correct tree which will enhance your Christmas.</p>-->

        
    <div class="control">
    
    <button class="btn">
        <span class="price">{{event.regularPrice}}</span>
        <span class="shopping-cart"><i class="fa fa-shopping-cart" aria-hidden="true"></i></span>
        <span class="buy" data-toggle="modal" data-target="#purchase-modal" @click="calcPrice(1)">Get now</span>
    </button>    
   
    
</div>


       <h5 class="address"><i class="fa fa-map-marker location-pin" aria-hidden="true"></i>{{this.parseLocation()}}</h5>      
</div>
    
<div class="product-image">
    
    <img :src="event.poster" alt="">
    

<div class="info">
    
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
    `
});