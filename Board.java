package othelloapp;  // othello app package

import java.util.LinkedList;  // linklist 사용할거임

public class Board {  // board class 정의
        public static final int MAX_COLUMNS = 8;  // col 8로 고정
        public static final int MAX_ROWS = 8;  // row 8로 고정

        private Stone grid[][] = null;  // 격자
        private int greenCount = 2;  // 검은 돌 처음
        private int blueCount = 2;  // 흰 돌 처음
        private int free_Spots = 62;  // 처음 제외 남은 자리
        private LinkedList possible = null;  // linked list

        public Board() {  // 보드판
                possible = new LinkedList();  
                grid = null;  
                reset();  
                greenCount = 2;
                blueCount = 2;
                free_Spots = 62;
        }

        public int blueCount() { return blueCount; }

        public int greenCount() { return greenCount; }
      
        // 빈 자리
    	public int free_Spots() { return free_Spots; }
        
    	// 테두리
        public Stone[][] getGrid() { return grid; }

        public void setGrid(Stone[][] grid) { this.grid = grid; }

        public boolean isCorner(Stone s) {  
                boolean isCorner = false;
                if ((s.getRow() == 0 && s.getCol() == 0) || (s.getRow() == 7 && s.getCol() == 0) || (s.getRow() == 0 && s.getCol() == 7) || (s.getRow() == 7 && s.getCol() == 7)) { isCorner = true; }
                return isCorner;
        }
        
        // search
        public boolean place(int row, int col, char color) {
                boolean good = true;
                Stone temp = null;
                LinkedList fll = new LinkedList();
                int nullCount = 0;
                int i = 0;


                if (grid[row][col].getColor() != Stone.EMPTY) {
                        good = false;
                        return good;
                }

                temp = new Stone(row,col,color);

                fll = search_Horizontal(temp);
                if (fll.size() > 0) {
                        for (i = 0; i < fll.size(); i++) {
                        	reverseHorizontal(temp.getRow(),temp.getCol(),((Stone)fll.get(i)).getCol(),temp.getColor());
                        }
                } 
                else {
                	nullCount++;
                }
                
                fll = search_Vertical(temp);
                if (fll.size() > 0) {
                       	for (i = 0; i < fll.size(); i++) {
                       		reverseVertical(temp.getRow(),((Stone)fll.get(i)).getRow(),temp.getCol(),temp.getColor());
                        }
                } 
                else {
                        nullCount++;
                }

                fll = search_Diagnal(temp);
                if (fll.size() > 0) {
                        for (i = 0; i < fll.size(); i++) {
                        	reverseDiagnal(temp.getRow(),temp.getCol(),((Stone)fll.get(i)).getRow(),((Stone)fll.get(i)).getCol(),temp.getColor());
                        }
                } 
                else {
                        nullCount++;
                }

                if (nullCount == 3) {  
                        good = false;
                } 
                else {
                        if (color == Stone.BLUE) {
                        	grid[row][col].setColor(Stone.BLUE);
                        } 
                        else {
                        	grid[row][col].setColor(Stone.GREEN);
                        }
                }
                return good;
        }
        
        
        // reverse
        // 수평
        private void reverseHorizontal(int row, int startCol, int stopCol, char color) {
                if (startCol > stopCol)		 {
                        for (int c = stopCol+1; c < startCol; c++) {
                        	grid[row][c].setColor(color);
                        }
                }
                else {
                        for (int c = startCol+1; c < stopCol; c++) {
                        	grid[row][c].setColor(color);
                        }
                }
        }

        // 수직
        private void reverseVertical(int startRow,int stopRow, int col, char color) {
                if (startRow > stopRow)		 {
                        for (int r = stopRow+1; r < startRow; r++) {
                        	grid[r][col].setColor(color);
                        }
                } 
                else {
                        for (int r = startRow+1; r < stopRow; r++) {
                        	grid[r][col].setColor(color);
                        }
                }
        }

        // 대각선
        private void reverseDiagnal(int startRow, int startCol, int stopRow, int stopCol, char color) {
                while (startRow != stopRow && startCol != stopCol) {
                        grid[startRow][startCol].setColor(color);

                        if (startRow > stopRow) {
                                startRow--;
                        } 
                        else {
                                startRow++;
                        }
                        if (startCol > stopCol) {
                                startCol --;
                        } 
                        else {
                                startCol++;
                        }
                }

        }

