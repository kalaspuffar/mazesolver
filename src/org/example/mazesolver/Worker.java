package org.example.mazesolver;

public class Worker implements Runnable{
    private boolean[][] maze = new boolean[4][5];

    private int playerPosX = 4;
    private int playerPosY = 2;
    private int playerDir = 1;
    private int currentLen = 15;
    public int[] newPath = new int[currentLen+10];


    public Worker(int[] partPath) {
        for(int i=0; i<10; i++) newPath[currentLen + i] = partPath[i];
    }

    @Override
    public void run() {
//        long start = System.currentTimeMillis();
        this.initNewPath();
        while(this.pathGenerator()) {
            this.initMap();
            this.motionMachine(newPath);
            if(this.solved()) {
                this.printPath(newPath);
            }
        }
//        System.out.println("DONE "+(System.currentTimeMillis()-start));
    }

    // Actions 0 = move, 1 = light, 2 = turn cw, 3 = turn ccw
    private void printPath(int[] partToPrint) {
        for(int i : partToPrint) {
            switch(i) {
                case 0:
                    System.out.print("move  ");
                    break;
                case 1:
                    System.out.print("light ");
                    break;
                case 2:
                    System.out.print("cw    ");
                    break;
                case 3:
                    System.out.print("ccw   ");
                    break;
                default:
            }
        }
        System.out.println();
    }

    public void initNewPath() {
        for(int i=0; i<currentLen; i++) newPath[i] = 0;
    }

    public boolean pathGenerator() {
        int pos = currentLen - 1;
        while(pos > -1) {
            if(newPath[pos] == 3) {
                newPath[pos] = 0;
                pos--;
            } else {
                newPath[pos]++;
                return true;
            }
        }
        return false;
    }

    public void motionMachine(int[] path) {
        for(int action : path) {
            if(action == -1) return;
            doAction(action);
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


    public static void main(String[] args) {
        Worker w = new Worker(new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        w.playerDir = 3;
        w.turn(false);
        System.out.println(w.playerDir);
    }
}
