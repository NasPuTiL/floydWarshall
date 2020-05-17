<template>
  <VModal :isActive="isActive" @clickModal="cancel">
    <transition name="item">
      <div class="form" v-if="isActive">
        <VInput v-model="newNode.content.text" placeholder="name" /><br />
        <VButton @click="ok">OK</VButton>
        <VButton class="danger" @click="cancel">Cancel</VButton>
      </div>
    </transition>
  </VModal>
</template>
<script>
export default {
  props: {
    isActive: Boolean,
    node: {
      type: Object,
      default() {
        return {
          id: "",
          shape: "ellipse",
          width: 60,
          height: 60,
          stroke: "",
          strokeWeight: 0,
          content: {
            text: "none",
            url: "",
            color: "#fab1a0"
          }
        };
      }
    }
  },
  watch: {
    node() {
      this.newWidth = parseInt(this.node.width);
      this.newHeight = parseInt(this.node.Height);
    }
  },
  data() {
    return {
      newNode: this.node
    };
  },
  methods: {
    ok() {
      this.newNode.shape = 'ellipse';
      this.newNode.width = 60;
      this.newNode.height = 60;
      this.newNode.content.color = '#fab1a0';
      this.$emit("ok", this.newNode);
    },
    cancel() {
      this.$emit("cancel");
    }
  }
};
</script>
<style lang="scss" scoped>
input {
  width: 95%;
  margin-bottom: 5px;
}
select {
  margin-bottom: 5px;
}
.item-enter-active {
  transition: all 0.8s ease;
}
.item-leave-active {
  transition: all 0.3s ease;
}
.item-enter,
.item-leave-to {
  opacity: 0;
}
</style>