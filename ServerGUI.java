package pkg445group;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author anne
 */
public class ServerGUI implements ActionListener {

    private ArrayList<String> allServers;
    private JFrame frame;
    private JPanel panel;
    private JList<String> list;
    private JButton selectServer;
    private DefaultListModel listModel;
    private int s;

    ServerGUI(ArrayList<String> servers) {
        allServers = servers;
        init();
    }

    private void init() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        panel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel();

        for (String s : allServers) {
            listModel.addElement(s);
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(new ServerListListener());
        panel.add(list, BorderLayout.CENTER);
        selectServer = new JButton("select server");
        selectServer.addActionListener(this);
        panel.add(selectServer, BorderLayout.SOUTH);
        panel.setVisible(true);
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        s = list.getSelectedIndex();
        // s is leader that clients communicate with
        //testing gui
        System.out.println(allServers.get(s));

    }

    public class ServerListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (list.getSelectedIndex() == -1) {

                    selectServer.setEnabled(false);

                } else {

                    selectServer.setEnabled(true);
                }
            }

        }

    }

}
