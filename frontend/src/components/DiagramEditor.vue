<template>
  <div id="editor">
    <VButton v-if="!editable" @click="editable = true">Edit</VButton>
    <span v-else>
      <VButton @click="endEdit">Static Vue</VButton>
    </span>
    <VButton @click="openModal">New Node</VButton>
    <VButton @click="openInputModal">Import/Export</VButton>
    <VButton @click="downloadSVG">Download SVG</VButton>
    <br />
            <select v-model="startNode">
                <option v-for=" node in graphData.nodes" :key="node.id">ID:{{node.id}}. Name: {{node.content.text}}</option>
            </select>
            <select v-model="endNode">
                <option v-for=" node in graphData.nodes" :key="node.id">ID:{{node.id}}. Name: {{node.content.text}}</option>
            </select>
    <VButton @click="sendJSONToBackend" :nodes="graphData.nodes" :links="graphData.nodes">Send JSON To backent to count distanse beetwen nodes</VButton>
    <EditNodeModal
      :node="{ content: {} }"
      :isActive="isModalActive"
      @ok="addNode"
      @cancel="cancel"
    />
    <EditNodeModal
      :node="tmpNode"
      :isActive="isEditModalActive"
      @ok="editNode"
      @cancel="cancel"
    />
    <EditLinkModal
      :link="tmpLink"
      :isActive="isEditLinkModalActive"
      @ok="editLink"
      @cancel="cancel"
    />
    <InputModal
      :text="json"
      :isActive="isInputModalActive"
      @ok="importData"
      @cancel="cancel"
    />
    <Diagram
      :width="graphData.width || 2000"
      :height="graphData.height || 1000"
      :fluid="settings.isFluid"
      :scale="settings.scale"
      :background="graphData.background || '#fafafa'"
      :showGrid="graphData.showGrid"
      :nodes="graphData.nodes"
      :links="graphData.links"
      :editable="editable"
      :labels="
        graphData.labels || {
          edit: 'Edit',
          remove: 'Remove',
          link: 'New Link',
          select: 'Select',
          cancel: 'Cancel',
          copy: 'Copy'
        }
      "
      @editNode="openNodeEdit"
      @editLink="openLinkEdit"
      @nodeChanged="nodeChanged"
      @linkChanged="linkChanged"
    >
    </Diagram> 
  </div>
</template>

<script>
import Diagram from "./Diagram";
import EditNodeModal from "../lib/EditNodeModal";
import EditLinkModal from "../lib/EditLinkModal";
import InputModal from "../lib/InputModal";

