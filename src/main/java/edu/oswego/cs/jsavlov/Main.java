package edu.oswego.cs.jsavlov;

import edu.oswego.cs.jsavlov.client.AtmxClient;
import edu.oswego.cs.jsavlov.cluster.AtmxCluster;
import edu.oswego.cs.jsavlov.ui.TextEditorUI;
import io.atomix.AtomixClient;
import io.atomix.catalyst.transport.Address;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main
{

    private static String LOCAL_HOSTNAME;
    private static final String ARG_GUI = "gui";
    private static boolean using_gui = false;


    static {
        try {
           LOCAL_HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private boolean enteringInput = false;

    public static void main(String[] args)
    {
        // Parse command line arguments
        for (String workingArg : args) {
            if (workingArg.equals(ARG_GUI)) {
                using_gui = true;
                continue;
            }
        }

        // Create the central cluster object
        AtmxCluster mainCluster = new AtmxCluster();

        // create the main
        TextEditorUI mainWindow = null;


        // If the command line arguments indicate we are using a gui,
        // create a main window.
        if (using_gui) {
            System.out.println("Using a GUI...");
            mainWindow = new TextEditorUI(mainCluster);
        }

    }


    private static final class MasterKeyControlListener implements KeyListener
    {

        private final AtmxClient client;

        MasterKeyControlListener(AtmxClient client)
        {
            this.client = client;
        }

        @Override
        public void keyTyped(KeyEvent e)
        {

        }

        @Override
        public void keyPressed(KeyEvent e)
        {

        }

        @Override
        public void keyReleased(KeyEvent e)
        {

        }
    }

}
