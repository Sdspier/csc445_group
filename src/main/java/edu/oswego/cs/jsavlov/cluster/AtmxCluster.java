package edu.oswego.cs.jsavlov.cluster;

import edu.oswego.cs.jsavlov.Main;
import io.atomix.AtomixReplica;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.copycat.server.storage.Storage;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by jason on 5/9/16.
 */
public class AtmxCluster
{
    /**
     * Constants
     */
    protected static final String STORAGE_DIRECTORY = System.getProperty("user.home"); // What should our storage directory be?
    protected static final String SHARED_STRING = "shared_str";


    private static String LOCAL_HOSTNAME;
    public static int DEFAULT_PORT = 9491;
    static {
        try {
            LOCAL_HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instance variables
     */
    private AtomixReplica atomix;
    private String currentValue = "";
    private int port_number;

    public static void main(String args[])
    {
        new AtmxCluster();
    }

    /**
     * Constructor
     */
    public AtmxCluster()
    {
        port_number = DEFAULT_PORT;
        init();
    }

    public AtmxCluster(int port)
    {
        port_number = port;
        init();
    }

    /**
     * init():
     *
     * Initializes the AtmxCluster. This should only be called upon object creation (i.e. in the constructor)
     */
    private void init()
    {
        ArrayList<Address> hostList = new ArrayList<>();
        Address selfAddr = new Address(LOCAL_HOSTNAME, port_number);
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
            if (hostList.contains(selfAddr)) {
                hostList.remove(selfAddr);
            }
            //hostList = new ArrayList(hostList.subList(1, hostList.size()));
        } catch (FileNotFoundException ex) {
            System.out.println("Error: hosts.txt file not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // TODO: Add code to remove "copycat" files present in the storage directory

        // Create the replica
        atomix = AtomixReplica.builder(selfAddr)
                .withTransport(new NettyTransport())
                .withStorage(new Storage(STORAGE_DIRECTORY))
                .build();

        // Bootstrap the edu.oswego.cs.jsavlov.cluster
        atomix.bootstrap().join().join(hostList).thenRun(() -> {
            System.out.println("Bootstrap and join complete.");
        });

        System.out.println("Cluster commenced.");
    }


    /**
     * @param msg: The new message from the edu.oswego.cs.jsavlov.client to be broadcast across the edu.oswego.cs.jsavlov.cluster.
     *
     * Used to broadcast messages across the edu.oswego.cs.jsavlov.cluster
     */
    public void postNewMessage(String msg)
    {
        atomix.getValue(SHARED_STRING).thenAccept((value) -> {
            value.set(msg).thenRun(() -> {
                System.out.println("Posted new message: " + msg);
            });
        });
    }
}
