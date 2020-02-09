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

                    </v-card>
                </v-col>
            </v-row>
        </v-container>

    </v-app>
</template>

<script>
import {eventBus} from "../main";

export default {
    name: 'HelloWorld',
    mounted: function () {
        if (localStorage.getItem('token')!=null){
            this.$http.get('http://localhost:9000/user/about',
                {
                    params: {},
                    headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}
                }).then(req => (req.json().then(data => (this.files = data.files))), req => (req.json().then(data => eventBus.$emit('lout',data.error))))
        }
    },
    data:function () {
 return{
     files: null
 }
    }

};
</script>
