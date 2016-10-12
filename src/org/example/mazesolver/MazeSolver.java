package org.example.mazesolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MazeSolver {

    // ccw, move, light, move, light, ccw, move, move, move, ccw, move, move, light, cw, move, light
    // ccw, move, ccw, move, move, move, move, light

    // Actions 0 = move, 1 = light, 2 = turn cw, 3 = turn ccw
    private static int[] startPath = new int[] {
            3, 0, 1, 0, 1, 3, 0, 0, 0, 3, 0, 0, 1, 2, 0, 1,
            3, 0, 3, 0, 0, 0, 0, 1
    };
    private static int startPathLen = startPath.length; // 25 ?

    private static int LENGTH = 11;
    private int currentLen = 1;
    public int[] newPath = new int[LENGTH];

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] arg) {
        MazeSolver ms = new MazeSolver();
        ms.initNewPath();
        while (ms.currentLen < LENGTH) {
            int[] array = ms.pathGenerator();
            executor.submit(new Worker(Arrays.copyOf(array, array.length)));
        }
        System.out.println("Prepared workers.");

        try {
            executor.awaitTermination(5, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initNewPath() {
        for(int i=0; i<LENGTH; i++) newPath[i] = -1;
    }


    public int[] pathGenerator() {
        if(newPath[currentLen - 1] == -1) {
            for(int i=0; i<currentLen; i++) newPath[i] = 0;
        } else {
            int pos = currentLen - 1;
            while(pos > -1) {
                if(newPath[pos] == 3) {
                    newPath[pos] = 0;
                    pos--;
                } else {
                    newPath[pos]++;
                    return newPath;
                }
            }
            currentLen++;
            System.out.println("New len: "+currentLen);
        }
        return newPath;
    }

    /*
    public void printMaze() {
        for(int y = 0; y < 4; y++) {
            System.out.println("-----------");
            System.out.print("|");
            for(int x = 0; x < 5; x++) {
                System.out.print((maze[y][x]) ? "*|" : " |");
            }
            System.out.println();
        }
        System.out.println("-----------");
    }
    */
}
