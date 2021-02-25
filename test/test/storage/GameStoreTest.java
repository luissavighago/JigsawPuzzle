package test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import puzzle.GameCommander;
import puzzle.GamePreferences;
import puzzle.edge.Edge;
import puzzle.edge.StandardEdgeProducer;
import puzzle.pieces.MultiPiece;
import puzzle.pieces.PuzzlePieceDisposer;
import puzzle.pieces.SinglePiece;
import puzzle.storeage.JigsawPuzzleException;
import puzzle.storeage.LoadGameException;
import puzzle.storeage.SaveGameException;
import puzzle.storeage.StorageUtil;
import puzzle.ui.GameMainWindow;
import puzzle.ui.PuzzleImage;


public class GameStoreTest {
	
	static String imagesDir = "/images/";
	static String image1 = imagesDir + "KingPenguin.jpg";
	static String image2 = imagesDir + "LASkyline.jpg";
	static String image3 = imagesDir + "PortoCovo.jpg";
	
	static GamePreferences gp;
	static PuzzleImage pi;
	static int side = 40;
	static boolean allowTurn = true;
	static boolean showOutline = false;
	static boolean showShadow = true;
	static boolean highlight = true;
	static boolean sound = false;
	static int columns = 20;
	static int rows = 15;
	static {
		// init the preferences here
		BufferedImage buf = null;
		try {
			pi = new PuzzleImage(GameStoreTest.class.getResource((image1)));
		} catch (JigsawPuzzleException e) {
			e.printStackTrace();
		}

		pi.isResizableToGoodQuality(20);
		pi.resize();
		
		gp = new GamePreferences(side, pi, allowTurn, showOutline, showShadow, highlight, sound, new StandardEdgeProducer());
	}
	
	private void createGameStaff() throws JigsawPuzzleException {
		GameMainWindow.startUI();
		try {
			// BE AWARE THAT that MAY BE TOO SHORT DUE DO CPU load
			Thread.sleep(1000);
		} catch (InterruptedException e5) {
			e5.printStackTrace();
		}
		GameCommander.getInstance().newGame(gp);
	}
	
	@Test
	public void storeRestorePuzzleImageAndGamePreferencesTest() {
		
		Dimension resampled = pi.getResampleSize();

		gp.setColumns(columns);
		gp.setRows(rows);
		gp.calcInitialPieces();
		//gp.calcDeducedAttributes();
        try {

			Document doc = StorageUtil.createDOMDocument();
			Element root = doc.createElement("PrefAndImageTest");
			doc.appendChild(root);
			gp.store(root);
			
			// store as a file
			StorageUtil.saveAsXML(doc, new File("puzzleImageAndGamePreferencesTest.xml"));
			
			// TEST starts after here all things are in the gp 
			GamePreferences copy = new GamePreferences();
			copy.restore(root);
			
			assertEquals(side, copy.getSideLength());
			assertEquals(allowTurn, copy.isAllowTurn());
			assertEquals(showOutline, copy.isShowOutline());
			assertEquals(showShadow, copy.isShowShadow());
			assertEquals(sound, copy.isSound());
			assertEquals(columns, copy.getColumns());
			assertEquals(rows, copy.getRows());
			assertEquals(rows*columns, copy.getInitialPieces());
			
			Dimension d = copy.getImage().getResampleSize();
			assertEquals(resampled.height, d.height);
			assertEquals(resampled.width, d.width);
			
			
			ImageIO.write(copy.getImage().getOriginalImage(), "png", new File("pic.png"));
			
		} catch (SaveGameException e) {
			e.printStackTrace();
			fail();
		} catch (LoadGameException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} 
	}

