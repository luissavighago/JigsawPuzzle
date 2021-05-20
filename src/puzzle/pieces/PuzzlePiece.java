/*
 * PuzzleStueck.java
 *
 * Created on 27. August 2006, 15:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package puzzle.pieces;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Vector;

import puzzle.GameCommander;
import puzzle.GamePreferences;
import puzzle.Offset;
import puzzle.PuzzleProperties;
import puzzle.Turnable;
import puzzle.edge.Edge;
import puzzle.storeage.JigsawPuzzleException;
import puzzle.storeage.Storeable;

/**
 * 
 * @author Heinz
 */
/**
 * 
 * @autor Heinz
 */

public abstract class PuzzlePiece implements Storeable, Turnable {

	/**
	 * Additional Gap for painting issues. If you do not provide 1-2 points at
	 * least, you cannot use the outline because it might be drawn outside the
	 * shape and then you get graphical bugs
	 */
	/**
	 * Lacuna adicional para problemas de pintura. Se você não fornecer 1-2 pontos em
	 * pelo menos, você não pode usar o contorno porque pode ser desenhado fora do
	 * forma e, em seguida, você obtém erros gráficos
	 */
	protected static final int GAP_X = 5;

	/**
	 * gap y
	 * VIEW
	 */
	/**
	 * gap y
	 * VISUALIZAR
	 */
	protected static final int GAP_Y = 5;

	/**
	 * gap widht
	 * VIEW
	 */
	/**
	 * largura da lacuna
	 * VISUALIZAR
	 */
	protected static final int GAP_WIDTH = 10;

	/**
	 * gap height
	 * VIEW
	 */
	/**
	 * altura da lacuna
	 * VISUALIZAR
	 */
	protected static final int GAP_HEIGHT = 10;

	/**
	 * the shape (the outline shape of that puzzle piece)
	 * VIEW
	 */
	/**
	 * a forma (o contorno da peça do quebra-cabeça)
	 * VISUALIZAR
	 */
	protected transient Shape puzzleShape;
	
	/**
	 * inidcates if this puzzle piece is highlighted or not
	 * VIEW
	 */
	/**
	 * indica se esta peça do quebra-cabeça está destacada ou não
	 * VISUALIZAR
	 */
	protected boolean highlighted = false;
	
	/**
	 * todas as arestas que esta peça possui.
	 * MODELO
	 */
	protected transient List<Edge> edges;
	
	/**
	 * if piece is within this point true, false otherwise
	 * VIEW
	 */
	/**
	 * se a peça está dentro deste ponto verdadeiro, falso caso contrário
	 * VISUALIZAR
	 */
	public abstract boolean isHit(Point punkt);

	/**
	 * definir destaque
	 * VISUALIZAR
	 */
	public void highlight() {
		this.highlighted = true;
	}
	
	/**
	 * reset the higlight.
	 * VIEW
	 */
	/**
	 * redefinir o destaque.
	 * VISUALIZAR
	 */
	public void unhighlight() {
		this.highlighted = false;
	}
	
	/**
	 * paints this piece in the given rectangle clipping area
	 * VIEW
	 */
	/**
	 * pinta esta peça na área de recorte do retângulo fornecida
	 * VISUALIZAR
	 */
	public final void renderInClip(Graphics2D g2d) throws JigsawPuzzleException {
		Rectangle rect = this.getBoundingRectangle();
		GamePreferences gp = GameCommander.getInstance().getPreferences();
		// shadow is the first
		// sombra é a primeira
		if (gp.isShowShadow()) {
			renderShadowInClip(g2d, rect);
		}
		// than draw the face
		// então desenhe o rosto
		renderFaceInClip(g2d, rect);
		
		// finally draw the outline
		// finalmente desenhe o contorno
		if (gp.isShowOutline() || (this.highlighted && gp.isHighlight())) {
			renderOutlineInClip(g2d, rect);
		}
		
	}

	/**
	 * draw the face of the puzzle piece
	 * VIEW
	 * @param g2d
	 * @param rect
	 */
	/**
	 * desenhe o rosto da peça do quebra-cabeça
	 * VISUALIZAR
	 * @param g2d
	 * @param rect
	 */
	protected abstract void renderFaceInClip(Graphics2D g2d, Rectangle rect);

