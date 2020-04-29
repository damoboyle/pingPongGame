/*
 * Assignment 4 - CSCI 282
 * PongDisplay.java
 * Damian O Boyle
 * November 19, 2018
 * 
 * PURPOSE : To create and display the Game Board and its Key Game Components
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PongDisplay extends JPanel implements ActionListener
{
    PongLogic game;
    
    Color background = Color.black;
    Color components = Color.white;
    
    int width, height;
    int indent = 25;
    
    Timer timer = new Timer (10, this);
    
    public PongDisplay (int wid, int hei)
    {
        width = wid; height = hei;
        game = new PongLogic(width, height);
        
        //Mouse Listener for Player 1
        this.addMouseMotionListener( new MouseAdapter(){
        @Override
	public void mouseMoved(MouseEvent me)
	{
            mouseMove(me);                              //Move Paddle with Mouse
	}
	});
        
        //Key Lisyener for Player 2
        this.addKeyListener(new KeyListener(){
        @Override
        public void keyPressed(KeyEvent ke)
        {
            keyPress(ke);                               //Move Paddle with Keys
        }
        public void keyReleased(KeyEvent ke){}
        public void keyTyped(KeyEvent ke){}
        });
        
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }
    
    public void mouseMove(MouseEvent me)
    {
        int position = me.getY();
        
        game.player1.moveP1(position);
        game.limitP1();
        repaint();
    }
    
    public void keyPress(KeyEvent ke)
    {
        int key = ke.getKeyCode();
        
        if (key == KeyEvent.VK_UP)
            game.player2.moveP2(true);  //up is true;
        if (key == KeyEvent.VK_DOWN)
            game.player2.moveP2(false); //down is true;
        
        game.limitP2();
        repaint();
    }
    
    @Override
    public void actionPerformed (ActionEvent ae)
    {
        //Game Animation
        game.ball.move();
        game.wallCollision();
        game.paddleCollision();
        game.score();
        repaint();
        
        //Win Declaration
        if (game.endGame() != 0)
            winner();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        int divider = 4, multiplier = 3, halfer = 2;
        
	super.paintComponent(g);
        g.setColor(background);
        g.fillRect(0, 0, width, height);                                        //Background
        
        int lineSize = 2;
        g.setColor(components);
        g.fillRect(width/halfer, 0, lineSize, height);                          //Half Court Line
        
        int indicatorSize = 20;
        g.setFont(new Font("Ariel", Font.BOLD, indicatorSize));
        int player_indent = 30;
        g.drawString("P1", width/halfer - player_indent - indent, indent);      //Player 1 Indicator
        g.drawString("P2", width/halfer + player_indent, indent);               //Player 2 Indicator
        
        int scoreSize = 50;
        g.setFont(new Font("Ariel", Font.BOLD, scoreSize));
        int score_indent = 50;
        g.drawString(String.valueOf(game.score1), width/divider - indent, score_indent);              //Player 1 Score
        g.drawString(String.valueOf(game.score2), width/divider*multiplier - indent, score_indent);   //Player 2 Score
        
        game.player1.paintComponent(g, components);                             //Player 1 Paddle
        game.player2.paintComponent(g, components);                             //Player 2 Paddle
        
        timer.start();                                                          //Autonomous Animation
        game.ball.paintComponent(g, components);                                //Ball
    }
    
    public void winner()
    {
        String winner = "";
        
        if (game.endGame() == 1)                                                //Player 1 Won
        {
            winner = "Player 1 Won this Game!\n";
            game.p1_wins++;
        }
        else if (game.endGame() == 2)                                           //Player 2 Won
        {
            winner = "Player 2 Won this Game!\n";
            game.p2_wins++;
        }
        
        String totals = "\nPlayer 1 Wins: "+game.p1_wins
                      + "\nPlayer 2 Wins: "+game.p2_wins;
        winner += totals;
        
        JOptionPane.showMessageDialog(null, winner, "Winner", 1);
        
        String[] options = {"Yes", "Save Highscore and Quit"};
        int position =  JOptionPane.showOptionDialog(null, "Would you like another Game",
                          "Continue Playing?",
                          JOptionPane.YES_NO_OPTION,
                          JOptionPane.QUESTION_MESSAGE,
                          null, options, options[0]);
        
        if (position == 0)            //Contine
        {
            game.newGame();
        }
        else if (position == 1)      //Save and Quit
        {
            int winScore;
            String message1 = "Enter a Name";
            if (game.p1_wins > game.p2_wins)
            {
                message1 = "Player 1: "+message1;
                winScore = game.p1_wins;
            }
            else
            {
                message1 = "Player 2: "+message1;
                winScore = game.p2_wins;
            }
            
            String name = JOptionPane.showInputDialog(null, message1, "Enter Your Name", 1);
            
            File fileConnection = new File("highscores.txt");
            try
            {
                FileWriter outWriter = new FileWriter(fileConnection, true);

                BufferedWriter appender = new BufferedWriter(outWriter);
                appender.write("\n"+name+" "+winScore);
                
                appender.close();
                outWriter.close();
            }
            catch(IOException ioe)
            {
                System.err.print("\nError saving Highscore to file!");
            }
            
            String message2 = "Highscore Saved,\nClosing Game";
            JOptionPane.showMessageDialog(null, message2, "Game Over!", 0);
            
            System.exit(0);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Closing Game", "Game Over!", 0);
            System.exit(0);
        }
    }
}