/*
 * Assignment 4 - CSCI 282
 * PongWindow.java - Executable
 * Damian O Boyle
 * November 19, 2018
 * 
 * PURPOSE : To Display the Game Board and its Components fully operational within a Window
 *              Provide a place to implement JMenu Components
 */

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;

public class PongWindow extends JFrame implements ActionListener, MenuListener
{
	private int width  = 1100;
	private int height = 600;

        PongDisplay gameDisplay;
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1, menu2, menu3;
        JMenuItem save, load, view, clear;
        
        String[] high_names;
        int[] high_scores;
        int scores;
        
        public PongWindow ()
        {
            int lowerBuffer = 52;       //39 + menu (23)
            int lateralBuffer = 6;
            
            this.setTitle                   ("Pong");
	    this.setSize                    (width+lateralBuffer, height+lowerBuffer);  //JFrame is improperly sized
	    this.setDefaultCloseOperation   (JFrame.EXIT_ON_CLOSE);
	    
            gameDisplay = new PongDisplay(width, height);
            
            this.add(gameDisplay);
            this.setJMenuBar(menuBar);
            menuBar();
            
            this.setVisible(true);
            this.setResizable(false);
        }
        
        public void menuBar ()
        {
            menu1 = new JMenu("New Game");
            menu2 = new JMenu("Save Games");
            menu3 = new JMenu("High Scores");

            save = new JMenuItem("Save Game");
            load = new JMenuItem("Load Game");
            view = new JMenuItem("View Leaderboard");
            clear = new JMenuItem("Clear Leaderboard");

            menu1.addMenuListener(this);
            save.addActionListener(this);
            load.addActionListener(this);
            view.addActionListener(this);
            clear.addActionListener(this);

            menu2.add(save);
            menu2.add(load);
            menu3.add(view);
            menu3.add(clear);

            menuBar.add(menu1);
            menuBar.add(menu2);
            menuBar.add(menu3);
        }
        
        @Override
        public void actionPerformed (ActionEvent ae)
        {

            //MenuBar Item Operations
            if (ae.getSource().equals(save))    //Save Game to File
            {
                gameDisplay.game.pause();

                String message = "Please Enter a Name for the Save Game File";
                String name = JOptionPane.showInputDialog(null, message, "Choose a File Name", 1);
                saveGame(name+".txt");
            }
            if (ae.getSource().equals(load))    //Load Game from File
            {
                gameDisplay.game.pause();

                String message = "Please Enter the Name of the Save Game File\n"
                               + "you wish to Load";
                String name = JOptionPane.showInputDialog(null, message, "Choose a File", 1);
                loadGame(name+".txt");

                gameDisplay.game.ball.spawn(gameDisplay.game.score1, gameDisplay.game.score2);
            }
            if (ae.getSource().equals(view))    //View High Scores
            {
                gameDisplay.game.pause();
                
                loadScores();
                String highscores = "";
                for (int index = 0; index < scores; index++)  
                    highscores += (index+1)+" "+high_names[index]+" "+high_scores[index]+"\n";
                
                JOptionPane.showMessageDialog(null, highscores, "High Scores", 1);
                
                gameDisplay.game.resume();
            }
            if (ae.getSource().equals(clear))   //Clear High Score List
            {
                gameDisplay.game.pause();
                
                try
                {
                    PrintWriter writer = new PrintWriter("highscores.txt");
                    writer.print("");
                    writer.close();

                    String message = "The Highscore List has been cleared!";
                    JOptionPane.showMessageDialog(null, message, "Success", 1);
                }
                catch (Exception e)
                {
                    String message = "Error Clearing Highscores\n"
                                   + "Returning to previous Game";
                    JOptionPane.showMessageDialog(null, message, "Deletion Error", 0);
                }
                
                gameDisplay.game.resume();
            }
        }

        public void saveGame(String fName)
        {
            File fileConnection = new File (fName);
            try
            {
                FileWriter outWriter = new FileWriter(fileConnection);

                outWriter.write(gameDisplay.game.toString());
                outWriter.close();
            }
            catch(IOException ioe)
            {
                String message = "Error Saving Game\n"
                               + "Closing Game";
                JOptionPane.showMessageDialog(null, message, "Save Error", 0);
                System.exit(0);
            }
            
            String message = "Game has been Saved\n"
                           + "Closing Game";
            JOptionPane.showMessageDialog(null, message, "Game Saved", 0);
            System.exit(0);
        }

        public void loadGame(String fName)
        {
            File fileConnection = new File(fName);
            try
            {
                Scanner inScan = new Scanner(fileConnection);
                gameDisplay.game.score1 = inScan.nextInt();
                gameDisplay.game.score2 = inScan.nextInt();
                gameDisplay.game.p1_wins = inScan.nextInt();
                gameDisplay.game.p2_wins = inScan.nextInt();
            }
            catch(Exception e){}
        }
        
        public void loadScores()
        {
            File fileConnection = new File("highscores.txt");
            int max = 10;
            scores = 0;
       
            try
            {
                Scanner inScan = new Scanner(fileConnection);
                while(inScan.hasNext())
                {
                    String line = inScan.nextLine();                            //Iterate through Lines
                    scores++;                                                   //Count Highscores
                }
                
                inScan.close();                                                 //Close File
            }
            catch (IOException ioe){}
            
            if (scores > max)
            {
                //Display 10 Highscores
                scores = max;
                
                high_names = new String[max];
                high_scores = new int[max];
            }
            else
            {
                //Display All Highscores Available
                high_names = new String[scores];
                high_scores = new int[scores];
            }
            
            int index = 0;
            try
            {
                Scanner inScan = new Scanner(fileConnection);
                while (inScan.hasNext())
                {
                    String player = inScan.nextLine();
                    Scanner token = new Scanner(player).useDelimiter(" ");
                    
                    String name = token.next();
                    int score = token.nextInt();
                    
                    high_names[index] = name;
                    high_scores[index] = score;
                    
                    index++;
                }
            }
            catch(Exception e)
            {
                String message = "Error Retrieving Highscores\n"
                               + "Returning to previous Game";
                JOptionPane.showMessageDialog(null, message, "Load Error", 0);
            }
            
            //Sort Highscores (Bubble Sort)
            for(int iterate = 0; iterate < scores; iterate++)
                for(int dex = 0; dex < scores-1; dex++)
                {
                    int hold_score;
                    String hold_name;

                    if(high_scores[dex] < high_scores[dex+1])
                    {
                        //Sort Score
                        hold_score = high_scores[dex];
                        high_scores[dex] = high_scores[dex+1];
                        high_scores[dex+1] = hold_score;
                        
                        //Sort Name
                        hold_name = high_names[dex];
                        high_names[dex] = high_names[dex+1];
                        high_names[dex+1] = hold_name;
                    }
                }
        }

        @Override
        public void menuSelected(MenuEvent me)
        {
            if (me.getSource().equals(menu1))   //Focus Changes from Gameboard to MenuBar//
            {
                gameDisplay.game.pause();
                
                String message = "Starting a New Game\n"
                               + "Previous Highscores will be lost!";
                JOptionPane.showMessageDialog(null, message, "New Game", 1);
                
                gameDisplay.game.p1_wins = 0;
                gameDisplay.game.p2_wins = 0;
                gameDisplay.game.newGame();
            }
        }
        public void menuDeselected(MenuEvent me){}          //Not Needed
        public void menuCanceled(MenuEvent me){}            //Not Needed
        
        public static void main (String[] args)
        {
            PongWindow pong = new PongWindow();
        }
}