package com.lysy.floydWarshall.entity;

import java.util.Objects;

public class Node {
    private String id;
    private String text;

    public Node(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id) &&
                text.equals(node.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
