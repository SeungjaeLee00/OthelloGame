package othelloapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

class OthelloApp extends JFrame {
        private Board b;
        private BoardPanel bp;
        private Container container;
        private SpringLayout layout;
        private char userColor, whoStarts;

        private JTextArea debug = null;
        private JScrollPane debugScroll = null;

        private JLabel user = null;
        private JLabel userScore = null;
        private JLabel computer = null;
        private JLabel computerScore = null;
        private JButton newgame = null;
        private JButton realgame = null;
        private JButton exit = null;
        private Checkbox showMoves = null;


        public OthelloApp() {

                debug = new JTextArea();
                debug.setEditable(false);
                debug.setFont(new Font("Arial",Font.PLAIN,10));
                debugScroll = new JScrollPane(debug);	
                debugScroll.setWheelScrollingEnabled(true);
                debugScroll.setAutoscrolls(true);
                user = new JLabel("USER");
                computer = new JLabel("SERVER");
                userScore = new JLabel("2");
                computerScore = new JLabel("2");

                showMoves = new Checkbox("Hint!");
                showMoves.setFont(new Font("Arial",Font.BOLD,12));

                b = new Board();
                bp = new BoardPanel(b,userColor,whoStarts,debug,userScore,computerScore,showMoves);

                newGame();

                realgame = new JButton("REAL GAME");
                exit    = new JButton("EXIT");
                exit.addActionListener(
                        new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                        System.exit(0);
                                }
                        }
                );

                showMoves.addItemListener(new ShowMovesHandler(bp));

                container = getContentPane();
                layout = new SpringLayout();
                container.setLayout(layout);

                addComponent(bp,5,5,33*8,33*8);
                addComponent(user,33*9,5,80,20);
                addComponent(userScore,33*9+10,20,30,20);
                addComponent(computer,33*9-10,60,80,20);
                addComponent(computerScore,33*9+10,75,30,20);
                addComponent(debugScroll,5,33*8+10,33*8,60);
                addComponent(showMoves,33*8+10,33*4,100,20);
                addComponent(realgame,33*8+10,33*5,100,30);
                addComponent(exit,33*8+10,33*6,100,30);

                JLabel name = new JLabel("naromi&jaejae");
                name.setFont(new Font("Arial",Font.BOLD,10));
                addComponent(name,33*9-10,33*9,100,20);


                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setTitle("Othello");
                setResizable(false);
                setSize(390,370);

                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension screen = tk.getScreenSize();
                int sX = (int)(screen.getWidth()-getWidth())/2;
                int sY = (int)(screen.getHeight()-getHeight())/2;
                setLocation(sX,sY);
                setVisible(true);
        }

        private void addComponent(Component component, int x, int y, int width, int height) {
                container.add(component);
                SpringLayout.Constraints compCon = layout.getConstraints(component);
                compCon.setX(Spring.constant(x));
                compCon.setY(Spring.constant(y));
                compCon.setWidth(Spring.constant(width));
                compCon.setHeight(Spring.constant(height));
        }

        private void newGame() {
                bp.endGame();
                bp.reset();
        }

        public static void main(String args[]) {
                JFrame.setDefaultLookAndFeelDecorated(true);
                OthelloApp mainFrame = new OthelloApp();                	
        }
        
        private class ShowMovesHandler implements ItemListener {
                 private JPanel f;
                 public ShowMovesHandler(JPanel frame) {
                         f = frame;
                 }
                 public void itemStateChanged(ItemEvent e) {
                         f.repaint();
                 }
        }
}
