Vue.component("EventListView", {


    data() {
        return {
            events: [],
            types: [],
            searchQuery: null,
            selectedType: null,
            isSold: null,
            all: []
        }
    },

    // created() {
    //     this.events = [1,2,3,4,5]
    //     this.events.push({eventType:"event1"})
    // },

    mounted() {
        this.loadAll();
    },

    methods: {
        loadAll() {
            axios.get("/events").then(
                response => {
                    this.events = response.data;
                    this.all = response.data
                    for(const e in response.data) {
                        console.log(e)
                        if(this.types.indexOf(this.events[e].eventType) === -1) {
                            this.types.push(this.events[e].eventType)
                        }
                    }
                })
        },
        onSeeMore(event) {
            this.chosenEvent = event;
        },
        searchEvents() {
            if(this.searchQuery === null || this.searchQuery.trim() === "") {
                this.loadAll()
                return;
            }
            axios.get("/searchEvents?search=" + this.searchQuery)
                .then(response => {
                    if(response.data.length !== 0) {
                        this.events = response.data
                        this.all = response.data
                    }
                    else
                        alert("No results!")
                })
        },
        getFilter() {
            const soldList = []
            const typeList = []
            let filteredArray = []
            let noSold = false;
            let noType = false;

            if(this.isSold !== null) {
                for(const e in this.all) {
                    if(this.isSold) {
                        if(this.all[e].availableTickets === 0) {
                            soldList.push(this.all[e])
                        }
                    }
                    if(!this.isSold) {

                        if(this.all[e].availableTickets !== 0) {
                            soldList.push(this.all[e])
                        }
                    }
                }
                if(soldList.length === 0) noSold = true;
            }
            if(this.selectedType !== null) {
                for(const e in this.all) {

                    if(this.all[e].eventType === this.selectedType) {
                        console.log("sss")
                        typeList.push(this.all[e])
                    }
                }
                if(typeList.length === 0) noType = true;
            }

            if(typeList.length !== 0 && soldList.length !== 0) {
                filteredArray = typeList.filter(value => soldList.includes(value));
                console.log("usai")
            } else if(noSold || noType) {
                this.events = this.all
                this.loadAll()
                this.selectedType = "All"
                this.isSold = false;
                this.searchQuery = ""
            } else{
                if(typeList.length !== 0) filteredArray = typeList
                else filteredArray = soldList
            }

            if(filteredArray.length === 0) {
                this.events = this.all
                alert("No events match this filter. All will be shown")
                this.loadAll()
                this.selectedType = "All"
                this.isSold = false;
                this.searchQuery = ""
            } else
            this.events = filteredArray
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
  <div class="row">
  <div class="col-5">
  <select class="custom-select" @change="getFilter" v-model="selectedType">
    <option value="All">All types</option>
    <option v-for="type in types" :key="type" :value="type">{{type}}</option>
  </select></div>
  <div class="form-check form-switch col-5 mt-2">
  <input class="form-check-input" type="checkbox" @change="getFilter" v-model="isSold" id="flexSwitchCheckDefault">
  <label class="form-check-label" for="flexSwitchCheckDefault">Is sold</label>
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