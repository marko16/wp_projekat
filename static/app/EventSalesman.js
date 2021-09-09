Vue.component("EventSalesman", {
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

        axios.get('/eventSalesman?salesman=' + window.localStorage.getItem('username'))
            .then(response => {
                if(response.data.length !== 0) {
                    this.items = response.data;
                } else
                    alert("You do not have any events")
            })
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
        sortBy (key) {
            this.sort.isAsc = this.sort.key === key ? !this.sort.isAsc : false;
            this.sort.key = key;
        },
        selectRow(item){
            console.log(item)
            this.selectedItem = item;
        },
        editEvent() {
            if(this.selectedItem !== -1)
            this.$router.push({path: "/editEvent/" + this.selectedItem, name: "editEvent", params: {id: this.selectedItem}})
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
          :class="sortedClass('name')"
          @click="sortBy('name')"
        >
          Name
        </th>
        <th
          :class="sortedClass('eventType')"
          @click="sortBy('eventType')"
        >
          Type
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
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="item in sortedItems"
        :key="item.id"
        @click="selectRow(item.id)" :class="{'highlight': (item.id == selectedItem)}"
      >
        <td>{{ item.name }}</td>
        <td>{{ item.eventType }}</td>
        <td>{{ item.startTime }}</td>
        <td>{{ item.location.street }} {{ item.location.number }}, {{ item.location.city }}</td>
        <td>{{ item.regularPrice }}</td>

      </tr>
    </tbody>
  </table>
  <button class="btn btn-primary" @click="editEvent">Edit event</button>
  </div>
  
    `

});