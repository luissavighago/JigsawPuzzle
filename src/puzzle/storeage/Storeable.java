package puzzle.storeage;


import org.w3c.dom.Node;


/**
 * to be able to store certain objects
 * @author Heinz
 *
 */
/**
 * ser capaz de armazenar certos objetos
 * @autor Heinz
 *
 */
public interface Storeable {
	
	/**
	 * store objects into the given element path
	 * @param current
	 * @throws SaveGameException
	 */
	/**
	 * armazenar objetos no caminho de elemento fornecido
	 * @param atual
	 * @throws SaveGameException
	 */
	public void store(Node current) throws SaveGameException;
	
	/**
	 * restore objects directly from the given element path
	 * @param current
	 * @throws LoadGameException
	 */
	/**
	 * restaura objetos diretamente do caminho do elemento fornecido
	 * @param atual
	 * @throws LoadGameException
	 */
	public void restore(Node current) throws LoadGameException;

}
