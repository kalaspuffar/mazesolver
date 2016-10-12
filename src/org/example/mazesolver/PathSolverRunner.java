package org.example.mazesolver;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PathSolverRunner {

    // Main = 6, F1 = 3, F2 = 2, F3 = 2
    // main Index 0-5
    // F1 Index 6-8
    // F2 Index 9-10
    // F3 Index 11-12
//    private int[] path = new int[] {0,0,0,0,0,0, 0,0,0, 0,0, 0,0} ;
    private int[] path = new int[] {0,0,0,0,0,0} ;

    // 0 = move, 1 = cw, 2 = ccw, 3 = light, 4 = F1, 5 = F2, 6 = F3
//    private int[] path = new int[] {5,1,6,5,1,5} ;

    public int[] getPath() {
        return path;
    }


    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        PathSolverRunner pf = new PathSolverRunner();
        while(pf.pathGenerator()) {
            executor.submit(new PathSolver(Arrays.copyOf(pf.getPath(), 6)));
        }
        System.out.println("Prepared workers.");

        try {
            executor.awaitTermination(5, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean pathGenerator() {
        int pos = path.length - 1;
        while(pos > -1) {
            if(path[pos] == 6) {
                path[pos] = 0;
                pos--;
            } else {
                path[pos]++;
                return true;
            }
        }
        return false;
    }
}
