package puzzle.edge;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Map;

import puzzle.GameCommander;
import puzzle.storeage.JigsawPuzzleException;

/**
 * 
 * @author Heinz
 */
/**
 * 
 * @autor Heinz
 */
public class EdgeDisposer {

	/**
	 * this one saves the contrary edges
	 */
	/**
	 * este salva as bordas contrárias
	 */
	private final Map<Integer, GeneralPath> edgePaths;

	public EdgeDisposer() {
		this.edgePaths = new HashMap<Integer, GeneralPath>();
	}

	public void reset() {
		this.edgePaths.clear();
	}

	/**
	 * generates a new randomly choosen edge shape (as GeneralPath instance)
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * gera uma nova forma de borda escolhida aleatoriamente (como instância GeneralPath)
	 * @throws JigsawPuzzleException 
	 */
	public GeneralPath generateNewEdgeShape(int edgeNumber, Edge.Type type) throws JigsawPuzzleException {
		
		AbstractEdgeProducer producer = GameCommander.getInstance().getPreferences().getEdgeProducer();
		producer.init(GameCommander.getInstance().getPreferences()
				.getSideLength());
		producer.produce();

		edgePaths.put(edgeNumber, new GeneralPath(producer.getBothShapes()[0]));
		GeneralPath copy = new GeneralPath(producer.getBothShapes()[1]);
		turnToType(type, copy);
		return copy;
	}

	/**
	 * finds the contrary edge shape that fits to the generated shape with the
	 * same edgeNumber.
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * encontra a forma de borda contrária que se ajusta à forma gerada com o
	 * mesmo edgeNumber.
	 * @throws JigsawPuzzleException 
	 */
	public GeneralPath findContraryEdgeShape(int edgeNumber, Edge.Type type) throws JigsawPuzzleException {

		if (!edgePaths.containsKey(edgeNumber))
			throw new JigsawPuzzleException("error in shape list occured: no shape");
		if (edgePaths.get(edgeNumber) == null)
			throw new JigsawPuzzleException("error in shape list occured: shape null");
		
		Shape s = edgePaths.get(edgeNumber);
		edgePaths.remove(edgeNumber);
		GeneralPath copy = new GeneralPath(s);
		turnToType(type, copy);
		return copy;
	}

	/**
	 * turns this shape to be in the right
	 * 
	 * @param typ
	 *            turn to this type
	 * @param tomodify
	 *            the gp instance which should be turned
	 * @throws JigsawPuzzleException 
	 */
	/**
	 * transforma esta forma para ficar à direita
	 * 
	 * @param typ
	 *            virar para este tipo
	 * @param para modificar
	 *            a instância gp que deve ser ativada
	 * @throws JigsawPuzzleException 
	 */
	private void turnToType(Edge.Type typ, GeneralPath tomodify) throws JigsawPuzzleException {

		switch (typ) {

		case TOP:
			// do nothing because it's already in correct place
			// não faça nada porque já está no lugar correto
			break;

		case BOTTOM:
			tomodify.transform(AffineTransform.getRotateInstance(Math
					.toRadians(180), 0, 0));
			break;

		case LEFT:
			tomodify.transform(AffineTransform.getRotateInstance(Math
					.toRadians(270), 0, 0));
			break;

		case RIGHT:
			tomodify.transform(AffineTransform.getRotateInstance(Math
					.toRadians(90), 0, 0));
			break;

		default:
			throw new JigsawPuzzleException("Invalid Type");
		}

	}
}