	/**
	 * draw the outlined Shape of the piece
	 * VIEW
	 * @param g2d
	 * @param rect
	 */
	/**
	 * desenhe a forma delineada da peça
	 * VISUALIZAR
	 * @param g2d
	 * @param rect
	 */
	protected void renderShadowInClip(Graphics2D g2d, Rectangle rect) {
		g2d.setClip(rect.x, rect.y, rect.width, rect.height);

		int shadowLength = GameCommander.getInstance().getPreferences()
				.getShadowLength();
		AffineTransform mover = AffineTransform.getTranslateInstance(
				shadowLength, shadowLength);
		Shape shadowShape = mover.createTransformedShape(this.puzzleShape);
		g2d.setColor(PuzzleProperties.PIECE_SHADOW_COLOR);
		g2d.fill(shadowShape);
	}
	
	/**
	 * draws the outline of this shape within the clips
	 * VIEW
	 * @param g2d
	 * @param rect
	 */
	/**
	 * desenha o contorno desta forma dentro dos clipes
	 * VISUALIZAR
	 * @param g2d
	 * @param rect
	 */
	protected void renderOutlineInClip(Graphics2D g2d, Rectangle rect) {
		g2d.setClip(rect.x, rect.y, rect.width, rect.height);
		GamePreferences gp = GameCommander.getInstance().getPreferences();

		g2d.setStroke(gp.getOutlineStroke());
		
		// set highlight or not.
		// definir destaque ou não.
		if (!this.highlighted || !gp.isHighlight())
			g2d.setColor(PuzzleProperties.PIECE_COLOR);
		else
			g2d.setColor(PuzzleProperties.PIECE_HIGHLIGHTED_COLOR);
		
		g2d.draw(this.puzzleShape);
	}

	/**
	 * returns a bounding box (mustn't be the smallest)
	 * VIEW
	 */
	/**
	 * retorna uma caixa delimitadora (não deve ser a menor)
	 * VISUALIZAR
	 */
	public abstract Rectangle getBoundingRectangle() throws JigsawPuzzleException ;

	/**
	 * Test if this piece is within the given rectangle (either entirely or
	 * partially)
	 * VIEW
	 * @return false if there is no area that belongs to both (the specified
	 *         rect and the one from this piece)
	 */
	/**
	* Teste se esta peça está dentro do retângulo dado (totalmente ou
	* parcialmente)
	* VISUALIZAR
	* @return false se não houver nenhuma área que pertença a ambos (o especificado
	* rect e o desta peça)
	*/
	public abstract boolean isWithinRectangle(Rectangle rect);
	
	/**
	 * returns the shape
	 * VIEW
	 */
	/**
	 * retorna a forma
	 * VISUALIZAR
	 */
	public Shape getShape() throws JigsawPuzzleException {
		return this.puzzleShape;
	}
	
	/**
	 * a method to rebuild the shape which one can get with getShape() afterwards.
	 */
	/**
	 * um método para reconstruir a forma que se pode obter com getShape () posteriormente.
	 */
	protected abstract void buildShape() throws JigsawPuzzleException ;

	/**
	 * test if this piece is near (MAX_SNAP_DISTANCE) to another "brother"
	 * piece, if so it will snap to this one. Meaning that it will additionally
	 * move the remainig length to the other piece and returns true if something
	 * to snap to was found and this was snapped to, false otherwise
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * teste se esta peça está perto (MAX_SNAP_DISTANCE) de outro "irmão"
	 * peça, em caso afirmativo, ele se ajustará a esta. O que significa que será adicionalmente
	 * move o comprimento restante para a outra peça e retorna verdadeiro se algo
	 * para ajustar foi encontrado e este foi ajustado, caso contrário, falso
	 * @throws JigsawPuzzleException 
	 */

	public PuzzlePiece snap() throws JigsawPuzzleException {
		
		GameCommander gC = GameCommander.getInstance();

		List<Edge> myResolvableEdges = this.getResolvableEdges();
		List<PuzzlePiece> allPieces = gC.getPieceDisposer().getPuzzlePieces();
		for (Edge ownEdge : myResolvableEdges) { // for all own open edges
												 // para todas as próprias bordas abertas

			// edgeNumber -> get contrary type
			// edgeNumber -> obter tipo contrário
			int ownEdgeNumber = ownEdge.getEdgePairNumber();
			Edge.Type contraryEdgeType = Edge.contraryEdgeChar(ownEdge.getType());

			for (PuzzlePiece piece : allPieces) { // for every piece
												  // para cada peça
				if (piece == this) {
					continue; // if same contine
							  // se o mesmo continuar
				}
				// get all edges of the desired type (contraryEdgeType)
				// obtém todas as bordas do tipo desejado (counterEdgeType)
				List<Edge> list = piece.getResolvableEdges(contraryEdgeType);
				if (list.isEmpty()) {
					continue; // if none continue
							  // se nenhum continuar
				}
				// find the contrary edge by it's number
				// encontre a borda contrária pelo seu número
				Edge contraryEdge = null;
				for (Edge k : list) {
					if (k.getEdgePairNumber() == ownEdgeNumber) {
						contraryEdge = k;
						break;
					}
				}

				if (contraryEdge == null) {
					continue; // if the contrary Edge not in this piece go on
							  // se o contrário Edge não estiver nesta peça, continue
				}

				Point firstPoint = ownEdge.calculatePoint();
				Point secondPoint = contraryEdge.calculatePoint();

				final double dist = firstPoint.distance(secondPoint);

				if (dist < PuzzleProperties.MAX_SNAP_DISTANCE) {

					Offset offToFit = new Offset(secondPoint.x - firstPoint.x,
							secondPoint.y - firstPoint.y);

					this.move(offToFit);
					// retrieve the new piece
					// recupere a nova peça
					PuzzlePiece pp = GameCommander.getInstance().getPieceDisposer().assamblyPieces(this, ownEdge,
									piece, contraryEdge);
					return pp;
				}
			}
		}
		return null;
	}

