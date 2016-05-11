import edu.oswego.cs.jsavlov.client.AtmxClient;
import io.atomix.catalyst.transport.Address;

import java.io.*;
import java.util.ArrayList;

public class Main
{
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

}
