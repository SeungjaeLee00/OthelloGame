package othelloapp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.util.LinkedList;
import java.net.URL;

public class BoardPanel extends JPanel {
        private Board b;
        private char userColor;
        private char compColor;
        private char currentTurn;
        private int level;
        private boolean userWent;
        private boolean noUserMove;
        private boolean gameOver;
        private boolean showMoves;

        private JTextArea debug;
        private JLabel user;
        private JLabel comp;
        private Checkbox showM;

        private Image boardTile;
        private Image blueStone;
        private Image greenStone;
        private boolean imagesLoaded;

        public BoardPanel(Board board, char userColor, char whoStarts, JTextArea debugger, JLabel userScore, JLabel compScore,Checkbox showM) {
                imagesLoaded = false;
                loadImages();

                debug = debugger;
                user  = userScore;
                comp  = compScore;
                this.showM = showM;

                b = board;
                this.userColor = userColor;
                currentTurn = whoStarts;
                addMouseListener(new PlayerMoved());

                if (userColor == Stone.BLUE) { compColor = Stone.GREEN; } 
                else { compColor = Stone.BLUE; }
                if (userColor == currentTurn) { userWent = false; } 
                else { userWent = true; }
                addMouseMotionListener(new CurrentMousePosition(this));
        }

        public void reset() {
                String colors[] = {"초록색","파란색"};
                String skill[] = {"난이도 하","난이도 상"};
                
                String username = JOptionPane.showInputDialog(null, "사용자의 이름을 설정하세요.", "jaejae");

                int userC = JOptionPane.showOptionDialog(this,"무슨 색 돌을 이용하시겠습니까?","원하는 돌 선택",
                                JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE,null,colors,colors[0]);

                if (userC == 0) {
                        userColor = Stone.GREEN;
                        compColor = Stone.BLUE;
                } 
                else {
                        userColor = Stone.BLUE;
                        compColor = Stone.GREEN;
                }

               
                int starting = JOptionPane.showOptionDialog(this,"무슨 색 돌이 먼저 움직이겠습니까?","게임 진행 차례",
                                JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE,null,colors,colors[0]);

                if (starting == 0) { currentTurn = Stone.GREEN; } 
                else { currentTurn = Stone.BLUE; }

                if (userColor == currentTurn) { userWent = false; } 
                else { userWent = true; }

                
                level = JOptionPane.showOptionDialog(this,"난이도를 선택해주세요.","난이도 설정",
                                        JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE,null,skill,skill[0]);

                b.reset();
                debug.setText("");
                gameOver = false;
                repaint();
        }

        public void endGame() {
                gameOver = true;
        }

        public void paint(Graphics g) {
          super.paint(g);
                drawBoard(g);

                if (((userColor != currentTurn && userWent) || noUserMove) && !gameOver) {
                	Stone stone;

                        debug.append("Thinking...\n");
                        if (level == 0) { stone = ComputerPlayer.getNoviceMove(b,compColor); } 
                        else { stone = ComputerPlayer.getIntMove(b,compColor); }
                        try {
                            b.place(stone.getRow(),stone.getCol(),compColor);
                                userWent = false;

                                debug.append(new String("SERVER: ["+(stone.getRow()+1)+","+(stone.getCol()+1)+"]\n"));

                                LinkedList possible = b.possibleMoves(userColor);
                                if (possible.size() == 0) {
                                        debug.append("USER has no place to put stone!\nchange the order .\n");
                                        noUserMove = true;
                                        repaint();
                                } 
                                else {
                                        noUserMove = false;
                                        currentTurn = userColor;
                                }
                        } catch (NullPointerException err) {
                                if (userWent) { debug.append("SERVER has no place to put stone.\n");}
                                LinkedList possible = b.possibleMoves(userColor);
                                if (possible.size() == 0) {
                                        debug.append("GAME OVER\n");
                                        gameOver = true;
                                        noUserMove = true;
                                        gameOver();
                                }
                        }
                }
                placestones(g);

                if (showM.getState()) { showPossibleMoves(g, userColor); }

                if (userColor == Stone.BLUE) {
                        user.setText(String.valueOf(b.blueCount()));
                        comp.setText(String.valueOf(b.greenCount()));
                } 
                else {
                        user.setText(String.valueOf(b.greenCount()));
                        comp.setText(String.valueOf(b.blueCount()));
                }
        }

