package atmxclient;

import io.atomix.AtomixClient;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import java.util.Arrays;
import java.util.List;

public class AtmxClient {

    public AtmxClient() {
        init();
    }

    public void init() {

        AtomixClient client = AtomixClient.builder()
                .withTransport(new NettyTransport())
                .build();

        List<Address> cluster = Arrays.asList(
                new Address("127.0.0.1", 2703),
                new Address("127.0.0.1", 2715),
                new Address("127.0.0.1", 8700)
        );

        client.connect(cluster).thenRun(() -> {
            System.out.println("Client connected!");
        });

    }

}
