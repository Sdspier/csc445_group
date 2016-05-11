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
import java.util.concurrent.ExecutionException;

public class Main
{

    private static String LOCAL_HOSTNAME;
    private static final String ARG_GUI = "gui";
    private static final String ARG_PORT = "port";
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
        // start out using the default port
        // the command line can call for a custom port number
        int use_port = AtmxCluster.DEFAULT_PORT;
        // Parse command line arguments
        for (String workingArg : args) {
            String workingArgFirst = workingArg.split(":")[0];
            switch (workingArgFirst) {
                case ARG_GUI:
                    using_gui = true;
                    continue;
                case ARG_PORT:
                    String port_str = workingArg.split(":")[1];
                    use_port = Integer.parseInt(port_str);
                    System.out.println("Using custom port: " + Integer.toString(use_port));
                    continue;
                default:
                    System.out.println("Unknown argument. Exiting...");
                    System.exit(1);
                    break;
            }

        }

        // Create the central cluster object
        AtmxCluster mainCluster = null;
        try {
            mainCluster = new AtmxCluster(use_port);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