export default {
  name: "DiagramEditor",
  components: {
    Diagram,
    EditNodeModal,
    EditLinkModal,
    InputModal
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {
          width: 2000,
          height: 1000,
          background: "#fafafa",
          showGrid: false,
          labels: {
            edit: "Edit",
            remove: "Remove",
            link: "New Link",
            select: "Select",
            copy: "Copy"
          },
          nodes: [],
          links: []
        };
      }
    }
  },
  computed: {
    graphData: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("input", val);
      }
    }
  },
  data() {
    return {    
      startNode: "",
      endNode: "",
      sender: "",
      name: "",
      url: "",
      color: "",
      json: "",
      isModalActive: false,
      isEditModalActive: false,
      isEditLinkModalActive: false,
      isInputModalActive: false,
      editable: true,
      settings: {
        width: 1500,
        height: 1000,
        isFluid: false,
        scale: "1",
        showGrid: false
      },
      tmpNode: {
        id: "",
        shape: "rectangle",
        width: 0,
        height: 0,
        content: {
          text: "",
          url: "",
          color: ""
        }
      },
      tmpLink: {
        id: "",
        content: {
          value: "",
          color: "",
          pattern: "solid",
          arrow: "none"
        }
      },
    };
  },
  methods: {
    clearDiagram() {
      this.graphData.nodes = [];
      this.graphData.links = [];
    },
    generateID() {
      return (
        new Date().getTime().toString(16) +
        Math.floor(Math.random() * 1000000).toString(16)
      );
    },
    openModal() {
      this.isModalActive = true;
    },
    cancel() {
      this.isModalActive = false;
      this.isEditModalActive = false;
      this.isEditLinkModalActive = false;
      this.isInputModalActive = false;
      this.isAskClearDiagram = false;
      this.isSettingsModalActive = false;
    },
    addNode(item) {
      this.graphData.nodes.push({
        id: this.generateID(),
        content: {
          text: item.content.text,
          url: item.content.url,
          color: item.content.color
        },
        width: parseInt(item.width) || 150,
        height: parseInt(item.height) || 60,
        stroke: item.stroke,
        strokeWeight: item.strokeWeight,
        shape: item.shape,
        point: {
          x: 10,
          y: 100 + Math.random() * 100
        }
      });
      this.isModalActive = false;
    },
    openNodeEdit(item) {
      this.tmpNode.id = item.id;
      this.tmpNode.content.text = item.content.text;
      this.tmpNode.content.url = item.content.url;
      this.tmpNode.content.color = item.content.color;
      this.tmpNode.shape = item.shape;
      this.tmpNode.stroke = item.stroke;
      this.tmpNode.strokeWeight = item.strokeWeight;
      this.tmpNode.width = item.width;
      this.tmpNode.height = item.height;
      this.isModalActive = false;
      this.isEditModalActive = true;
    },
    editNode(item) {
      let tmp = this.graphData.nodes.find(x => x.id === item.id);
      tmp.content.text = item.content.text;
      tmp.content.url = item.content.url;
      tmp.content.color = item.content.color;
      tmp.shape = item.shape;
      tmp.stroke = item.stroke;
      tmp.strokeWeight = item.strokeWeight;
      tmp.width = parseInt(item.width);
      tmp.height = parseInt(item.height);
      this.isEditModalActive = false;
    },
    openLinkEdit(item) {
      this.tmpLink.id = item.id;
      this.tmpLink.content = Object.assign({}, item.content);
      this.isEditLinkModalActive = true;
    },
    editLink(item) {
      let tmp = this.graphData.links.find(x => x.id === item.id);
      tmp.color = item.content.color;
      tmp.shape = item.content.shape;
      tmp.pattern = item.content.pattern;
      tmp.arrow = item.content.arrow;
      tmp.value = item.content.value;
      this.isEditLinkModalActive = false;
    },
    endEdit() {
      this.editable = false;
    },
    nodeChanged(obj) {
      this.graphData.nodes = obj.nodes;
    },
    linkChanged(obj) {
      this.graphData.links = obj.links;
    },
    openInputModal() {
      this.isInputModalActive = true;
      this.json = JSON.stringify(this.graphData);
    },
    importData(value) {
      const obj = JSON.parse(value.text);
      if (obj) {
        this.graphData = obj;
        this.isInputModalActive = false;
      }
    },
    downloadSVG() {
      const blob = new Blob(
        [document.getElementById("svg-diagram-show-area").innerHTML],
        {
          type: "image/svg+xml"
        }
      );
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "image.svg";
      link.click();
    },
    changeGrid() {
      this.graphData.width = parseInt(this.settings.width);
      this.graphData.height = parseInt(this.settings.height);
      this.graphData.showGrid = this.settings.showGrid;
    },
    sendJSONToBackend() {
      if(this.startNode !== "" && this.endNode !== ""){
        this.graphData.startNode = this.startNode;
        this.graphData.endNode = this.endNode;
        this.sendMessage();
      }
    },
    updateSettings(val) {
      this.settings = Object.assign({}, val);
      this.changeGrid();
    },
    sendMessage(){
          this.$http.post('http://localhost:8080/floydWarshall', {
          structJSON: JSON.parse(JSON.stringify(this.graphData))
            },
            {
                headers: {
                    'Access-Control-Allow-Headers': 'Content-Type',
                    'Content-Type': 'application/json',
                },
            }).then(function (response) {
                if (response.status === 200) {
                  if(response.body !== "999"){
                    alert('The shortest distance between two nodes is equal ' + response.body);
                  }else{
                    alert('There is no path between this two nodes. (' + response.body + ').')
                  }
                  console.log('ok', response)
                }
            }, function (err) {
                console.log('err', err)
            })
    }
  }
};
</script>
