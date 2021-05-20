/*
 * Kante.java
 *
 * Created on 27. August 2006, 15:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package puzzle.edge;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import puzzle.GameCommander;
import puzzle.Offset;
import puzzle.Turnable;
import puzzle.pieces.SinglePiece;
import puzzle.storeage.JigsawPuzzleException;
import puzzle.storeage.LoadGameException;
import puzzle.storeage.SaveGameException;
import puzzle.storeage.StorageUtil;
import puzzle.storeage.Storeable;

/**
 * 
 * @author Heinz
 */
/**
 * 
 * @autor Heinz
 */
public class Edge implements Storeable, Turnable {
	
	private static final Logger logger = Logger.getLogger(Edge.class);

	/**
	 * the different types of edges
	 * 
	 * @author Heinz
	 * 
	 */
	/**
	 * os diferentes tipos de bordas
	 * 
	 * @autor Heinz
	 * 
	 */
	public static enum Type {
		TOP, BOTTOM, RIGHT, LEFT, NULL
	}

	/**
	 * the type of this edge
	 * MODEL
	 */
	/**
	 * o tipo desta borda
	 * MODELO
	 */
	private Type type;

	/**
	 * always two edges share the same number
	 * MODEL
	 */
	/**
	 * sempre duas arestas compartilham o mesmo número
	 * MODELO
	 */
	private int edgePairNumber;

	/**
	 * the path of this edge
	 * VIEW
	 */
	/**
	 * o caminho desta borda
	 * VISUALIZAR
	 */
	private Shape edgeShape;

	/**
	 * the offset for the edges relative to the piece
	 * VIEW
	 */
	/**
	 * o deslocamento para as arestas em relação à peça
	 * VISUALIZAR
	 */
	private Offset offset;

	/**
	 * the piece for that edge
	 * MODEL
	 */
	/**
	 * a peça para aquela borda
	 * MODELO
	 */
	private SinglePiece ownerPiece;

	/**
	 * indicates whether the ownerPiece was set
	 * MODEL
	 */
	/**
	 * indica se ownerPiece foi definido
	 * MODELO
	 */
	private boolean isOwnerPieceSet = false;

	/**
	 * indicate where this edge has found his partner or not
	 * MODEL
	 */
	/**
	 * indique onde esta vantagem encontrou seu parceiro ou não
	 * MODELO
	 */
	private boolean open;

	/**
	 * creates an edge
	 * 
	 * @param type
	 *            OBEN, UNTEN, RECHTS oder LINKS
	 * @param edgePairNumber
	 *            the number
	 * @param versatz
	 *            der Versatz vom Mittelpunkt des Puzzlestücks zu dem
	 *            Kantenpunkt
	 */
	/**
	 * cria uma vantagem
	 * 
	 * @param modelo
	 *            OBEN, UNTEN, RECHTS oder LINKS
	 * @param edgePairNumber
	 *            o número
	 * @param versatz
	 *            der Versatz vom Mittelpunkt des Puzzlestücks zu dem
	 *            Kantenpunkt
	 */
	public Edge(Type type, int edgePairNumber, GeneralPath path) {
		this.type = type;
		this.edgePairNumber = edgePairNumber;
		this.open = true;
		this.edgeShape = path;
	}
	
	/**
	 * constructor should only be used for restoring
	 */
	/**
	 * construtor deve ser usado apenas para restaurar
	 */
	public Edge() {
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type typ) {
		this.type = typ;
	}

	public int getEdgePairNumber() {
		return this.edgePairNumber;
	}

	public Offset getOffset() {
		int x = offset.getX();
		int y = offset.getY();
		return new Offset(x, y);
	}

	public void setOffset(Offset offs) {
		this.offset = new Offset(offs.getX(), offs.getY());
	}

	public SinglePiece getOwnerPiece() {
		return this.ownerPiece;
	}

	public void setOwnerPiece(SinglePiece piece) throws JigsawPuzzleException {
		if (!isOwnerPieceSet) {
			this.ownerPiece = piece;
			isOwnerPieceSet = true;
		} else {
			logger.error("tried to reset the owner");
			throw new JigsawPuzzleException("tried to reset the owner!");
		}
	}

	/**
	 * returns true if this edge is open
	 * @return true if open, false otherwise
	 */
	/**
	 * retorna verdadeiro se esta borda estiver aberta
	 * @return true se aberto, false caso contrário
	 */
	public boolean isOpen() {
		return this.open;
	}
	
	/**
	 * an edge is resolvable if it is open and not of the Type.NULL
	 * @return
	 */
	/**
	 * uma borda é resolvível se for aberta e não do Type.NULL
	 * @return
	 */
	public boolean isResolvable() {
		if ((this.isOpen())&&(getType() != Type.NULL)) {
			return true;
		}
		return false;
	}

	/**
	 * you can never reopen a closed pair of edges
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * você nunca pode reabrir um par fechado de arestas
	 * @throws JigsawPuzzleException 
	 */
	public void close() throws JigsawPuzzleException {
		if (this.type == Type.NULL) {
			throw new JigsawPuzzleException("never close a NULL type edge.");
		}
		this.open = false;
	}

