<template>
  <v-app>
  <v-toolbar dense color="deep-orange lighten-3"  >
    <v-toolbar-title><v-btn text to="/"> <v-icon>face</v-icon> Chost</v-btn></v-toolbar-title>
    <v-btn to="/fileUploading" color="primary" text>Upload file</v-btn>
    <v-btn to="/marketplace" color="primary" text>Marketplace</v-btn>
    <v-spacer></v-spacer>
      <v-btn  v-if="auth" to="/home" color="primary">{{ user.login }}</v-btn>
      <v-btn  v-if="auth" @click="logout" color="error">Logout</v-btn>
      <v-btn  v-else to="/auth" prepend-icon="contact_mail" color="primary">Auth</v-btn>

  </v-toolbar>
    <v-content>
    <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>

import {eventBus} from "./main";

export default {

  name: 'App',
  data: function () {
    return {
      token: '',
      auth: false,
      user: {}
    }
  },
  methods: {
    logout: function () {
      localStorage.removeItem("token")
      localStorage.removeItem("user")
      this.user = {};
      this.auth = false;

      this.$router.push("/auth")
    }
  },
  mounted: function () {
    eventBus.$on('lout',()=>{
      this.logout()
    });
    if (localStorage.getItem('token')!=null) {
      this.$http.get('http://localhost:9000/user/me', {
        params: {},
        headers: {'Authorization': 'Bearer ' +localStorage.getItem('token')}
      }).then(resp => (resp.json().then(req => (this.user = req, this.auth = true, localStorage.setItem('user', JSON.stringify(req))))),
      resp => (resp.json().then(req => (eventBus.$emit('lout',req.error)))))
    }

  }

};
</script>
