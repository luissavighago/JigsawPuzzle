package test;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.junit.Test;

import puzzle.edge.AbstractEdgeProducer;
import puzzle.edge.ModernEdgeProducer;
import puzzle.pieces.MultiPiece;
import puzzle.storeage.JigsawPuzzleException;

public class NewAssemblyStrategyTest {

	@Test
	public void testGetPoints() throws JigsawPuzzleException {
		class MPTestClass extends MultiPiece {
			public Point[] getPoints2(GeneralPath gp) throws JigsawPuzzleException {
				return this.getPoints(gp);
			}
		}
		
		MPTestClass mp = new MPTestClass();

		AbstractEdgeProducer edgeProducer = new ModernEdgeProducer();

		edgeProducer.init(40);
		edgeProducer.produce();

		Shape[] both = edgeProducer.getBothShapes();
		GeneralPath gp = (GeneralPath) both[1];
		
		Point[] pts = mp.getPoints2(gp);
		System.out.println(pts[0].x);
		System.out.println(pts[0].y);
	}

}
