
package gui;

import java.util.ArrayList;

/**
 *
 * @author anne
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> servers = new ArrayList();
        for(int i = 1; i < 4; i++){
            String s  = "Server".concat(Integer.toString(i));
            servers.add(s);
        }
        ServerGUI gui = new ServerGUI(servers);
        
    }
    
}
