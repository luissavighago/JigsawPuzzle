/*
 * Main.java
 *
 * Created on 27. August 2006, 15:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package puzzle;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import puzzle.ui.GameMainWindow;

/**
 *
 * @author Heinz
 */
/**
*
* @autor Heinz
*/
public class Main {
	
	private static Logger logger = Logger.getLogger(Main.class);
    
    /** Creates a new instance of Main */
	/** Cria uma nova instância de Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    /**
     * @param args os argumentos da linha de comando
     */
    public static void main(String[] args) {
    	setLookAndFeel();

    	
    	logger.debug("loading properties");
    	PuzzleProperties.loadLocal();
    	logger.debug("loading Window");
        GameMainWindow.startUI();
    }

	private static void setLookAndFeel() {
		try {
			LookAndFeel[] lnfs = UIManager.getAuxiliaryLookAndFeels();
			if (lnfs != null) {
				for (LookAndFeel lnf : lnfs)
					System.out.println(lnf);
			}

			UIManager.setLookAndFeel(
			        "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
			// TODO Auto-generated catch block
			// TODO bloco de captura gerado automaticamente
			e.printStackTrace();
		}
	}

}
