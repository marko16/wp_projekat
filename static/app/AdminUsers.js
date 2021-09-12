Vue.component("AdminUsers", {
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

        axios.get("/users")
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
        blockUser() {
            console.log(this.selectedItem)
            axios.post("/block?username=" + this.selectedItem.username + "&role=" + this.selectedItem.role)
                .then(response => {
                    if(response.data)
                        alert("You have successfully blocked this user")
                    else
                        alert("This user is already blocked")
                })
        },
        unblockUser() {
            console.log(this.selectedItem)
            axios.post("/unblock?username=" + this.selectedItem.username + "&role=" + this.selectedItem.role)
                .then(response => {
                    if(response.data)
                        alert("You have successfully unblocked this user")
                    else
                        alert("This user is already unblocked")
                })
        },
        parsePoints(points) {
            if(points === -1) return "-"
            else return points
        },
        parseType(type) {
            if(type === undefined) return "-"
            else return String(type).charAt(0) + String(type).toString().slice(1).toLowerCase()
        },
        parseRole(role) {
            return String(role).charAt(0) + String(role).toString().slice(1).toLowerCase()
        },
        searchUsers() {
            if(this.searchQuery === null) return;
            axios.get("/searchUsers?search=" + this.searchQuery)
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
    <button class="btn btn-primary" type="button" @click="searchUsers">Search</button>
  </div>
    </div>
     <table class="table sortable table-striped table-hover"
        id="table"
     >
    <thead class="thead-dark">
      <tr class="clickable-row">
        <th
          :class="sortedClass('username')"
          @click="sortBy('username')"
        >
          Username
        </th>
        <th
          :class="sortedClass('fisrtName')"
          @click="sortBy('fisrtName')"
        >
          First Name
        </th>
        <th
          :class="sortedClass('lastName')"
          @click="sortBy('lastName')"
        >
          Last name
        </th>
        <th
          :class="sortedClass('role')"
          @click="sortBy('role')"
        >
          Role
        </th>
        <th
          :class="sortedClass('userType')"
          @click="sortBy('userType')"
        >
          User type
        </th>
        <th
          :class="sortedClass('points')"
          @click="sortBy('points')"
        >
          Points
        </th>
        <th
          :class="sortedClass('isBlocked')"
          @click="sortBy('isBlocked')"
        >
          Is blocked
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="item in sortedItems"
        :key="item.id"
        @click="selectRow(item)" :class="{'highlight': (item == this.selectedItem)}"
      >
        <td>{{ item.username }}</td>
        <td>{{ item.firstName }}</td>
        <td>{{ item.lastName }}</td>
        <td>{{ parseRole(item.role) }}</td>
        <td>{{ parseType(item.userType) }}</td>
        <td>{{ parsePoints(item.points) }}</td>
        <td>{{ item.isBlocked ? "Yes" : "No" }}</td>

      </tr>
    </tbody>
  </table>
  <div>
  <button class="btn btn-danger" @click="blockUser">Block user</button>
  <button class="btn btn-success" @click="unblockUser">Unblock user</button>
  </div>
  </div>
  
   `
});