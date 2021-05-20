package puzzle.edge;

import puzzle.pieces.SinglePiece;

public class EdgeModel {
	
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
	private boolean isOwnerPieceSet;

	/**
	 * indicate where this edge has found his partner or not
	 * MODEL
	 */
	/**
	 * indique onde esta vantagem encontrou seu parceiro ou não
	 * MODELO
	 */
	private boolean open;

}
