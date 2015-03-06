/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author j2cf, Catarina
 */
public class EditedFilesDaoTest {
    
    public EditedFilesDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFilesList method, of class EditedFilesDao.
     */
    @Test
    public void testGetFilesList() {
        System.out.println("getFilesList");
        String base = "0d91b8aba8236053b0961f53ec7fad2eb525b993";
        String parent = "b3182e83cd910c7b1a8266c757afe71e9cbd8065";
        File path = new File("C:/Workspace-Netbeans/Caminho");
        EditedFilesDao instance = new EditedFilesDao();
        List<String> expResult = null;
        List<String> result = instance.getFilesList(base, parent, path);
        assertEquals(expResult, result);
        System.out.println(result.toString());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
