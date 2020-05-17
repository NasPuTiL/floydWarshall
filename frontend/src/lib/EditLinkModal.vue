<template>
  <VModal :isActive="isActive" @clickModal="cancel">
    <transition name="item">
      <div class="form" v-if="isActive">
        <h2>Edit link</h2>
        <label>Value:</label>
        <VInput v-model="newLink.value" placeholder="value" /><br />
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
    link: {
      type: Object,
      default() {
        return {
          id: "0", 
          value: "0",    
          content: {
            value: '0',                 
            color: "#000000",
            shape: "straight",
            pattern: "solid",
            arrow: "dest"
          }
        };
      }
    }
  },
  computed: {
    newLink: {
      get() {
        return this.link.content;
      }
    }
  },
  methods: {
    ok() {
      console.log('XD: ' + this.link.content.value),    
      this.$emit("ok", {
      id: this.link.id,
        content: {
          color: '#000000',
          shape: 'straight',
          pattern: 'solid',
          arrow: 'dest',
          value: this.link.content.value,
        }      
      });
    },
    cancel() {
      this.$emit("cancel");
    }
  }
};
</script>
<style lang="scss" scoped>
input {
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
select {
  margin-bottom: 5px;
}
</style>
