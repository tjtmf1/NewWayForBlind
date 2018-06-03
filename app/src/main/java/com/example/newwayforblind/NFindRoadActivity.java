package com.example.newwayforblind;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NFindRoadActivity extends AppCompatActivity {

    public static final int MAX_NODE = 50;
    Tree tree[];

    EditText edit_start;
    EditText edit_dest;
    TextView text_result;
    String result="";

    int st_idx;//start 가 tree의 list내에서 몇번째 index인지
    int dest_idx;
    int dest_i; //다른 트리에 있을 경우 필요.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfind_road);

        text_result=(TextView)findViewById(R.id.result);
        edit_dest=(EditText)findViewById(R.id.editDest);
        edit_start=(EditText)findViewById(R.id.editStart);

        init();//트리 초기화 및 맵 생성
    }

    void init() { //tree초기화
        /**
         * tree 4개 생성
         */
        tree=new Tree[4];

        tree[0]=new Tree();
        tree[0].treeID = 1;
        tree[0].lastIdx = 7;

        tree[0].list[0].roomData = "입구A";
        tree[0].list[1].roomData = "101";
        tree[0].list[2].roomData = "103";
        tree[0].list[3].roomData = "104";
        tree[0].list[4].roomData = "105";
        tree[0].list[5].roomData = "106";
        tree[0].list[6].roomData = "108";
        tree[0].list[7].roomData = "109";

        tree[0].list[8].roomData = "116";
        tree[0].list[9].roomData = "115";
        tree[0].list[10].roomData = "107";
        for (int i = 0; i < 11; i++) {
            tree[0].list[i].treeNum = 1;
        }

        //child 관계 정보 생성
        tree[0].list[0].child[0] = tree[0].list[1].roomData;
        tree[0].list[0].child[1] = tree[0].list[8].roomData;
        tree[0].list[1].child[0] = tree[0].list[2].roomData;
        tree[0].list[2].child[0] = tree[0].list[3].roomData;
        tree[0].list[3].child[0] = tree[0].list[4].roomData;
        tree[0].list[4].child[0] = tree[0].list[5].roomData;
        tree[0].list[5].child[0] = tree[0].list[6].roomData;
        tree[0].list[6].child[0] = tree[0].list[7].roomData;

        tree[0].list[8].child[0] = tree[0].list[9].roomData;
        tree[0].list[9].child[0] = tree[0].list[10].roomData;

        //parent 관계 정보 생성
        tree[0].list[1].parent = tree[0].list[0].roomData;
        tree[0].list[2].parent = tree[0].list[1].roomData;
        tree[0].list[3].parent = tree[0].list[2].roomData;
        tree[0].list[4].parent = tree[0].list[3].roomData;
        tree[0].list[5].parent = tree[0].list[4].roomData;
        tree[0].list[6].parent = tree[0].list[5].roomData;
        tree[0].list[7].parent = tree[0].list[6].roomData;
        tree[0].list[8].parent = tree[0].list[0].roomData;
        tree[0].list[9].parent = tree[0].list[8].roomData;

        //jump 정보 생성
        tree[0].list[4].jump = 3;   //3번 트리의 입구B로 뛰세요
        tree[0].list[7].jump = 109;   //2번트리의 109로 뛰세요.

        tree[0].list[4].right = new String[]{"101", "103", "104", "116"};
        tree[0].list[4].left = new String[]{"108", "107", "115", "109"};
        tree[0].list[4].lsize = 4;
        tree[0].list[4].rsize = 4;


        tree[0].list[7].left = new String[]{"남자화장실", "남자장애인화장실", "여자화장실", "113", "114"};
        tree[0].list[7].right = new String[]{"101", "103", "104", "105", "108", "115", "107"};

        tree[0].list[7].lsize = 5;
        tree[0].list[7].lsize = 7;

        //거리 정보 생성
        tree[0].list[1].up_dist = 10;
        tree[0].list[2].up_dist = 10;
        tree[0].list[3].up_dist = 10;
        tree[0].list[4].up_dist = 10;
        tree[0].list[5].up_dist = 10;
        tree[0].list[6].up_dist = 10;
        tree[0].list[7].up_dist = 50;

        tree[0].list[8].up_dist = 30;
        tree[0].list[9].up_dist = 50;
        tree[0].list[10].up_dist = 10;


        tree[0].list[0].down_dist[0] = 10;	//입구A 밑의 왼쪽에는 101이 있지요.
        tree[0].list[1].down_dist[0] = 10;
        tree[0].list[2].down_dist[0] = 10;
        tree[0].list[3].down_dist[0] = 10;
        tree[0].list[4].down_dist[0] = 10;
        tree[0].list[5].down_dist[0] = 10;
        tree[0].list[6].down_dist[0] = 50;

        tree[0].list[0].down_dist[1] = 30;
        tree[0].list[8].down_dist[0] = 50;
        tree[0].list[9].down_dist[0] = 10;


	/*
	#2 트리 생성
	*/
        tree[1]=new Tree();
        tree[1].treeID = 2;

        tree[1].list[0].roomData = "109";
        tree[1].list[1].roomData = "남자화장실";
        tree[1].list[2].roomData = "남자장애인화장실";
        tree[1].list[3].roomData = "여자화장실";
        tree[1].list[4].roomData = "113";
        tree[1].list[5].roomData = "114";

        for (int i = 0; i < 11; i++) {
            tree[1].list[i].treeNum = 2;
        }

        //child 관계 정보 생성
        tree[1].list[0].child[0] = tree[1].list[1].roomData;   //남자화장실
        tree[1].list[0].child[1] = tree[1].list[5].roomData;   //114
        tree[1].list[1].child[0] = tree[1].list[2].roomData;   //남자장애인화장실
        tree[1].list[2].child[0] = tree[1].list[3].roomData;   //여자화장실
        tree[1].list[3].child[0] = tree[1].list[4].roomData;   //113

        //parent 관계 정보 생성
        tree[1].list[1].parent = tree[1].list[0].roomData;
        tree[1].list[2].parent = tree[1].list[1].roomData;
        tree[1].list[3].parent = tree[1].list[2].roomData;
        tree[1].list[4].parent = tree[1].list[3].roomData;

        tree[1].list[5].parent = tree[1].list[0].roomData;

        //jump 정보 생성
        tree[1].list[0].jump = 109;   //1번트리의 109로 뛰세요.
        tree[1].list[0].left = new String[]{"남자화장실", "남자장애인화장실", "여자화장실", "113", "114"};
        tree[1].list[0].right = new String[]{"101", "103", "104", "105", "108", "115", "107"};
        tree[1].list[0].lsize = 5;
        tree[1].list[0].rsize = 7;


        //거리 정보 생성
        tree[1].list[1].up_dist = 10;
        tree[1].list[2].up_dist = 10;
        tree[1].list[3].up_dist = 10;
        tree[1].list[4].up_dist = 50;

        tree[1].list[5].up_dist = 40;

        tree[1].list[0].down_dist[0] = 10;
        tree[1].list[1].down_dist[0] = 10;
        tree[1].list[2].down_dist[0] = 10;
        tree[1].list[3].down_dist[0] = 50;

        tree[1].list[0].down_dist[1] = 40;

	/*
	#3 트리 생성
	*/
        tree[2]=new Tree();
        tree[2].treeID = 3;

        tree[2].list[0].roomData = "입구B";
        tree[2].list[1].roomData = "102";

        for (int i = 0; i < 11; i++) {
            tree[2].list[i].treeNum = 3;
        }

        //child 관계 정보 생성
        tree[2].list[0].child[0] = tree[2].list[1].roomData;   //102

        //parent 관계 정보 생성
        tree[2].list[1].parent = tree[2].list[0].roomData;

        //jump 정보 생성
        tree[2].list[1].jump = 4;   //4번트리로 뛰세요.
        //tree[2].list[1].left = new string[]{""}

        //거리 정보 생성
        tree[2].list[1].up_dist = 40;

        tree[2].list[0].down_dist[0] = 40;


	/*
	#4 트리 생성
	*/
        tree[3]=new Tree();
        tree[3].treeID = 4;

        tree[3].list[0].roomData = "102";
        tree[3].list[1].roomData = "102-1";
        tree[3].list[2].roomData = "도서관";
        tree[3].list[3].roomData = "엘리베이터";

        for (int i = 0; i < 11; i++) {
            tree[3].list[i].treeNum = 4;
        }

        //child 관계 정보 생성
        tree[3].list[0].child[0] = tree[3].list[1].roomData;   //102-1
        tree[3].list[1].child[0] = tree[3].list[2].roomData;   //도서관
        tree[3].list[2].child[0] = tree[3].list[3].roomData;   //엘리베이터

        //parent 관계 정보 생성
        tree[3].list[1].parent = tree[3].list[0].roomData;
        tree[3].list[2].parent = tree[3].list[1].roomData;
        tree[3].list[3].parent = tree[3].list[2].roomData;


        //jump 정보 생성
        tree[3].list[0].jump = 3;   //3번트리로 뛰세요.

        //거리 정보 생성
        tree[3].list[1].up_dist = 10;
        tree[3].list[2].up_dist = 10;
        tree[3].list[3].up_dist = 10;

        tree[3].list[0].down_dist[0] = 10;
        tree[3].list[1].down_dist[0] = 10;
        tree[3].list[2].down_dist[0] = 10;

    }

    int search(String str,int check) {//***아직 동일한 이름의 노드는 처리하지 않은 상태~

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < MAX_NODE - 1; j++) {
                if (str.equals(tree[i].list[j].roomData)) {
                    //찾음
                    switch (check){
                        case 1:
                            st_idx=j;
                            break;
                        case 2:
                            dest_idx=j;
                            break;
                        case 3:
                            dest_i=j;
                            break;
                        default:
                            break;
                    }
                    return tree[i].treeID;//tree의 고유번호를 반환
                }
            }
        }
        return 0;  //못 찾을 경우, 트리에 등록되지 않았을 경우.
    }

    void findRoad(String start, String destination, int start_treeNum, int dest_treeNum) {//경로를 찾고 해당경로를 출력하는 함수
 /*
[경로 검색 알고리즘]
1. 트리의 ID 검사
 - 아이디 값이 같은 경우
   -> 무조건 직진
   따라서 '얼마나 직진?' 에 대한 정보만 필요함
-  아이디 값이 다른 경우
 */

        int st_dir=0;//출발지는 root기준으로 왼쪽? 오른쪽?
        int dest_dir=0;//도착지는 root기준으로 왼쪽? 오른쪽?

        if (start_treeNum == dest_treeNum) {
            int treeNum = start_treeNum - 1;
            int x = 0; int y = 0;

		/*
		[st와 dt가 root를 기준으로 오른쪽인지 왼쪽인지 판별하는 알고리즘]
		- 각 tree마다 last idx가있음
		- st의 idx 와 dt의 idx 사이에 last idx 가 있을 경우, st와 dt는 root를 기준으로 서로 반대방향에 있음
		- 이외의 경우라면 모두 root 를 기준으로 한쪽에 몰려있는 경우임
		*/
            if (st_idx <= tree[treeNum].lastIdx&&
                    tree[treeNum].lastIdx <= dest_idx) {
                //st와 dt 가 루트로부터 얼마만큼 떨어져 있는지를 계산
                //st먼저 검사합니당
                for (int i = 0; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                y += tree[treeNum].list[0].down_dist[1];//미리 더해주자
                for (int i = tree[treeNum].lastIdx + 1; i < dest_idx; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }
            else if (st_idx <= tree[treeNum].lastIdx) {
                //st와 dt가 모두 root를 기준으로 왼쪽에 존재함
                //st먼저 검사합니당
                for (int i = 0; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                for (int i = 0; i < dest_idx; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }
            else if (st_idx > tree[treeNum].lastIdx) {
                //st와 dt가 모두 root를 기준으로 오른쪽에 존재함
                //st먼저 검사합니당
                //x += tree[treeNum].list[0].down_dist[1];//미리 더해주자
                for (int i = tree[treeNum].lastIdx + 1; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                for (int i = tree[treeNum].lastIdx + 1; i < dest_idx; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }
            // text_result.setText(text_result.getText().toString()+Math.abs(x-y)+"만큼 직진~\n");
            result +=Math.abs(x-y)+"만큼 직진~ ->";
        }
        else {
            //starttreeNum과 DesttreeNum이 다를 경우

		/*#1(109) ~ #2 : 2
		#2 ~ #1(109) : 1-109
		#1(105) ~ #3 : 3
		#3 ~ #1(105) : 1-105
		#3 ~ #4 : 4
		#4 ~ #3 : 3
		//
		#4 ~ #2 : #4->#3->#1->#2
		#2 ~ #4 : #2->#1->#3->#4
		//
		#1 ~ #4 : #1->#3->#4
		#4 ~ #1 : #4->#3->#1
		//
		#3->#2 : #3->#1->#2
		#2->#3 : #2->#1->#3

		*/
            int treeNum = start_treeNum-1;
            int x = 0; int y = 0;
            dest_i=0;
            int num=0;

            if ((start_treeNum == 4 && dest_treeNum == 2)
                    || (start_treeNum == 4 && dest_treeNum == 1)
                    || (start_treeNum == 3 && dest_treeNum == 2)) {
                //#4 ~ #2 : #4->#3->#1->#2
                //#3 ~#2
                //#4 ~ #1

                //모두 방향이 비슷함.
                int rootin[] = { 4,3,1,2 };
                for (int i = 0; i < 4; i++) {
                    if (start_treeNum == rootin[i]) {
                        //직진코드 삽입하기
                        //cout << "직진띠~(시범코드)" << endl;
                        //직진(st_idx, 이 트리의 분기점으로 ㄱㄱ)
                        //이때 1번트리에서 105로 가는지, 109로 가는지는 코드 합치면서 조건문 더 써주면 될 듯.
                        findRoad(start, destination, rootin[i + 1], dest_treeNum);
                        return;
                    }
                }

            }
            else if ((start_treeNum == 2 && dest_treeNum == 4)
                    || (start_treeNum == 1 && dest_treeNum == 4)
                    || (start_treeNum == 2 && dest_treeNum == 3)) {

                int rootin[] = { 2,1,3,4 };
                for (int i = 0; i < 4; i++) {
                    if (start_treeNum == rootin[i]) {
                        //직진코드 삽입하기
                        //cout << "직진띠~(시범코드)" << endl;
                        findRoad(start, destination, start_treeNum, rootin[i + 1]);
                        //return;
                    }
                }
            }


            //점프할 때 검사 필요함.
            if (start_treeNum == 1 && dest_treeNum == 2) {
                //1번 109에서 2번 109로 가야함.
                search("109", 3);
            }

            else if (start_treeNum == 2 && dest_treeNum == 1) {
                //1-109로 이동해야함.
                search("109", 3);

            }
            else if (start_treeNum == 1 && dest_treeNum == 3) {
                //가까운 분기점 105로 이동
                search("105", 3);

            }
            else if (start_treeNum == 3 && dest_treeNum == 1) {
                search("입구B", 3);
            }
            else if (start_treeNum == 3 && dest_treeNum == 4) {
                search("102", 3);

            }
            else if (start_treeNum == 4 && dest_treeNum == 3) {
                //가까운 분기점(102)으로 이동
                search("102", 3);

            }

		/*
		[st와 dt가 root를 기준으로 오른쪽인지 왼쪽인지 판별하는 알고리즘]
		- 각 tree마다 last idx가있음
		- st의 idx 와 dt의 idx 사이에 last idx 가 있을 경우, st와 dt는 root를 기준으로 서로 반대방향에 있음
		- 이외의 경우라면 모두 root 를 기준으로 한쪽에 몰려있는 경우임
		*/
            if (st_idx <= tree[treeNum].lastIdx&&
                    tree[treeNum].lastIdx <= dest_i) {
                //st와 dt 가 루트로부터 얼마만큼 떨어져 있는지를 계산
                //st먼저 검사합니당
                for (int i = 0; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                y += tree[treeNum].list[0].down_dist[1];//미리 더해주자
                for (int i = tree[treeNum].lastIdx + 1; i < dest_i; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }
            else if (st_idx <= tree[treeNum].lastIdx) {
                //st와 dt가 모두 root를 기준으로 왼쪽에 존재함
                //st먼저 검사합니당
                for (int i = 0; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                for (int i = 0; i < dest_i; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }
            else if (st_idx > tree[treeNum].lastIdx) {
                //st와 dt가 모두 root를 기준으로 오른쪽에 존재함
                //st먼저 검사합니당
                //x += tree[treeNum].list[0].down_dist[1];//미리 더해주자
                for (int i = tree[treeNum].lastIdx + 1; i < st_idx; i++) {
                    x += tree[treeNum].list[i].down_dist[0];
                }

                //다음으로 dt를 검사합니당
                for (int i = tree[treeNum].lastIdx + 1; i < dest_i; i++) {
                    y += tree[treeNum].list[i].down_dist[0];
                }
            }

            // text_result.setText(text_result.getText().toString()+Math.abs(x-y)+"만큼 직진~\n");
            result+=Math.abs(x-y)+"만큼 직진~ ->";

            //점프할 때 검사 필요함.
            if (start_treeNum == 1 && dest_treeNum == 2) {
                //1번 109에서 2번 109로 가야함.
                // text_result.setText( text_result.getText()+"\n"+"오른쪽 방향으로 꺾으세요.\n");
                result+= "오른쪽 방향으로 꺾으세요 ->";

                start_treeNum = 2;
                st_idx = dest_i;
                findRoad(start, destination, start_treeNum, dest_treeNum);

            }
            else if (start_treeNum == 2 && dest_treeNum == 1) {
                //1-109로 이동해야하는 상황

                for (int i = 0; i < tree[dest_treeNum - 1].list[dest_i].lsize; i++)
                {
                    if (tree[dest_treeNum - 1].list[dest_i].left[i].equals(start)) {
                        //109라인에서 1번트리로 갈때
                        //text_result.setText( text_result.getText()+"\n"+"왼쪽 방향으로 꺾으세요.\n");
                        result+= "왼쪽 방향으로 꺾으세요 ->";

                        st_idx = dest_i;
                        start_treeNum = 1;
                        findRoad(start, destination, start_treeNum, dest_treeNum);
                        break;
                    }
                }
            }
            else if (start_treeNum == 1 && dest_treeNum == 3) {

                //1번 트리의 105에서 입구B로 이동하는 상황(직선 길이 잘모름....)
			/*
			101->102 : 직진 and 오른방향으로 꺾기
			107->102 : 직진 and 왼쪽방향으로 꺾기

			3->1의 경우 이하동문

			*/

                for (int i = 0; i < tree[start_treeNum - 1].list[dest_i].lsize; i++) {
                    if (tree[start_treeNum - 1].list[dest_i].left[i].equals(start)) {
                        //107->입구B쪽으로 가는길, 왼쪽 꺾기

                        //text_result.setText( text_result.getText()+"\n"+"왼쪽 방향으로 꺾으세요.\n");
                        result+= "왼쪽 방향으로 꺾으세요 ->";

                        st_idx = dest_i;
                        start_treeNum = 3;
                        findRoad(start, destination, start_treeNum, dest_treeNum);
                        break;
                    }
                }
                for (int i = 0; i < tree[start_treeNum - 1].list[dest_i].rsize; i++) {
                    if (tree[start_treeNum - 1].list[dest_i].right[i].equals(start)) {
                        //107->입구B쪽으로 가는길, 왼쪽 꺾기

                        //text_result.setText( text_result.getText()+"오른쪽 방향으로 꺾으세요.\n");
                        result+= "오른쪽 방향으로 꺾으세요 ->";

                        st_idx = dest_i;
                        start_treeNum = 3;
                        findRoad(start, destination, start_treeNum, dest_treeNum);
                        break;
                    }
                }

            }
            else if (start_treeNum == 3 && dest_treeNum == 1) {
                //입구B 또는 102에서 1번라인으로 가려고 할 때
                //입구B가 dest_i임, 이때, 입구B는 105로 진입이 가능함.
                //그러면 그냥 찾을 때, 105로 찾으면 되는거 아냐?

                search("105", 3);

                for (int i = 0; i < tree[dest_treeNum - 1].list[dest_i].lsize; i++)
                {
                    if (tree[dest_treeNum - 1].list[dest_i].left[i].equals(destination)) {
                        //입구B에서 1번트리로 갈때
                        //text_result.setText( text_result.getText()+"\n"+"오른쪽 방향으로 꺽으세요.\n");
                        result+= "오른쪽 방향으로 꺾으세요 ->";

                        st_idx = dest_i;
                        start_treeNum = 1;
                        findRoad(start, destination, start_treeNum, dest_treeNum);
                        break;
                    }
                }
                for (int i = 0; i < tree[dest_treeNum - 1].list[dest_i].rsize; i++)
                {
                    if (tree[dest_treeNum - 1].list[dest_i].right[i].equals(destination)) {
                        //입구B에서 1번트리로 갈때
                        //text_result.setText( text_result.getText()+"\n"+"왼쪽 방향으로 꺽으세요.\n");
                        result+= "왼쪽 방향으로 꺾으세요 ->";

                        st_idx = dest_i;
                        start_treeNum = 1;
                        findRoad(start, destination, start_treeNum, dest_treeNum);
                        break;
                    }
                }
            }
            else if (start_treeNum == 3 && dest_treeNum == 4) {
                //3번트리에서 4번트리로 가는 방향
                start_treeNum = 4;
                st_idx = dest_i;
                //text_result.setText( text_result.getText()+"\n"+"직진하세요.\n");
                result+="직진";

                findRoad(start, destination, start_treeNum, dest_treeNum);


            }
            else if (start_treeNum == 4 && dest_treeNum == 3) {

                //text_result.setText( text_result.getText()+"\n"+"직진하세요.\n");
                result+="직진";

                start_treeNum = 3;
            }

        }
    }


    public void btnStart(View view) {

        //result에 해당하는 텍스트 초기화
        text_result.setText("");
        result="";

        String st=edit_start.getText().toString();
        String dt=edit_dest.getText().toString();

        int start_dest=1;    //true일때 start, false일 때 dest
        int start_treeID=search(st, 1);
        start_dest=2;
        int dest_treeID = search(dt, 2);

        //길찾기 ㄱㄱ
        findRoad(st, dt, start_treeID, dest_treeID);

        text_result.setText(result);

    }
}
