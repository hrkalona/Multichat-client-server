import java.io.BufferedReader;
import java.io.PrintWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class UserInfo {
    private PrintWriter out;
    private BufferedReader in;
    private String IP;
    private int PORT;
    private String Name;

    public UserInfo(PrintWriter out, BufferedReader in, String IP, int PORT, String Name) {

        this.out = out;
        this.in = in;
        this.IP = IP;
        this.PORT = PORT;
        this.Name = Name;
        
    }

    public PrintWriter getOut() {

        return this.out;

    }

    public BufferedReader getIn() {

        return this.in;

    }

    public String getIP() {

        return this.IP;
        
    }

    public int getPort() {

        return this.PORT;
        
    }

    public String getName() {

        return this.Name;
        
    }

}
