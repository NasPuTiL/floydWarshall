<template>
  <div id="app">
    <h2>Tomasz Kowalski. Algorytm Floyd-Warshall</h2>
    <br />
    <DiagramEditor v-model="graph"></DiagramEditor>
  </div>
</template>

<script>
import data from "./data.json";
import { DiagramEditor } from "../index.js";
export default {
  name: "app",
  components: {
    DiagramEditor
  },
  data() {
    return {
      demo: "default",
      graph: {}
    };
  },
  mounted() {
    this.graph = data;
  },
  methods: {
    refresh() {
      this.graph = data;
    }
  }
};
</script>

<style lang="scss">
body {
  #app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    // text-align: center;
    color: #2c3e50;
    text-align: center
  }
  .scrollXY {
    overflow-x: auto;
    overflow-y: auto;
  }
}
</style>
