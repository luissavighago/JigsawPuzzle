package puzzle.gameevent;

import puzzle.storeage.JigsawPuzzleException;

/**
 * As this is sth. like the Observer interface.
 * An implementing instance can add himself to
 * a GameState to reviece Events through the
 * eventHappened method.
 * @author Heinz
 *
 */
/**
 * 
Como isso é sth. como a interface do Observer.
 * Uma instância de implementação pode se adicionar a
 * um GameState para reviver eventos por meio do
 * método eventHappened.
 * @autor Heinz
 *
 */
public interface GameEventListener {
	
	/**
	 * is called if an event happened in the 
	 * GameState
	 * @param ge the information about the event
	 * @throws JigsawPuzzleException
	 */
	/**
	 * é chamado se um evento aconteceu no
	 * GameState
	 * @param obter as informações sobre o evento
	 * @throws JigsawPuzzleException
	 */
	public void eventHappened(GameEvent ge) throws JigsawPuzzleException ;

}
