package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

/**
 * a table panel displays players' names, meeple colors, numbers of meeples left, scores
 */
public class PlayersTablePanel extends JPanel{
    private static final int TABLE_WIDTH = 930;
    private static final int TABLE_HEIGHT = 100;
    private static final String[] HEADERS = {"Color", "Player Name", "Meeples Left", "Score"};
    private static final String[] COLORS_NAME = {"PINK", "ORANGE", "MAGENTA", "GREEN", "BLUE"};
    public static final Color[] COLORS = {Color.PINK, Color.ORANGE, Color.MAGENTA, Color.GREEN, Color.BLUE};
    private Carcassonne carcassonne;
    private List<String> players;
    private int numOfPlayers;
    private String[][] tableData;
    private DefaultTableModel playersTable;

    PlayersTablePanel(Carcassonne carcassonne, List<String> players) {
        this.carcassonne = carcassonne;
        this.players = players;
        numOfPlayers = players.size();
        tableData = new String[numOfPlayers][HEADERS.length];
        updateTableData();
        playersTable = new DefaultTableModel(tableData, HEADERS) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(playersTable);
        table.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            JTextField editor = new JTextField();
            if (value != null)
                editor.setText(value.toString());
            editor.setBorder(BorderFactory.createEmptyBorder());
            for (int i = 0; i < 5; i++) {
                if (row == i && column == 0) {
                    editor.setBackground(COLORS[i]);
                }
            }
            return editor;
        });
        this.setLayout(new BorderLayout());
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        this.add(tablePane,BorderLayout.CENTER);
    }

    void updateTableData() {
        for(int i = 0; i < numOfPlayers; i++) {
            for (int j = 0; j < HEADERS.length; j++) {
                switch (j) {
                    case 0: tableData[i][0] = COLORS_NAME[i]; continue;
                    case 1: tableData[i][1] = players.get(i); continue;
                    case 2: tableData[i][2] = Integer.toString(carcassonne.getMeepleNum(i)); continue;
                    case 3: tableData[i][3] = Integer.toString(carcassonne.getScore(i)); continue;
                    default: break;
                }
            }
        }
    }

    void updateTable() {
        updateTableData();
        playersTable.setDataVector(tableData, HEADERS);
    }
}
