/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

import br.uff.ic.gems.tipmerge.model.Committer;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Catarina
 */
public class StatisticsTest {
	
	public StatisticsTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}

	@Test
	public void testGetMedian_IntegerArr() {
		System.out.println("getMedian");
		Integer[] values = null;
		double expResult = 0.0;
		double result = Statistics.getMedian(values);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMedian_doubleArr() {
		System.out.println("getMedian");
		double[] values = null;
		double expResult = 0.0;
		double result = Statistics.getMedian(values);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMAD() {
		System.out.println("getMad");
		List<Integer> values = Arrays.asList(2, 5, -1, 3, 4, 5, 0, 2);
		Statistics instance = new Statistics();
		Double result = Statistics.getMAD(values);
		Double valorEsperado = 2.0;
		
		assertEquals(valorEsperado, result);
	}

	@Test
	public void testGetMZScore() {
		System.out.println("getMZScore");
		List<Integer> values = Arrays.asList(-1, 0, 2,2,3,4,5,5);
		List<Double> expResult = Arrays.asList(-1.180,-0.843, -0.169, -0.169, 0.169, 0.506, 0.843, 0.843);
		List<Double> result = Statistics.getMZScore(values);
		assertEquals(expResult, result);
	}

	@Test
	public void testGetMZScoreCommitter() {
		System.out.println("getMZScoreCommitter");
		List<Committer> committer = null;
		List<Committer> expResult = null;
		List<Committer> result = Statistics.getMZScoreCommitter(committer);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}
	
}
