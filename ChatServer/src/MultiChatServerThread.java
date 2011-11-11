import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MultiChatServerThread extends Thread {
    private ArrayList<UserInfo> info;
    private UserInfo entry;
    private Socket socket;
    private String connected_hand_shake;
    private String disconnected_hand_shake;
    private String whois_hand_shake;
    public static final int MAX_SIZE_OF_NAME = 15;

    public MultiChatServerThread(ArrayList<UserInfo> info, UserInfo entry , Socket socket, String connected_hand_shake, String disconnected_hand_shake, String whois_hand_shake) {

	super("MultiChatServerThread");
        this.info = info;
        this.entry = entry;
        this.socket = socket;
        this.connected_hand_shake = connected_hand_shake;
        this.disconnected_hand_shake = disconnected_hand_shake;
        this.whois_hand_shake = whois_hand_shake;

    }


    public void run() {

        Calendar calendar = new GregorianCalendar();

        calendar = new GregorianCalendar();
        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] The Client " + entry.getIP() + " [ " + entry.getName() + " ]" + " has connected to the Server.");

        long connected_time = System.currentTimeMillis();

	try {

	    String inputLine;

            for(int i = 0; i < info.size(); i++) {
                if(info.get(i) != entry) {
                    info.get(i).getOut().println(connected_hand_shake);
                    info.get(i).getOut().println(entry.getName());
                }
                info.get(i).getOut().println(info.size() - 1);
            }

            for(int i = 0; i < info.size(); i++) {
                for(int j = 0; j < info.size(); j++) {
                    if(info.get(i) != info.get(j)) {
                        info.get(i).getOut().println(info.get(j).getName());
                    }
                }
            }

            try {
	        while ((inputLine = entry.getIn().readLine()) != null) {
                    if(inputLine.equals(whois_hand_shake)) {
                        inputLine = entry.getIn().readLine();
                        UserInfo ptr = null;
                        for(int i = 0; i < info.size(); i++) {
                            if(info.get(i).getName().equalsIgnoreCase(inputLine)) {
                                ptr = info.get(i);
                                break;
                            }
                        }
                        if(ptr != null) {
                            entry.getOut().println(whois_hand_shake);
                            entry.getOut().println(ptr.getName() + " @ " + ptr.getIP());
                        }
                        else {
                            entry.getOut().println(whois_hand_shake);
                            entry.getOut().println(inputLine + " does not exist.");
                        }
                    }
                    else {
                        for(int i = 0; i < info.size(); i++) {
                            info.get(i).getOut().println(inputLine);
                            info.get(i).getOut().println(entry.getName());
                        }
                        calendar = new GregorianCalendar();
                        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] " + String.format("%" + MAX_SIZE_OF_NAME + "s", entry.getName()) + " : " + inputLine);
                    }
                }
            }
            catch(SocketException ex) {}

            for(int i = 0; i < info.size(); i++) {
                if(info.get(i) != entry) {
                    info.get(i).getOut().println(disconnected_hand_shake);
                    info.get(i).getOut().println(entry.getName());
                }
            }
            for(int i = 0; i < info.size(); i++) {
                if(info.get(i) == entry) {
                    info.remove(i);
                }
            }

            for(int i = 0; i < info.size(); i++) {
                info.get(i).getOut().println(info.size() - 1);
            }

            for(int i = 0; i < info.size(); i++) {
                for(int j = 0; j < info.size(); j++) {
                    if(info.get(i) != info.get(j)) {
                        info.get(i).getOut().println(info.get(j).getName());
                    }
                }
            }

            connected_time = System.currentTimeMillis() - connected_time;

            if((connected_time / 1000) < 60) {
                calendar = new GregorianCalendar();
                System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] " + entry.getIP() + " [ " + entry.getName() + " ] was connected to the Server for " + (connected_time / 1000) % 60 + " second(s).");
            }
            else {
                if((connected_time / 1000 / 60) < 60) {
                    calendar = new GregorianCalendar();
                    System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] " + entry.getIP() + " [ " + entry.getName() + " ] was connected to the Server for " + (connected_time / 1000 / 60) % 60 + " minute(s) " + (connected_time / 1000) % 60 + " second(s).");
                }
                else {
                    if((connected_time / 1000 / 60 / 60) <  24) {
                        calendar = new GregorianCalendar();
                        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] " + entry.getIP() + " [ " + entry.getName() + " ] was connected to the Server for " + connected_time / 1000 / 60 / 60 + " hour(s) " + (connected_time / 1000 / 60) % 60 + " minute(s) " + (connected_time / 1000) % 60 + " second(s).");
                    }
                    else {
                        calendar = new GregorianCalendar();
                        System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] " + entry.getIP() + " [ " + entry.getName() + " ] was connected to the Server for " + connected_time / 1000 / 60 / 60 / 24 + " day(s) " + (connected_time / 1000 / 60 / 60) % 24 + " hour(s) " + (connected_time / 1000 / 60) % 60 + " minute(s) " + (connected_time / 1000) % 60 + " second(s).");
                    }
                }
            }

            calendar = new GregorianCalendar();
            System.out.println("<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + ">" + " [Thread:" + String.format("%4d", getId()) + "] This thread is now terminating.");
            
	    entry.getIn().close();
	    socket.close();

	}
        catch (IOException e) {}
        
    }
}
