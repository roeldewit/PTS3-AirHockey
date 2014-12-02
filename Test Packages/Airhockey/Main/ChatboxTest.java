/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.User.User;
import java.util.ArrayList;
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
public class ChatboxTest {

    Chatbox chatbox;

    // text of the chatboxline + By + name of the user that posted it 
    ChatboxLine halloByPiet;
    ChatboxLine somethingElseBySander;

    @Before
    public void setUp() {
        chatbox = new Chatbox();

        User piet = new User();
        User sander = new User();

        halloByPiet = new ChatboxLine("hallo", piet);
        somethingElseBySander = new ChatboxLine("somethingElse", sander);
    }

    /**
     * Test of writeLine method, of class Chatbox.
     */
    @Test
    public void testWriteLine() {
        chatbox.writeLine(halloByPiet);
        chatbox.writeLine(somethingElseBySander);
        
        ChatboxLine lines[] = new ChatboxLine[] { halloByPiet, somethingElseBySander};
        
        assertArrayEquals(lines, chatbox.getLines().toArray());
    }
}
