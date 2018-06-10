package com.example.newwayforblind;

public class OrientationCheck {
    private Orientation mOrientation;
    int warningCount=0;

    final int THRESHOLD=65;//임계치
    final int TURN_THRESHOLD=35;//뒤돌 때 임계치
    final int CORRECTION=360;//보정
    final int ALLOW=4;//임계치를 넘는것을 허용하는 횟수
    int last=0;
    OrientationCheck(){

    }
    public String checkDirection(int ori){
        int a=last+THRESHOLD;
        int b=last-THRESHOLD;
        int c, d;
        int warningCnt=0;
        String dir = "";
        if(last == 0){
            last = ori;
            return "직진";
        }

        if(last < 180){
            c = last + 180 + TURN_THRESHOLD;
            d = last + 180 - TURN_THRESHOLD;
        }else{
            c = last - 180 + TURN_THRESHOLD;
            d = last - 180 - TURN_THRESHOLD;
        }

        if(270<=last && last < 360){ // 4사분면
            // 방향을 전환하지 않음
            if((a <= 360 && b <=ori && ori < a) || // a <= 360
                    (a > 360 && ((b<=ori&&ori<=360) || (0<=ori&&ori<=(a-CORRECTION))))){ // a > 360
                dir = "직진";
            }else{ // 방향을 전환 함
                if(last - 180 + TURN_THRESHOLD <= ori && ori < last){
                    // 왼쪽
                    dir = "왼쪽";
                }else if((0 <= ori && ori < last - 180 - TURN_THRESHOLD) || (last <= ori && ori < 360)){
                    // 오른쪽
                    dir = "오른쪽";
                }else{
                    // 뒤로 돌았음
                    dir = "뒤돌기";
                }
            }
        }else if(0 <= last && last < 90){ // 1사분면
            // 방향을 전환하지 않음
            if((b > 0 && b <= ori && ori < a) || // b > 0
                    (b <= 0 && (((b + CORRECTION) <= ori && ori <= 360) || (0 <= ori && ori <= a)))) { // b <= 0
                dir = "직진";
            }
            else{ // 방향을 전환 함
                if(last <= ori && ori < last + 180 - TURN_THRESHOLD){
                    // 오른쪽
                    dir = "오른쪽";
                }else if((last + 180 + TURN_THRESHOLD <= ori && ori < 360) || (0 <= ori && ori < last)){
                    // 왼쪽
                    dir = "왼쪽";
                }else{
                    // 뒤로 돌았음
                    dir = "뒤돌기";
                }
            }
        }else if(90 <= last && last < 180){ //2사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
                dir = "직진";
            }else{ // 방향을 전환 함
                if(last <= ori && ori < last + 180 - TURN_THRESHOLD){
                    // 오른쪽
                    dir = "오른쪽";
                }else if((c>360 && c - 360 <= ori && ori < last) ||
                        (c<360 && ((c <= ori && ori < 360) || (0 <= ori && ori < last)))){
                    // 왼쪽
                    dir = "왼쪽";
                }else{
                    // 뒤로 돌았음
                    dir = "뒤돌기";
                }
            }
        }else if(180 <= last && last < 270){ // 3사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
                dir = "직진";
            }else{ // 방향을 전환 함
                if((d<0 && last < ori && ori <= d + 360) ||
                        (d>0 && ((0 <= ori && ori < d) || (last <= ori && ori <360)))){
                    // 오른쪽
                    dir = "오른쪽";
                }else if(last - 180 + TURN_THRESHOLD <= ori && ori < last){
                    // 왼쪽
                    dir = "왼쪽";
                }else{
                    // 뒤로 돌았음
                    dir = "뒤돌기";
                }
            }
        }else{
            dir = "불행";
        }

        last=ori;
        return dir;
    }
}
