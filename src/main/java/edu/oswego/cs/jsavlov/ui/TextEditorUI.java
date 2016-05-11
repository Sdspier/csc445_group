package edu.oswego.cs.jsavlov.ui;

import edu.oswego.cs.jsavlov.cluster.AtmxCluster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TextEditorUI extends JFrame {

    /**
     * Instance variables
     */
    private JTextArea area = new JTextArea(20, 120);
    private String currentFile = "[ TEXT FILE ]";
    private boolean changed = false;
    private static String areaText;
    private final AtmxCluster cluster;

    public static void main(String[] args)
    {

        TextEditorUI ui = new TextEditorUI(new AtmxCluster());
    }


    /**
     * Constructor
     */
    public TextEditorUI(AtmxCluster cluster) {
        this.cluster = cluster;
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);

        JToolBar JTB = new JToolBar();
        add(JTB, BorderLayout.NORTH);
        JTB.addSeparator();
        JTB.add(QUIT);
        JTB.addSeparator();
        JTB.add(PUSH_TO_SERVER);
        JTB.addSeparator();
        JTB.setFloatable(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        area.addKeyListener(k1);
        setTitle(currentFile);
        setVisible(true);
    }

    /**
     * Initialize the edu.oswego.cs.jsavlov.cluster
     */
    private void init_cluster()
    {

    }

    private KeyListener k1 = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            changed = true;
        }
    };

    Action PUSH_TO_SERVER = new AbstractAction("[ Push to Server ]") {
        public void actionPerformed(ActionEvent e) {
            areaText = area.getText();
            pushToServer();
        }
    };

    private void pushToServer() {
        if (areaText.length() < 1) {
            JOptionPane.showMessageDialog(area,
                    "Nothing pushed to server.");
        } else {
            // Push the new string to the edu.oswego.cs.jsavlov.cluster


            JOptionPane.showMessageDialog(area,
                    "[ " + areaText + " ]" + "\n Pushed to server");
        }


    }


    Action QUIT = new AbstractAction("[ Quit ]") {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

//    ActionMap m = area.getActionMap();

}

