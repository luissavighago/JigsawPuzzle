package puzzle.gameevent;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import puzzle.storeage.JigsawPuzzleException;

/**
 * Event Listener implementation.
 * This is sth. like the observer pattern,
 * this is the Observable anybody implementing
 * the GameEventListener interface can add himself
 * for getting informed about events.
 * @author Heinz
 *
 */
/**
 * Implementação do listener de eventos.
 * Este é o sth. como o padrão do observador,
 * este é o observável que qualquer um está implementando
 * a interface GameEventListener pode adicionar a si mesmo
 * para se informar sobre eventos.
 * @autor Heinz
 *
 */
public class GameState {
	
	private static Logger logger = Logger.getLogger(GameState.class);

	/**
	 * all listeners
	 */
	/**
	 * todos os ouvintes
	 */
	private List<GameEventListener> gameListeners;

	public GameState() {
		this.gameListeners = new ArrayList<GameEventListener>();
	}

	/**
	 * this method informs all the appended listeners about the
	 * change that is defined by the GameEvent ge.
	 * @param ge
	 * @throws JigsawPuzzleException
	 */
	/**
	 * este método informa todos os ouvintes anexados sobre o
	 * alteração que é definida pelo GameEvent ge.
	 * @param ge
	 * @throws JigsawPuzzleException
	 */
	public void deliverEvent(GameEvent ge) throws JigsawPuzzleException {
		//logger.debug("delivering event: " + ge.toString());
		for (GameEventListener gel : this.gameListeners) {
			gel.eventHappened(ge);
		}
	}
	
	/**
	 * add sb. as a listener for game events.
	 * @param gel
	 */
	/**
	 * adicione sb. como ouvinte de eventos de jogos.
	 * @param gel
	 */
	public void addListener(GameEventListener gel) {
		//logger.debug("adding listener: " + gel.getClass().getCanonicalName());
		if (!this.gameListeners.contains(gel)) {
			this.gameListeners.add(gel);
		}
	}
	
	/**
	 * remove sb. as the listener for game events.
	 * @param gel
	 * @return
	 */
	/**
	 * remover sb. como o ouvinte dos eventos do jogo.
	 * @param gel
	 * @return
	 */
	public boolean removeListener(GameEventListener gel) {
		//logger.debug("removing listener: " + gel.getClass().toString());
		return this.gameListeners.remove(gel);
	}

}
