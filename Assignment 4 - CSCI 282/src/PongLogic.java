/*
 * Assignment 4 - CSCI 282
 * PongLogic.java
 * Damian O Boyle
 * November 18, 2018
 * 
 * PURPOSE : To provide Game Logic and Movement criteria for Game Components
 */

public class PongLogic
{
    int width, height;
    int winScore = 2;
    int score1, score2;
    int p1_wins, p2_wins;
    int xPause, yPause;
    
    PongPaddle player1;
    PongPaddle player2;
    
    PongBall ball;
    
    public PongLogic (int wid, int hei)
    {
        width = wid;
        height = hei;
        
        player1 = new PongPaddle ();
        player2 = new PongPaddle (width);
        
        ball = new PongBall (width, height);
        ball.spawn(score1, score2);
    }
    
    public void limitP1()
    {
        if (player1.yCoord + player1.length > height)
            player1.yCoord = height - player1.length;
    }
    
    public void limitP2()
    {
        if (player2.yCoord < 0)
            player2.yCoord = 0;
        if (player2.yCoord + player2.length > height)
            player2.yCoord = height - player2.length;
    }

    public void wallCollision()
    {
        if (ball.yCoord <= 0)
            ball.yMotion = -ball.yMotion;                   //Bounce off Upper Wall
        if (ball.yCoord >= height - ball.diameter)
            ball.yMotion = -ball.yMotion;                   //Bounce off Lower Wall
    }
    
    public void paddleCollision()
    {
        if (ball.xCoord < player1.xCoord + player1.breath && ball.xCoord > player1.xCoord) //Axis
            if (ball.yCoord + ball.diameter > player1.yCoord && ball.yCoord < player1.yCoord + player1.length) //Position
                ball.xMotion = -ball.xMotion;
        
         if (ball.xCoord + ball.diameter > player2.xCoord && ball.xCoord + ball.diameter < player2.xCoord + player2.breath)  //Axis
            if (ball.yCoord + ball.diameter > player2.yCoord && ball.yCoord < player2.yCoord + player2.length)  //Position
                ball.xMotion = -ball.xMotion;
    } 
    
    public void score()
    {
        int delay = 100;
        
        if (ball.xCoord <= 0-delay- ball.diameter)
        {
            score2++;
            ball.spawn(score1, score2);
        }
        if (ball.xCoord >= width+delay)
        {
            score1++;
            ball.spawn(score1, score2);
        }
    }
    
    public void pause()
    {
        //Save Movement Conditions for Retstart
        xPause = ball.xMotion;
        yPause = ball.yMotion;
        
        //Freeze Ball
        ball.xMotion = 0;
        ball.yMotion = 0;
        
        //Freeze Paddles //Lock yCoord//Use Holder?//
    }
    
    public void resume()
    {
        ball.xMotion = xPause;
        ball.yMotion = yPause;
    }
    
    public int endGame()
    {
        if (score1 == winScore)
        {
            //Freeze Ball
            ball.xMotion = 0;
            ball.yMotion = 0;
            
            return 1;                   //Indicate that Player 1 has Won
        }
        else if (score2 == winScore)
        {
            //Freeze Ball
            ball.xMotion = 0;
            ball.yMotion = 0;
            
            return 2;                   //Indicate that Player 2 has Won
        }
        return 0;
    }
    
    public void newGame()
    {
        score1 = 0;
        score2 = 0;
        ball.spawn(score1, score2);
    }
    
   @Override
    public String toString()
    {
        return score1+" "+score2+" "+p1_wins+" "+p2_wins;
    }
}