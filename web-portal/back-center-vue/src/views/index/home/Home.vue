<template>
  <h3>
    {{ message }}
  </h3>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
@Component
export default class Home extends Vue {
  message = 'welcome'
}
</script>

<style></style>
