<template>
    <v-app>
        <v-card
                class="mx-auto"
                width="700"
        >
            <v-card-text>
                <v-card-title primary-title class="justify-center" style="color: red"><div><h2>{{ user.login }}</h2><v-img :src="user.imageURL"/></div></v-card-title>

                <p>Email: {{ user.email}}</p>
                <p>Name: {{ user.name}}</p>
                <v-card-title primary-title class="justify-center" style="color: red"><div><h2>Your posts:</h2><v-img :src="user.imageURL"/></div></v-card-title>
                <v-data-table :headers="headers" :items="user.ownedFiles">
                    <template v-slot:item="row">
                        <tr>
                            <td>{{row.item.name}}</td>
                            <td>
                                <v-btn color="error" @click="onButtonClick(row.item)">
                                    Delete
                                </v-btn>
                            </td>
                        </tr>
                    </template>
                </v-data-table>
            </v-card-text>
            <v-card-actions>

            </v-card-actions>
        </v-card>
    </v-app>
</template>

<script>
    import {eventBus} from "../main";

    export default {
        name: "HomePage",
        mounted: function () {
            if (localStorage.getItem('user') != null) {
                this.user = JSON.parse(localStorage.getItem('user'))
                if (localStorage.getItem('token') != null) {
                    this.$http.get('http://localhost:9000/user/about',
                        {
                            params: {},
                            headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                        }).then(req => (req.json().then(data => (this.user = data))), req => (req.json().then(data => eventBus.$emit('lout', data.message))))
                } else {
                    eventBus.$emit('lout', 'Unauthorized')
                }
            }
        },
        data: function () {
            return {
                headers: [
                    {
                        text: 'Name',
                        align: 'left',
                        sortable: false,
                        value: 'name',
                    }
                ],
                user: {}
            }
        },
        methods: {
            onButtonClick: function (item) {
                if (localStorage.getItem('token') != null) {
                    var id = this.user.ownedFiles.indexOf(item)
                    if (id > -1) {
                        this.user.ownedFiles.splice(id, 1);
                    }
                    this.$http.post('http://localhost:9000/file/remove', item.id,
                        {
                            params: {},
                            headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                        }).then(req => req, req => (req.json().then(data => eventBus.$emit('lout', data.message))))
                } else {
                    eventBus.$emit('lout', 'Unauthorized')

                }
            }
        }
    }
</script>

<style scoped>

</style>