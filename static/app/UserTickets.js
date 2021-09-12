Vue.component("UserTickets", {

    data() {
        return {
            sort: {
                key: '',
                isAsc: false,
                selectedItem: null
            },
            items: [],
            all: [],
            searchQuery: null,
            isReserved: null,
            selectedType: null,
        }
    },

    mounted() {
        $('#table2').on('click', 'tbody tr', function(event) {
            $(this).addClass('highlight').siblings().removeClass('highlight');
            console.log(this.selectedItem)
        });

        this.loadAll();
    },

    methods: {
        loadAll() {
            axios.get("/ticketsUser?customer=" + window.localStorage.getItem("username"))
                .then(response => {
                    console.log(response.data)
                    if(response.data.length !== 0) {
                        this.items = response.data;
                        this.all = response.data
                    }
                    else
                        alert("You have no tickets reserved")
                });
        },
        sortedClass (key) {
            return this.sort.key === key ? `sorted ${this.sort.isAsc ? 'asc' : 'desc' }` : '';
        },
        sortBy (key) {
            this.sort.isAsc = this.sort.key === key ? !this.sort.isAsc : false;
            this.sort.key = key;
        },
        selectRow(item){
            this.selectedItem = item;
        },
        cancelReservation() {
            if(this.selectedItem === null)
                alert("No event has been selected!")
            axios.post('/cancelReservation', {}, { params: { id: this.selectedItem }})
                .then(response => {
                    alert(response.data)
                    this.loadAll()
                })
        },
        parseDate(date) {
            return date.split("T").join(", ")
        },
        searchUsers() {
            if(this.searchQuery !== null && this.searchQuery.trim() !== "") {
            console.log("Sad")
            axios.get("/searchUsers?search=" + this.searchQuery)
                .then(response => {
                    if(response.data.length !== 0)
                        this.events = response.data
                    else
                        alert("No results!")
                })
            }
        },
        searchTickets() {
            if(this.searchQuery === null || this.searchQuery.trim() === "")
            {
                this.loadAll()
                return;
            }
            axios.get("/searchTickets?search=" + this.searchQuery)
                .then(response => {
                    if(response.data.length !== 0)
                        this.items = response.data
                    else
                        alert("No results!")
                })
        },
        getFilter() {
            const soldList = []
            const typeList = []
            let filteredArray = []
            let noType = false;
            let noSold = false;

            if(this.isReserved !== null) {
                for(const e in this.all) {
                    if(this.isReserved) {
                        if(this.all[e].isReserved) {
                            soldList.push(this.all[e])
                        }
                    }
                    if(!this.isReserved) {
                        if(!this.all[e].isReserved) {
                            soldList.push(this.all[e])
                        }
                    }
                }
                if(soldList.length === 0) noSold = true
            }

            if(this.selectedType !== null) {
                for(const e in this.all) {
                    console.log(this.all[e].type)
                    if(this.all[e].type.toLowerCase() === this.selectedType.toLowerCase()) {
                        typeList.push(this.all[e])
                    }
                }
                if(typeList.length === 0) noType = true;
            }

            if(typeList.length !== 0 && soldList.length !== 0) {
                filteredArray = typeList.filter(value => soldList.includes(value));
            } else if(noType || noSold) {
                this.items = this.all
                this.selectedType = "All"
                this.isReserved = false;
                this.searchQuery = ""
            }
                else {
                if(typeList.length !== 0) filteredArray = typeList
                else {
                    console.log(soldList)
                    filteredArray = soldList
                }
            }

            if(filteredArray.length === 0) {
                this.items = this.all
                alert("No events match this filter. All will be shown")
                this.selectedType = "All"
                this.isReserved = false;
                this.searchQuery = ""
            } else {
                this.items = filteredArray
            }
        }
    },

    computed: {
        sortedItems () {
            const list = this.items.slice();
            if (!!this.sort.key) {
                list.sort((a, b) => {
                    a = a[this.sort.key]
                    b = b[this.sort.key]

                    return (a === b ? 0 : a > b ? 1 : -1) * (this.sort.isAsc ? 1 : -1)
                });
            }
            return list;
        }
    },

    template: `
    <div class="container">
    <div class="input-group mb-3">
  <input type="text" class="form-control" placeholder="Search..." aria-label="" aria-describedby="basic-addon2" v-model="searchQuery">
  <div class="input-group-append">
    <button class="btn btn-primary" type="button" @click="searchTickets">Search</button>
  </div>
    </div>
    <div class="row">
  <div class="col-5">
  <select class="custom-select" @change="getFilter" v-model="selectedType">
    <option value="All">All types</option>
    <option value="Regular">Regular</option>
    <option value="Fanpit">Fan pit</option>
    <option value="VIP">VIP</option>
  </select></div>
  <div class="form-check form-switch col-5 mt-2 mb-5">
  <input class="form-check-input" type="checkbox" @change="getFilter" v-model="isReserved" id="flexSwitchCheckDefault">
  <label class="form-check-label" for="flexSwitchCheckDefault">Is reserved</label>
</div>
    </div>
     <table class="table table-striped table-hover"
        id="table2"
     >
    <thead class="thead-dark">
      <tr class="clickable-row">
        <th
          :class="sortedClass('eventName')"
          @click="sortBy('eventName')"
        >
          Event Name
        </th>
        <th
          :class="sortedClass('customerUsername')"
          @click="sortBy('customerUsername')"
        >
          Customer
        </th>
        <th
          :class="sortedClass('eventDate')"
          @click="sortBy('eventDate')"
        >
          Start time
        </th>
        <th
          :class="sortedClass('type')"
          @click="sortBy('type')"
        >
          Ticket Type
        </th>
        <th
          :class="sortedClass('price')"
          @click="sortBy('price')"
        >
          Regular price
        </th>
        <th
          :class="sortedClass('isReserved')"
          @click="sortBy('isReserved')"
        >
          Reserved
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="item in sortedItems"
        :key="item.id"
        @click="selectRow(item.id)" :class="{'highlight': (item.id == this.selectedItem)}"
      >
        <td>{{ item.eventName }}</td>
        <td>{{ item.customerUsername }}</td>
        <td>{{ parseDate(item.eventDate) }}</td>
        <td>{{ item.type }}</td>
        <td>{{ item.price }}</td>
        <td>{{ item.isReserved ? "RESERVED" : "DELETED" }}</td>

      </tr>
    </tbody>
  </table>
  <button class="btn btn-danger" @click="cancelReservation">Cancel Reservation</button>
  </div>
    `
});