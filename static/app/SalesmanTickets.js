Vue.component("SalesmanTickets", {

    data() {
        return {
            sort: {
                key: '',
                isAsc: false,
                selectedItem: -1
            },
            items: []
        }
    },

    mounted() {
        $('#table').on('click', 'tbody tr', function(event) {
            $(this).addClass('highlight').siblings().removeClass('highlight');
        });

        axios.get("/tickets?salesman=" + window.localStorage.getItem("username"))
            .then(response => {
                console.log(response.data)
                if(response.data.length !== 0)
                    this.items = response.data;
                else
                    alert("You have no tickets reserved for your events")
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
        @click="selectRow(item.id)" :class="{'highlight': (item.id == selectedItem)}"
      >
        <td>{{ item.eventName }}</td>
        <td>{{ item.customerUsername }}</td>
        <td>{{ item.eventDate }}</td>
        <td>{{ item.type }}</td>
        <td>{{ item.price }}</td>
        <td>{{ item.isReserved ? "RESERVED" : "DELETED" }}</td>

      </tr>
    </tbody>
  </table>
  </div>
  
   `
});