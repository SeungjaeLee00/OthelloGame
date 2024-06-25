package othelloapp;

import java.util.*;

public class ComputerPlayer {
         final static int score[][] = {{9,1,3,3,3,3,1,9}, {1,1,2,2,2,2,1,1}, {3,2,2,1,1,2,2,3}, {3,2,1,0,0,1,2,3}, {3,2,1,0,0,1,2,3}, {3,2,2,1,1,2,2,3},  {1,1,2,2,2,2,1,1}, {9,1,3,3,3,3,1,9}};
         
     // level: го
        public static Stone getNoviceMove(Board board, char stoneColor) {
        	Stone temp = null;
                LinkedList possible = board.possibleMoves(stoneColor);
                if (possible.size() > 0) {
                        long choice = Math.round(0 + (Math.random() * (possible.size() -1)));
                        temp = (Stone)possible.get((int)choice);
                }
                return temp;
        }

     // level: ╩С
        public static Stone getIntMove(Board board, char stoneColor) {
        	Stone temp = null;
                LinkedList possible = board.possibleMoves(stoneColor);
                LinkedList highs = new LinkedList();

                if (possible.size() > 0) {
                        int i = 0;
                        int high = 0;
                        while (i < possible.size()) {
                                temp = (Stone)possible.get(i);
                                if (board.isCorner(temp)) {
                                        return temp;
                                } 
                                else {
                                	Stone temp2 = (Stone)possible.get(high);
                                	if (score[temp2.getRow()][temp2.getCol()] < score[temp.getRow()][temp.getCol()]) {
                                		high = i;
                                		highs.add(temp2);
                                		}
                                }
                                i++;
                        }
                        if (highs.size() > 1) {
                                long choice = Math.round(0 + (Math.random()*(highs.size() -1)));
                                temp = (Stone) highs.get((int)choice);
                        } 
                        else {
                                temp = (Stone) possible.get(high);
                        }
                }
                return temp;
        }

        
        private static LinkedList sortList(LinkedList orig) {
        	Stone[] temp = new Stone[orig.size()];
        	Stone tmp = null;
        	Stone tmp2 = null;
                for (int i=0;i<orig.size();i++) { temp[i] = (Stone)orig.get(i); }
                for (int pass = 1; pass < temp.length-1; pass++) {
                        for (int element =0; element < temp.length-1; element++) {
                                tmp = temp[element];
                                tmp2= temp[element+1];
                                if (score[tmp.getRow()][tmp.getCol()] < score[tmp2.getRow()][tmp2.getCol()]) { swap(temp,element,element+1); }
                        }
                }

                LinkedList temp2 = new LinkedList();
                for (int i=0;i<temp.length;i++) { temp2.add(temp[i]); }
                return temp2;
        }

        private static void swap(Stone[] list, int first, int second) {
        	Stone hold;

                hold = list[first];
                list[first] = list[second];
                list[second] = hold;
        }
}