	/**
	 * to retrieve all edges
	 * MODEL
	 */
	/**
	 * para recuperar todas as bordas
	 * MODELO
	 */
	public List<Edge> getEdges() {
		return this.edges;
	}

	/**
	 * to retrieve all edges from the parameter type
	 * MODEL
	 */
	/**
	 * para recuperar todas as arestas do tipo de parâmetro
	 * MODELO
	 */
	public abstract List<Edge> getEdges(Edge.Type typ);

	/**
	 * to retrieve a specific edge with the parameter number
	 * MODEL
	 */
	/**
	* para recuperar uma borda específica com o número do parâmetro
	* MODELO
	*/
	public Edge getEdge(int edgeNumber) {
		List<Edge> liste = this.getEdges();

		for (Edge k : liste) {
			if (k.getEdgePairNumber() == edgeNumber)
				return k;
		}
		return null;
	}

	/**
	 * retrieve edges that are not already closed!
	 * Also edges at the border of the puzzle
	 * are returned, because they will never be closed!
	 * MODEL
	 */
	/**
	 * recupere bordas que ainda não estão fechadas!
	 * Também bordas na borda do quebra-cabeça
	 * são devolvidos, pois nunca serão fechados!
	 * MODELO
	 */
	public List<Edge> getOpenEdges() {
		List<Edge> liste = this.getEdges();
		List<Edge> erg = new Vector<Edge>();

		for (Edge k : liste) {
			if (k.isOpen())
				erg.add(k);
		}

		return erg;
	}
	
	/**
	 * retrieve an open edge with the specified number, or null if such an edge
	 * wasn't found
	 * MODEL
	 */
	/**
	 * recuperar uma borda aberta com o número especificado, ou nulo se tal borda* não foi encontrado
	 * MODELO
	 */
	public Edge getOpenEdge(int edgeNumber) {
		List<Edge> liste = this.getOpenEdges();

		for (Edge k : liste) {
			if (k.isOpen() && k.getEdgePairNumber() == edgeNumber)
				return k;
		}
		return null;

	}
	
	/**
	 * retrieve edges that have a counterpart in this game, and 
	 * are not already connected to that counterpart.
	 * MODEL
	 * @return
	 */
	/**
	 * recuperar arestas que têm uma contrapartida neste jogo, e
	 * ainda não estão conectados a essa contraparte.
	 * MODELO
	 * @return
	 */
	public List<Edge> getResolvableEdges() {
		List<Edge> liste = this.getEdges();
		List<Edge> erg = new Vector<Edge>();

		for (Edge k : liste) {
			if (k.isResolvable())
				erg.add(k);
		}

		return erg;
	}

	/**
	 * retrieves the open resolvable(open and not NULL type) of the specified type
	 * MODEL
	 */
	/**
	 * recupera o aberto resolvível (tipo aberto e não NULL) do tipo especificado
	 * MODELO
	 */
	public List<Edge> getResolvableEdges(Edge.Type edgeType) {
		List<Edge> liste = this.getEdges(edgeType);
		List<Edge> erg = new Vector<Edge>();

		for (Edge k : liste) {
			if ((k.isOpen()))
				erg.add(k);
		}
		return erg;
	}

	

	/**
	 * Retrieves 1 for single pieces, and the number of single pieces for multi
	 * pieces
	 * MODEL
	 */
	/**
	 * Recupera 1 para peças individuais e o número de peças individuais para multi
	 * peças
	 * MODELO
	 */
	public abstract int getPieceCount();

	/**
	 * moves the piece the specified offset
	 * MODEL
	 */
	/**
	 * move a peça no deslocamento especificado
	 * MODELO
	 */
	public abstract void move(Offset to);

}
