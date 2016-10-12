package org.example.mazesolver;

import java.util.Arrays;

public class PathSolver implements Runnable{
    private boolean[][] maze = new boolean[4][5];

    private int playerPosX = 4;
    private int playerPosY = 2;
    private int playerDir = 1;

    // F1 Index 0-2
    // F2 Index 3-4
    // F3 Index 5-6
    private int[] main;
    private int[] functions = new int[] {0,0,0, 0,0, 0,0} ;
//    private int[] functions = new int[] {0,0,3, 4,4, 1,0} ;

    // Values
    // 0 = move, 1 = cw, 2 = ccw, 3 = light, 4 = F1, 5 = F2, 6 = F3
    public PathSolver(int[] main) {
        this.main = main;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while(pathGenerator()) {
            if(hasLoops(0, 3, false, false, false)
                    || hasLoops(3, 2, false, false, false)
                    || hasLoops(5, 2, false, false, false)) continue;

            this.initMap();
            this.runMain();
            if(this.solved()) {
                System.out.println("Solution found! "+ Arrays.toString(main)+ " "+Arrays.toString(functions));
            }
        }
//        System.out.println("DONE! "+(System.currentTimeMillis()-startTime));
    }


    public void runFunction(int index, int step) {
        for(int i=index; i < index + step; i++) {
            switch(functions[i]) {
                case 0:
                case 1:
                case 2:
                case 3:
                    doAction(functions[i]);
                    break;
                case 4:
                    runFunction(0, 3);
                    break;
                case 5:
                    runFunction(3, 2);
                    break;
                case 6:
                    runFunction(5, 2);
                    break;
            }
        }
    }

    public void runMain() {
        for(int i=0; i < 6; i++) {
            switch(main[i]) {
                case 0:
                case 1:
                case 2:
                case 3:
                    doAction(main[i]);
                    break;
                case 4:
                    runFunction(0, 3);
                    break;
                case 5:
                    runFunction(3, 2);
                    break;
                case 6:
                    runFunction(5, 2);
                    break;
            }
        }
    }


    // Actions 0 = move, 1 = light, 2 = turn cw, 3 = turn ccw
    public void doAction(int action) {
        switch(action) {
            case 0:
                move();
                break;
            case 1:
                light();
                break;
            case 2:
                turn(false);
                break;
            case 3:
                turn(true);
                break;
            default:
        }
    }

    public void light() {
        maze[playerPosY][playerPosX] = false;
    }

    public void turn(boolean ccw) {
        if(ccw) {
            playerDir = playerDir <= 0 ? 3 : playerDir - 1;
        } else {
            playerDir = playerDir >= 3 ? 0 : playerDir + 1;
        }
    }

    // Player direction 0 = North, 1 = East, 2 = South, 3 = West
    public void move() {
        switch(playerDir) {
            case 0:
                if(playerPosY > 0) playerPosY--;
                break;
            case 1:
                if(playerPosX < 4) playerPosX++;
                break;
            case 2:
                if(playerPosY < 3) playerPosY++;
                break;
            case 3:
                if(playerPosX > 0) playerPosX--;
                break;
        }
    }

    public void initMap() {
        playerPosX = 4;
        playerPosY = 2;
        playerDir = 1;

        maze[0][4] = true;
        maze[1][4] = true;
        maze[3][4] = true;
        maze[2][0] = true;
        maze[2][1] = true;
    }
    public boolean solved() {
        return !maze[0][4] && !maze[1][4] && !maze[3][4] && !maze[2][0] && !maze[2][1];
    }

    public boolean pathGenerator() {
        int pos = functions.length - 1;
        while(pos > -1) {
            if(functions[pos] == 6) {
                functions[pos] = 0;
                pos--;
            } else {
                functions[pos]++;
                return true;
            }
        }
        return false;
    }

    public boolean hasLoops(int index, int step, boolean seenF1, boolean seenF2, boolean seenF3) {
        for(int i=index; i < index + step; i++) {
            switch(functions[i]) {
                case 4:
                    if(seenF1) return true;
                    if(hasLoops(0, 3, true, seenF2, seenF3)) return true;
                    break;
                case 5:
                    if(seenF2) return true;
                    if(hasLoops(3, 2, seenF1, true, seenF3)) return true;
                    break;
                case 6:
                    if(seenF3) return true;
                    if(hasLoops(5, 2, seenF1, seenF2, true)) return true;
                    break;
            }
        }
        return false;
    }
}
