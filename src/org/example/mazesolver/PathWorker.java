package org.example.mazesolver;

import java.util.Arrays;

public class PathWorker implements Runnable{
    // F1 Index 0-2
    // F2 Index 3-4
    // F3 Index 5-6
    private int[] main;
    private int[] functions = new int[] {0,0,0, 0,0, 0,0} ;

    // Values
    // 0 = move, 1 = cw, 2 = ccw, 3 = light, 4 = F1, 5 = F2, 6 = F3
    private MoveNode solution = null;

    public PathWorker(MoveNode solution, int[] main) {
        this.solution = solution;
        this.main = main;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while(pathGenerator()) {
            seenF1 = false;
            seenF2 = false;
            seenF3 = false;
            if(hasLoops(0, 3) || hasLoops(3, 2) || hasLoops(5, 2)) continue;
            MoveNode test = solution;
            if(checkMain(test)) {
                System.out.println("Solution found! "+ Arrays.toString(main)+ " "+Arrays.toString(functions));
            }
        }
//        System.out.println("DONE! "+(System.currentTimeMillis()-startTime));
    }

    public boolean checkFunction(MoveNode solution, int index, int step) {
        for(int i=index; i < index + step; i++) {
            if(solution.end) return true;

            switch(functions[i]) {
                case 0:
                    if(solution.move == null) return false;
                    solution = solution.move;
                    break;
                case 1:
                    if(solution.cw == null) return false;
                    solution = solution.cw;
                    break;
                case 2:
                    if(solution.ccw == null) return false;
                    solution = solution.ccw;
                    break;
                case 3:
                    if(solution.light == null) return false;
                    solution = solution.light;
                    break;
                case 4:
                    if(checkFunction(solution, 0, 3)) return true;
                    break;
                case 5:
                    if(checkFunction(solution, 3, 2)) return true;
                    break;
                case 6:
                    if(checkFunction(solution, 5, 2)) return true;
                    break;
            }
        }
        return false;
    }

    public boolean checkMain(MoveNode solution) {
        for(int i=0; i < 6; i++) {
            if(solution.end) return true;

            switch(main[i]) {
                case 0:
                    if(solution.move == null) return false;
                    solution = solution.move;
                    break;
                case 1:
                    if(solution.cw == null) return false;
                    solution = solution.cw;
                    break;
                case 2:
                    if(solution.ccw == null) return false;
                    solution = solution.ccw;
                    break;
                case 3:
                    if(solution.light == null) return false;
                    solution = solution.light;
                    break;
                case 4:
                    if(checkFunction(solution, 0, 3)) return true;
                    break;
                case 5:
                    if(checkFunction(solution, 3, 2)) return true;
                    break;
                case 6:
                    if(checkFunction(solution, 5, 2)) return true;
                    break;
            }
        }
        return false;
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

    private boolean seenF1 = false;
    private boolean seenF2 = false;
    private boolean seenF3 = false;

    public boolean hasLoops(int index, int step) {
        for(int i=index; i < index + step; i++) {
            switch(functions[i]) {
                case 4:
                    if(seenF1) return true;
                    seenF1 = true;
                    if(hasLoops(0, 3)) return true;
                    break;
                case 5:
                    if(seenF2) return true;
                    seenF2 = true;
                    if(hasLoops(3, 2)) return true;
                    break;
                case 6:
                    if(seenF3) return true;
                    seenF3 = true;
                    if(hasLoops(5, 2)) return true;
                    break;
            }
        }
        return false;
    }
}
