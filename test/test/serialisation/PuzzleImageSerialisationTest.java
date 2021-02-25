package test.serialisation;

import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.junit.Test;

import puzzle.storeage.JigsawPuzzleException;
import puzzle.ui.PuzzleImage;

public class PuzzleImageSerialisationTest {
	
	
	@Test
	public void testSerializationGeneral() {
		String file = "fileTest.ser";
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(new Date());
			oos.writeObject("hAlLo");
			oos.writeInt(42);
			oos.writeObject(new Dimension(123, 321));
			oos.close();
			// done
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Date d = (Date) ois.readObject();
			String s = (String) ois.readObject();
			int i = ois.readInt();
			Dimension dim = (Dimension) ois.readObject();
			
			ois.close();
			System.out.println("string: " + s + " int: " + i + " date " + d + " dimen " + dim);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//@Test
	public void testAll() {

		PuzzleImage pz = null;
		try {
			pz = new PuzzleImage(getClass().getResource("/images/KingPenguin.jpg"));
		} catch (JigsawPuzzleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!pz.isResizableToGoodQuality(20)) {
			fail("no valid size");
		}
		pz.resize();
		
		ObjectOutputStream objOut;
		try {
			objOut = new ObjectOutputStream(new FileOutputStream("test.ser"));
			objOut.writeObject(pz);
			objOut.flush();
			objOut.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ---------------------------------------
		ObjectInputStream objIn;
		try {
			objIn = new ObjectInputStream(new FileInputStream("test.ser"));
			PuzzleImage pzImage = (PuzzleImage) objIn.readObject();
			pzImage.isResizableToGoodQuality(20);
			pzImage.resize();
			
			ImageIO.write(pzImage.getOriginalImage(), "jpg", new File("originalImage.jpg"));
			ImageIO.write(pzImage.getImage(), "jpg", new File("resizedImage.jpg"));
			
			// here a manual step could evaluate if this picture is similar to the original one.
			
			objIn.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


}
