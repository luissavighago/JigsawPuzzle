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
public interface GameEventListener {
	
	/**
	 * is called if an event happened in the 
	 * GameState
	 * @param ge the information about the event
	 * @throws JigsawPuzzleException
	 */
	public void eventHappened(GameEvent ge) throws JigsawPuzzleException ;

}
