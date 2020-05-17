package com.lysy.floydWarshall.algorithm;

import com.lysy.floydWarshall.entity.Node;
import com.lysy.floydWarshall.utilis.ParseStructure;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmFloydWarshall {
    private Integer[][] table;
    private  List<Node> arrayList;

    public AlgorithmFloydWarshall() {
        this.arrayList = new ArrayList<>(ParseStructure.nodeList.values());
        this.table = new Integer[arrayList.size()][arrayList.size()];
        fillTable();
    }

    public void printArray() {
        System.out.print("  ");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i).getText() + " ");
        }
        System.out.println();
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i).getText() + " ");
            for (int j = 0; j < arrayList.size(); j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void fillTable() {
        for(int i = 0; i < arrayList.size(); i ++){
            for(int j = 0; j < arrayList.size(); j ++){
                this.table[i][j] = ParseStructure.getValueFromLink(arrayList.get(i), arrayList.get(j));
            }
        }
    }

    public void algFloydWarshall(){
        for (int k = 0; k < ParseStructure.nodeList.size(); k++) {
            for (int i = 0; i < ParseStructure.nodeList.size(); i++) {
                for (int j = 0; j < ParseStructure.nodeList.size(); j++) {
                    if(this.table[i][k] + this.table[k][j] < this.table[i][j]){
                        this.table[i][j] = this.table[i][k] + this.table[k][j];
                    }
                }
            }
        }
    }

    public int findShortestPath(Node u, Node v) {
        if(u.equals(v)){
            return 0;
        }
        int start = -1;
        int end = -1;

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).equals(u)){
                start = i;
            }
            if(arrayList.get(i).equals(v)){
                end = i;
            }
        }
        return table[start][end];
    }
}