     // 판 reset
        public void reset() {  
                blueCount = 2;
                greenCount = 2;
                free_Spots = 62;
                grid = new Stone[8][8];
                for (int r = 0;r < Board.MAX_ROWS; r++) {
                        for (int c = 0; c < Board.MAX_COLUMNS; c++) {
                                if ((r == 3 && c == 4) || (r == 4 && c == 3)) {
                                        grid[r][c] = new Stone(r,c,Stone.GREEN);
                                } 
                                else {
                                        if ((r == 3 && c == 3) || (r == 4 && c == 4) ) {
                                                grid[r][c] = new Stone(r,c,Stone.BLUE);
                                        } 
                                        else {
                                                grid[r][c] = new Stone(r,c,Stone.EMPTY);
                                        }
                                }

                        }
                }
        }
        
        
        // search
        // 수평
        public LinkedList search_Horizontal(Stone from) {
                LinkedList temp = new LinkedList();
                Stone blah = null;
                
                blah = search_Left(from);
                try {
                	if (blah != null) {
                		temp.add(blah);
                		}
                } catch (NullPointerException err) {}

                blah = null;
                blah = search_Right(from);
                try {
                	if (blah != null) {
                		temp.add(blah);
                        }
                } catch (NullPointerException err) {}
                return temp;
        }

        // 왼쪽 
        private Stone search_Left(Stone from) {
                boolean stop = false;
                int c = 0;
                int flipCount = 0;
                Stone temp = null;

                stop = false;
                if (from.getCol() > 0) {
                        if (grid[from.getRow()][from.getCol()-1].getColor() != Stone.EMPTY &&
                                grid[from.getRow()][from.getCol()-1].getColor() != from.getColor()) {
                                c = from.getCol()-1;
                                while (!stop && c >= 0) {
                                        if (grid[from.getRow()][c].getColor() != from.getColor() &&
                                                        grid[from.getRow()][c].getColor() != Stone.EMPTY) {
                                                                flipCount++;
                                        } 
                                        else {
                                                if (flipCount > 0 && grid[from.getRow()][c].getColor() == Stone.EMPTY) {
                                                        stop = true;
                                                } 
                                                else {
                                                        if (grid[from.getRow()][c].getColor() == from.getColor() &&
                                                                flipCount > 0) {
                                                                        temp = grid[from.getRow()][c];
                                                                        flipCount = 0;
                                                                        stop = true;
                                                        }
                                                }
                                        }
                                        c--;
                                }
                        }

                }
                return temp;
        }

        // 오른쪽 
        private Stone search_Right(Stone from) {
                boolean stop = false;
                int c = 0;
                int flipCount = 0;
                Stone temp = null;

                if (from.getCol() < Board.MAX_COLUMNS-1) {
                        if (grid[from.getRow()][from.getCol()+1].getColor() != Stone.EMPTY &&
                                grid[from.getRow()][from.getCol()+1].getColor() != from.getColor()) {
                                c = from.getCol()+1;
                                stop = false;
                                while (!stop && c < Board.MAX_COLUMNS) {
                                        if (grid[from.getRow()][c].getColor() != from.getColor() &&
                                                        grid[from.getRow()][c].getColor() != Stone.EMPTY) {
                                                                flipCount++;
                                        } 
                                        else {
                                                if (flipCount > 0 && grid[from.getRow()][c].getColor() == Stone.EMPTY) {
                                                        stop = true;
                                                } 
                                                else {
                                                        if (grid[from.getRow()][c].getColor() == from.getColor() &&
                                                                flipCount > 0) {
                                                                        temp = grid[from.getRow()][c];
                                                                        flipCount = 0;
                                                                        stop = true;
                                                        }
                                                }
                                        }
                                        c++;
                                }
                        }
                }
                return temp;
        }

        // 수직
        public LinkedList search_Vertical(Stone from) {

                LinkedList temp = new LinkedList();
                Stone blah = null;
                boolean stop = false;
                int r = 0;
                int flipCount = 0;

                flipCount = 0;
                blah = search_Up(from);
                try {
                        if (blah != null) {
                                temp.add(blah);
                        }
                } catch (NullPointerException err) {}


                blah = null;
                blah = search_Down(from);
                try {
                        if (blah != null) {
                                temp.add(blah);
                        }
                } catch (NullPointerException err) {}
                return temp;
        }

        // 위로
        private Stone search_Up(Stone from) {
        		Stone temp = null;
                boolean stop = false;
                int r = 0;
                int flipCount = 0;

                if (from.getRow() > 0) {
                        if (grid[from.getRow()-1][from.getCol()].getColor() != Stone.EMPTY && grid[from.getRow()-1][from.getCol()].getColor() != from.getColor()) {
                                r = from.getRow()-1;
                                while (!stop && r >= 0) {
                                        if (grid[r][from.getCol()].getColor() != from.getColor() && grid[r][from.getCol()].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        	} 
                                        else {
                                        	if (flipCount > 0 && grid[r][from.getCol()].getColor() == Stone.EMPTY) {
                                        		stop = true;
                                        		}
                                        	else {
                                        		if (grid[r][from.getCol()].getColor() == from.getColor() && flipCount > 0) {
                                        			temp = grid[r][from.getCol()];
                                        			flipCount = 0;
                                        			stop = true;
                                        			}

                                        	}

                                        }
                                        r--;
                                        }
                                }
                        }
                return temp;
        }