        private void gameOver() {
                if (userColor == Stone.BLUE) {
                        if (b.blueCount() > b.greenCount()) { debug.append("USER wins!\n"); } 
                        else {
                                if (b.blueCount() < b.greenCount()) { debug.append("SERVER wins!\n"); } 
                                else { debug.append("You tied!\n"); }
                        }
                } 
                else {
                        if (b.blueCount() > b.greenCount()) { debug.append("SERVER wins!\n"); } 
                        else {
                                if (b.blueCount() < b.greenCount()) { debug.append("USER wins!\n"); } 
                                else { debug.append("You tied!\n"); }
                        }
                }
        }

        private void loadImages() {
          boardTile = new ImageIcon(OthelloApp.class.getResource("gray.PNG")).getImage();  // 보드 배경색
          blueStone = new ImageIcon(OthelloApp.class.getResource("blue_stone.png")).getImage(); 
          greenStone = new ImageIcon(OthelloApp.class.getResource("green_stone.png")).getImage(); 

          MediaTracker mt = new MediaTracker(this);
          mt.addImage(blueStone,0);
          mt.addImage(greenStone,0);
         
          try {mt.waitForAll(0);}
          catch (Exception err) {
            System.out.println(err);
          }

          imagesLoaded = true;
        }

        private void drawBoard(Graphics g) {
          if (imagesLoaded) {
            Insets insets = getInsets();
            int x = insets.left;
            int y = insets.top;
            for (int r = 0; r < 8; r++) {
              x = insets.left;
              for (int c = 0; c < 8; c++) {
                g.drawImage(boardTile, x, y, Color.green, null);
                x += 32;
              }
              y += 32;
            }
          }
    }


        private void placestones(Graphics g) {
          if (imagesLoaded) {
        	  Stone grid[][] = b.getGrid();
            Insets insets = getInsets();
            for (int r = 0; r < Board.MAX_ROWS; r++) {
              for (int c = 0; c < Board.MAX_COLUMNS; c++) {
                if (grid[r][c].getColor() == Stone.GREEN) {
                  g.drawImage(greenStone, insets.left + (c*32), insets.top + (r*32), null, null);
                }
                else {
                  if (grid[r][c].getColor() == Stone.BLUE) {
                    g.drawImage(blueStone, insets.left + (c * 32),
                                insets.top + (r * 32), null, null);
                  }
                }
              }
            }
          }
    }

        private void drawstone(Graphics g, int r, int c) {
                Insets insets = getInsets();
                g.drawOval(insets.left+(c*33),insets.top+(r*33),30,30);
                g.fillOval(insets.left+(c*33),insets.top+(r*33),31,31);
        }

        private void showPossibleMoves(Graphics g, char color) {
            LinkedList temp2 = b.possibleMoves(color);
            Stone temp = null;
            try {
                if (temp2.size() > 0 && temp2 != null) {
                        Insets insets = getInsets();
                        temp = null;
                        for (int i=0; i<temp2.size(); i++) {
                                temp = (Stone)temp2.get(i);
                                g.setColor(Color.yellow);
                                g.drawRect(insets.left+(temp.getCol()*32),insets.top+(temp.getRow()*32),30,30);
                        	}
                	}
            } catch (NullPointerException err) {
                        System.out.println(err);
        	}
        }

        private class PlayerMoved implements MouseListener {

                public PlayerMoved() {}

                public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!noUserMove) {
                                        Insets insets = getInsets();

                                    int y = (e.getX()-insets.left)/32;
                                    int x = (e.getY()-insets.top)/32;
                                        if (b.place(x,y,userColor)) {
                                                userWent = true;
                                                currentTurn = compColor;

                                                debug.append(new String("USER: ["+(x+1)+","+(y+1)+"]\n"));

                                                repaint();
                                        } 
                                        else { debug.append("Can't put stone!\n"); }
                                } 
                                else {
                                        if (gameOver) {
                                                debug.append("Game Over.\n");
                                                gameOver();
                                        } 
                                        else { debug.append("No more moves.\n"); }
                                }
                        }
                }

                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
        }

        private class CurrentMousePosition implements MouseMotionListener {
                private JPanel p;
                public CurrentMousePosition(JPanel panel) {
                        p = panel;
                }

                public void mouseMoved(MouseEvent e) {
                        p.repaint();
                        Graphics g = p.getGraphics();
                        Insets insets = getInsets();
                        int y = (e.getX()-insets.left)/32;
                        int x = (e.getY()-insets.top)/32;
                        g.setColor(Color.red);
                        g.drawRect(insets.left+(y*32),insets.top+(x*32),30,30);
                }
                public void mouseDragged(MouseEvent e) {}
        }
}
