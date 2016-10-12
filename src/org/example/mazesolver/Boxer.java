package org.example.mazesolver;

import java.util.Arrays;

public class Boxer {

    // Actions 0 = move, 1 = light, 2 = turn cw, 3 = turn ccw
    private int[] testPath = new int[] {
            3, 0, 1, 0, 1, 3, 0, 0, 0, 3, 0, 0, 1, 2, 0, 1,
            3, 0, 3, 0, 0, 0, 0, 1
    };

    // Box 1 = 4, box 2 = 5, box 3 = 6
    private int[] box1 = new int[3];
    private int[] box2 = new int[2];
    private int[] box3 = new int[2];

    private int[] main = new int[20];

    public static void main(String[] args) {
        Boxer b = new Boxer();
        b.findLargestRecurring();
    }

    public void findLargestRecurring() {
        int occuring = 1;
        int[] largest = new int[]{};
        for (int size = testPath.length / 2; size > 1; size--) {
            for (int i = 0; i < testPath.length - size; i++) {
                int occurance = recurringXTimes(Arrays.copyOfRange(testPath, i, i+size));
                if (occurance > 1 && size > 2 && occurance * size > occuring * largest.length) {
                    largest = Arrays.copyOfRange(testPath, i, i+size);
                    occuring = occurance;
                }
            }
        }

        System.out.println(Arrays.toString(largest));
        System.out.println(occuring);
    }

    private int recurringXTimes(int[] search) {
        int contains = 0;
        outer: for(int i=0; i<testPath.length-search.length; i++) {
            for(int j=0; j<search.length; j++) {
                if(testPath[i+j] != search[j]) continue outer;
            }
            contains++;
        }
        return contains;
    }

    private static boolean contains(int boxId, int[] box) {
        for(int i : box) if (i == boxId) return true;
        return false;
    }
}