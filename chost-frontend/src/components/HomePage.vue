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
                <v-card-title primary-title class="justify-center" style="color: red"><div><h2>Deals:</h2><v-img :src="user.imageURL"/></div></v-card-title>
                <h2 v-for="file in user.files" :key="file">
                    {{ file.name }}
                    <p></p>
                </h2>
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
        mounted:function () {
            if (localStorage.getItem('user')!=null){
                 this.user = JSON.parse(localStorage.getItem('user'))
                if (localStorage.getItem('token')!=null){
                    this.$http.get('http://localhost:9000/user/about',
                        {
                            params: {},
                            headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                        }).then(req => (req.json().then(data => (this.user = data))), req => (req.json().then(data => eventBus.$emit('lout',data.error))))
                } else{
                    eventBus.$emit('lout','Unauthorized')
                }
            }
        },
        data:function () {
            return{
                user:{}
            }
        }
    }
</script>

<style scoped>

</style>