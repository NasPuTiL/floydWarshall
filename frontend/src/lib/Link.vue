<template>
  <g>
    <line
      :x1="calcSource().x"
      :y1="calcSource().y"
      :x2="calcDestination().x"
      :y2="calcDestination().y"
      :stroke="'#000000'"
      stroke-width="3"
      fill="none"
      :stroke-dasharray="definePattern(link.pattern)"
      :marker-start="
        link.arrow ===  ''
      "
      :marker-end="
        link.arrow === 'dest' || link.arrow === 'both' ? `url(#${link.id})` : ''
      "
    />

     <text
        :x="(calcSource().x + calcDestination().x) / 2 "
        :y="(calcSource().y + calcDestination().y) / 2 "
        fill="#ff0000"
        font-family="Meiryo UI, sans-serif"
        font-size="20"
        text-anchor="middle"
      >
       {{ this.link.value }} 
      </text>

    <marker
      :id="link.id"
      markerUnits="userSpaceOnUse"
      orient="auto-start-reverse"
      markerWidth="15"
      markerHeight="15"
      viewBox="0 0 10 10"
      refX="5"
      refY="5"
    >
      <polygon points="0,1.5 0,8.5 10,5 " :fill="link.color || '#ffeaa7'" />
    </marker>
    <g v-if="editable">
      <!-- <line
        :x1="calcSource().x"
        :y1="calcSource().y"
        :x2="point.x"
        :y2="point.y"
        stroke="lightgray"
      />
      <line
        :x1="point.x"
        :y1="point.y"
        :x2="calcDestination().x"
        :y2="calcDestination().y"
        stroke="lightgray"
      /> -->
      <ellipse
        :id="id"
        :cx="(calcSource().x + calcDestination().x) / 2 +30"
        :cy="(calcSource().y + calcDestination().y) / 2 - 9"
        rx="15"
        ry="15"
        fill="#228B22"
        stroke-width="2"
        class="grab"
        @click="select"
        @mousedown="mousedown"
        @touchstart="mousedown"
        @mousemove="mousemove"
        @touchmove="mousemove"
        @mouseup="mouseup"
        @touchend="mouseup"
      />
    </g>
    <g>
      <text
        :x="(calcSource().x + calcDestination().x) / 2  + 15"
        :y="(calcSource().y + calcDestination().y) / 2  - 30"      
        fill="#00b894"
        @click="edit"
        v-if="selected"
        class="button"
      >
        {{ labels.edit || "Edit" }}
      </text>
      <text
        :x="(calcSource().x + calcDestination().x) / 2  + 5"
        :y="(calcSource().y + calcDestination().y) / 2  + 30"    
        fill="#00b894"
        @click="remove"
        v-if="selected"
        class="button"
      >
        {{ labels.remove || "Remove" }}
      </text>
    </g>
  </g>
</template>
<script>
import mouseLocation from "../mouseLocation";
export default {
  mixins: [mouseLocation],
  props: {
    selected: Boolean,
    editable: Boolean,
    source: {
      id: Number,
      x: Number,
      y: Number
    },
    destination: {
      id: Number,
      x: Number,
      y: Number
    },
    link: {
      id: String,
      value: String,
      color: {
        type: String,
        default: "##000000"
      },
      shape: {
        type: String,
        default: "straight"
      },
      pattern: {
        type: String,
        default: "solid"
      },
      arrow: {
        type: String,
        default: "none"
      },
      point: {
        x: Number,
        y: Number
      }
    },
    labels: Object,
    scale: String,
    rWidth: Number,
    rHeight: Number
  },
  computed: {},
  data() {
    return {
      startPosition: null,
      cursorOffset: {
        x: 0,
        y: 0
      },
      id: this.link.id,
      point: this.link.point,
      value: this.link.value
    };
  },
  methods: {
    mousedown(e) {
      const [x, y] = this.getLocation(e);
      this.cursorOffset.x = x;
      this.cursorOffset.y = y;
      this.startPosition = { x: this.point.x, y: this.point.y };
      document.addEventListener("mousemove", this.mousemove);
      document.addEventListener("mouseup", this.mouseup);
    },
    mousemove(e) {
      if (this.startPosition) {
        e.preventDefault();
        const [x, y] = this.getLocation(e);
        this.point.x =
          this.startPosition.x +
          (x - this.cursorOffset.x) / this.rWidth / parseFloat(this.scale);
        this.point.y =
          this.startPosition.y +
          (y - this.cursorOffset.y) / this.rHeight / parseFloat(this.scale);
        this.$emit("updateLocation", {
          id: this.id,
          x: this.point.x,
          y: this.point.y
        });
      }
    },
    definePattern(p) {
      if (p === "solid") {
        return 0;
      } else if (p === "dash") {
        return 10;
      } else if (p === "dot") {
        return 3;
      } else {
        return 0;
      }
    },
    mouseup() {
      this.startPosition = null;
      document.removeEventListener("mousemove", this.mousemove);
      document.removeEventListener("mouseup", this.mouseup);
    },
    remove() {
      this.$emit("remove", this.id);
    },
    select() {
      this.$emit("select", this.id);
    },
    edit() {
      this.$emit("editLink", {
        id: this.link.id,
        // content: {
        //   color: "#ffeaa7",
        //   shape: "straight",
        //   pattern: "solid",
        //   arrow: "dest",
        //   value: this.link.value
        //}
        value: this.link.value
      });
    },
    calcSource() {
      let dx = this.point.x - this.source.point.x - this.source.width / 2;
      let dy = this.point.y - this.source.point.y - this.source.height / 2;
      if (dx === 0) {
        dx = 0.01;
      }
      if (Math.abs(dy / dx) <= 1) {
        if (dx >= 0) {
          return {
            x: this.source.point.x + this.source.width,
            y: this.source.point.y + this.source.height / 2
          };
        } else {
          return {
            x: this.source.point.x,
            y: this.source.point.y + this.source.height / 2
          };
        }
      } else {
        if (dy >= 0) {
          return {
            x: this.source.point.x + this.source.width / 2,
            y: this.source.point.y + this.source.height
          };
        } else {
          return {
            x: this.source.point.x + this.source.width / 2,
            y: this.source.point.y
          };
        }
      }
    },
    calcDestination() {
      let dx =
        this.point.x - this.destination.point.x - this.destination.width / 2;
      let dy =
        this.point.y - this.destination.point.y - this.destination.height / 2;
      if (dx === 0) {
        dx = 0.01;
      }
      if (Math.abs(dy / dx) <= 1) {
        if (dx >= 0) {
          return {
            x: this.destination.point.x + this.destination.width,
            y: this.destination.point.y + this.destination.height / 2
          };
        } else {
          return {
            x: this.destination.point.x,
            y: this.destination.point.y + this.destination.height / 2
          };
        }
      } else {
        if (dy >= 0) {
          return {
            x: this.destination.point.x + this.destination.width / 2,
            y: this.destination.point.y + this.destination.height
          };
        } else {
          return {
            x: this.destination.point.x + this.destination.width / 2,
            y: this.destination.point.y
          };
        }
      }
    }
  }
};
</script>
<style scoped></style>
