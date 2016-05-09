import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        // Load the hosts.
        // Hosts are in the hosts.txt file, formatted as such:
        // [host]:[port]'

        ArrayList<HostHolder> hostList = new ArrayList<>();

        File hostFile = new File("hosts.txt");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(hostFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitStr = line.split(":");
                String hostStr = splitStr[0];
                int hostPort = Integer.getInteger(splitStr[0]);
                hostList.add(new HostHolder(hostStr, hostPort));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: hosts.txt file not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static final class HostHolder {
        final String host;
        final int port;

        HostHolder(String host, int port) {
            this.host = host;
            this.port = port;
        }
    }
}
