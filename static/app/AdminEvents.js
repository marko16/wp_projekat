Vue.component("AdminEvents", {
    data () {
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

        this.loadAll();
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

    methods: {
        sortedClass (key) {
            return this.sort.key === key ? `sorted ${this.sort.isAsc ? 'asc' : 'desc' }` : '';
        },
        loadAll() {
            axios.get('/eventAdmin')
                .then(response => {
                    console.log(response.data)
                    if(response.data.length !== 0) {
                        this.items = response.data;
                    } else
                        alert("You do not have any events")
                })
        },
        sortBy (key) {
            this.sort.isAsc = this.sort.key === key ? !this.sort.isAsc : false;
            this.sort.key = key;
        },
        selectRow(item){
            console.log(item)
            this.selectedItem = item;
        },
        deleteEvent() {
            axios.post("/deleteEvent?event=" + this.selectedItem)
            .then(response => {
                if(response.data) {
                    alert("You have successfully deleted this event")
                    this.loadAll()
                }
                else alert("This event is already deleted")
            });
        },
        activateEvent() {
            axios.post("/activateEvent?event=" + this.selectedItem)
                .then(response => {
                        if(response.data) {
                            this.loadAll()
                            alert("You have successfully activated this event")
                        }
                        else alert("This event is already active")})
        },
        parseDate(date) {
            return date.split("T").join(", ")
        },
    },

    template: `
<div class="container">
     <table class="table table-striped table-hover"
        id="table"
     >
    <thead class="thead-dark">
      <tr class="clickable-row">
        <th
          :class="sortedClass('name')"
          @click="sortBy('name')"
        >
          Name
        </th>
        <th
          :class="sortedClass('eventType')"
          @click="sortBy('eventType')"
        >
          Event type
        </th>
        <th
          :class="sortedClass('startTime')"
          @click="sortBy('startTime')"
        >
          Start time
        </th>
        <th
          :class="sortedClass('location')"
          @click="sortBy('location')"
        >
          Location
        </th>
        <th
          :class="sortedClass('regularPrice')"
          @click="sortBy('regularPrice')"
        >
          Regular price
        </th>
        <th
          :class="sortedClass('averageRating')"
          @click="sortBy('averageRating')"
        >
          Average rating
        </th>
        <th
          :class="sortedClass('deleted')"
          @click="sortBy('deleted')"
        >
          Is deleted
        </th>
        <th
          :class="sortedClass('isActive')"
          @click="sortBy('isActive')"
        >
          Is Active
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="item in sortedItems"
        :key="item.id"
        @click="selectRow(item.id)" :class="{'highlight': (item.id == this.selectedItem)}"
      >
        <td>{{ item.name }}</td>
        <td>{{ item.eventType }}</td>
        <td>{{ parseDate(item.startTime) }}</td>
        <td>{{ item.location.street }} {{ item.location.number }}, {{ item.location.city }}</td>
        <td>{{ item.regularPrice }}</td>
        <td>4</td>
        <td>{{ item.deleted ? "Yes" : "No" }}</td>
        <td>{{ item.isActive ? "Yes" : "No" }}</td>

      </tr>
    </tbody>
  </table>
  <div>
    <button class="btn btn-danger" @click="deleteEvent">Delete event</button>
    <button class="btn btn-primary" @click="activateEvent">Activate event</button>
  </div>
  </div>
  
    `

});