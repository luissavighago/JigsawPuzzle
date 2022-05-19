package puzzle.storeage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Base64;

public class StorageUtil {
	
	/**
	 * find a direct node under the provided node with the name given in
	 * nodeName
	 * 
	 * @param parent
	 * @param nodeName
	 * @return Node if one was found according to the name or null otherwise
	 */
	/**
	 * encontre um nó direto sob o nó fornecido com o nome dado em
	 * nodeName
	 * 
	 * @param parent
	 * @param nodeName
	 * @return Nó se um foi encontrado de acordo com o nome ou nulo caso contrário
	 */
	public static Node findDirectChildNode(Node parent, String nodeName) {

		NodeList nodeList = parent.getChildNodes();
		int nodeSize = nodeList.getLength();
		for (int i = 0; i < nodeSize; i++) {
			Node act = nodeList.item(i);
			if (nodeName.equals(act.getNodeName())) {
				return act;
			}
		}
		return null;
	}

	/**
	 * used to store a special portion of binary data represented as byte[] to a
	 * given element. Use restoreBinaryData as the reversion
	 * 
	 * @param e
	 *            to which to store to
	 * @param nodeName
	 *            the newly created node under which the data should be stored
	 * @param data
	 *            the data to be stored
	 */
	/**
	 * usado para armazenar uma porção especial de dados binários representados como byte [] para um
	 * determinado elemento. Use restoreBinaryData como a reversão
	 * 
	 * @param e
	 *            para o qual armazenar para
	 * @param nodeName
	 *            o nó recém-criado sob o qual os dados devem ser armazenados
	 * @param data
	 *            os dados a serem armazenados
	 */
	public static void storeBinaryData(Node e, String nodeName, byte[] data) {
		// encode the byte stream in base64
		// codifica o fluxo de bytes em base64
		String b64EncodedData = Base64.getEncoder().encodeToString(data);		

		Document doc = e.getOwnerDocument();

		// form an outer node with the requested name
		// forma um nó externo com o nome solicitado
		Node outerNode = doc.createElement(nodeName);
		// init a anonymous CDATA section
		// init uma seção CDATA anônima
		CDATASection dataNode = doc.createCDATASection("");
		// put the data into the section
		// coloque os dados na seção
		dataNode.setNodeValue(b64EncodedData);
		// append the datanode as child to the outer node and the outer node to
		// e
		// anexa o datanode como filho ao nó externo e o nó externo ao
		// e
		outerNode.appendChild(dataNode);
		e.appendChild(outerNode);
	}

	/**
	 * Restores a portion of bytes from a given element and the given nodeName.
	 * Use storeBinaryData as the reversal method
	 * 
	 * @param e
	 *            the element from which to restore
	 * @param nodeName
	 *            the name under which the data was stored
	 * @return the data that was stored
	 * @throws LoadGameException 
	 * @throws IOException
	 */
	/**
	 * Restaura uma parte dos bytes de um determinado elemento e do nodeName fornecido.
	 * Use storeBinaryData como método de reversão
	 * 
	 * @param e
	 *            o elemento a partir do qual restaurar
	 * @param nodeName
	 *            o nome sob o qual os dados foram armazenados
	 * @return os dados que foram armazenados
	 * @throws LoadGameException 
	 * @throws IOException
	 */
	public static byte[] restoreBinaryData(Node e, String nodeName) throws LoadGameException {
		// get the outer node
		// obtém o nó externo
		Node outerNode = findDirectChildNode(e, nodeName);
		// get the CDATA child section
		// obter a seção filho CDATA
		CDATASection dataNode = (CDATASection) outerNode.getChildNodes()
				.item(0);

		// decode from base 64
		// decodifica da base 64
		byte[] data;
		try {
			data = Base64.getDecoder().decode(dataNode.getData());
			return data;
		} catch (DOMException e1) {
			e1.printStackTrace();
			throw new LoadGameException(e1);
		}
		
	}

