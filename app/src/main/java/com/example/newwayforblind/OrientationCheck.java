package com.example.newwayforblind;

public class OrientationCheck {
    int warningCount=0;

    final int THRESHOLD=80;//임계치
    final int CORRECTION=360;//보정
    final int ALLOW=4;//임계치를 넘는것을 허용하는 횟수
    int pre=0;
    OrientationCheck(){

    }
    public String checkDirection(int now){
        String dir="";//현재 방향정보
        int check=Math.abs(pre-now);

        //부호 체크
        if(pre*now>0&&pre!=0&&now!=0){ //부호가 같은 경우
            //임계치 확인
            if(check<=THRESHOLD){
                //방향을 변동하지 않음
                dir = "";
                if(warningCount>0){
                    warningCount--;
                }
            }else{
                //방향 변동 flag ON
                warningCount++;
                if(warningCount>ALLOW){//허용 오차를 넘었을 경우 =>방향 변동 인식
                    //오른쪽, 왼쪽 판단
                    warningCount=0;//초기화
                    if(pre<now){
                        //오른쪽
                        dir="오른쪽";
                    }else{
                        //왼쪽
                        dir="왼쪽";
                    }
                }
            }
        }else if(pre*now<0&&pre!=0&&now!=0){
            //부호가 다른 경우
            if( (90<=now&&now<=180) && (-180<=pre&& pre<=90)){
                //음수값을 양수로 보정
                pre=pre+CORRECTION;
                check=Math.abs(pre-now);
                //임계치 확인
                if(check<=THRESHOLD){
                    //방향을 변동하지 않음
                    dir = "";
                    if(warningCount>0){
                        warningCount--;
                    }
                }else {//방향 변동 flag ON
                    warningCount++;
                    if (warningCount > ALLOW) {//허용 오차를 넘었을 경우 =>방향 변동 인식
                        //무조건 왼쪽
                        dir="왼쪽";
                    }
                }
            }else if((90<=pre&&pre<=180) && (-180<=now&& now<=90) ){
                //음수값을 양수로 보정
                now=now+CORRECTION;
                check=Math.abs(pre-now);
                //임계치 확인
                if(check<=THRESHOLD){
                    //방향을 변동하지 않음
                    if(warningCount>0){
                        warningCount--;
                    }
                }else {//방향 변동 flag ON
                    warningCount++;
                    if (warningCount > ALLOW) {//허용 오차를 넘었을 경우 =>방향 변동 인식
                        //무조건 오른쪽
                        dir="오른쪽";
                    }
                }
            }

        }
        pre=now;
        return dir;//현재 방향정보를 return
    }
}
