package client;

import io.atomix.AtomixClient;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.collections.internal.QueueCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtmxClient {

    private List<Address> clusterHostList = null;
    private static final List<Address> DEFAULT_HOSTS;

    static {
        DEFAULT_HOSTS = Arrays.asList(
                new Address("127.0.0.1", 2703),
                new Address("127.0.0.1", 2715),
                new Address("127.0.0.1", 8700)
        );
    }

    public AtmxClient() {
        // No hosts provided.. Use the predefined set
        this.clusterHostList = new ArrayList<>(DEFAULT_HOSTS);
        init();
    }

    public AtmxClient(List<Address> hostList)
    {
        this.clusterHostList = hostList;
    }

    private void init() {

        AtomixClient client = AtomixClient.builder()
                .withTransport(new NettyTransport())
                .build();

        client.connect(clusterHostList).thenRun(() -> {
            System.out.println("Client connected!");
        });



    }

}
