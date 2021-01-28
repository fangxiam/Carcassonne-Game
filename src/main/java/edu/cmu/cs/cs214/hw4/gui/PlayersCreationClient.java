package edu.cmu.cs.cs214.hw4.gui;
import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * players enter names here and generate game interface
 */
public class PlayersCreationClient extends JPanel{
    private static final int FRAME_WIDTH = 850;
    private static final int FRAME_HEIGHT = 780;
    private static final int SCROLL_SPEED = 16;
    private static final int MAX_PLAYERS = 5;
    private static final int NAME_FIELD = 20;
    private JFrame startFrame;
    private List<String> players;

    PlayersCreationClient(JFrame frame) {
        this.startFrame = frame;
        this.players = new ArrayList<>();
        JLabel playerNameLabel = new JLabel("Player Name:");
        final JTextField playerNameText = new JTextField(NAME_FIELD);
        JButton addPlayerButton = new JButton("Add Player");
        JPanel playerPanel = new JPanel();
        playerPanel.add(playerNameLabel, BorderLayout.WEST);
        playerPanel.add(playerNameText, BorderLayout.CENTER);
        playerPanel.add(addPlayerButton, BorderLayout.EAST);
        ActionListener newPlayerListener = e -> {
            String name = playerNameText.getText().trim();
            if (!name.isEmpty() && !this.players.contains(name)) {
                this.players.add(name);
            } else {
                wrongPlayerName();
            }
            playerNameText.setText("");
            playerNameText.requestFocus();
        };
        addPlayerButton.addActionListener(newPlayerListener);
        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(e -> {
            if(!this.players.isEmpty() && this.players.size() >= 2 && this.players.size() <= MAX_PLAYERS) {
                try {
                    startGameSession();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                wrongPlayersNum();
            }
        });
        setLayout(new BorderLayout());
        add(playerPanel, BorderLayout.NORTH);
        add(startGameButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    void startGameSession() throws IOException {
        startFrame.dispose();
        startFrame = null;
        Carcassonne carcassonne = new Carcassonne(players);
        JFrame mainFrame = new JFrame("Carcassonne by Fangxian_Mi");
        BoardPanel boardPanel = new BoardPanel(carcassonne);
        TileDisplayPanel tileDisplayPanel = new TileDisplayPanel(carcassonne);
        boardPanel.setTileDisplayPanel(tileDisplayPanel);
        PlayersTablePanel playersTablePanel = new PlayersTablePanel(carcassonne, players);
        boardPanel.setPlayersTablePanel(playersTablePanel);
        boardPanel.setDoNotPlaceMeepleBtn(tileDisplayPanel.getDoNotPlaceMeepleBtn());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        JScrollPane boardPane = new JScrollPane(boardPanel);
        boardPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);
        boardPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        mainFrame.add(tileDisplayPanel, BorderLayout.EAST);
        mainFrame.add(boardPane, BorderLayout.CENTER);
        mainFrame.add(playersTablePanel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    void wrongPlayersNum() {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        CarcassonneUI.showDialog(frame, "Wrong Players Number", "Only for Two to Five Players, please add players again!");
        players = new ArrayList<>();
    }

    void wrongPlayerName() {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        CarcassonneUI.showDialog(frame, "Wrong Player name", "Players names have to be different and not null!");
        players = new ArrayList<>();
    }
}
