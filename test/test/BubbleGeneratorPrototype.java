package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import puzzle.edge.AbstractEdgeProducer;
import puzzle.edge.StandardEdgeProducer;
import puzzle.storeage.JigsawPuzzleException;

public class BubbleGeneratorPrototype {
	
	static BubbleGeneratorPrototype proto;

	public static void main(String args[]) {
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					proto = new BubbleGeneratorPrototype();
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						proto.showFullEdgeShape();
					} catch (JigsawPuzzleException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public BubbleGeneratorPrototype() {
	}
	
	private void showFullEdgeShape() throws JigsawPuzzleException {
		AbstractEdgeProducer prod = new StandardEdgeProducer();
		prod.init(60);
		prod.produce();
		Shape[] shapes = prod.getBothShapes();
		
		JTestFrame testFrame = new JTestFrame();
		// retranslate the shape to the middle of the window
		AffineTransform af = AffineTransform.getTranslateInstance(100, 100);
		Shape s = af.createTransformedShape(shapes[0]);
		
		testFrame.initShape(s);
		testFrame.setVisible(true);
		
	}

	private final class JTestFrame extends JFrame {

		private BufferedImage img;

		JTestFrame() {
			Dimension size = new Dimension(200, 200);
			this.setSize(size);
			this.setPreferredSize(size);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("drawable Frame");
		}

		public void initPoints(Point[] pts) {
			img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 200, 200);
			g2d.setColor(Color.ORANGE);

			int length = pts.length;
			Point startPoint = pts[0];
			for (int i = 1; i < length; i++) {
				Point tmp = pts[i];
				g2d.drawLine(startPoint.x + 100, startPoint.y + 100,
						tmp.x + 100, tmp.y + 100);
				startPoint = tmp;
			}

			g2d.dispose();
		}

		public void initShape(Shape s) {

			img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 200, 200);
			g2d.setColor(Color.ORANGE);
			g2d.draw(s);
			g2d.dispose();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(img, 0, 0, null);
		}

	}

}
