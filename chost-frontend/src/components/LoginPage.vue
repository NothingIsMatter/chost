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
                            cols="12"
                            sm="8"
                            md="4"
                    >
                        <v-card class="elevation-12">
                            <v-toolbar
                                    color="brown darken-4"
                                    dark
                                    flat
                            >
                                <v-toolbar-title>Login</v-toolbar-title>
                                <v-spacer></v-spacer>

                                  <v-btn color="red accent-1" small to="/register">Register</v-btn>

                            </v-toolbar>
                            <v-card-text>
                                <v-alert v-if="errorMsg" type="error">
                                   {{ errorMsg}}
                                </v-alert>
                                <v-form>
                                    <v-text-field
                                            label="Login"
                                            name="login"
                                            prepend-icon="person"
                                            type="text"
                                            v-model="login"
                                    />
                                    <v-text-field
                                            v-model="password"
                                            label="Password"
                                            name="password"
                                            prepend-icon="lock"
                                            type="password"
                                    />
                                </v-form>
                                <v-btn small color="red draken-2" @click="redirectToOAuth2"><v-icon>email</v-icon> Login via Google</v-btn>

                            </v-card-text>
                            <v-card-actions>
                                <v-spacer />
                                <v-btn color="primary" @click="sendLogin">Login</v-btn>

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
    import {eventBus} from "../main";

    export default {
        props: {
            source: String,
        },
        data(){
            return{
                errorMsg: '',
                login:'',
                token:'',
                password:'',
                img : require('./assets/google_signin.png')
            }
        },
        methods:{
            sendLogin:function(){
                this.$http.post("http://localhost:9000/auth/login","{\"login\":\""+this.login+"\",\"password\":\""+this.password+"\"}").then(resp =>  (resp.text().then(resz => (window.location.href="http://localhost:8080/oauth2/redirect?token="+resz,this.$router.push("/")))), resp => (resp,this.errorMsg = 'Invalid credentials'))

            },
            redirectToOAuth2: function () {
                window.location.href = "http://localhost:9000/oauth2/authorize/google?redirect_uri=http://localhost:8080/oauth2/redirect"
            }
        },
        mounted() {
            eventBus.$on('lout',(msg)=>{
                this.errorMsg = msg ||'Unknown error'
            })
        }
    }
</script>