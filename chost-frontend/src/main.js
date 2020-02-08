import Vue from 'vue'
import App from './App.vue'
import 'vuetify/dist/vuetify.min.css'
import Vuetify from "vuetify";
import vuetify from './plugins/vuetify';
import VueRouter from 'vue-router'
import OAuth2RedirectionHandler from "./components/OAuth2RedirectionHandler";
import LoginPage from "./components/LoginPage";
import HelloWorld from "./components/HelloWorld";
import RegisterPage from "./components/RegisterPage";
import VueResource from 'vue-resource'
import HomePage from "./components/HomePage";
import FileLoadingPage from "./components/FileLoadingPage";
import Marketplace from "./components/Marketplace";
Vue.use(VueRouter)

const routes = [
  {path: '/register', component: RegisterPage},
  {path: '/marketplace', component: Marketplace},
  {path: '*', component: HelloWorld},
  {path: '/fileUploading', component: FileLoadingPage},
  {path: '/auth', component: LoginPage},
  {path: '/home', component: HomePage},
  {path: '/oauth2/redirect', component: OAuth2RedirectionHandler}
]
// eslint-disable-next-line no-unused-vars
export const eventBus = new Vue();
const router = new VueRouter({
  mode:'history',
  routes
})
Vue.config.productionTip = false
Vue.use(Vuetify)
Vue.use(VueResource)


new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
