package puzzle.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import puzzle.GameCommander;
import puzzle.GamePreferences;
import puzzle.PuzzleProperties;
import puzzle.gameevent.GameEvent;
import puzzle.gameevent.GameEventListener;
import puzzle.storeage.JigsawPuzzleException;
import puzzle.storeage.LoadGameException;
import puzzle.storeage.SaveGameException;
import puzzle.storeage.StorageUtil;
import puzzle.storeage.Storeable;

public class GameMainWindow extends JFrame implements GameEventListener, Storeable {

	private static final Logger logger = Logger.getLogger(GameMainWindow.class);
	
	private static GameMainWindow This;
	
	public static GameMainWindow getInstance() {
        return This;
    }

	public static void startUI() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                This = new GameMainWindow();
                This.setVisible(true);
            }
        });

    }

	private TutorialDialog startDialog;
	private TutorialDialog tutorialDialog;
	private PreviewDialog previewDialog;
	private LoadSaveFileChooser fileChooser;

	private JMenuBar menuBar;
	
	private JMenu gameMenu;
	private JMenuItem gameNew;
	private JMenuItem gameReset;
	private JMenuItem gameLoad;
	private JMenuItem gameSave;
	private JMenuItem gameExit;
	
	private JMenu viewMenu;
	private JMenuItem preview;
	
	private JMenu optionMenu;
	private JCheckBoxMenuItem outlineCheck;
	private JCheckBoxMenuItem shadowCheck;
	private JCheckBoxMenuItem highlightCheck;
	private JCheckBoxMenuItem antiAliasingCheck;
	private JCheckBoxMenuItem soundCheck;
	
	private GamePanel gamePanel;
	private JScrollPane gamePanelScroll;
	private JPanel statusPanel;

	private JLabel statusInformation;
	private int startingPieceCount;
	private int actualPieceCount;

	private GameMainWindow() {
		this.setIconImage(new ImageIcon(getClass().getResource(
				PuzzleProperties.APPLICATION_ICON_FILE)).getImage());
		
		String header = PuzzleProperties.getLocalized("gameHeader");
		this.setTitle(header + " " + PuzzleProperties.GAME_BUILD_VERSION);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(400, 300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initComponents();
		this.fileChooser = new LoadSaveFileChooser(this);
		this.previewDialog = new PreviewDialog(this);
		this.createBufferStrategy(2);
		
		// add me as a listener to the Game state
		GameCommander.getInstance().addListener(this);
		
		if (this.tutorialDialog == null) {
			this.tutorialDialog =  new TutorialDialog(this);
		}
        this.tutorialDialog.setVisible(true);
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	private void initComponents() {
		this.gamePanel = new GamePanel();
		this.addKeyListener(this.gamePanel.getInputListener());
		
		this.gamePanelScroll = new JScrollPane(gamePanel);
		this.gamePanelScroll.getViewport().add(gamePanel);

		this.menuBar = new JMenuBar();
		this.gameMenu = new JMenu();
		this.gameMenu.setText(PuzzleProperties.getLocalized("gameMenu"));
		
		this.gameNew = new JMenuItem(PuzzleProperties.getLocalized("gameMenuNewGame"));
		this.gameNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clickedNewGame();
            }
        });
		
		this.gameReset = new JMenuItem(PuzzleProperties.getLocalized("gameMenuResetGame"));
		this.gameReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
					clickedResetGame();
				} catch (JigsawPuzzleException e) {
					logger.error("reset game error" + e.toString());
				}
            }
        });
		
		this.gameLoad = new JMenuItem(PuzzleProperties.getLocalized("gameMenuLoadGame"));
		this.gameLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clickedLoadGame();
            }
        });
		
		this.gameSave = new JMenuItem(PuzzleProperties.getLocalized("gameMenuSaveGame"));
		this.gameSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clickedSaveGame();
            }
        });
		
		this.gameExit = new JMenuItem(PuzzleProperties.getLocalized("gameMenuExitGame"));
		this.gameExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clickedExitGame();
            }
        });
		
		this.viewMenu = new JMenu();
		this.viewMenu.setText(PuzzleProperties.getLocalized("viewMenu"));
		this.preview = new JMenuItem();
		this.preview.setText(PuzzleProperties.getLocalized("viewMenuPreview"));
		this.preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clickedPreview();
            }
        });
		
		this.optionMenu = new JMenu();
		this.optionMenu.setText(PuzzleProperties.getLocalized("options"));
		
		this.outlineCheck = new JCheckBoxMenuItem();
		this.outlineCheck.setText(PuzzleProperties.getLocalized("optionShowOutline"));
		this.outlineCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	boolean outline = outlineCheck.getState();
            	GameCommander.getInstance().getPreferences().setShowOutline(outline);
            	try {
					gamePanel.reRender();
				} catch (JigsawPuzzleException e) {
					logger.error("error in rerendering" + e.toString());
				}
            }
        });
		
		this.shadowCheck = new JCheckBoxMenuItem();
		this.shadowCheck.setText(PuzzleProperties.getLocalized("optionShowShadow"));
		this.shadowCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	boolean shadow = shadowCheck.getState();
            	GameCommander.getInstance().getPreferences().setShowShadow(shadow);
            	try {
					gamePanel.reRender();
				} catch (JigsawPuzzleException e) {
					logger.error("error in rerendering" + e.toString());
				}
            }
        });
		
		this.highlightCheck = new JCheckBoxMenuItem();
		this.highlightCheck.setText(PuzzleProperties.getLocalized("optionHighlight"));
		this.highlightCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	boolean high = highlightCheck.getState();
            	GameCommander.getInstance().getPreferences().setHighlight(high);
            	try {
					gamePanel.reRender();
				} catch (JigsawPuzzleException e) {
					logger.error("error in rerendering" + e.toString());
				}
            }
        });
		
		this.antiAliasingCheck = new JCheckBoxMenuItem();
		this.antiAliasingCheck.setText(PuzzleProperties.getLocalized("viewMenuAntiAliasing"));
		this.antiAliasingCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	boolean ali = antiAliasingCheck.getState();
            	GameCommander.getInstance().getPreferences().setAntiAliasing(ali);
            	try {
					gamePanel.repaint();
					gamePanel.reRender();
				} catch (JigsawPuzzleException e) {
					logger.error("error in rerendering" + e.toString());
				}
            }
        });
		
		this.soundCheck = new JCheckBoxMenuItem();
		this.soundCheck.setText(PuzzleProperties.getLocalized("optionSoundOn"));
		this.soundCheck.setState(true);
		this.soundCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	boolean snd = soundCheck.getState();
            	GameCommander.getInstance().getPreferences().setSound(snd);
            }
        });
		
		// disable the options
		enableOptions(false);
		
		// game menu
		this.gameMenu.add(this.gameNew);
		this.gameMenu.add(this.gameReset);
		this.gameMenu.add(new JSeparator());
		this.gameMenu.add(this.gameLoad);
		this.gameMenu.add(this.gameSave);
		this.gameMenu.add(new JSeparator());
		this.gameMenu.add(this.gameExit);
		
		// view menu
		this.viewMenu.add(this.preview);
		this.viewMenu.add(this.antiAliasingCheck);
		
		// option menu
		this.optionMenu.add(this.outlineCheck);
		this.optionMenu.add(this.shadowCheck);
		this.optionMenu.add(this.highlightCheck);
		this.optionMenu.add(this.soundCheck);
		
		// menu bar
		this.menuBar.add(this.gameMenu);
		this.menuBar.add(this.optionMenu);
		this.menuBar.add(this.viewMenu);
		
		// the status stuff
		this.statusPanel = new JPanel();
		this.statusInformation = new JLabel();
		this.statusPanel.add(this.statusInformation);
		this.initUI();
		
		// add stuff to the pane
		this.setLayout(new BorderLayout());
		this.getContentPane().add(gamePanelScroll, BorderLayout.CENTER);
		this.getContentPane().add(statusPanel, BorderLayout.SOUTH);
		this.setJMenuBar(this.menuBar);
		
		this.pack();
		
	}
	
	/**
	 * START CLICKED
	 */
	protected void clickedExitGame() {
		System.exit(0);
	}

	private void clickedNewGame() {
		if (this.startDialog == null) {
			this.startDialog = new TutorialDialog(this);
		}
        this.startDialog.setVisible(true);
	}

	private void clickedPreview() {
			this.previewDialog.setVisible(true);
	}
	
	private void clickedResetGame() throws JigsawPuzzleException {
		GameCommander.getInstance().resetGame();
		this.reset();
	}
	
	private void clickedSaveGame() {
		File toSave = this.fileChooser.openSaveDialog();
		if (toSave == null) {
			return; // ignore and donot save anything
		}
		try {
			GameCommander.getInstance().saveGame(toSave);
		} catch (SaveGameException e) {
			e.printStackTrace();
			showErrorMessage(PuzzleProperties.getLocalized("storeSaveErrorTitle"), PuzzleProperties.getLocalized("storeSaveErrorMessage"));
		}
	}

	private void clickedLoadGame() {
		File toLoad = this.fileChooser.openLoadDialog();
		if (toLoad == null) {
			return; // ignore if nothing selected
		}
		try {
			GameCommander.getInstance().loadGame(toLoad);
		} catch (LoadGameException e) {
			e.printStackTrace();
			showErrorMessage(PuzzleProperties.getLocalized("storeLoadErrorTitle"), PuzzleProperties.getLocalized("storeLoadErrorMessage"));
		}
	}
	
	/**
	 * END CLICKED
	 * @throws JigsawPuzzleException 
	 */
	
	private void reset() throws JigsawPuzzleException {
		this.initUI();
		this.enableOptions(false);
		this.gamePanel.reRender();
		this.previewDialog.unloadImage();
	}
	
	private void enableOptions(boolean enable) {
		this.outlineCheck.setEnabled(enable);
		this.shadowCheck.setEnabled(enable);
		this.highlightCheck.setEnabled(enable);
		this.antiAliasingCheck.setEnabled(enable);
		this.soundCheck.setEnabled(enable);
		
		this.gameSave.setEnabled(enable);		
	}
	
	private void initUI() {
		this.statusInformation.setText(PuzzleProperties.getLocalized("puzzlePieces"));
	}
	
	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, title, message, JOptionPane.ERROR_MESSAGE);
	}
    
	/**
	 * this should set the boundaries, if and only if the 
	 * @param xbound
	 * @param ybound
	 */
    public void setBoundaries(int xbound, int ybound) {
		GamePreferences gp = GameCommander.getInstance().getPreferences();

		// set the size so that you can almost 'hide' a piece and it's possible to grab it although
		Dimension wishedSize = new Dimension(xbound + gp.getSideLength(), ybound + gp.getSideLength());
		// the old size from the panel
		Dimension oldSize = this.gamePanel.getSize();
		
		// x,y set the larger variants
		int xSize, ySize;
		if (wishedSize.width > oldSize.width)
			xSize = wishedSize.width;
		else xSize = oldSize.width;
		if (wishedSize.height > oldSize.height)
			ySize = wishedSize.height;
		else ySize = oldSize.height;
		
		// the larger version for x and y for wished and old sizes
		Dimension newSize = new Dimension(xSize, ySize);

		// later size was set!
		this.gamePanel.setPreferredSize(newSize);
		this.gamePanel.setSize(newSize);
	}
    
    public void setPieceCount(int pieceCount) {
    	this.actualPieceCount = pieceCount;
    	String pieceText = PuzzleProperties.getLocalized("puzzlePieces");
    	this.statusInformation.setText(pieceText + " " + this.actualPieceCount + "/" + this.startingPieceCount);
    }
    
    public void startGame(GamePreferences gp) throws JigsawPuzzleException {
		this.previewDialog.loadImage(gp.getImage().getImage());

		this.enableOptions(true);
		
		this.outlineCheck.setState(gp.isShowOutline());
		this.shadowCheck.setState(gp.isShowShadow());
		this.highlightCheck.setState(gp.isHighlight());
		this.antiAliasingCheck.setState(gp.isAntiAliasing());
		this.soundCheck.setState(gp.isSound());
		
		this.gamePanel.reRender();
		this.repaint();
	}

	public void eventHappened(GameEvent ge) throws JigsawPuzzleException {
		GamePreferences gp;
		switch(ge.getType()) {
		case START_GAME:
			gp  = (GamePreferences)ge.getInfo();
			this.startingPieceCount = gp.getInitialPieces();
			this.setPieceCount(this.startingPieceCount);
			this.startGame(gp);
			break;
		case LOAD_GAME:
			gp = (GamePreferences)ge.getInfo();
			this.startGame(gp);
			break;
		case SNAP_PIECE:
			int pieces = (Integer)ge.getInfo();
			this.setPieceCount(pieces);
			break;
		}
	}

	@Override
	public void restore(Node current) throws LoadGameException {
		Node mainWindowNode = StorageUtil.findDirectChildNode(current, "GameMainWindow");
		NamedNodeMap nnm = mainWindowNode.getAttributes();
		Node sizeW = nnm.getNamedItem("SizeWidth");
		int width = Integer.parseInt(sizeW.getNodeValue());
		
		Node sizeH = nnm.getNamedItem("SizeHeight");
		int height = Integer.parseInt(sizeH.getNodeValue());
		this.setBoundaries(width, height);
		
		Node initialPieceCount = nnm.getNamedItem("InitialPieceCount");
		this.startingPieceCount = Integer.parseInt(initialPieceCount.getNodeValue());
		
		Node pieceCount = nnm.getNamedItem("PieceCount");
		this.setPieceCount(Integer.parseInt(pieceCount.getNodeValue()));
	}

	@Override
	public void store(Node current) throws SaveGameException {
		Document doc = current.getOwnerDocument();
		Element mainWindowNode = doc.createElement("GameMainWindow");
		Dimension prefSize = this.gamePanel.getPreferredSize();
		
		mainWindowNode.setAttribute("SizeWidth", ""+prefSize.width);
		mainWindowNode.setAttribute("SizeHeight", ""+prefSize.height);
		mainWindowNode.setAttribute("InitialPieceCount", ""+this.startingPieceCount);
		mainWindowNode.setAttribute("PieceCount", ""+this.actualPieceCount);
		current.appendChild(mainWindowNode);
	}

}
