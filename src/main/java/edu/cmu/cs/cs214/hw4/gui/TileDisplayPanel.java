package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.core.Tile;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * display a tile image after each draw, operation reminders and warnings and a button indicates player not placing a meeple
 */
public class TileDisplayPanel extends JPanel {
    private static final int PANEL_WIDTH = 200;
    private static final int PANEL_HEIGHT = 500;
    private static final int ROTATE_BUTTON_SIZE = 95;
    private static final String ROTATE_BY_CLOCKWISE = "<html>Click ↑ to rotate</html>";
    private Carcassonne carcassonne;
    private ImageOperation tileImageOperation = new TileImageOperation();
    private BufferedImage curImage;
    private Tile curTile;
    private JButton rotateButton;
    private JLabel curPlayerReminder;
    private JLabel wrongOperationMessage;
    private JButton doNotPlaceMeepleBtn;
    private JLabel placeMeepleReminder;
    private boolean isPlaced = false;

    TileDisplayPanel(Carcassonne carcassonne) throws IOException {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.carcassonne = carcassonne;
        rotateButton = new JButton();
        rotateButton.setPreferredSize(new Dimension(ROTATE_BUTTON_SIZE, ROTATE_BUTTON_SIZE));
        curTile = carcassonne.getDeck().drawTile();
        setCurImage(tileImageOperation.getImage(curTile.getCode().charAt(0)));
            rotateButton.addActionListener(e -> {
                if(!isPlaced) {
                    curImage = tileImageOperation.rotateClockWise(curImage);
                    setCurImage(curImage);
                    curTile.rotate();
                }
            });
        JLabel rotateReminder = new JLabel(ROTATE_BY_CLOCKWISE);
        curPlayerReminder = new JLabel();
        updateCurPlayerName();
        placeMeepleReminder = new JLabel("<html>Click the tile on board<br> to place a meeple</br>" +
                "<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; OR</br></html>");
        doNotPlaceMeepleBtn = new JButton("Not Place a Meeple");
        wrongOperationMessage = new JLabel();
        wrongOperationMessage.setForeground(Color.red);
        add(rotateButton);
        rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(rotateReminder);
        placeMeepleReminder.setVisible(false);
        doNotPlaceMeepleBtn.setVisible(false);
        wrongOperationMessage.setVisible(false);
        add(curPlayerReminder);
        add(placeMeepleReminder);
        add(doNotPlaceMeepleBtn);
        add(wrongOperationMessage);
    }

    boolean getIsPlaced() {
        return isPlaced;
    }

    void setIsPlaced() {
        isPlaced = true;
    }

    void setIsNotPlaced() {
        isPlaced = false;
    }

    JLabel getWrongOperationMessageLabel() {
        return wrongOperationMessage;
    }

    JLabel getPlaceMeepleReminderet() {
        return placeMeepleReminder;
    }

    void updateCurPlayerName() {
        String curPlayerName = carcassonne.getPlayer(carcassonne.getCurPlayerId());
        curPlayerReminder.setText("<html>Current Player: " + curPlayerName + "<br>Tiles Left: " + carcassonne.getDeck().getNumberOfTiles()+"</br></html>");
        curPlayerReminder.setBackground(PlayersTablePanel.COLORS[carcassonne.getCurPlayerId()]);
        curPlayerReminder.setOpaque(true);
    }

    Tile getCurTile() {
        return curTile;
    }

    void setCurTile(Tile newTile) {
        curTile = newTile;
    }

    void setCurImage(BufferedImage newImage) {
        curImage = newImage;
        ImageIcon imageIcon = new ImageIcon(curImage);
        rotateButton.setIcon(imageIcon);
    }

    BufferedImage getCurImage() {
        return curImage;
    }

    JButton getDoNotPlaceMeepleBtn() {
        return doNotPlaceMeepleBtn;
    }
}
