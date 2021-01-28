package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.core.Deck;
import edu.cmu.cs.cs214.hw4.core.Tile;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * a panel shows game board, players place tiles and meeples here, which is also related with tiles display panel and players information panel.
 */
class BoardPanel extends JPanel {
    private static final int TILE_OFFSET = 93;
    private static final int TILE_FRAGRANT = 31;
    private static final int BOARD_SIZE = 143;
    private static final int INITIAL_SIZE = 6;
    private static final int BOARD_MIDDLE = 71;
    private static final int BUTTON_VGAP = 4;
    private static final int BUTTON_HGAP = 3;
    private int topBound;
    private int leftBound;
    private int downBound;
    private int rightBound;
    private JPanel responsivePanel;
    private int curRow;
    private int curCol;
    private Carcassonne carcassonne;
    private Board board;
    private Deck deck;
    private int[] meepleBoard;
    private int[] scoreBoard;
    private TileDisplayPanel tileDisplayPanel;
    private PlayersTablePanel playersTablePanel;
    private ImageOperation tileImageOperation = new TileImageOperation();
    private JButton[][] boardBtns;
    private JButton doNotPlaceMeepleBtn = new JButton();
    private BufferedImage[][] boardTileImgWithNoMeeple;

    BoardPanel(Carcassonne carcassonne) {
        this.carcassonne = carcassonne;
        board = carcassonne.getBoard();
        deck = carcassonne.getDeck();
        boardBtns = new JButton[BOARD_SIZE][BOARD_SIZE];
        boardTileImgWithNoMeeple = new BufferedImage[BOARD_SIZE][BOARD_SIZE];
        meepleBoard = new int[carcassonne.getNumOfPlayers()];
        for (int i = 0; i < carcassonne.getNumOfPlayers(); i++) {
            meepleBoard[i] = carcassonne.getMeepleNum(i);
        }
        scoreBoard = new int[carcassonne.getNumOfPlayers()];
        for (int i = 0; i < carcassonne.getNumOfPlayers(); i++) {
            scoreBoard[i] = carcassonne.getScore(i);
        }

        initializeAllTilesPlacements();
        responsivePanel = new JPanel();
        updateResponsivePanel(BOARD_MIDDLE - INITIAL_SIZE / 2, BOARD_MIDDLE - INITIAL_SIZE / 2,
                BOARD_MIDDLE + INITIAL_SIZE / 2, BOARD_MIDDLE + INITIAL_SIZE / 2);
        add(responsivePanel);
    }
    void initializeAllTilesPlacements() {
        for(int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardBtns[row][col] = new JButton();
                boardBtns[row][col].setPreferredSize(new Dimension(TILE_OFFSET, TILE_OFFSET));
                if(row == BOARD_MIDDLE && col == BOARD_MIDDLE) {
                    try {
                        ImageIcon startIcon = new ImageIcon(tileImageOperation.getImage('D'));
                        boardBtns[row][col].setIcon(startIcon);
                        continue;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                int r = row;
                int c = col;
                boardBtns[r][c].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (!tileDisplayPanel.getIsPlaced()) {
                            try{
                                board.placeTile(r,c, tileDisplayPanel.getCurTile());
                                tileDisplayPanel.getWrongOperationMessageLabel().setVisible(false);
                                doNotPlaceMeepleBtn.setVisible(true);
                                tileDisplayPanel.getPlaceMeepleReminderet().setVisible(true);
                                updateBoard(r, c);
                                tileDisplayPanel.setIsPlaced();
                                curRow = r;
                                curCol = c;
                                BufferedImage curImg = tileDisplayPanel.getCurImage();
                                ImageIcon curIcon = new ImageIcon(curImg);
                                boardBtns[r][c].setIcon(curIcon);
                                boardBtns[r][c].getIcon();
                                boardBtns[r][c].setBorder(BorderFactory.createLineBorder(Color.RED, 1, true));

                            } catch (IllegalArgumentException e) {
                                tileDisplayPanel.getWrongOperationMessageLabel().setVisible(true);
                                tileDisplayPanel.getWrongOperationMessageLabel().setText("X Invalid Position or Rotation!");
                            }
                        } else if (r == curRow && c == curCol) {
                            int x = evt.getX();
                            int y = evt.getY();
                            if (y >= 0 && y <= TILE_FRAGRANT && x >= y && x < TILE_OFFSET - y) {
                                meeplePlacement(Tile.TOP_INDEX);
                            } else if (x >=TILE_OFFSET - TILE_FRAGRANT && x <= TILE_OFFSET && y < x && y >= TILE_OFFSET - x) {
                                meeplePlacement(Tile.RIGHT_INDEX);
                            } else if (y >= TILE_OFFSET - TILE_FRAGRANT && y <= TILE_OFFSET && y >= x && x >= TILE_OFFSET - y) {
                                meeplePlacement(Tile.DOWN_INDEX);
                            } else if (x >= 0 && x <= TILE_FRAGRANT && y > x && y <= TILE_OFFSET - x) {
                                meeplePlacement(Tile.LEFT_INDEX);
                            } else {
                                meeplePlacement(Tile.MONASTERY_INDEX);
                            }
                        }
                        doNotPlaceMeepleBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                if (tileDisplayPanel.getIsPlaced()){
                                    scoreAndRemoveMeeple();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    void meeplePlacement(int segmentIdx) {
        try {
            board.placeMeeple(tileDisplayPanel.getCurTile(), curRow, curCol, segmentIdx, carcassonne.getCurPlayerId(), meepleBoard);
            tileDisplayPanel.getWrongOperationMessageLabel().setVisible(false);
            doNotPlaceMeepleBtn.setVisible(false);
            tileDisplayPanel.getPlaceMeepleReminderet().setVisible(false);
            boardTileImgWithNoMeeple[curRow][curCol] = tileDisplayPanel.getCurImage();
            BufferedImage imgWithMeeple = tileImageOperation.withCircle(tileDisplayPanel.getCurImage(),
                    PlayersTablePanel.COLORS[carcassonne.getCurPlayerId()], segmentIdx);
            ImageIcon imgIconWithMeeple = new ImageIcon(imgWithMeeple);
            boardBtns[curRow][curCol].setIcon(imgIconWithMeeple);
            scoreAndRemoveMeeple();
        } catch (IllegalArgumentException e) {
            tileDisplayPanel.getWrongOperationMessageLabel().setVisible(true);
            tileDisplayPanel.getWrongOperationMessageLabel().setText("X Cannot Place Meeple Here!");
        }
    }

    void scoreAndRemoveMeeple() {
        Set<List<Integer>> removedMeeplesIdxboard = board.checkAndScore(tileDisplayPanel.getCurTile(), scoreBoard, meepleBoard);
        for(List<Integer> idx: removedMeeplesIdxboard) {
            int row = idx.get(0);
            int col = idx.get(1);
            BufferedImage imgWithoutMeeple = boardTileImgWithNoMeeple[row][col];
            ImageIcon imgIconWithOutMeeple = new ImageIcon(imgWithoutMeeple);
            boardBtns[row][col].setIcon(imgIconWithOutMeeple);
        }
        updatePlayersInfo();
        playersTablePanel.updateTable();
        switchToNextPlayer();
    }

    void updatePlayersInfo() {
        for (int i = 0; i < carcassonne.getNumOfPlayers(); i++) {
            carcassonne.setMeepleNum(meepleBoard[i], i);
        }
        for (int i = 0; i < carcassonne.getNumOfPlayers(); i++) {
            carcassonne.setScore(scoreBoard[i], i);
        }
    }

    void switchToNextPlayer() {
        boardBtns[curRow][curCol].setBorder(BorderFactory.createEmptyBorder());
        tileDisplayPanel.getWrongOperationMessageLabel().setVisible(false);
        doNotPlaceMeepleBtn.setVisible(false);
        tileDisplayPanel.getPlaceMeepleReminderet().setVisible(false);
        try {
            Tile newTile = deck.drawTile();
            while (board.getLegalPositions(board.getOuterVacancies(), newTile).size() <= 0) {  // consider no tiles left!!  try catch
                newTile = deck.drawTile();
            }
            tileDisplayPanel.setCurTile(newTile);
            tileDisplayPanel.setIsNotPlaced();
            try {
                tileDisplayPanel.setCurImage(tileImageOperation.getImage(newTile.getCode().charAt(0)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            carcassonne.switchPlayer();
            tileDisplayPanel.updateCurPlayerName();
            playersTablePanel.updateTable();
        } catch (IllegalArgumentException e) {
            tileDisplayPanel.getWrongOperationMessageLabel().setText("<html>&nbsp;Game over !");
            tileDisplayPanel.getWrongOperationMessageLabel().setVisible(true);
            board.finalScore(scoreBoard, meepleBoard);
            updatePlayersInfo();
            playersTablePanel.updateTable();
            gameEnded(carcassonne.getWinners());
        }
    }

    void updateResponsivePanel(int topBound, int leftBound, int downBound, int rightBound){
        this.topBound = topBound;
        this.leftBound = leftBound;
        this.downBound = downBound;
        this.rightBound = rightBound;
        responsivePanel.setLayout(new GridLayout(downBound - topBound + 1, rightBound - leftBound + 1, -BUTTON_HGAP, -BUTTON_VGAP));
        for(int i = topBound; i <= downBound; i++) {
            for (int j = leftBound; j <= rightBound; j++) {
                responsivePanel.add(boardBtns[i][j]);
            }
        }
    }

    void updateBoard(int row, int col) {
        if (row == topBound) {
            updateResponsivePanel(topBound-1, leftBound, downBound, rightBound);
        } else if (row == downBound) {
            updateResponsivePanel(topBound, leftBound, downBound+1, rightBound);
        } else if (col == leftBound) {
            updateResponsivePanel(topBound, leftBound-1, downBound, rightBound);
        } else if (col == rightBound) {
            updateResponsivePanel(topBound, leftBound, downBound, rightBound+1);
        }
    }

    void setTileDisplayPanel(TileDisplayPanel tileDisplayPanel) {
        this.tileDisplayPanel = tileDisplayPanel;
    }
    void setDoNotPlaceMeepleBtn(JButton doNotPlaceMeepleBtn) {
        this.doNotPlaceMeepleBtn = doNotPlaceMeepleBtn;
    }
    void setPlayersTablePanel(PlayersTablePanel playersTablePanel) {
        this.playersTablePanel = playersTablePanel;
    }
    void gameEnded(List<Integer> winners) {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        StringBuilder sb = new StringBuilder();
        for (Integer playerId: winners) {
            sb.append(carcassonne.getPlayer(playerId)).append(" ");
        }
        CarcassonneUI.showDialog(frame, "Carcassonne Winner!", sb + "just won the game!");
    }
}
