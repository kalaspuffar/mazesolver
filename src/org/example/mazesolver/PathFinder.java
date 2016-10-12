package org.example.mazesolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PathFinder {

    // Main = 6, F1 = 3, F2 = 2, F3 = 2
    // main Index 0-5
    // F1 Index 6-8
    // F2 Index 9-10
    // F3 Index 11-12
//    private int[] path = new int[] {0,0,0,0,0,0, 0,0,0, 0,0, 0,0} ;
    private int[] path = new int[] {0,0,0,0,0,0} ;

    // Values
    // 0 = move, 1 = cw, 2 = ccw, 3 = light, 4 = F1, 5 = F2, 6 = F3

    private MoveNode solution = new MoveNode();

    public void init() {
        try(BufferedReader br = new BufferedReader(new FileReader("solutions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                while(line.contains("  ")) {
                    line = line.replaceAll("  ", " ");
                }

                MoveNode nextNode = solution;
                for(String s : line.split(" ")) {
                    if(s.equalsIgnoreCase("move")) {
                        if(nextNode.move == null) nextNode.move = new MoveNode();
                        nextNode = nextNode.move;
                    }
                    if(s.equalsIgnoreCase("cw")) {
                        if(nextNode.cw == null) nextNode.cw = new MoveNode();
                        nextNode = nextNode.cw;
                    }
                    if(s.equalsIgnoreCase("ccw")) {
                        if(nextNode.ccw == null) nextNode.ccw = new MoveNode();
                        nextNode = nextNode.ccw;
                    }
                    if(s.equalsIgnoreCase("light")) {
                        if(nextNode.light == null) nextNode.light = new MoveNode();
                        nextNode = nextNode.light;
                    }
                }
                nextNode.end = true;
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getPath() {
        return path;
    }

    public MoveNode getSolution() {
        return solution;
    }

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        PathFinder pf = new PathFinder();
        pf.init();
        while(pf.pathGenerator()) {
            executor.submit(new PathWorker(pf.getSolution(), Arrays.copyOf(pf.getPath(), 6)));
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