	/**
	 * returns the middlepoint of the shape added to the offset
	 * TODO this seems to be a problem because one uses the ownerPiece to calculate the point
	 * and on the other hand has something like an offset - not too lucky
	 */
	/**
	 * retorna o ponto médio da forma adicionada ao deslocamento
	 * TODO isso parece ser um problema porque se usa o ownerPiece para calcular o ponto
	 * e, por outro lado, tem algo como um deslocamento - não é muito sortudo
	 */
	public Point calculatePoint() {
		Point psp = ownerPiece.getPoint();
		int x = psp.x + offset.getX();
		int y = psp.y + offset.getY();
		return new Point(x, y);
	}
	
	/**
	 * moves the whole shape of this piece!
	 * @param to
	 */
	/**
	 * move toda a forma desta peça!
	 * @param to
	 */
	public void move(Offset to) {
		AffineTransform af = AffineTransform.getTranslateInstance(to.getX(), to
				.getY());
		this.edgeShape = af.createTransformedShape(this.edgeShape);
	}

	public Shape getShape() {
		return this.edgeShape;
	}
	
	public void turnDegrees(Point turnPoint, int degree) {
		
		logger.debug("rotating Edge");
		
		final int halfSideLength = GameCommander.getInstance().getPreferences()
		.getSideLength() / 2;
		
		AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(degree), turnPoint.x, turnPoint.y);
		this.edgeShape = af.createTransformedShape(this.edgeShape);
		
		// setting the new offset is kind of correct!
		// TODO of course this also is a problem! 
		// definir o novo deslocamento é meio correto!
		// TODO claro que isso também é um problema!
		switch (this.type) {
		case BOTTOM:
			this.setType(Edge.Type.LEFT);
			this.setOffset(new Offset(-halfSideLength, 0));
			break;
		case LEFT:
			this.setType(Edge.Type.TOP);
			this.setOffset(new Offset(0, -halfSideLength));
			break;
		case RIGHT:
			this.setType(Edge.Type.BOTTOM);
			this.setOffset(new Offset(0, halfSideLength));
			break;
		case TOP:
			this.setType(Edge.Type.RIGHT);
			this.setOffset(new Offset(halfSideLength, 0));
			break;
		case NULL:
			break;
		}
	}

	/**
	 * get contrary edge's number.
	 * 
	 * @param edgeChar
	 *            my edge's char
	 * @return the contrary edge's number.
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * obtenha o número da borda contrária.
	 * 
	 * @param edgeChar
	 *            o char da minha borda
	 * @return o número da borda contrária.
	 * @throws JigsawPuzzleException 
	 */
	public static Type contraryEdgeChar(Type edgeChar) throws JigsawPuzzleException {
		switch (edgeChar) {
		case TOP:
			return Type.BOTTOM;
		case BOTTOM:
			return Type.TOP;
		case RIGHT:
			return Type.LEFT;
		case LEFT:
			return Type.RIGHT;
		case NULL:
			return Type.NULL;
		default:
			throw new JigsawPuzzleException("Invalid Type");
		}
	}

	@Override
	public void restore(Node current) throws LoadGameException {
		Node edge = StorageUtil.findDirectChildNode(current, "Edge");
		
		NamedNodeMap nnm = edge.getAttributes();
		Node n;
		
		n = nnm.getNamedItem("Type");
		int t = Integer.parseInt(n.getNodeValue());
		switch (t) {
		case 1:
			this.type = Type.TOP;
			break;
		case 2:
			this.type = Type.BOTTOM;
			break;
		case 3:
			this.type = Type.RIGHT;
			break;
		case 4:
			this.type = Type.LEFT;
			break;
		case 5:
			this.type = Type.NULL;
			break;
		default:
			throw new LoadGameException("Invalid Type");
		}
		
		n = nnm.getNamedItem("EdgePairNumber");
		this.edgePairNumber = Integer.parseInt(n.getNodeValue());
		
		n = nnm.getNamedItem("Open");
		this.open = Boolean.parseBoolean(n.getNodeValue());
		
		this.edgeShape = (Shape) StorageUtil.restoreSerialisableObject(edge, "EdgePath");

		Node edgeOffset = StorageUtil.findDirectChildNode(edge, "EdgeOffset");
		
		this.offset = new Offset();
		this.offset.restore(edgeOffset);
	}

	@Override
	public void store(Node current) throws SaveGameException {
		Document doc = current.getOwnerDocument();
		Element edge = doc.createElement("Edge");
		
		int t;
		switch (this.type) {
		case TOP:
			t = 1;
			break;
		case BOTTOM:
			t = 2;
			break;
		case RIGHT:
			t = 3;
			break;
		case LEFT:
			t = 4;
			break;
		case NULL:
			t = 5;
			break;
		default:
			throw new SaveGameException("Invalid Type");
		}
		edge.setAttribute("Type", ""+t);
		edge.setAttribute("EdgePairNumber", ""+this.edgePairNumber);
		
		StorageUtil.storeSerialisableObject(edge, "EdgePath", this.edgeShape);
		
		Element offset = doc.createElement("EdgeOffset");
		this.offset.store(offset);
		
		/* owner piece is not set but done in single piece!
		 * so this also applies for isOwnerPieceSet. 
		 */
		/* peça do proprietário não é definida, mas feita em uma única peça!
		 * * então isso também se aplica a isOwnerPieceSet.
		 */
		
		edge.setAttribute("Open", ""+this.open);

		edge.appendChild(offset);
		current.appendChild(edge);
	}
	
	public String toString() {
		return this.getClass().getName()+", EdgePairNumber:"+getEdgePairNumber()+
		", Type:"+getType()+", Offset:"+offset.toString()+", open?"+this.open;
	}

}
