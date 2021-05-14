package puzzle.ui;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


import puzzle.PuzzleProperties;

public class TutorialDialog extends JDialog {
	

	private final Dimension tutorialPanelSize = new Dimension(504, 425);

	// parts that are in the design

	private TutorialPanel tutorialPanel;

	public TutorialDialog(Frame owner) {
		super(owner, true);
		this.setTitle(PuzzleProperties.getLocalized("newGameDialogHeader"));
		this.setLocationByPlatform(true);
		this.setResizable(true);


		this.initComponents(owner);
	}


	private void initComponents(Frame owner) {
		this.setLayout(new BorderLayout());

		this.tutorialPanel = new TutorialPanel();
		this.tutorialPanel.setPreferredSize(this.tutorialPanelSize);
		this.tutorialPanel.setSize(this.tutorialPanelSize);

		try {
			Image image = ImageIO.read(new File("Tutorial.jpg"));
			this.tutorialPanel.loadImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		JPanel principalPanel;
		principalPanel = new JPanel();
		principalPanel.setLayout(new BoxLayout(principalPanel, BoxLayout.X_AXIS));

		JPanel imagePanel;
		imagePanel = new JPanel();
		imagePanel
				.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));

		imagePanel.add(this.tutorialPanel);

		principalPanel.add(imagePanel);
		
		this.add(principalPanel, BorderLayout.CENTER);
		
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
		this.pack();
		
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}

	private final class TutorialPanel extends JPanel {

		private final Border border = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		private JLabel label;

		TutorialPanel() {
			setResizable(false);
			this.setLayout(new BorderLayout());
			this.setBorder(this.border);
			this.label = new JLabel();
			this.add(this.label, BorderLayout.CENTER);
		}

		/**
		 * load this image (having right size) into the center of preview
		 * 
		 * @param img
		 */
		void loadImage(Image img) {
			this.label.setIcon(new ImageIcon(img));
			// this.setSize(previewPanelSize);
			this.setPreferredSize(tutorialPanelSize);
			this.repaint();
		}

		/**
		 * unloads this image and resets the noImagePresent String from the
		 * current language defaults
		 */
		void unloadImage() {
			this.label.setText(PuzzleProperties.getLocalized("noImagePresent"));
		}

	}

}
