/*
 * GamePanel.java
 *
 * Created on 27. August 2006, 21:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package puzzle.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import puzzle.GameCommander;
import puzzle.PuzzleProperties;
import puzzle.gameevent.GameEvent;
import puzzle.gameevent.GameEventListener;
import puzzle.pieces.PuzzlePiece;
import puzzle.storeage.JigsawPuzzleException;

/**
 * 
 * @author Heinz
 */
/**
 * 
 * @autor Heinz
 */
public class GamePanel extends JPanel implements GameEventListener {
	
	private Logger logger = Logger.getLogger(GamePanel.class);
	
	// offscreenImage from the double buffer
	// offscreenImage do buffer duplo
	private Image offscreenImage;
	
	private final PuzzleInputListener inputListener;

	public PuzzleInputListener getInputListener() {
		return inputListener;
	}

	private final GameCommander gC = GameCommander.getInstance();
	
	/** Creates a new instance of GamePanel */
	/** Cria uma nova instância de GamePanel */
	public GamePanel() {
		this.inputListener = new PuzzleInputListener();
		this.addMouseListener(this.inputListener);
		this.addMouseMotionListener(this.inputListener);
		
		GameCommander.getInstance().addListener(this);
	}

	/**
	 * paints the offscreen to screen buffer
	 */
	/**
	 * pinta o buffer fora da tela para a tela
	 */
	/*
	public void paint(Graphics g) {
		super.paint(g);

		if (this.gC.isGameRunning()) {
			if (GameCommander.getInstance().getPreferences().isAntiAliasing())
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			if ((offscreenImage != null))
				g.drawImage(offscreenImage, 0, 0, null);
		}
	}
	*/
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.gC.isGameRunning()) {
			if (GameCommander.getInstance().getPreferences().isAntiAliasing())
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			if ((offscreenImage != null))
				g.drawImage(offscreenImage, 0, 0, null);
		}
		
	}

	/**
	 * deletes a specific piece from the drawable area.
	 * it does that by simply deleting the area and
	 * drawing all pieces which are in the area of 
	 * that piece again.
	 * @param actPs the piece to delete
	 */
	/**
	 * exclui uma peça específica da área de desenho.
	 * ele faz isso simplesmente excluindo a área e
	 * desenho de todas as peças que estão na área de
	 * aquela peça novamente.
	 * @param atua na peça a ser excluída
	 */
	private void deleteFromArea(PuzzlePiece actPs) throws JigsawPuzzleException {
		Rectangle clippingArea = actPs.getBoundingRectangle();

		Graphics2D offscreenPainter = (Graphics2D) offscreenImage.getGraphics()
				.create();
		// clear the area first
		// limpe a área primeiro
		offscreenPainter.setClip(clippingArea);
		offscreenPainter.setColor(PuzzleProperties.BACKGROUND_COLOR);
		offscreenPainter.fill(clippingArea);
		
		if (GameCommander.getInstance().getPreferences().isAntiAliasing())
			offscreenPainter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw 'em now
		// desenhe-os agora
		for (PuzzlePiece ps : GameCommander.getInstance().getPieceDisposer().getPuzzlePieces()) {
			if (ps.isWithinRectangle(clippingArea) && ps != actPs) {
				ps.renderInClip(offscreenPainter);
			}
		}

		offscreenPainter.dispose();
		this.repaint(clippingArea);
	}

	/**
	 * draws exactly only that piece into its drawable area
	 * @param piece the piece to draw
	 */
	/**
	 * desenha exatamente apenas aquela peça em sua área de desenho
	 * @param peça a peça a desenhar
	 */
	private void drawToArea(PuzzlePiece piece) throws JigsawPuzzleException {
		Rectangle clippingArea = piece.getBoundingRectangle();
		Graphics2D offscreenPainter = (Graphics2D) offscreenImage.getGraphics()
				.create();
		offscreenPainter.setClip(clippingArea);
		piece.renderInClip(offscreenPainter);
		offscreenPainter.dispose();
		this.repaint(clippingArea);
	}

	/**
	 * called to create new image and render it within it's given coordinates.
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * chamado para criar uma nova imagem e renderizá-la dentro de suas coordenadas fornecidas.
	 * @throws JigsawPuzzleException 
	 */
	public void reRender() throws JigsawPuzzleException {
		Graphics2D dbg;

		boolean deleteImageContent = false;		

		// do this only once, or if the image has changed size!
		// faça isso apenas uma vez, ou se o tamanho da imagem mudou!
		if ((offscreenImage == null) || (!isSameSize(offscreenImage, this.getPreferredSize()))) {
			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration();			
			offscreenImage = gc.createCompatibleImage(this.getSize().width, this.getSize().height);
			deleteImageContent = true;
		} 
		// getting graphics context.
		// obtendo contexto gráfico.
		dbg = (Graphics2D) offscreenImage.getGraphics();
		
		if (deleteImageContent) { // do that if only renewing!
								  // faça isso apenas renovando!
			dbg.clearRect(0, 0, this.offscreenImage.getWidth(null), this.offscreenImage.getHeight(null));
		}

		// paint background
		// pintar fundo
		dbg.setColor(PuzzleProperties.BACKGROUND_COLOR);
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// fill the pic with the pieces
		// preencha a foto com as peças
		List<PuzzlePiece> Puzzlestuecke = GameCommander.getInstance().getPieceDisposer()
				.getPuzzlePieces();
		for (PuzzlePiece ps : Puzzlestuecke) {
			ps.renderInClip(dbg);
		}
		dbg.dispose();
		this.repaint();
	}
	
	/**
	 * finds out if the image has the same size as the Dimension
	 * @param i
	 * @param d
	 * @return
	 */
	/**
	 * descobre se a imagem tem o mesmo tamanho que a dimensão
	 * @param i
	 * @param d
	 * @return
	 */
	private boolean isSameSize(Image i, Dimension d) {		
		int imageWidth = i.getWidth(null);
		int imageHeight = i.getHeight(null);
		
		int dimWidth = d.width;
		int dimHeight = d.height;
		
		//remoção de ifs
		// remoção de ifs
		return ((imageWidth == dimWidth) && (imageHeight == dimHeight));
	}

	public Dimension getPreferredSize() {
		return this.getSize();
	}

	public void eventHappened(GameEvent ge) throws JigsawPuzzleException {
		switch (ge.getType()) {
		case PREPARE_TO_TURN_PIECE:
		case PREPARE_TO_MOVE_PIECE:
		case PREPARE_TO_HIGHLIGHT_PIECE:
			this.deleteFromArea((PuzzlePiece)ge.getInfo());
			break;
		case TURN_PIECE:
		case MOVE_PIECE:
		case HIGHLIGHT_PIECE:
			this.drawToArea((PuzzlePiece)ge.getInfo());
			break;
		default:
			break;
		}
		
	}
}
