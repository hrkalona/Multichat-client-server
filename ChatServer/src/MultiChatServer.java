import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class MultiChatServer {

    public static void main(String[] args) throws IOException {
      ServerSocket connectionWithSender = null;
      boolean listening = true;
      String password;

        Calendar calendar = new GregorianCalendar();

        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " The Multithreaded Chat Server is now starting.");

        calendar = new GregorianCalendar();
        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " Enter the server's password.");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        password = stdIn.readLine();
        
        try {
            connectionWithSender = new ServerSocket(5002);
        } catch (IOException e) {
            calendar = new GregorianCalendar();
            System.err.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " Could not listen on port: 5002.");
            System.exit(-1);
        }


        InetAddress thisIp = InetAddress.getLocalHost();

        calendar = new GregorianCalendar();
        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " IP: " + thisIp.getHostAddress() + " PORT: " + connectionWithSender.getLocalPort());
        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " PASSWORD: " + password);
        
        MultiChatServerThread thread;

        calendar = new GregorianCalendar();
        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " Awaiting connections.");
        
        ArrayList<UserInfo> info = new ArrayList<UserInfo>(10);
        
        Random generator = new Random(System.currentTimeMillis());

        String connected_hand_shake = "";
        String disconnected_hand_shake = "";
        String whois_hand_shake = "";

        for(int i = 0; i < 256; i++) {
            connected_hand_shake += generator.nextInt(10);
        }

        do {
            for(int i = 0; i < 256; i++) {
                disconnected_hand_shake += generator.nextInt(10);
            }
        } while(connected_hand_shake.equals(disconnected_hand_shake));

        do {
            for(int i = 0; i < 256; i++) {
                whois_hand_shake += generator.nextInt(10);
            }
        } while(connected_hand_shake.equals(whois_hand_shake) || disconnected_hand_shake.equals(whois_hand_shake));

        while (listening) {

            try {
                Socket SenderSocket = connectionWithSender.accept();
                PrintWriter out = new PrintWriter(SenderSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(SenderSocket.getInputStream()));
                String pw_entry = in.readLine();

                out.println("waitForPasswordVerification");
                if(!pw_entry.equals(password)) {
                    SenderSocket.close();
                    in.close();
                    out.close();
                }
                out.println("Check");
            
                String name = in.readLine();

                Boolean name_found = false;
                out.println("waitForNameChecking");
                for(int i = 0; i < info.size(); i++) {
                    if(info.get(i).getName().equalsIgnoreCase(name)) {
                        SenderSocket.close();
                        in.close();
                        out.close();
                        name_found = true;
                    }
                }

                if(name_found == false) {
                    out.println("Check");
                    out.println(connected_hand_shake);
                    out.println(disconnected_hand_shake);
                    out.println(whois_hand_shake);
                    UserInfo entry = new UserInfo(out, in, ("" + SenderSocket.getInetAddress()).substring(1), SenderSocket.getPort(), name);
                    info.add(entry);
                    thread = new MultiChatServerThread(info, entry, SenderSocket, connected_hand_shake, disconnected_hand_shake, whois_hand_shake);
                    thread.start();
                }
            }
            catch(Exception ex) {}

        }
	  

        connectionWithSender.close();
    }
}
