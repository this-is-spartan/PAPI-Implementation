package test;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SomeExpansion extends PlaceholderExpansion {
    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }
    @Override
    public String getIdentifier() {
        return "example";
    }

    @Override
    public String getAuthor() {
        return "ahmed.jar";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        // %example_placeholder1%
        if(identifier.equals("motd")){
            try {
                Socket sock = new Socket("hypixel.net", 25565);
                DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                DataInputStream in = new DataInputStream(sock.getInputStream());
                out.write(0xFE);
                int b;
                StringBuffer str = new StringBuffer();
                while ((b = in.read()) != -1) {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
// Not sure what use the two characters are so I omit them
                        str.append((char) b);
                        System.out.println(b + ":" + ((char) b));
                    }
                }
                String[] data = str.toString().split("§");
                String serverMotd = data[0];
                int onlinePlayers = Integer.parseInt(data[1]);
                int maxPlayers = Integer.parseInt(data[2]);
                return String.format(
                        "MOTD: \"%s\"\nOnline Players: %d/%d", serverMotd,
                        onlinePlayers, maxPlayers);
            } catch (UnknownHostException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}
