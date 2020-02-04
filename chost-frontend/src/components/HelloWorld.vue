<template>
    <v-app>
        <v-layout align-start>
            <v-flex fill-height>
                <v-card v-for="file in files" :key="file">
                    <v-img
                            class="white--text align-end"
                            height="200px"
                            :src="'http://localhost:9000/img/'+file.iconPath"
                    >
                        <v-card-title>{{ file.name }}</v-card-title>
                    </v-img>
                    <v-card-text>{{ file.description}}</v-card-text>


                </v-card>

            </v-flex>
        </v-layout>

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
