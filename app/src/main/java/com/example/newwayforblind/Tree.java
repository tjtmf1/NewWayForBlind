package com.example.newwayforblind;

public class Tree {

    final static int MAX_CHILD=2;
    final static int MAX_NODE=50;
    TreeNode[] list;
    int lastIdx;
    int treeID;

    public Tree(){
        for (int i = 0; i < MAX_NODE; i++) {
            list = new TreeNode[i];
        }
        for(int i=0;i<MAX_NODE-1;i++)
            list[i]=new TreeNode();
        treeID = -1;
        lastIdx = -1;
    }

}
