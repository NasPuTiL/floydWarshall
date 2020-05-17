package com.lysy.floydWarshall.entity;

public class Link {
    private String id;
    private Node startNode;
    private Node endNode;
    private int value;

    public Link(String id, Node startNode, Node endNode, int value) {
        this.id = id;
        this.startNode = startNode;
        this.endNode = endNode;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
