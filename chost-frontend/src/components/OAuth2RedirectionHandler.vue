<template>
    <h1>Redirection..</h1>
</template>

<script>

    export default {
        name: "OAuth2RedirectionHandler",
        mounted() {
            const urlParams = new URLSearchParams(window.location.search);
            localStorage.setItem("token",urlParams.get("token"))
            this.$router.push('/')
        }
    }
</script>

<style scoped>

</style>