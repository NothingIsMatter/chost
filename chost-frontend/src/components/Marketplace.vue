<template>
    <v-app class="teal lighten-5">
        <v-container fluid >
                <v-alert v-if="errMsg" type="error" >{{errMsg}}</v-alert>
            <v-row align="center" justify="center">
            <v-col v-for="file in files" :key="file" cols="5"  >
                <v-card >
                    <v-img
                            class="white--text align-end"
                            :aspect-ratio="16/9"
                            max-height="200px"
                            :src="'http://localhost:9000/img/'+file.iconPath"
                    >
                        <v-card-title>{{ file.name }}</v-card-title>
                    </v-img>
                    <v-card-text>{{ file.description}}</v-card-text>
                    <v-card-actions>
                        <h1>Price: {{file.price}}</h1>
                        <v-btn class="primary" @click="buyFile(file)" >
                            Buy!
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
            </v-row>
        </v-container>

    </v-app>
</template>

<script>
    import {eventBus} from "../main";

    export default {
        data:function(){
            return{
                files:[],
                errMsg:null
            }
        },
        methods:{
            buyFile:function (file) {
                if (localStorage.getItem('token') != null) {

                    this.$http.post('http://localhost:9000/file/buy',file.id,
                        {
                            params: {},
                            headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                        }).then(req => (req,this.$router.push("/"),eventBus.$emit("balance",file.price),(this.removeElement(file))), req => (req.json().then(data =>( this.errMsg=data.message))))
                } else {
                    eventBus.$emit('lout', 'Unauthorized')
                }
            },
            removeElement:function(file){
                if (this.files.length!=1) {
                    var id = this.files.indexOf(file)
                    if (id > -1) this.files.splice(id, 1);
                } else {
                    this.files = null;
                }
            }
        },
        mounted: function () {
            if (localStorage.getItem('token') != null) {
                this.$http.get('http://localhost:9000/file/marketplace',
                    {
                        params: {},
                        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                    }).then(req => (req.json().then(data => (this.files = data))), req => (req.json().then(data => eventBus.$emit('lout', data.message))))
            } else {
                eventBus.$emit('lout', 'Unauthorized')
            }
        },
        name: "Marketplace"
    }
</script>

<style scoped>

</style>