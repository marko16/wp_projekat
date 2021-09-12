Vue.component("AdminTickets", {

    data() {
        return {
            sort: {
                key: '',
                isAsc: false,
                selectedItem: -1
            },
            items: [],
            searchQuery: null
        }
    },

    mounted() {
        $('#table').on('click', 'tbody tr', function(event) {
            $(this).addClass('highlight').siblings().removeClass('highlight');
        });

        axios.get("/ticketsAdmin?admin=" + window.localStorage.getItem("username"))
            .then(response => {
                console.log(response.data)
                if(response.data.length !== 0)
                    this.items = response.data;
                else
                    alert("There is no tickets in the database!")
            });
    },

    methods: {
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
        deleteTicket() {
            axios.post("/deleteTicket?ticket=" + this.selectedItem)
                .then(response => {
                    if(response.data) alert("You have deleted this ticket!")
                    else alert("Ticket already deleted")
                })
        },
        parseDate(date) {
            return date.split("T").join(", ")
        },
        searchTickets() {
            axios.get("/searchTickets?search=" + this.searchQuery)
                .then(response => {
                    console.log(response.data)
                    if(response.data.length !== 0)
                        this.items = response.data
                    else
                        alert("No results!")
                })
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
     <table class="table table-striped table-hover"
        id="table"
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
          Event Type
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
  <button class="btn btn-danger" @click="deleteTicket">Delete ticket</button>
  </div>
  
   `
});