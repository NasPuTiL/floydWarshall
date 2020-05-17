package com.lysy.floydWarshall.utilis;

import com.lysy.floydWarshall.entity.Link;
import com.lysy.floydWarshall.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.*;

public class ParseStructure {
    static public Map<String, Node> nodeList;
    static public Map<String, Link> linkList;
    static private Node sourceNode;
    static private Node destinationNode;

    static {
        nodeList = new LinkedHashMap<>();
        linkList = new LinkedHashMap<>();
    }

    public static void upnarse(JSONObject structJSON){
        createNodes(structJSON);
        createLinks(structJSON);

        String idStart = structJSON.get("startNode").toString().substring(3,19);
        String idEnd = structJSON.get("endNode").toString().substring(3,19);
        sourceNode = nodeList.get(idStart);
        destinationNode = nodeList.get(idEnd);
    }

    private static void createNodes(JSONObject structJSON) {
        JSONArray nodes = structJSON.optJSONArray("nodes");
        for(int i = 0 ; i < nodes.length(); i ++){
            JSONObject node = nodes.getJSONObject(i);
            String id = (String) node.get("id");
            String text = (String) node.getJSONObject("content").get("text");
            nodeList.put(id, new Node(id, text));
        }
    }

    private static void createLinks(JSONObject structJSON) {
        JSONArray links = structJSON.optJSONArray("links");
        System.out.println("links = " + links);
        for(int i = 0 ; i < links.length(); i ++){
            JSONObject link = links.getJSONObject(i);
            String id = (String) link.get("id");
            String source = (String) link.get("source");
            String destination = (String) link.get("destination");
            int value = Integer.valueOf(String.valueOf(link.get("value")));
            linkList.put(id, new Link(id, nodeList.get(source),nodeList.get(destination), value));
        }
    }

    public static int getValueFromLink(Node startNode, Node endNode){
        if(startNode.equals(endNode)){
            return 0;
        }
        for(Link link : linkList.values()){
            if(link.getStartNode().equals(startNode) && link.getEndNode().equals(endNode)){
                return link.getValue();
            }
        }
        //return Integer.MAX_VALUE;
        return 999;
    }

    public static Node getSourceNode() {
        return sourceNode;
    }

    public static Node getDestinationNode() {
        return destinationNode;
    }
}
