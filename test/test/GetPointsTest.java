package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.junit.Test;

import puzzle.pieces.MultiPiece;
import puzzle.storeage.JigsawPuzzleException;

public class GetPointsTest {
	
	static class TestMultiPiece extends MultiPiece {
		
		public Point[] getPoints2(Shape s) throws JigsawPuzzleException {
			return this.getPoints(s);
		}
	}
	
	@Test
	public void testGetPoints1() throws JigsawPuzzleException {
		
		TestMultiPiece tmp = new TestMultiPiece();
		GeneralPath gp1 = new GeneralPath();
		// test 1
		gp1.moveTo(1, 1);
		gp1.lineTo(2, 2);
		gp1.quadTo(3, 4, 5, 6);
		gp1.lineTo(5, 8);
		Point[] testPoint = tmp.getPoints2(gp1);
		assertEquals(1, testPoint[0].x);
		assertEquals(1, testPoint[0].y);
		assertEquals(5, testPoint[1].x);
		assertEquals(8, testPoint[1].y);
		// test 2
		gp1 = new GeneralPath();
		gp1.moveTo(15, 0);
		gp1.lineTo(10, 20);
		gp1.lineTo(10, 12);
		gp1.lineTo(0, 7);
		gp1.quadTo(1, 2, 3, 4);
		testPoint = tmp.getPoints2(gp1);
		assertEquals(15, testPoint[0].x);
		assertEquals(0, testPoint[0].y);
		assertEquals(0, testPoint[1].x);
		assertEquals(7, testPoint[1].y);
	}
	
	@Test
	public void testGetPoints2() throws JigsawPuzzleException {
		TestMultiPiece tmp = new TestMultiPiece();
		GeneralPath gp1;
		gp1 = new GeneralPath();
		gp1.moveTo(20, 100);
		gp1.lineTo(12, 12);
		gp1.moveTo(30, 40); // this cannot happen!
		gp1.quadTo(1, 2, 3, 4);
		gp1.lineTo(1, 1);
		try {
			tmp.getPoints2(gp1);
			fail("did not work");
		} catch (JigsawPuzzleException e) {
			// all clear!
		}
	}

}
