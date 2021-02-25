package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.junit.Test;

import puzzle.edge.AbstractEdgeProducer;
import puzzle.edge.ModernEdgeProducer;
import puzzle.storeage.JigsawPuzzleException;

public class PieceEdgeProducesVisualTest {
	
	
	
	private JFrame testFrame;
	
	@Test
	public void testPieceEdgeProducerVisual() throws JigsawPuzzleException {
		AbstractEdgeProducer edgeProducer = new ModernEdgeProducer();
		
		edgeProducer.init(40);
		edgeProducer.produce();
		
		Shape[] both = edgeProducer.getBothShapes();
		
		Shape current = both[1];
		AffineTransform af = AffineTransform.getTranslateInstance(100, 100);
		AffineTransform af2 = AffineTransform.getScaleInstance(2, 2);

		current = af2.createTransformedShape(current);
		current = af.createTransformedShape(current);
		
		this.testFrame = new JTestFrame(current);
		this.testFrame.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	static final class JTestFrame extends JFrame {
		
		private final Dimension SIZE = new Dimension(200, 200);
		
		private Shape theVisualElement;
		private BufferedImage img;
		
		JTestFrame(Shape s) {
			Dimension size = SIZE;
			this.setSize(size);
			this.setPreferredSize(size);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.theVisualElement = s;
			
			
			img = new BufferedImage(SIZE.width, SIZE.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 200, 200);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(100, 100, 1, 1);
			g2d.setColor(Color.ORANGE);
			g2d.draw(theVisualElement);
			g2d.dispose();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(img, 0, 0, null);
		}
		
	}

}
