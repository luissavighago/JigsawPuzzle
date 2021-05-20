package puzzle.ui.extensions;

import java.io.File;
import java.util.List;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;

import puzzle.PuzzleProperties;

/**
 * This class is a FileFilter that filters
 * files for their extensions.
 * E.g. it is capable of finding all .jpg
 * files within a directory.
 * Use it in JFileChooser.setFileFilter
 * @author Heinz
 */
/**
 * Esta classe é um FileFilter que filtra
 * arquivos para suas extensões.
 * Por exemplo. é capaz de encontrar todos os .jpg
 * arquivos dentro de um diretório.
 * Use-o em JFileChooser.setFileFilter
 * @autor Heinz
 */
public class ExtensionFileFilter extends FileFilter {
    
	/**
	 * the standard delimter which delimts the filename
	 * from it's extension!
	 */
	/**
	 * o delimitador padrão que delimita o nome do arquivo
	 * de sua extensão!
	 */
	private final static char standardDelimiter = '.';
	
	/**
     * the list of extensions
     */
	/**
     * a lista de extensões
     */
    private List<String> extensions;
    
    /**
     * if true than this FileFilter accepts
     * directories (shows them one can go into)
     * if false than this is not allowed
     */
    /**
     * se verdadeiro, então o FileFilter aceita
     * diretórios (mostra a eles que se pode entrar)
     * se falso, então não é permitido
     */
    private boolean acceptDirectorys;
    
    public ExtensionFileFilter() {
        this.extensions = new Vector<String>();
        this.acceptDirectorys = true;
    }
    
    public void addExtension(String extension) {
        String toadd = extension.toLowerCase();
        for (String st: extensions) {
            if (st.equalsIgnoreCase(toadd)) return;
        }
        this.extensions.add(toadd);
    }
    
    public void removeExtension(String extension) {
        String toadd = extension.toLowerCase();
        for (String st: extensions) {
            if (st.equalsIgnoreCase(toadd)) {
                extensions.remove(st);
            };
        }
    }
    
    public boolean acceptsDirectorys() {
        return this.acceptDirectorys;
    }
    
    public void setDirectoryAccept(boolean da) {
        this.acceptDirectorys = da;
    }
    
    public boolean accept(File f) {
        
        if ((this.acceptDirectorys) && f.isDirectory()) return true;
        String filename = f.getName().toLowerCase(); 
        for (String st: extensions) {
            if (filename.length() > st.length()) {
                String last = filename.substring(filename.length()-st.length()-1);
                if (!last.startsWith(standardDelimiter+"")) continue;
                if (st.equalsIgnoreCase(last.substring(1))) return true;
            }
            
        }
        return false;
    }
    
    public String getDescription() {
        StringBuffer StB = new StringBuffer();

        if (!this.extensions.isEmpty()) {
            StB.append(PuzzleProperties
            		.getLocalized("acceptedExtensions"));
            
            for (String st: extensions) {
                StB.append(standardDelimiter+st+" ");
            }
        }
        return new String(StB);
    }
    
}
