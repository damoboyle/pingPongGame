/*
 * Assignment 4 - CSCI 282
 * PongPaddle.java
 * Damian O Boyle
 * October 23, 2018
 * 
 * PURPOSE : To Provide Structure for the Player Paddles on the Game Board
 */

import java.awt.*;

public class PongPaddle
{
    int breath = 15, length = 75;           //Paddle Dimensions
    int indent = 75;                        //Indentation Value
    int xCoord, yCoord = 0;                 //Paddle Position Values
    
    //Player 1 Constructor
    public PongPaddle ()
    {
        xCoord = indent;
    }
    
    //Player 2 Constructor
    public PongPaddle (int width)
    {
        xCoord = width-indent-breath; 
    }
    
     public void paintComponent(Graphics g, Color colour)
    {
        g.setColor(colour);
        g.fillRect(xCoord, yCoord, breath, length);
    }
     
    public void moveP1 (int position)
    {
            yCoord = position;                      //Move Paddle
    }
    
    public void moveP2 (boolean up)
    {
        int speed = 15;
        
	if (up)                                     //Move Up
            yCoord -= speed;
	else                                        //Move Down
            yCoord += speed;
    }
}