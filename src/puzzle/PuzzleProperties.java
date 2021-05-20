/*
 * PuzzleProperties.java
 *
 * Created on 14. September 2006, 11:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package puzzle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ResourceBundle;

/**
 * 
 * @author Heinz
 */
/**
 * 
 * @autor Heinz
 */
public class PuzzleProperties {

	public static final String APPLICATION_ICON_FILE = "/pics/puzzle.gif";
	
	public static final String GAME_STORAGE_VERSION = "1.5";
	
	public static final String GAME_VERSION = "1.5.4";
	
	public static final String GAME_BUILD_VERSION = GAME_VERSION + " build 10. Juli 2010";

	public static final int[] EDGE_LENGTH = { 20, 40, 60 };

	public static final double MAX_SNAP_DISTANCE = 20.0;

	public static final Color PIECE_COLOR = Color.BLACK;
	
	public static final Color PIECE_HIGHLIGHTED_COLOR = Color.GREEN;

	public static final Color PIECE_SHADOW_COLOR = new Color(128, 128, 128, 128);

	public static final Color BACKGROUND_COLOR = Color.white;

	/*
	public static final Stroke PIECE_STROKE = new BasicStroke(2.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, null, 0.0f);
	*/
	
	private static ResourceBundle resourceBundle;
	
	/**
	 * prefix for language file
	 */
	/**
	 * prefixo para arquivo de linguagem
	 */
	private static final String languageFilePrefix = "puzzleLanguage";

	/**
	 * the placeholder if no language has been loaded, or key has no value
	 */
	/**
	 * o placeholder se nenhum idioma tiver sido carregado ou a chave não tiver valor
	 */
	private static final String placeholder = "*";

	/**
	 * indicates whether the language has been loaded or not
	 */
	/**
	 * indica se o idioma foi carregado ou não
	 */
	private static boolean languageInitiated;
	
	/**
	 * returns the local text String for this key
	 */
	/**
	 * retorna a string de texto local para esta chave
	 */
	public static String getLocalized(String key) {
    	String localValue = null;
    	if (languageInitiated) {
    		localValue = resourceBundle.getString(key);
    	} 
    	if (localValue == null) {
    		return placeholder;
    	} else if (localValue.equals("")) {
    		return "**********no value present**********";
    	} else {
    		return localValue;
    	}
    }
	
	public static void loadLocal() {
		resourceBundle = ResourceBundle.getBundle(languageFilePrefix);
		languageInitiated = true;
	}
	
	/** Creates a new instance of PuzzleProperties */
	/** Cria uma nova instância de PuzzleProperties */
	private PuzzleProperties() {
		
	}

}
