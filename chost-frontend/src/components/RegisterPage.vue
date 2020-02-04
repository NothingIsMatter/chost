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
                                <v-toolbar-title>Register</v-toolbar-title>
                                <v-spacer></v-spacer>
                                <v-btn color="red accent-1" small to="/auth" >Login</v-btn>
                            </v-toolbar>
                            <v-card-text>
                                <v-alert v-if="error" type="error">
                                    {{ error }}
                                </v-alert>
                                <v-form v-model="valid">
                                    <v-text-field
                                            label="Login"
                                            name="Login"
                                            v-model="login"
                                            :rules="loginRules"
                                            prepend-icon="person"
                                            type="text"
                                    />

                                    <v-text-field
                                            label="Password"
                                            name="password"
                                            v-model="password"
                                            :rules="passwordRules"
                                            validate-on-blur
                                            prepend-icon="lock"
                                            type="password"
                                    />
                                    <v-text-field
                                            label="Repeat password"
                                            name="password2"
                                            v-model="password2"
                                            :rules="passwordRules"
                                            v-on:blur="validate"
                                            validate-on-blur
                                            prepend-icon="lock"
                                            type="password"
                                    />
                                    <span style="color:red" v-if="!passwordDontMatch">Passwords dont match!</span>
                                <v-text-field
                                        label="Name"
                                        name="name"
                                        :rules="nameRules"
                                        v-model="name"
                                        prepend-icon="account_box"
                                        validate-on-blur
                                        type="text"
                                />

                                <v-text-field
                                        label="Email"
                                        name="password"
                                        v-model="email"
                                        :rules="emailRules"
                                        validate-on-blur
                                        prepend-icon="contact_mail"
                                        type="text"
                                />
                                </v-form>

                            </v-card-text>
                            <v-card-actions>
                                <v-spacer />
                                <v-btn color="primary" @click="submitForm" :disabled="!valid">Register</v-btn>
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
    export default {
        name: "RegisterPage",
        data:function(){
            return{
                error:'',
                valid:false,
                login:'',
                loginRules:[
                    (v) => !!v || 'Login is required',
                ],
                password:'',
                passwordRules: [
                    (v) => v.length>=6 || 'Password should contain at least 6 symbols'
                ],
                password2:'',
                passwordDontMatch:false,
                email:'',
                emailRules:[
                    (v) => !!v || 'E-mail is required',
                    (v) => /^\w+([/.-]?\w+)*@\w+([/.-]?\w+)*(\.\w{2,3})+$/.test(v) || 'E-mail must be valid'
        ],
                name:'',
                nameRules:[
                    (v) => !!v || 'Name is required'
                ]
            }
        },
        methods:{
            submitForm:function () {
                localStorage.removeItem('ACCESS_TOKEN')
                this.$http.post("http://localhost:9000/auth/signup","{\"login\":\""+this.login+"\",\"password\":\""+this.password+"\",\"email\":\""+this.email+"\",\"name\":\""+this.name+"\"}").then(response => (response, this.$router.push('/auth')), response => (response.json().then(data => (this.error = data.message))))

            },
            validate:function () {
                if (this.password===this.password2){
                        this.passwordDontMatch=true
                } else{
                    this.passwordDontMatch = false
                }
            }
        }

    }
</script>

<style scoped>

</style>