        // 아래로
        private Stone search_Down(Stone from) {
	    		Stone temp = null;
	            boolean stop = false;
	            int r = 0;
	            int flipCount = 0;

                if (from.getRow() < Board.MAX_ROWS-1) {
                        if (grid[from.getRow()+1][from.getCol()].getColor() != Stone.EMPTY && grid[from.getRow()+1][from.getCol()].getColor() != from.getColor()) {
                                r = from.getRow()+1;
                                while (!stop && r < Board.MAX_ROWS) {
                                        if (grid[r][from.getCol()].getColor() != from.getColor() && grid[r][from.getCol()].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        }
                                        else {
                                        	if (flipCount > 0 && grid[r][from.getCol()].getColor() == Stone.EMPTY) {
                                        		stop = true;
                                        		} 
                                        	else {
                                        		if (grid[r][from.getCol()].getColor() == from.getColor() && flipCount > 0) {
                                        			temp = grid[r][from.getCol()];
                                                    flipCount = 0;
                                                    stop = true;
                                                    }
                                        		}
                                        }
                                        r++;
                                }
                        }
                }

                return temp;
        }

        // 대각선
        public LinkedList search_Diagnal(Stone from) {
                LinkedList temp = new LinkedList();
                int r = from.getRow();
                int c = from.getCol();
                boolean stop = false;
                int flipCount = 0;

                if (from.getRow() < Board.MAX_ROWS-1 && from.getCol() < Board.MAX_COLUMNS-1) {
                        if (grid[from.getRow()+1][from.getCol()+1].getColor() != Stone.EMPTY &&
                                grid[from.getRow()+1][from.getCol()+1].getColor() != from.getColor()) {
                                stop = false;
                                c = from.getCol()+1;
                                r = from.getRow()+1;

                                while (!stop && r < Board.MAX_ROWS && c < Board.MAX_COLUMNS) {
                                        if (grid[r][c].getColor() != from.getColor() && grid[r][c].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        } 
                                        else {
                                                if (flipCount > 0 && grid[r][c].getColor() == Stone.EMPTY) {
                                                	stop = true;
                                                } 
                                                else {
                                                        if (grid[r][c].getColor() == from.getColor() && flipCount > 0) {
                                                        	temp.add(grid[r][c]);
                                                            flipCount = 0;
                                                            stop = true;
                                                            }
                                                        }
                                                }
                                        r++;
                                        c++;
                                }
                        }
                }
                if (from.getRow() < Board.MAX_ROWS-1 && from.getCol() > 0) {
                        if (grid[from.getRow()+1][from.getCol()-1].getColor() != Stone.EMPTY && grid[from.getRow()+1][from.getCol()-1].getColor() != from.getColor()) {
                                flipCount = 0;
                                stop = false;
                                c = from.getCol()-1;
                                r = from.getRow()+1;

                                while (!stop && c >= 0 && r < Board.MAX_ROWS) {
                                        if (grid[r][c].getColor() != from.getColor() && grid[r][c].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        } 
                                        else {
                                        	if (flipCount > 0 && grid[r][c].getColor() == Stone.EMPTY) {
                                        		stop = true;
                                        		} else {
                                                        if (grid[r][c].getColor() == from.getColor() && flipCount > 0) {
                                                        	temp.add(grid[r][c]);
                                                            flipCount = 0;
                                                            stop = true;
                                                            }
                                                        }
                                        	}
                                        r++;
                                        c--;
                                }
                        }
                }
                if (from.getRow() > 0 && from.getCol() < Board.MAX_COLUMNS-1) {
                        if (grid[from.getRow()-1][from.getCol()+1].getColor() != Stone.EMPTY && grid[from.getRow()-1][from.getCol()+1].getColor() != from.getColor()) {
                                flipCount = 0;
                                stop = false;
                                c = from.getCol()+1;
                                r = from.getRow()-1;

                                while (!stop && c < Board.MAX_COLUMNS && r >= 0) {
                                        if (grid[r][c].getColor() != from.getColor() && grid[r][c].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        } 
                                        else {
                                        	if (flipCount > 0 && grid[r][c].getColor() == Stone.EMPTY) {
                                        		stop = true;
                                                } 
                                        	else {
                                        		if (grid[r][c].getColor() == from.getColor() && flipCount > 0) {
                                        			temp.add(grid[r][c]);
                                                    flipCount = 0;
                                                    stop = true;
                                                    }
                                        		}
                                        }
                                        r--;
                                        c++;
                                }
                        }
                }
                if (from.getRow() > 0 && from.getCol() > 0) {
                        if (grid[from.getRow()-1][from.getCol()-1].getColor() != Stone.EMPTY && grid[from.getRow()-1][from.getCol()-1].getColor() != from.getColor()) {
                                flipCount = 0;
                                stop = false;
                                c = from.getCol()-1;
                                r = from.getRow()-1;

                                while (!stop && c >= 0 && r >= 0) {
                                        if (grid[r][c].getColor() != from.getColor() && grid[r][c].getColor() != Stone.EMPTY) {
                                        	flipCount++;
                                        } 
                                        else {
                                        	if (flipCount > 0 && grid[r][c].getColor() == Stone.EMPTY) {
                                        		stop = true;
                                                } 
                                        	else {
                                        		if (grid[r][c].getColor() == from.getColor() && flipCount > 0) {
                                        			temp.add(grid[r][c]);
                                                    flipCount = 0;
                                                    stop = true;
                                                    }
                                        		}
                                        }
                                        r--;
                                        c--;
                                }
                        }
                }
                return temp;
        }

