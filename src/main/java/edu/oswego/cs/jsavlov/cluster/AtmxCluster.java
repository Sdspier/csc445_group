package edu.oswego.cs.jsavlov.cluster;

import edu.oswego.cs.jsavlov.Main;
import io.atomix.AtomixReplica;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.group.DistributedGroup;
import io.atomix.group.LocalMember;
import io.atomix.group.internal.LocalGroupMember;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jason on 5/9/16.
 */
public class AtmxCluster
{
    /**
     * Constants
     */
    private static final String GROUP_KEY = "main_group";
    protected static final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/csc445_group_config/"; // What should our storage directory be?
    protected static final String SHARED_STRING = "shared_str";
    private static final String PRODUCER_MESSAGE = "prod_message";

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
    private DistributedGroup mainGroup;
    private String currentValue = "";
    private LocalMember mainMember;
    private int port_number;
    private final List<ClusterActionListener> actionListenerList = new ArrayList<>();

    public static void main(String args[])
    {
        try {
            new AtmxCluster();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor
     */
    public AtmxCluster() throws ExecutionException, InterruptedException
    {
        port_number = DEFAULT_PORT;
        init();
    }

    public AtmxCluster(int port) throws ExecutionException, InterruptedException
    {
        port_number = port;
        init();
    }

    /**
     * init():
     *
     * Initializes the AtmxCluster. This should only be called upon object creation (i.e. in the constructor)
     */
    private void init() throws ExecutionException, InterruptedException
    {
        // Remove the copycat stuff

            File configDir = new File(STORAGE_DIRECTORY);
            if (configDir.isDirectory()) {
                File[] dirFiles = configDir.listFiles();
                for (File f : dirFiles) {
                    f.delete();
                }
            }

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

        // Create the replica
        atomix = AtomixReplica.builder(selfAddr)
                .withTransport(new NettyTransport())
                .withStorage(new Storage(STORAGE_DIRECTORY))
                .build();

        // Bootstrap the edu.oswego.cs.jsavlov.cluster
        atomix.bootstrap(hostList).join();

        System.out.println("Bootstrap and join complete.");


        mainGroup = atomix.getGroup(GROUP_KEY).get();

        mainGroup.join().thenAccept((member) -> {
            mainMember = member;
            System.out.println("Joined a group.");
            mainMember.messaging().consumer(PRODUCER_MESSAGE).onMessage((consumer) -> {
                // Update the liseners
                for (ClusterActionListener listener : actionListenerList) {
                    listener.onReceiveNewMessage(consumer.message().toString());
                }
            });
        });



        mainGroup.onJoin(groupMember -> {
            System.out.println("Member " + groupMember.toString() + " joined the group.");
        });

        mainGroup.onLeave(groupMember -> {
            System.out.println("Member " + groupMember.toString());
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
                mainGroup.messaging().producer(PRODUCER_MESSAGE).send(msg).thenRun(() -> {
                    System.out.println("Posted new message: " + msg);
                });
            });
        });

    }

    public void addListener(ClusterActionListener listener)
    {
        this.actionListenerList.add(listener);
    }
}
