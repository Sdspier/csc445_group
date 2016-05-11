package edu.oswego.cs.jsavlov;

import edu.oswego.cs.jsavlov.client.AtmxClient;
import io.atomix.catalyst.transport.Address;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main
{

    static {
        try {
            System.out.println("hostname = " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private boolean enteringInput = false;

    public static void main(String[] args)
    {
        // Load the hosts.txt.
        // Hosts are in the hosts.txt.txt file, formatted as such:
        // [host]:[port]'

        ArrayList<Address> hostList = new ArrayList<>();

        File hostFile = new File("hosts.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(hostFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitStr = line.split(":");
                String hostStr = splitStr[0];
                int hostPort = Integer.valueOf(splitStr[1]);
                hostList.add(new Address(hostStr, hostPort));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: hosts.txt file not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        AtmxClient client = new AtmxClient(hostList);

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