        public String toString() {
                StringBuffer buffer = new StringBuffer();

                final String top        = new String("   0 1 2 3 4 5 6 7 \n");
                final String topNbottom = new String("  -----------------\n");

                buffer.append(top);
                buffer.append(topNbottom);

                for (int r = 0; r < Board.MAX_ROWS; r++) {
                        buffer.append(r+" |");
                        for (int c = 0; c < Board.MAX_COLUMNS; c++) {
                                if (grid[r][c].getColor() != Stone.EMPTY) {
                                        buffer.append(grid[r][c].getColor());
                                } 
                                else {
                                	buffer.append('*');
                                }
                                buffer.append("|");
                        }
                        buffer.append("\n");
                }
                buffer.append(topNbottom+"\n");
                buffer.append("Score\n---------------\n");

                possible = possibleMoves(Stone.BLUE);
                try {
                        if (possible != null && possible.size() > 0) {
                                for (int i =0; i < possible.size(); i++) {
                                }
                        }
                } catch (NullPointerException err) {}
                possible = null;
                possible = possibleMoves(Stone.GREEN);
                try {
                        if (possible != null && possible.size() > 0) {
                                for (int i =0; i < possible.size(); i++) {
                                }
                        }
                } catch (NullPointerException err) {}

                buffer.append("Green - "+greenCount+"\n");
                buffer.append("Blue - "+blueCount+"\n");
                return buffer.toString();
        }

        public void figureScore() {
                greenCount = 0;
                blueCount = 0;
                free_Spots = 0;
                for (int r=0; r < Board.MAX_ROWS; r++) {
                        for (int c=0; c < Board.MAX_COLUMNS; c++) {
                                if (grid[r][c].getColor() == Stone.GREEN) {
                                	greenCount++;
                                } 
                                else {
                                	if (grid[r][c].getColor() == Stone.BLUE) {
                                		blueCount++;
                                        } 
                                	else {
                                		free_Spots++;
                                		}
                                }
                        }
                }
        }

        // 가능한 움직임
        public LinkedList possibleMoves(char color) {
                LinkedList temp = new LinkedList();
                LinkedList temp2 = new LinkedList();
                Stone blah = null;

                greenCount = 0;
                blueCount = 0;
                free_Spots = 0;
                for (int r=0; r < Board.MAX_ROWS; r++) {
                        for (int c=0; c < Board.MAX_COLUMNS; c++) {
                                if (grid[r][c].getColor() == Stone.EMPTY) {
                                        temp2 = search_Horizontal(new Stone(r,c,color));
                                        try {
                                                if (temp2 != null && temp2.size() > 0) {
                                                        temp.add(grid[r][c]);
                                                }
                                        } catch (NullPointerException err) {}

                                        temp2 = null;
                                        temp2 = search_Vertical(new Stone(r,c,color));
                                        try {
                                                if (temp2 != null && temp2.size() > 0) {
                                                        temp.add(grid[r][c]);
                                                }
                                        } catch (NullPointerException err) {}

                                        temp2 = null;
                                        temp2 = search_Diagnal(new Stone(r,c,color));
                                        try {
                                                if (temp2 != null && temp2.size() > 0) {
                                                        temp.add(grid[r][c]);
                                                }
                                        } catch (NullPointerException err) {}
                                } else {
                                        if (grid[r][c].getColor() == Stone.GREEN) {
                                        	greenCount++;
                                        } 
                                        else {
                                        	blueCount++;
                                        }
                                }
                        }
                }
                return temp;
        }
}