	@Test
	public void storeRestoreSinglePieceAndEdgeTest() throws JigsawPuzzleException {
		gp.setColumns(columns);
		gp.setRows(rows);
		gp.calcInitialPieces();
		gp.calcDeducedAttributes();
		createGameStaff();
		
		Point p = new Point(100, 123);
		Edge e1 = new Edge(Edge.Type.TOP, 1, new GeneralPath());
		Edge e2 = new Edge(Edge.Type.BOTTOM, 2, new GeneralPath());
		Edge e3 = new Edge(Edge.Type.RIGHT, 3, new GeneralPath());
		Edge e4 = new Edge(Edge.Type.LEFT, 4, new GeneralPath());
		Edge[] edges = {e1, e2, e3, e4};
		BufferedImage buf = null;
		try {
			buf = ImageIO.read(getClass().getResource(image2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SinglePiece singlePiece = new SinglePiece(edges, buf, p);
		
		try {
			Document doc = StorageUtil.createDOMDocument();
			Element root = doc.createElement("SinglePieceAndEdgeTestTest");
			doc.appendChild(root);
			singlePiece.store(root);
			// store to a file
			StorageUtil.saveAsXML(doc, new File("SinglePieceAndEdgeTest.xml"));
			
			// TEST starts here
			SinglePiece copy = new SinglePiece();
			copy.restore(root);
			
			assertEquals(p.x, copy.getPoint().x);
			assertEquals(p.y, copy.getPoint().y);
			
			assertNotNull(copy.getEdges(Edge.Type.TOP).get(0));
			assertNotNull(copy.getEdges(Edge.Type.BOTTOM).get(0));
			assertNotNull(copy.getEdges(Edge.Type.RIGHT).get(0));
			assertNotNull(copy.getEdges(Edge.Type.LEFT).get(0));
			
			assertNotNull(copy.getEdge(1));
			assertNotNull(copy.getEdge(2));
			assertNotNull(copy.getEdge(3));
			assertNotNull(copy.getEdge(4));
			
			Edge top = copy.getEdges(Edge.Type.TOP).get(0);
			Edge bottom = copy.getEdges(Edge.Type.BOTTOM).get(0);
			Edge right = copy.getEdges(Edge.Type.RIGHT).get(0);
			Edge left = copy.getEdges(Edge.Type.LEFT).get(0);
			
			assertEquals(1, top.getEdgePairNumber());
			assertEquals(2, bottom.getEdgePairNumber());
			assertEquals(3, right.getEdgePairNumber());
			assertEquals(4, left.getEdgePairNumber());
			
		} catch (SaveGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoadGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Test
	public void testMultiPiece() throws JigsawPuzzleException {
		// ##################### single1
		Point sp1_p = new Point(100, 123);
		Edge sp1_e1 = new Edge(Edge.Type.TOP, 1, new GeneralPath());
		Edge sp1_e2 = new Edge(Edge.Type.BOTTOM, 2, new GeneralPath());
		Edge sp1_e3 = new Edge(Edge.Type.RIGHT, 3, new GeneralPath());
		Edge sp1_e4 = new Edge(Edge.Type.LEFT, 4, new GeneralPath());
		Edge[] sp1_edges = {sp1_e1, sp1_e2, sp1_e3, sp1_e4};
		BufferedImage sp1_buf = null;
		try {
			sp1_buf = ImageIO.read(getClass().getResource(image3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SinglePiece sp1 = new SinglePiece(sp1_edges, sp1_buf, sp1_p);
		// ##################### single2
		Point sp2_p = new Point(456, 448);
		Edge sp2_e1 = new Edge(Edge.Type.TOP, 89, new GeneralPath());
		Edge sp2_e2 = new Edge(Edge.Type.BOTTOM, 92, new GeneralPath());
		Edge sp2_e3 = new Edge(Edge.Type.RIGHT, 34, new GeneralPath());
		Edge sp2_e4 = new Edge(Edge.Type.LEFT, 42, new GeneralPath());
		Edge[] sp2_edges = {sp2_e1, sp2_e2, sp2_e3, sp2_e4};
		BufferedImage sp2_buf = null;
		try {
			sp2_buf = ImageIO.read(getClass().getResource(image3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SinglePiece sp2 = new SinglePiece(sp2_edges, sp2_buf, sp2_p);
		// ##################### single3
		Point sp3_p = new Point(146, 481);
		Edge sp3_e1 = new Edge(Edge.Type.TOP, 72, new GeneralPath());
		Edge sp3_e2 = new Edge(Edge.Type.BOTTOM, 14, new GeneralPath());
		Edge sp3_e3 = new Edge(Edge.Type.RIGHT, 90, new GeneralPath());
		Edge sp3_e4 = new Edge(Edge.Type.LEFT, 7, new GeneralPath());
		Edge[] sp3_edges = {sp3_e1, sp3_e2, sp3_e3, sp3_e4};
		BufferedImage sp3_buf = null;
		try {
			sp3_buf = ImageIO.read(getClass().getResource(image3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SinglePiece sp3 = new SinglePiece(sp3_edges, sp3_buf, sp3_p);
		// ##################### create multipiece 
		MultiPiece mp = new MultiPiece(sp1, sp2);
		try {
			Document doc = StorageUtil.createDOMDocument();
			Element root = doc.createElement("MultiPieceTest");
			doc.appendChild(root);
			mp.store(root);
			StorageUtil.saveAsXML(doc, new File("MultiPieceTest.xml"));
			
			MultiPiece testPiece = new MultiPiece();
			testPiece.restore(root);
			
			// ######## now create the test
			
		} catch (SaveGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoadGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
		
	}
	
	@Test
	public void testPuzzlePieceDisposer() throws JigsawPuzzleException {
		Point sp1_p = new Point(100, 123);
		Edge sp1_e1 = new Edge(Edge.Type.TOP, 1, new GeneralPath());
		Edge sp1_e2 = new Edge(Edge.Type.BOTTOM, 2, new GeneralPath());
		Edge sp1_e3 = new Edge(Edge.Type.RIGHT, 3, new GeneralPath());
		Edge sp1_e4 = new Edge(Edge.Type.LEFT, 4, new GeneralPath());
		Edge[] sp1_edges = {sp1_e1, sp1_e2, sp1_e3, sp1_e4};
		BufferedImage sp1_buf = null;
		try {
			sp1_buf = ImageIO.read(getClass().getResource(image2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SinglePiece sp1 = new SinglePiece(sp1_edges, sp1_buf, sp1_p);
		// ##################### single2
		Point sp2_p = new Point(456, 448);
		Edge sp2_e1 = new Edge(Edge.Type.TOP, 89, new GeneralPath());
		Edge sp2_e2 = new Edge(Edge.Type.BOTTOM, 92, new GeneralPath());
		Edge sp2_e3 = new Edge(Edge.Type.RIGHT, 34, new GeneralPath());
		Edge sp2_e4 = new Edge(Edge.Type.LEFT, 42, new GeneralPath());
		Edge[] sp2_edges = {sp2_e1, sp2_e2, sp2_e3, sp2_e4};
		BufferedImage sp2_buf = null;
		try {
			sp2_buf = ImageIO.read(getClass().getResource(image2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SinglePiece sp2 = new SinglePiece(sp2_edges, sp2_buf, sp2_p);
		
		PuzzlePieceDisposer ppd = new PuzzlePieceDisposer();
		ppd.addPuzzleStueck(sp1);
		ppd.addPuzzleStueck(sp2);

		try {
			
			Document doc = StorageUtil.createDOMDocument();
			Node ppd_node = doc.createElement("PuzzlePieceDisposerTest");
			
			ppd.store(ppd_node);
			doc.appendChild(ppd_node);
			
			StorageUtil.saveAsXML(doc, new File("PuzzlePieceDisposerTest.xml"));
			
			// GO TEST ON!
			ppd.restore(ppd_node);
			
		} catch (SaveGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoadGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
}
