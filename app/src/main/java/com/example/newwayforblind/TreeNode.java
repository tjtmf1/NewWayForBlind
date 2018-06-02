package com.example.newwayforblind;

public class TreeNode {

    final static int MAX_CHILD=2;
    final static int MAX_NODE=50;

    String roomData;    //공간정보 이름
    int treeNum;//자신이 속한 트리의 번호

    String parent;//바로 위에 있는 노드에 대한 정보, root는 이 값을 -1로 갖는다.
    int up_dist;//바로 위 노드와의 거리차

    String child[]=new String[MAX_CHILD];//root를 제외한 모든 노드는 child가 1개
    int down_dist[]=new int[2];//바로 아래 노드와의 거리차 //right left

    int jump;//jump를 해야되는 tree의 번호, 하지 않는 경우는 -1으로 설정
    //jump를 해야되는 노드 : 105, 109, 입구B, 102

    int whoRU;//root니? root의 오른쪽에 있는애니? root의 왼쪽에 있는애니?=?root를 기준으로만!

    String[] left;   //점프시 왼쪽방향으로 꺽는지에 대한 정보를 담고있음.
    String[] right;   //점프시 오른쪽방향으로 꺽는지에 대한 정보를 담고있음.
    int lsize;   //left배열의 사이즈크기
    int rsize;   //right배열의 사이즈크기

    public TreeNode(){
        treeNum = -1;
        jump = -1;
        child[0] = "없음";
        child[1] = "없음";
        up_dist = -1;
        down_dist[0] = -1; down_dist[1] = -1;
        whoRU = -1;

    }
}
