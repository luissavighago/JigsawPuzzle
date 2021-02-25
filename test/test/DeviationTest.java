package test;

import static org.junit.Assert.fail;

import org.junit.Test;

import puzzle.edge.ModernEdgeProducer;

public class DeviationTest {
	
	@Test
	public void testDeviation() {
		for (int i = 0; i<100; i++) {
			int val = ModernEdgeProducer.randomlyGenerateDeviation(100, 20);
			if ((val < 80) || (val > 120)) {
				fail("not working: " + val);
			}
		}
		
		int val = ModernEdgeProducer.randomlyGenerateDeviation(10, 20);
		if ((val < 8) || (val > 12)) {
			fail("not working: " + val);
		}
		
		val = ModernEdgeProducer.randomlyGenerateDeviation(50, 50);
		if ((val < 25) || (val > 75)) {
			fail("not working: " + val);
		}
		
	}
	
	@Test
	public void testDeviation2() {
		for (int i = 0; i<100; i++) {
			int avg = 100;
			int dev = 10;
			int res = ModernEdgeProducer.randomlyGenerateDeviationAbs(avg, dev);
			if ((res > 110) || (res < 90))
				fail("incorrect val : " + res);
			
			avg = 250;
			dev = 12;
			res = ModernEdgeProducer.randomlyGenerateDeviationAbs(avg, dev);
			if ((res > 262) || (res < 238))
				fail("incorrect val : " + res);
		}
	}

}
