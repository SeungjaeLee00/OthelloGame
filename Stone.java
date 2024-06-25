package othelloapp;


public class Stone {
        private int  row = 0;
        private int  col = 0;
        private char color = EMPTY;
        public final static char GREEN = 'G';
        public final static char BLUE = 'B';
        public final static char EMPTY = 'E';
        public Stone() {
                row   = 0;
                col   = 0;
                color = EMPTY;
        }
        public Stone(int r, int c) {
                row = r;
                col = c;
        }
        public Stone(int r, int c, char color) {
                row   = r;
                col   = c;
                this.color = color;
        }
        public int getRow() { return row; }
        public int getCol() { return col; }
        public char getColor() { return color; }
        public void setRow(int r) { row = r; }
        public void setCol(int c) { col = c; }
        public void setColor(char col) { color = col; }
        public String toString() { return new String("("+row+","+col+") - Color: "+color); }
}