/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.User;

import Airhockey.Elements.Bat;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pieper126
 */
public class PlayerTest {

    // bat + id + Sc + score 
    Player red1Sc20;
    Player green2Sc19;
    Player blue3Sc21;

    Bat red;
    Bat green;
    Bat blue;

    @Before
    public void setUp() {
        red1Sc20 = new Player(1);
        green2Sc19 = new Player(2);
        blue3Sc21 = new Player(3);

        red1Sc20.setScore(20);
        green2Sc19.setScore(19);
        blue3Sc21.setScore(21);

        red = new Bat(1, 0, 0, Color.RED);
        green = new Bat(2, 0, 0, Color.GREEN);
        blue = new Bat(3, 0, 0, Color.BLUE);

        red1Sc20.setBat(red);
        green2Sc19.setBat(green);
        blue3Sc21.setBat(blue);
    }

    /**
     * Test of upScore method, of class Player.
     */
    @Test
    public void testUpScore() {
        red1Sc20.upScore();
        green2Sc19.upScore();
        blue3Sc21.upScore();

        assertEquals("score should be increased by one", 21, red1Sc20.getScore());
        assertEquals("score should be increased by one", 20, green2Sc19.getScore());
        assertEquals("score should be increased by one", 22, blue3Sc21.getScore());
    }

    /**
     * Test of downScore method, of class Player.
     */
    @Test
    public void testDownScore() {
        red1Sc20.downScore();
        green2Sc19.downScore();
        blue3Sc21.downScore();

        assertEquals("score should be lowered by one", 19, red1Sc20.getScore());
        assertEquals("score should be lowered by one", 18, green2Sc19.getScore());
        assertEquals("score should be lowered by one", 20, blue3Sc21.getScore());
    }

    /**
     * Test of getBat method, of class Player.
     */
    @Test
    public void testGetBat() {
        assertEquals("right bat should be returned", red, red1Sc20.getBat());
        assertEquals("right bat should be returned", blue, blue3Sc21.getBat());
        assertEquals("right bat should be returned", green, green2Sc19.getBat());
    }

    /**
     * Test of getScore method, of class Player.
     */
    @Test
    public void testGetScore() {
        assertEquals("score should be returned", 20, red1Sc20.getScore());
        assertEquals("score should be returned", 19, green2Sc19.getScore());
        assertEquals("score should be returned", 21, blue3Sc21.getScore());
    }

    /**
     * Test of getId method, of class Player.
     */
    @Test
    public void testGetId() {
        assertEquals("id should be returned", 1, red1Sc20.getId());
        assertEquals("id should be returned", 2, green2Sc19.getId());
        assertEquals("id should be returned", 3, blue3Sc21.getId());
    }

}
