<template>
    <v-app id="inspire">
        <v-img src="https://i.picsum.photos/id/715/1920/900.jpg">
            <v-content>
                <v-container
                        class="fill-height"
                        fluid

                >
                    <v-row
                            align="center"
                            justify="center"
                    >
                        <v-col
                                cols="6"
                                sm="8"
                                md="4"
                        >
                            <v-card class="elevation-12">
                                <v-toolbar
                                        color="primary"
                                >
                                    <v-toolbar-title>Uploading a file</v-toolbar-title>
                                </v-toolbar>
                                <v-card-text>

                                    <v-alert v-if="errorMsg" type="error">
                                        {{ errorMsg}}
                                    </v-alert>
                                    <v-form v-model="valid">
                                        <v-text-field
                                                :rules="nameRules"
                                                label="Name"
                                                name="login"
                                                prepend-icon="person"
                                                type="text"
                                                v-model="name"
                                        />
                                      <v-textarea label="Description" dense v-model="description"></v-textarea>
                                      <v-text-field label="Price" dense v-model="price"></v-text-field>
                                        <v-file-input  label="Choose files" v-model="img" @change="change" @click:clear="clearImg"></v-file-input>
                                        <span>Preview:</span>
                                        <v-img v-if="url" :src="url" height="300" width="1000" />
                                        <v-spacer></v-spacer>
                                        <v-file-input
                                                v-model="files"
                                                color="deep-purple accent-4"
                                                counter
                                                label="File input"
                                                multiple
                                                placeholder="Select your files"
                                                prepend-icon="mdi-paperclip"
                                                outlined
                                                :show-size="1000"
                                        >
                                            <template v-slot:selection="{ index, text }">
                                                <v-chip
                                                        v-if="index < 2"
                                                        color="deep-purple accent-4"
                                                        dark
                                                        label
                                                        small
                                                >
                                                    {{ text }}
                                                </v-chip>

                                                <span
                                                        v-else-if="index === 2"
                                                        class="overline grey--text text--darken-3 mx-2"
                                                >
        +{{ files.length - 2 }} File(s)
      </span>
                                            </template>
                                        </v-file-input>
                                        <v-checkbox v-model="whiteListed" label="WhiteListed?"></v-checkbox>
                                        <v-combobox
                                                v-if="whiteListed"
                                                v-model="select"
                                                :items="items"
                                                label="Enter login of user that will be able to buy your post(Press ENTER to apply)"
                                                multiple
                                        ></v-combobox>
                                        <v-combobox
                                                v-if="whiteListed"
                                                v-model="select"
                                                :items="items"
                                                readonly
                                                chips
                                                label="Users:"
                                                multiple
                                        ></v-combobox>
                                    </v-form>

                                </v-card-text>
                                <v-card-actions>

                                    <v-btn color="primary" @click="sendForm" :disabled="!valid">Upload</v-btn>

                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </v-content>
        </v-img>
    </v-app>
</template>

<script>
    /* eslint-disable no-console */
    import {eventBus} from "../main";

    export default {
        name: "FileLoadingPage",
        data : function () {
            return{
                items:[],
                select:[],
                whiteListed:'',
                name:'',
                description:'',
                nameRules:[
                    (v) => !!v || 'Login is required',
                ],
                files:null,
                img: null,
                url: null,
                valid:false,
                price:0
            }
        },
        methods:{
            change:function(){
                this.url = URL.createObjectURL(this.img)

            },
            onFileChange:function(e){
                const url = e.target.files[0]
                this.url = URL.createObjectURL(url)
                alert(this.url)
            },
                setImg: function () {
                    alert(this.img)
            },
            clearImg:function () {
                this.url = null
            },
            sendForm:function () {
                let formData = new FormData()

                if(this.files){
                    formData.append("title", this.img)
                    for (let file of this.files) {
                        formData.append("files", file, file.name);
                    }
                    formData.append("name",this.name)
                    formData.append("desc",this.description)
                    formData.append("price",this.price)
                    if (this.select.length>0){
                        formData.append("whitelist",this.select)
                    }
                    this.$http.post('http://localhost:9000/file/upload',formData,
                        {
                            headers: {
                                'Content-Type': 'multipart/form-data',
                                'Authorization': 'Bearer '+localStorage.getItem('token')
                            }
                        }).then( response => {
                        console.log('Success!')
                        console.log({response})
                    }, err=>{
                            err,
                          err.json().then(msg => (eventBus.$emit('lout',msg.error)))
                        this.$router.push({path:'/auth'})


                    }).catch(error => {
                        console.log(error)
                    })
                }
                else {
                    console.log('there are no files.')
                }
            }
        }
    }
</script>

<style scoped>

</style>