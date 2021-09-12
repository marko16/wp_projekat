Vue.component("EventListView", {


    data() {
        return {
            events: [],
            searchQuery: null,
        }
    },

    // created() {
    //     this.events = [1,2,3,4,5]
    //     this.events.push({eventType:"event1"})
    // },

    mounted() {
        axios.get("/events").then(
            response => {
                this.events = response.data;
            })
    },

    methods: {
        onSeeMore(event) {
            this.chosenEvent = event;
        },
        searchEvents() {
            if(this.searchQuery === null) return;
            axios.get("/searchEvents?search=" + this.searchQuery)
                .then(response => {
                    console.log(response.data)
                    if(response.data.length !== 0)
                        this.events = response.data
                    else
                        alert("No results!")
                })
        }
    },

    template: `
<div class="container" style="width: 50%">
    <div class="input-group mb-3">
  <input type="text" class="form-control" placeholder="Search..." aria-label="" aria-describedby="basic-addon2" v-model="searchQuery">
  <div class="input-group-append">
    <button class="btn btn-primary" type="button" @click="searchEvents">Search</button>
  </div>
    </div>
    <div>
        <div v-for="e in events" :key="e.id">
<!--            <div class="card m-2 p-2">-->
<!--                <div class="row">-->
<!--                    <div class="col col-lg-3">-->
<!--                        <img src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" height="200">-->
<!--                    </div>-->
<!--                    <div class="col">-->
<!--                       <h2>sdljkgfnsl;d</h2>-->
<!--                        <div class="row"><p class="h-50 mb-4 text-justify">loreesaf;kljbeadpoiuktgaeiupfagpwiuesvfpisewbgfi</p></div>-->
<!--                    </div>-->
<!--                </div>-->
<!--            </div>-->

        <EventCard :event="e"/>
<!--    <div id="container" class="event-container">    -->
<!--    -->
<!--    <div class="product-details">-->
<!--        -->
<!--    <h1 class="event-title">CHRISTMAS TREE</h1>-->
<!--    <div class="col rate">-->
<!--          <input type="radio" id="star52" name="rate" value="5"/>-->
<!--          <label for="star52" title="text"></label>-->
<!--          <input type="radio" id="star42" name="rate" value="4"/>-->
<!--          <label for="star42" title="text"></label>-->
<!--          <input type="radio" id="star32" name="rate" value="3"/>-->
<!--          <label for="star32" title="text"></label>-->
<!--          <input type="radio" id="star22" name="rate" value="2"/>-->
<!--          <label for="star22" title="text"></label>-->
<!--          <input type="radio" id="star12" name="rate" value="1"/>-->
<!--          <label for="star12" title="text"></label> -->
<!--    </div>-->
<!--        -->
<!--        -->
<!--    <ul class="information">-->
<!--        <li><strong>Event type : </strong>5 Ft </li>-->
<!--        <li><strong>Start : </strong>Olive green</li>-->
<!--        <li><strong>Average rating : </strong>balls and bells</li>-->
<!--    </ul>    -->
<!--&lt;!&ndash;    <p class="information">" Let's spread the joy , here is Christmas , the most awaited day of the year.Christmas Tree is what one need the most. Here is the correct tree which will enhance your Christmas.</p>&ndash;&gt;-->

<!--        -->
<!--    <div class="control">-->
<!--    -->
<!--    <button class="btn">-->
<!--        <span class="price">$250</span>-->
<!--        <span class="shopping-cart"><i class="fa fa-shopping-cart" aria-hidden="true"></i></span>-->
<!--        <span class="buy">Get now</span>-->
<!--    </button>-->
<!--    -->
<!--   -->
<!--    -->
<!--</div>-->


<!--       <h5 class="address"><i class="fa fa-map-marker location-pin" aria-hidden="true"></i>XIII Vojvodjanske Brigade 106, Zrenjanin-->
<!--       </h5>      -->
<!--</div>-->
<!--    -->
<!--<div class="product-image">-->
<!--    -->
<!--    <img src="https://citypal.me/wp-content/uploads/2016/04/01-e1462017909580.jpg" alt="">-->
<!--    -->

<!--<div class="info">-->
<!--    -->
<!--</div>-->
<!--</div>-->

<!--</div>-->
        </div>
        
     
    </div>
    </div>
    
    `

})