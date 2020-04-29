/*
 * Assignment 4 - CSCI 282
 * PongPaddle.java
 * Damian O Boyle
 * October 24, 2018
 * 
 * PURPOSE : To Provide Structure for the Ball on the Game Board
 */

import java.awt.*;
import java.util.Random;

public class PongBall
{
    int diameter = 20;
    int xCoord, yCoord;
    int xMotion, yMotion;
    
    int width, height;
    int last_score1, last_score2;
    
    Random rand = new Random();
    
    public PongBall(int wid, int hei)
    {
        width = wid;
        height = hei;
    }
    
    public void paintComponent(Graphics g, Color colour)
    {
        g.setColor(colour);
        g.fillRect(xCoord, yCoord, diameter, diameter);
    }
    
    public void spawn(int score1, int score2)
    {
        int xStart, yStart, left, right;
        int up = -1, down = 1;
        int ballLeft = -1, ballRight = 1;
        
        int angles = 2;
        
        //Initial Co-Ordinates
        int halfer = 2;
        xCoord = width/halfer - diameter/halfer;
        yCoord = height/halfer - diameter/halfer;
 
        //Spawn Movement
        if (score1 > last_score1)
        {
            xMotion = ballLeft;          //Toward Player 1
            
            left = rand.nextInt(angles);
            
            if (left == 0)
                yMotion = up;
            else
                yMotion = down;
            
            last_score1++;
        }
        else if (score2 > last_score2)
        {
            xMotion = ballRight;          //Toward Player 2
            
            right = rand.nextInt(angles);
            
            if (right == 0)
                yMotion = up;
            else
                yMotion = down;
            
            last_score2++;
        }
        else
        {
            xStart = rand.nextInt(angles);
            yStart = rand.nextInt(angles);
            
            if (xStart == 0)
                xMotion = ballLeft;
            else
                xMotion = xStart;
            
            if (yStart == 0)
                yMotion = up;
            else
                yMotion = yStart;
        }
    }
    
    public void move()
    {
        int accelerator = 3;
        
        xCoord += xMotion * accelerator;
        yCoord += yMotion * accelerator;
    }
}
