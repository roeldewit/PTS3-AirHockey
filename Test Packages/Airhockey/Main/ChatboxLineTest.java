/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.User.User;
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
public class ChatboxLineTest {
    
    // text of the chatboxline + By + name of the user that posted it 
    ChatboxLine halloByPiet;
    ChatboxLine somethingElseBySander;
    
    User piet;
    User sander;
    
    @Before
    public void setUp() {
        piet = new User();
        sander = new User();
        
        halloByPiet = new ChatboxLine("hallo", piet);
        somethingElseBySander = new ChatboxLine("somethingElse", sander);
    }

    /**
     * Test of getUser method, of class ChatboxLine.
     */
    @Test
    public void testGetUser() {
        assertEquals("Should be the same person", piet, halloByPiet.getUser());
        assertEquals("Should be the same person", sander, somethingElseBySander.getUser());
    }

    /**
     * Test of getText method, of class ChatboxLine.
     */
    @Test
    public void testGetText() {
        assertEquals("Should have the same text", "hallo", halloByPiet.getText());
        assertEquals("Should have the same text", "somethingElse", somethingElseBySander.getText());
    }
    
}
