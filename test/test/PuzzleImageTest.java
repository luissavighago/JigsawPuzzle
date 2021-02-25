package test;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.junit.Test;

import puzzle.ui.PuzzleImage;
import static org.junit.Assert.*;

public class PuzzleImageTest {
	
	public static final int TYPE = BufferedImage.TYPE_INT_BGR;
	
	@Test
	public void testResizeToFit() {
		PuzzleImage pi;
		
		BufferedImage input;
		final Dimension panelSize = new Dimension(100, 100);
		Image result;
		
		// panel 100x100, image 50x30 -> result = 100x60
		input = new BufferedImage(50, 30, BufferedImage.TYPE_INT_RGB); 
		pi = new PuzzleImage(input);
		result = pi.resizeToFit(panelSize);
		assertEquals(100, result.getWidth(null));
		assertEquals(60, result.getHeight(null));
		
		// panel 100x100, image 500x100 -> result = 100x20
		input = new BufferedImage(500, 100, BufferedImage.TYPE_INT_RGB);
		pi = new PuzzleImage(input);
		result = pi.resizeToFit(panelSize);
		assertEquals(100, result.getWidth(null));
		assertEquals(20, result.getHeight(null));
		
		// panel 100x100, image 880x2000 -> result = 44x100
		input = new BufferedImage(880, 2000, BufferedImage.TYPE_INT_RGB);
		pi = new PuzzleImage(input);
		result = pi.resizeToFit(panelSize);
		assertEquals(44, result.getWidth(null));
		assertEquals(100, result.getHeight(null));
		
		// panel 100x100, image 120x90 -> result = 100x75
		input = new BufferedImage(120, 90, BufferedImage.TYPE_INT_RGB);
		pi = new PuzzleImage(input);
		result = pi.resizeToFit(panelSize);
		assertEquals(100, result.getWidth(null));
		assertEquals(75, result.getHeight(null));
		
		// panel 100x100, image 51x150 -> result = 34x100
		input = new BufferedImage(51, 150, BufferedImage.TYPE_INT_RGB);
		pi = new PuzzleImage(input);
		result = pi.resizeToFit(panelSize);
		assertEquals(34, result.getWidth(null));
		assertEquals(100, result.getHeight(null));
		
	}
	
	@Test
	public void testResize() {
		
		BufferedImage testImage;
		PuzzleImage pi;
		int sideLength;
		
		testImage = new BufferedImage(400, 200, TYPE);
		sideLength = 40;
		pi = new PuzzleImage(testImage);
		pi.isResizableToGoodQuality(sideLength);
		pi.resize();

	}
	

	@Test
	public void testIsResizableToGoodQualityBufferedImage() {
		
		BufferedImage testImage;
		PuzzleImage pi;
		int sideLength;
		boolean result;
		
		// some so on tests
		testImage = new BufferedImage(388, 190, TYPE);
		sideLength = 20;
		pi = new PuzzleImage(testImage);
		result = pi.isResizableToGoodQuality(sideLength);
		assertTrue(result);
		
		testImage = new BufferedImage(1024, 768, TYPE);
		sideLength = 60;
		pi = new PuzzleImage(testImage);
		result = pi.isResizableToGoodQuality(sideLength);
		assertTrue(result);
		
		testImage = new BufferedImage(500, 220, TYPE);
		sideLength = 40;
		pi = new PuzzleImage(testImage);
		result = pi.isResizableToGoodQuality(sideLength);
		assertTrue(result);
		
		// some uneasy
		testImage = new BufferedImage(20, 200, TYPE);
		sideLength = 40;
		pi = new PuzzleImage(testImage);
		// scale down from 20 to 0 so error should come up
		result = pi.isResizableToGoodQuality(sideLength);
		assertFalse(result);
		
		testImage = new BufferedImage(200, 20, TYPE);
		sideLength = 40;
		pi = new PuzzleImage(testImage);
		// scale down from 20 to 0 so error should come up
		result = pi.isResizableToGoodQuality(sideLength);
		assertFalse(result);
		
		testImage = new BufferedImage(91, 190, TYPE);
		sideLength = 60;
		pi = new PuzzleImage(testImage);
		// 91 upscale to 120 is bad quality -> 120/91 > 30%
		result = pi.isResizableToGoodQuality(sideLength);
		assertFalse(result);

	}

}
