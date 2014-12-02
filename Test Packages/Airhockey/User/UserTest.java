package Airhockey.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Airhockey.User.User;

/**
 *
 * @author Sam
 */
public class UserTest {
    
    // username Jan and rating 10
    private User jan10;
    
    // username Peter and rating 20
    private User peter20;
    
    // etc.
    private User piet23;
    
    double rating10;
    
    @Before
    public void setUp() {
        jan10 = new User();
        peter20 = new User();
        piet23 = new User();
        
        jan10.setUsername("Jan");
        peter20.setUsername("Peter");
        piet23.setUsername("Piet");
        
        rating10 = 10.0;
        
        jan10.setRating(rating10);
        peter20.setRating(20.0);
        piet23.setRating(23.0);
    }
    
    @Test
    public void userTesting(){
        
        // testing getUsername
        assertEquals("Jan", jan10.getUsername());
        assertEquals("Peter", peter20.getUsername());
        assertEquals("Piet", piet23.getUsername());
        
        // testing getrating
        // assertTrue is being used because asserEquals doesn't give the right answer
        assertTrue(10.0 == jan10.getRating());
        assertTrue(20.0 == peter20.getRating());
        assertTrue(23.0 == piet23.getRating());   
    }
}