	/**
	 * stores a serialisable object into that node, use
	 * restoreSerialisableObject as reverse function. Internally uses
	 * storeBinaryData function
	 * 
	 * @param e
	 *            the node under which to store
	 * @param nodeName
	 *            name of the node that will be created under e for carring the
	 *            data of the serialisable object
	 * @param serialObj
	 *            the object to store
	 * @throws SaveGameException 
	 * @throws IOException
	 */
	/**
	 * armazena um objeto serializável nesse nó, use
	 * restoreSerialisableObject como função reversa. Usa internamente
	 * função storeBinaryData
	 * 
	 * @param e
	 *            o nó sob o qual armazenar
	 * @param nodeName
	 *            nome do nó que será criado em e para realizar o
	 * 			  dados do objeto serializável
	 * @param serialObj
	 *            o objeto para armazenar
	 * @throws SaveGameException 
	 * @throws IOException
	 */
	public static void storeSerialisableObject(Node e, String nodeName,
			Object serialObj) throws SaveGameException {
		// create byte array stream to write to and surround by a
		// objectoutputstream
		// cria fluxo de matriz de bytes para escrever e circundar por um
		// objectoutputstream
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(byteArrayStream);
			oos.writeObject(serialObj);
			oos.flush();
			oos.close();
			byteArrayStream.close();
			storeBinaryData(e, nodeName, byteArrayStream.toByteArray());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new SaveGameException(e1);
		}
		
	}

	/**
	 * restores a serialisable object that was saved with the function
	 * storeSerialisableObject.
	 * 
	 * @param e
	 *            the node under which the data lies that should be restored
	 * @param nodeName
	 *            name of the node under which the serialisable object was
	 *            stored
	 * @return the object that was saved
	 * @throws LoadGameException 
	 * @throws IOException
	 */
	/**
	 * restaura um objeto serializável que foi salvo com a função
	 * storeSerialisableObject.
	 * 
	 * @param e
	 *            o nó sob o qual estão os dados que devem ser restaurados
	 * @param nodeName
	 *            nome do nó sob o qual o objeto serializável foi
	 *            armazenado
	 * @return o objeto que foi salvo
	 * @throws LoadGameException 
	 * @throws IOException
	 */
	public static Object restoreSerialisableObject(Node e, String nodeName) throws LoadGameException {
		try {
			byte[] data = restoreBinaryData(e, nodeName);
			ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(
					data);
			ObjectInputStream ois = new ObjectInputStream(byteArrayStream);

			return ois.readObject();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new LoadGameException(e1);
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new LoadGameException(e2);
		}
	}

	/**
	 * creates an empty DOM Document
	 * 
	 * @return the newly created document
	 * @throws ParserConfigurationException
	 */
	/**
	 * cria um documento DOM vazio
	 * 
	 * @return o documento recém-criado
	 * @throws ParserConfigurationException
	 */
	public static Document createDOMDocument() throws SaveGameException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try {
			builder = factory.newDocumentBuilder();
			return builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new SaveGameException(e);
		}
	}

	/**
	 * saves the given document in a File with the given name
	 * 
	 * @param doc
	 *            the document to save
	 * @param filename
	 *            the filename
	 * @throws SaveGameException
	 */
	/**
	 * salva o documento fornecido em um arquivo com o nome fornecido
	 * 
	 * @param doc
	 *            o documento para salvar
	 * @param filename
	 *            o nome do arquivo
	 * @throws SaveGameException
	 */
	public static void saveAsXML(Document doc, File file)
			throws SaveGameException {
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer aTransformer;
		try {
			aTransformer = tranFactory.newTransformer();

			Source src = new DOMSource(doc);
			FileOutputStream fos = new FileOutputStream(file);
			Result dest = new StreamResult(fos);

			aTransformer.transform(src, dest);
			fos.flush();
			fos.close();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveGameException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveGameException(e);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new SaveGameException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveGameException(e);
		}

	}

}
