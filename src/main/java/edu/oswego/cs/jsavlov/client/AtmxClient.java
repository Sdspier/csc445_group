package edu.oswego.cs.jsavlov.client;

import io.atomix.AtomixClient;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtmxClient {

    private List<Address> clusterHostList = null;
    private static final List<Address> DEFAULT_HOSTS;
    private AtomixClient client;

    String name = "foobar1";

    public static final String SHARED_STRING = "shared_str";

    static {
        DEFAULT_HOSTS = Arrays.asList(
                new Address("127.0.0.1", 2703),
                new Address("127.0.0.1", 2715),
                new Address("127.0.0.1", 8700)
        );
    }

    public AtmxClient() {
        // No hosts.txt provided.. Use the predefined set
        this.clusterHostList = new ArrayList<>(DEFAULT_HOSTS);
        init();
    }

    public AtmxClient(List<Address> hostList) {
        // Use the hosts.txt provided in args
        this.clusterHostList = hostList;
        init();
    }

    private void init() {

        client = AtomixClient.builder()
                .withTransport(new NettyTransport())
                .build();

        client.connect(clusterHostList).thenRun(() -> {
            System.out.println("Client connected!");
            this.initiateValueLoop();
        });




    }

    private void initiateValueLoop() {
        Integer send_ct = 0;
        for (;;) {
            try {
                    System.out.println(client.<String>getValue(SHARED_STRING).thenAccept((value) -> {

                    }));
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
