package test;

import java.awt.Point;

import org.junit.Test;

import puzzle.GameCommander;
import puzzle.GamePreferences;
import puzzle.Main;
import puzzle.Offset;
import puzzle.edge.Edge;
import puzzle.edge.StandardEdgeProducer;
import puzzle.gameevent.GameEvent;
import puzzle.pieces.PuzzlePiece;
import puzzle.pieces.SinglePiece;
import puzzle.storeage.JigsawPuzzleException;
import puzzle.ui.GameMainWindow;
import puzzle.ui.PuzzleImage;

/**
 * This game tester stub allows to build a whole puzzle together like one would
 * do it manually. This only starts as a
 * 
 * @author Heinz
 * 
 */
public class GameTesterStub {

	GameCommander gC;

	public static void main(String[] args) {
		try {
			new GameTesterStub().initGame("/images/SOTP.jpg", 60);
		} catch (JigsawPuzzleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGame() throws JigsawPuzzleException {
		// start the game
		int pieces = initGame("/images/LASkyline.jpg", 30);

		// select one
		PuzzlePiece startPiece = this.gC.getPieceDisposer().getPuzzlePieces()
				.get(0);
		Edge startEdge = startPiece.getResolvableEdges().get(0);
		// find the suitable and snap together -> so i have a biggest piece now
		// (at least 2 pieces together)
		findSuitablePiece(startPiece, startEdge);
		GameMainWindow.getInstance().repaint();

		// select the largest piece, because the two had composed to another
		// thing!
		PuzzlePiece large = null;
		for (PuzzlePiece pp : this.gC.getPieceDisposer().getPuzzlePieces()) {
			if (pp.getPieceCount() > 1) {
				large = pp;
				break;
			}
		}

		for (int i = 0; i < pieces - 2; i++) {
			Edge e = large.getResolvableEdges().get(0);
			findSuitablePiece(large, e);
			GameMainWindow.getInstance().repaint();
		}

		GameMainWindow.getInstance().repaint();
		sleep(10000);
	}

	private int initGame(String res, int sl) throws JigsawPuzzleException {
		Main.main(null);
		sleep(1500);
		
		this.gC = GameCommander.getInstance();

		int sideLength = sl;
		PuzzleImage image = null;

		image = new PuzzleImage(getClass().getResource(res));

		image.isResizableToGoodQuality(sideLength);
		image.resize();

		GamePreferences pref = new GamePreferences(sideLength, image, false,
				true, true, true, true, new StandardEdgeProducer());
		pref.calcDeducedAttributes();
		pref.calcInitialPieces();

		int pieces = pref.getInitialPieces();

		this.gC.newGame(pref);
		sleep(10000);
		return pieces;
	}

	private void findSuitablePiece(PuzzlePiece s, Edge e) throws JigsawPuzzleException {
		int edgeNum = e.getEdgePairNumber();

		for (PuzzlePiece pp : this.gC.getPieceDisposer().getPuzzlePieces()) {
			if (!(pp instanceof SinglePiece))
				continue;
			if (pp == s)
				continue;
			Edge c = pp.getEdge(edgeNum);
			if (c != null) {
				// move pp so that pos c and e are (nearly) equal

				Point eP = e.calculatePoint();
				Point cP = c.calculatePoint();

				int xMove = eP.x - cP.x;
				int yMove = eP.y - cP.y;

				System.out.println("defs" + xMove + "," + yMove);

				GameEvent ge = new GameEvent(
						GameEvent.State.PREPARE_TO_MOVE_PIECE, pp);
				this.gC.deliverEvent(ge);

				pp.move(new Offset(xMove, yMove));

				ge = new GameEvent(GameEvent.State.MOVE_PIECE, pp);
				this.gC.deliverEvent(ge);
				pp.snap();

				break;
			}
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
