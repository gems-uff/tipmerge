/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.dao;

import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.model.Repository;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author j2cf
 */
public class RepositoryDaoTest {
	
	public RepositoryDaoTest() {
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

	@Test
	public void testGetRepository() {
		System.out.println("getRepository");
		File path = new File("/Users/j2cf/Desktop/sapos/");
		RepositoryDao instance = new RepositoryDao(path);
		Repository expResult = null;
		Repository result = instance.getRepository();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}
	
	@Test
	public void testGetRepositoryPartial(){
		System.out.println("getRepository");
		File path = new File("/Users/j2cf/Desktop/sapos/");
		RepositoryDao instance = new RepositoryDao(path);
		Repository repo = instance.getRepository();
	}
	
}
