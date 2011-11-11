import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


enum StateOfClient {CONNECTED, DISCONNECTED};
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class ChatClient extends JFrame {
  private JPanel panel1;
  private JPanel panel2;
  private JPanel panel3;
  private TextArea textArea1;
  private TextArea textArea2;
  private TextField textField;
  private Calendar calendar;
  private String chat_entry;
  private PrintWriter out;
  private BufferedReader in;
  private String IP;
  private int PORT;
  private String SocketEntryMessage;
  private String SocketEntryName;
  private JButton button_send;
  private JFrame connect;
  private JTextField name_field;
  private JTextField ip_field1;
  private JTextField ip_field2;
  private JTextField ip_field3;
  private JTextField ip_field4;
  private JTextField port_field;
  private Socket connectionWithServer;
  private MenuItem menu_connect;
  private MenuItem menu_disconnect;
  private Boolean running;
  private String name_field_entry;
  private String ip_field1_entry;
  private String ip_field2_entry;
  private String ip_field3_entry;
  private String ip_field4_entry;
  private String port_field_entry;
  private String pw_field_entry;
  private JFrame fonts_color_frame;
  private JFrame background_color_frame;
  private JColorChooser font_color_chooser;
  private JColorChooser background_color_chooser;
  private Color background_color;
  private Color font_color;
  private JPasswordField password;
  private MenuItem menu_about;
  private JFrame about;
  private MenuItem menu_fonts_color;
  private MenuItem menu_background_color;
  private StateOfClient state;
  private MenuItem menu_sounds;
  private JFrame sounds_frame;
  private JComboBox[] choice;
  private JButton[] play;
  private JCheckBox[] checkbox;
  private String[] selected_sounds;
  private int[] selected_sounds_index;
  private Boolean[] enabled_sounds;
  private Settings settings;
  private ArrayList<String> last_used;
  private int last_used_index;
  private String connected_hand_shake;
  private String disconnected_hand_shake;
  private String whois_hand_shake;
  private int i;
  public static final int NUM_OF_SOUNDS = 6;
  public static final int MAX_SIZE_OF_NAME = 15;
  public static final int NUMBER_OF_LAST_USED = 40;


    public ChatClient() {
        super();
        setSize(796, 508);
        setTitle("Chat Client BETA");
        setResizable(false);

        loadSettings();

        background_color = settings.getBackgroundColor();
        getContentPane().setBackground(background_color);

        font_color = settings.getFontColor();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                
                try {
                    out.close();
                    in.close();
                    connectionWithServer.close();
                }
                catch (Exception ex) {}
               
                System.exit(0);

            }
        });
        

        chat_entry = "";
        SocketEntryMessage = "";
        SocketEntryName = "";

        running = true;
        state = StateOfClient.DISCONNECTED;

        name_field_entry = settings.getNameFieldEntry();
        ip_field1_entry = settings.getIpField1Entry();
        ip_field2_entry = settings.getIpField2Entry();
        ip_field3_entry = settings.getIpField3Entry();
        ip_field4_entry = settings.getIpField4Entry();
        port_field_entry = settings.getPortEntry();

        selected_sounds = new String[NUM_OF_SOUNDS];
        selected_sounds_index = new int[NUM_OF_SOUNDS];
        enabled_sounds = new Boolean[NUM_OF_SOUNDS];

        selected_sounds[0] = settings.getSelectedSounds()[0];
        selected_sounds[1] = settings.getSelectedSounds()[1];
        selected_sounds[2] = settings.getSelectedSounds()[2];
        selected_sounds[3] = settings.getSelectedSounds()[3];
        selected_sounds[4] = settings.getSelectedSounds()[4];
        selected_sounds[5] = settings.getSelectedSounds()[5];

        for(i = 0; i < NUM_OF_SOUNDS; i++) {
            selected_sounds_index[i] = settings.getSelectedSoundsIndex()[i];
            enabled_sounds[i] = settings.getEnabledSounds()[i];
        }

        last_used = new ArrayList<String>(NUMBER_OF_LAST_USED);
        last_used_index = 0;

        KeyListener keylistener = new KeyListener() {

            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER :
                        chat_entry =  textField.getText();
                        if(chat_entry.length() > 0) {
                            if(state == StateOfClient.DISCONNECTED) {
                                if(chat_entry.equals("/help")) {
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Available Commands:\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/quit\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/disconnect\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/whois \"name\"\n");
                                    textArea1.setCaretPosition(textArea1.getText().length());
                                    textField.setText("");
                                    textField.requestFocus();
                                    lastUsedListInsertion();
                                    return;
                                }
                                else {
                                    if(chat_entry.equals("/quit")) {
                                        try {
                                            out.close();
                                            in.close();
                                            connectionWithServer.close();
                                        }
                                        catch(Exception ex) {}
                                        System.exit(0);
                                    }
                                    else {
                                        if(chat_entry.equals("/disconnect")) {
                                            calendar = new GregorianCalendar();
                                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                            textArea1.setCaretPosition(textArea1.getText().length());
                                            textField.setText("");
                                        }
                                        else {
                                            if(chat_entry.length() > 7 && chat_entry.substring(0, 7).equals("/whois ")) {
                                                calendar = new GregorianCalendar();
                                                textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                                textArea1.setCaretPosition(textArea1.getText().length());
                                                textField.setText("");
                                            }
                                            else {
                                                if(chat_entry.charAt(0) == '/') {
                                                    calendar = new GregorianCalendar();
                                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Unknown command.\n");
                                                    textArea1.setCaretPosition(textArea1.getText().length());
                                                    textField.setText("");
                                                }
                                                else {
                                                    calendar = new GregorianCalendar();
                                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                                    textArea1.setCaretPosition(textArea1.getText().length());
                                                    textField.setText("");
                                                }
                                            }
                                        }
                                    }
                                }

                                if(enabled_sounds[5]) {
                                    InputStream sound_stream_in = null;
                                    try {
                                        sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[5] + ".wav");
                                    }
                                    catch(FileNotFoundException ex) {
                                        textField.requestFocus();
                                        lastUsedListInsertion();
                                        return;
                                    }

                                    AudioStream as = null;
                                    try {
                                        as = new AudioStream(sound_stream_in);
                                    }
                                    catch(IOException ex) {
                                        textField.requestFocus();
                                        lastUsedListInsertion();
                                        return;
                                    }

                                    AudioPlayer.player.start(as);
                                }
                            }
                            else {
                                if(chat_entry.equals("/help")) {
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Available Commands:\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/quit\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/disconnect\n");
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/whois \"name\"\n");
                                    textArea1.setCaretPosition(textArea1.getText().length());
                                    textField.setText("");
                                }
                                else {
                                    if(chat_entry.equals("/quit")) {
                                        try {
                                            out.close();
                                            in.close();
                                            connectionWithServer.close();
                                        }
                                        catch(Exception ex) {}
                                        System.exit(0);
                                    }
                                    else {
                                        if(chat_entry.equals("/disconnect")) {
                                            disconnect();
                                            textField.setText("");
                                        }
                                        else {
                                            if(chat_entry.length() > 7 && chat_entry.substring(0, 7).equals("/whois ")) {
                                                out.println(whois_hand_shake);
                                                out.println(chat_entry.substring(7, chat_entry.length()));
                                                textField.setText("");
                                            }
                                            else {
                                                if(chat_entry.charAt(0) == '/') {
                                                    calendar = new GregorianCalendar();
                                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Unknown command.\n");
                                                    textArea1.setCaretPosition(textArea1.getText().length());
                                                    textField.setText("");

                                                    if(enabled_sounds[5]) {
                                                        InputStream sound_stream_in = null;
                                                        try {
                                                            sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[5] + ".wav");
                                                        }
                                                        catch(FileNotFoundException ex) {
                                                            textField.requestFocus();
                                                            lastUsedListInsertion();
                                                            return;
                                                        }

                                                        AudioStream as = null;
                                                        try {
                                                            as = new AudioStream(sound_stream_in);
                                                        }
                                                        catch(IOException ex) {
                                                            textField.requestFocus();
                                                            lastUsedListInsertion();
                                                            return;
                                                        }

                                                        AudioPlayer.player.start(as);
                                                    }
                                                }
                                                else {
                                                    out.println(chat_entry);
                                                    textField.setText("");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            textField.requestFocus();
                            lastUsedListInsertion();
                        }
                        break;
                    case KeyEvent.VK_UP :
                        if(textField.isFocusOwner() && last_used.size() > 0) {
                            textField.setText(last_used.get(last_used_index));
                            textField.setCaretPosition(textField.getCaretPosition() + last_used.get(last_used_index).length());
                            if(last_used_index > 0) {
                                last_used_index--;
                            }
                            else {
                                last_used_index = last_used.size() - 1;
                            }
                        }
                        break;
                    case KeyEvent.VK_DOWN :
                        if(textField.isFocusOwner() && last_used.size() > 0) {
                            if(last_used_index < last_used.size() - 1) {
                                last_used_index++;
                            }
                            else {
                                last_used_index = 0;
                            }
                            textField.setText(last_used.get(last_used_index));
                            textField.setCaretPosition(textField.getCaretPosition() + last_used.get(last_used_index).length());
                        }
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP :
                        textField.setCaretPosition(textField.getCaretPosition() + 1);
                    break;
                }

            }

        };

        

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();

        setLayout(new FlowLayout());
        textArea1 = new TextArea(25, 87);
        textArea1.setEditable(false);
        textArea1.setBackground(Color.WHITE);
        textArea1.setForeground(font_color);

        textArea2 = new TextArea(25, 14);
        textArea2.setEditable(false);
        textArea2.setFont(new Font("Bold", Font.BOLD, 12));
        textArea2.setBackground(Color.WHITE);
        textArea2.setForeground(font_color);

        calendar = new GregorianCalendar();
        textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Type /help to get the available commands.\n");
        textArea1.setCaretPosition(textArea1.getText().length());

        textField = new TextField(87);
        textField.setForeground(font_color);
        textField.requestFocus();
        textField.addKeyListener(keylistener);
        
        
        button_send = new JButton("Send");

        button_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chat_entry =  textField.getText();
                if(chat_entry.length() > 0) {
                    if(state == StateOfClient.DISCONNECTED) {
                        if(chat_entry.equals("/help")) {
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Available Commands:\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/quit\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/disconnect\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/whois \"name\"\n");
                            textArea1.setCaretPosition(textArea1.getText().length());
                            textField.setText("");
                            textField.requestFocus();
                            lastUsedListInsertion();
                            return;
                        }
                        else {
                            if(chat_entry.equals("/quit")) {
                                try {
                                    out.close();
                                    in.close();
                                    connectionWithServer.close();
                                }
                                catch(Exception ex) {}
                                System.exit(0);
                            }
                            else {
                                if(chat_entry.equals("/disconnect")) {
                                    calendar = new GregorianCalendar();
                                    textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                    textArea1.setCaretPosition(textArea1.getText().length());
                                    textField.setText("");
                                }
                                else {
                                    if(chat_entry.length() > 7 && chat_entry.substring(0, 7).equals("/whois ")) {
                                        calendar = new GregorianCalendar();
                                        textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                        textArea1.setCaretPosition(textArea1.getText().length());
                                        textField.setText("");
                                    }
                                    else {
                                        if(chat_entry.charAt(0) == '/') {
                                            calendar = new GregorianCalendar();
                                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Unknown command.\n");
                                            textArea1.setCaretPosition(textArea1.getText().length());
                                            textField.setText("");
                                        }
                                        else {
                                            calendar = new GregorianCalendar();
                                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "You are not connected to any Server.\n");
                                            textArea1.setCaretPosition(textArea1.getText().length());
                                            textField.setText("");
                                        }
                                    }
                                }
                            }
                        }

                        if(enabled_sounds[5]) {
                            InputStream sound_stream_in = null;
                            try {
                                sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[5] + ".wav");
                            }
                            catch(FileNotFoundException ex) {
                                textField.requestFocus();
                                lastUsedListInsertion();
                                return;
                            }

                            AudioStream as = null;
                            try {
                                as = new AudioStream(sound_stream_in);
                            }
                            catch(IOException ex) {
                                textField.requestFocus();
                                lastUsedListInsertion();
                                return;
                            }

                            AudioPlayer.player.start(as);
                        }
                    }
                    else {
                        if(chat_entry.equals("/help")) {
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Available Commands:\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/quit\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/disconnect\n");
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "/whois \"name\"\n");
                            textArea1.setCaretPosition(textArea1.getText().length());
                            textField.setText("");
                        }
                        else {
                            if(chat_entry.equals("/quit")) {
                                try {
                                    out.close();
                                    in.close();
                                    connectionWithServer.close();
                                }
                                catch(Exception ex) {}
                                System.exit(0);
                            }
                            else {
                                if(chat_entry.equals("/disconnect")) {
                                    disconnect();
                                    textField.setText("");
                                }
                                else {
                                    if(chat_entry.length() > 7 && chat_entry.substring(0, 7).equals("/whois ")) {
                                        out.println(whois_hand_shake);
                                        out.println(chat_entry.substring(7, chat_entry.length()));
                                        textField.setText("");
                                    }
                                    else {
                                        if(chat_entry.charAt(0) == '/') {
                                            calendar = new GregorianCalendar();
                                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Unknown command.\n");
                                            textArea1.setCaretPosition(textArea1.getText().length());
                                            textField.setText("");

                                            if(enabled_sounds[5]) {
                                                InputStream sound_stream_in = null;
                                                try {
                                                    sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[5] + ".wav");
                                                }
                                                catch(FileNotFoundException ex) {
                                                    textField.requestFocus();
                                                    lastUsedListInsertion();
                                                    return;
                                                }

                                                AudioStream as = null;
                                                try {
                                                    as = new AudioStream(sound_stream_in);
                                                }
                                                catch(IOException ex) {
                                                    textField.requestFocus();
                                                    lastUsedListInsertion();
                                                    return;
                                                }

                                                AudioPlayer.player.start(as);
                                            }
                                        }
                                        else {
                                            out.println(chat_entry);
                                            textField.setText("");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    lastUsedListInsertion();
                }
                textField.requestFocus();
            }
        });

        panel1.add(textArea1);
        panel2.add(textArea2);
        panel3.add(textField);
        panel3.add(new JLabel("         "));
        panel3.add(button_send);
        panel3.add(new JLabel("         "));
        

        
        add(panel1);
        add(new JLabel(""));
        add(panel2);
        add(panel3);
        add(new JLabel("                                     "));

        MenuBar menubar = new MenuBar();

        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Options");
        Menu menu3 = new Menu("Help");

        menu_connect = new MenuItem("Connect");
        menu1.add(menu_connect);
        menu_disconnect = new MenuItem("Disconnect");
        menu_disconnect.setEnabled(false);
        menu1.addSeparator();
        menu1.add(menu_disconnect);
        menu1.addSeparator();
        MenuItem menu_quit = new MenuItem("Quit");
        menu1.add(menu_quit);

        menu_fonts_color = new MenuItem("Fonts Color");
        menu_background_color = new MenuItem("Background Color");
        menu_sounds = new MenuItem("Sounds");

        menu_about = new MenuItem("About");

        menu_quit.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
        menu_connect.setShortcut(new MenuShortcut(KeyEvent.VK_S));
        menu_disconnect.setShortcut(new MenuShortcut(KeyEvent.VK_D));
        menu_fonts_color.setShortcut(new MenuShortcut(KeyEvent.VK_F));
        menu_background_color.setShortcut(new MenuShortcut(KeyEvent.VK_B));
        menu_sounds.setShortcut(new MenuShortcut(KeyEvent.VK_U));
        menu_about.setShortcut(new MenuShortcut(KeyEvent.VK_A));

        menu2.add(menu_fonts_color);
        menu2.addSeparator();
        menu2.add(menu_background_color);
        menu2.addSeparator();
        menu2.add(menu_sounds);

        menu3.add(menu_about);
        
        menubar.add(menu1);
        menubar.add(menu2);
        menubar.add(menu3);

        setMenuBar(menubar);

        menu_quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    out.close();
                    in.close();
                    connectionWithServer.close();
                }
                catch (Exception ex) {}
                System.exit(0);
            }
        });

        menu_fonts_color.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int color_window_width = 440;
                int color_window_height = 420;
                fonts_color_frame = new JFrame("Fonts Color");
                fonts_color_frame.setLayout(new FlowLayout());
                fonts_color_frame.setSize(color_window_width, color_window_height);
                fonts_color_frame.setLocation((int)(getLocation().getX() + getSize().getWidth() / 2) - (color_window_width / 2), (int)(getLocation().getY() + getSize().getHeight() / 2) - (color_window_height / 2));
                fonts_color_frame.getContentPane().setBackground(background_color);
                fonts_color_frame.setResizable(false);
                menu_fonts_color.setEnabled(false);
                font_color_chooser = new JColorChooser();
                fonts_color_frame.add(font_color_chooser);
                

                fonts_color_frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        textField.requestFocus();
                        menu_fonts_color.setEnabled(true);
                        fonts_color_frame.dispose();

                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        font_color = font_color_chooser.getColor();
                        textArea1.setForeground(font_color);
                        textArea2.setForeground(font_color);
                        textField.setForeground(font_color);
                        try {
                            name_field.setForeground(font_color);
                            ip_field1.setForeground(font_color);
                            ip_field2.setForeground(font_color);
                            ip_field3.setForeground(font_color);
                            ip_field4.setForeground(font_color);
                            port_field.setForeground(font_color);
                            password.setForeground(font_color);
                        }
                        catch(NullPointerException ex) {}

                        settings.setFontColor(font_color);
                        saveSettings();

                        textField.requestFocus();
                        menu_fonts_color.setEnabled(true);
                        fonts_color_frame.dispose();
                    }
                });

                JButton close = new JButton("Cancel");
                close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textField.requestFocus();
                        menu_fonts_color.setEnabled(true);
                        fonts_color_frame.dispose();
                    }
                });

                fonts_color_frame.add(confirmButton);
                fonts_color_frame.add(close);

                fonts_color_frame.setVisible(true);

            }
        });

        menu_background_color.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int color_window_width = 440;
                int color_window_height = 420;
                background_color_frame = new JFrame("Background Color");
                background_color_frame.setLayout(new FlowLayout());
                background_color_frame.setSize(color_window_width, color_window_height);
                background_color_frame.setLocation((int)(getLocation().getX() + getSize().getWidth() / 2) - (color_window_width / 2), (int)(getLocation().getY() + getSize().getHeight() / 2) - (color_window_height / 2));
                background_color_frame.getContentPane().setBackground(background_color);
                background_color_frame.setResizable(false);
                menu_background_color.setEnabled(false);
                background_color_chooser = new JColorChooser();
                background_color_frame.add(background_color_chooser);


                background_color_frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        textField.requestFocus();
                        menu_background_color.setEnabled(true);
                        background_color_frame.dispose();

                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textField.requestFocus();
                        background_color = background_color_chooser.getColor();
                        background_color_frame.setBackground(background_color);
                        getContentPane().setBackground(background_color);

                        try {
                            connect.setBackground(background_color);
                        }
                        catch(NullPointerException ex) {}

                        try {
                            sounds_frame.getContentPane().setBackground(background_color);
                        }
                        catch(NullPointerException ex) {}

                        try {
                            fonts_color_frame.getContentPane().setBackground(background_color);
                        }
                        catch(NullPointerException ex) {}

                        try {
                            about.getContentPane().setBackground(background_color);
                        }
                        catch(NullPointerException ex) {}

                        settings.setBackgroundColor(background_color);
                        saveSettings();
                        
                        menu_background_color.setEnabled(true);
                        background_color_frame.dispose();
                    }
                });

                JButton close = new JButton("Cancel");
                close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textField.requestFocus();
                        menu_background_color.setEnabled(true);
                        background_color_frame.dispose();
                    }
                });

                background_color_frame.add(confirmButton);
                background_color_frame.add(close);

                background_color_frame.setVisible(true);

            }
        });

        menu_sounds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int sounds_window_width = 440;
                int sounds_window_height = 316;
                sounds_frame = new JFrame("Sounds");
                sounds_frame.setLayout(new FlowLayout());
                sounds_frame.setSize(sounds_window_width, sounds_window_height);
                sounds_frame.setLocation((int)(getLocation().getX() + getSize().getWidth() / 2) - (sounds_window_width / 2), (int)(getLocation().getY() + getSize().getHeight() / 2) - (sounds_window_height / 2));
                sounds_frame.getContentPane().setBackground(background_color);
                sounds_frame.setResizable(false);
                menu_sounds.setEnabled(false);

                JPanel panel8 = new JPanel();
                JPanel panel9 = new JPanel();
                JPanel panel10 = new JPanel();
                JPanel panel11 = new JPanel();
                JPanel panel12 = new JPanel();
                JPanel panel13 = new JPanel();

                panel8.setBackground(Color.LIGHT_GRAY);
                panel9.setBackground(Color.LIGHT_GRAY);
                panel10.setBackground(Color.LIGHT_GRAY);
                panel11.setBackground(Color.LIGHT_GRAY);
                panel12.setBackground(Color.LIGHT_GRAY);
                panel13.setBackground(Color.LIGHT_GRAY);

                choice = new JComboBox[NUM_OF_SOUNDS];
                play = new JButton[NUM_OF_SOUNDS];
                checkbox = new JCheckBox[NUM_OF_SOUNDS];

                for(i = 0; i < play.length; i++) {
                    checkbox[i] = new JCheckBox();
                    checkbox[i].setSelected(enabled_sounds[i]);
                    checkbox[i].addActionListener(new ActionListener() {
                        int temp = i;
                        
                        public void actionPerformed(ActionEvent e) {
                            if(checkbox[temp].isSelected()) {
                                choice[temp].setEnabled(true);
                                play[temp].setEnabled(true);
                            }
                            else {
                                choice[temp].setEnabled(false);
                                play[temp].setEnabled(false);
                            }
                        }
                    });
                }


                panel8.add(checkbox[0]);
                panel8.add(new JLabel("                "));
                panel8.add(new JLabel("Connected"));
                panel8.add(new JLabel("                           "));
                String[] connected_sounds = {"connect1", "connect2", "connect3"};
                choice[0] = new JComboBox(connected_sounds);
                
                panel9.add(checkbox[1]);
                panel9.add(new JLabel("             "));
                panel9.add(new JLabel("Disconnected"));
                panel9.add(new JLabel("                   "));
                String[] disconnected_sounds = {"disconnect1", "disconnect2"};
                choice[1] = new JComboBox(disconnected_sounds);
                
                panel10.add(checkbox[2]);
                panel10.add(new JLabel("           "));
                panel10.add(new JLabel("User Connected"));
                panel10.add(new JLabel("              "));
                String[] user_connected_sounds = {"userConnect1", "userConnect2", "userConnect3"};
                choice[2] = new JComboBox(user_connected_sounds);
                
                panel11.add(checkbox[3]);
                panel11.add(new JLabel("        "));
                panel11.add(new JLabel("User Disconnected"));
                panel11.add(new JLabel("      "));
                String[] user_disconnected_sounds = {"userDisconnect1", "userDisconnect2"};
                choice[3] = new JComboBox(user_disconnected_sounds);
                
                panel12.add(checkbox[4]);
                panel12.add(new JLabel("             "));
                panel12.add(new JLabel("New Message"));
                panel12.add(new JLabel("               "));
                String[] message_sounds = {"newMessage1", "newMessage2", "newMessage3", "newMessage4", "newMessage5"};
                choice[4] = new JComboBox(message_sounds);
                
                panel13.add(checkbox[5]);
                panel13.add(new JLabel("                "));
                panel13.add(new JLabel("Warnings"));
                panel13.add(new JLabel("                              "));
                String[] not_connected_sounds = {"warning1", "warning2", "warning3"};
                choice[5] = new JComboBox(not_connected_sounds);
                

                for(i = 0; i < play.length; i++) {
                    choice[i].setEnabled(enabled_sounds[i]);
                    choice[i].setSelectedIndex(selected_sounds_index[i]);
                    play[i] = new JButton("Play");
                    play[i].setEnabled(enabled_sounds[i]);
                    play[i].addActionListener(new ActionListener() {
                        int temp = i;
                        
                        public void actionPerformed(ActionEvent e) {
                            String selected = (String)choice[temp].getSelectedItem();

                            InputStream sound_stream_in = null;
                            try {
                                sound_stream_in = new FileInputStream("./Sounds/" + selected + ".wav");
                            }
                            catch(FileNotFoundException ex) {
                                return;
                            }
                            AudioStream as = null;
                            try {
                                as = new AudioStream(sound_stream_in);
                            }
                            catch(IOException ex) {
                                return;
                            }

                            AudioPlayer.player.start(as);
                        }
                    });
                }


                panel8.add(choice[0]);
                panel8.add(new JLabel("       "));
                panel8.add(play[0]);

                panel9.add(choice[1]);
                panel9.add(new JLabel("       "));
                panel9.add(play[1]);

                panel10.add(choice[2]);
                panel10.add(new JLabel("       "));
                panel10.add(play[2]);

                panel11.add(choice[3]);
                panel11.add(new JLabel("       "));
                panel11.add(play[3]);

                panel12.add(choice[4]);
                panel12.add(new JLabel("       "));
                panel12.add(play[4]);

                panel13.add(choice[5]);
                panel13.add(new JLabel("       "));
                panel13.add(play[5]);


                sounds_frame.add(panel8);
                sounds_frame.add(panel9);
                sounds_frame.add(panel10);
                sounds_frame.add(panel11);
                sounds_frame.add(panel12);
                sounds_frame.add(panel13);



                sounds_frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        textField.requestFocus();
                        menu_sounds.setEnabled(true);
                        sounds_frame.dispose();

                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for(i = 0; i < NUM_OF_SOUNDS; i++) {
                            enabled_sounds[i] = checkbox[i].isSelected();
                            selected_sounds_index[i] = choice[i].getSelectedIndex();
                            selected_sounds[i] = (String)choice[i].getSelectedItem();
                        }

                        settings.setEnabledSounds(enabled_sounds);
                        settings.setSelectedSoundsIndex(selected_sounds_index);
                        settings.setSelectedSounds(selected_sounds);
                        saveSettings();

                        textField.requestFocus();
                        menu_sounds.setEnabled(true);
                        sounds_frame.dispose();
                    }
                });

                JButton close = new JButton("Cancel");
                close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textField.requestFocus();
                        menu_sounds.setEnabled(true);
                        sounds_frame.dispose();
                    }
                });

                sounds_frame.add(confirmButton);
                sounds_frame.add(close);

                sounds_frame.setVisible(true);

            }
        });

        menu_connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              int connect_window_width = 240;
              int connect_window_height = 210;

                connect = new JFrame("Connection Options");
                connect.setSize(connect_window_width, connect_window_height);
                connect.setLocation((int)(getLocation().getX() + getSize().getWidth() / 2) - (connect_window_width / 2), (int)(getLocation().getY() + getSize().getHeight() / 2) - (connect_window_height / 2));
                connect.getContentPane().setBackground(background_color);
                connect.setResizable(false);
                menu_connect.setEnabled(false);

                connect.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {

                        if(name_field_entry.length() < 1 || name_field_entry.length() > MAX_SIZE_OF_NAME) {
                            name_field_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field1_entry) < 0 || Integer.parseInt(ip_field1_entry) > 255) {
                                ip_field1_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field1_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field2_entry) < 0 || Integer.parseInt(ip_field2_entry) > 255) {
                                ip_field2_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field2_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field3_entry) < 0 || Integer.parseInt(ip_field3_entry) > 255) {
                                ip_field3_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field3_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field4_entry) < 0 || Integer.parseInt(ip_field4_entry) > 255) {
                                ip_field4_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field4_entry = "";
                        }

                        try {
                            if(Integer.parseInt(port_field_entry) < 0 || Integer.parseInt(port_field_entry) > 65535) {
                                port_field_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            port_field_entry = "";
                        }

                        settings.setNameFieldEntry(name_field_entry);
                        settings.setIpField1Entry(ip_field1_entry);
                        settings.setIpField2Entry(ip_field2_entry);
                        settings.setIpField3Entry(ip_field3_entry);
                        settings.setIpField4Entry(ip_field4_entry);
                        settings.setPortEntry(port_field_entry);
                        saveSettings();

                        textField.requestFocus();
                        menu_connect.setEnabled(true);
                        connect.dispose();

                    }
                });

                connect.setLayout(new FlowLayout());
                
                name_field = new JTextField(10);
                ip_field1 = new JTextField(2);
                ip_field2 = new JTextField(2);
                ip_field3 = new JTextField(2);
                ip_field4 = new JTextField(2);
                port_field = new JTextField(5);
                password = new JPasswordField(10);

                name_field.setForeground(font_color);
                ip_field1.setForeground(font_color);
                ip_field2.setForeground(font_color);
                ip_field3.setForeground(font_color);
                ip_field4.setForeground(font_color);
                port_field.setForeground(font_color);
                password.setForeground(font_color);

                name_field.setText(name_field_entry);
                ip_field1.setText(ip_field1_entry);
                ip_field2.setText(ip_field2_entry);
                ip_field3.setText(ip_field3_entry);
                ip_field4.setText(ip_field4_entry);
                port_field.setText(port_field_entry);

                JButton connectButton = new JButton("Connect");
                connectButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        int error_found = 0;

                        name_field_entry = name_field.getText();
                        if(name_field_entry.length() < 1 || name_field_entry.length() > MAX_SIZE_OF_NAME) {
                            name_field.setBackground(Color.RED);
                            error_found++;
                        }
                        else {
                            name_field.setBackground(Color.WHITE);
                        }
                        

                        ip_field1_entry = ip_field1.getText();
                        try {
                            if(Integer.parseInt(ip_field1_entry) < 0 || Integer.parseInt(ip_field1_entry) > 255) {
                                ip_field1.setBackground(Color.RED);
                                error_found++;
                            }
                            else {
                                ip_field1.setBackground(Color.WHITE);
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field1.setBackground(Color.RED);
                            error_found++;
                        }

                        try {
                            ip_field2_entry = ip_field2.getText();
                            if(Integer.parseInt(ip_field2_entry) < 0 || Integer.parseInt(ip_field2_entry) > 255) {
                                ip_field2.setBackground(Color.RED);
                                error_found++;
                            }
                            else {
                               ip_field2.setBackground(Color.WHITE);
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field2.setBackground(Color.RED);
                            error_found++;
                        }

                        try {
                            ip_field3_entry = ip_field3.getText();
                            if(Integer.parseInt(ip_field3_entry) < 0 || Integer.parseInt(ip_field3_entry) > 255) {
                                ip_field3.setBackground(Color.RED);
                                error_found++;
                            }
                            else {
                                ip_field3.setBackground(Color.WHITE);
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field3.setBackground(Color.RED);
                            error_found++;
                        }

                        try {
                            ip_field4_entry = ip_field4.getText();
                            if(Integer.parseInt(ip_field4_entry) < 0 || Integer.parseInt(ip_field4_entry) > 255) {
                                ip_field4.setBackground(Color.RED);
                                error_found++;
                            }
                            else {
                                ip_field4.setBackground(Color.WHITE);
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field4.setBackground(Color.RED);
                            error_found++;
                        }

                        if(error_found == 0) {
                            IP = ip_field1_entry + "." + ip_field2_entry + "." + ip_field3_entry + "." + ip_field4_entry;
                        }


                        try {
                            port_field_entry = port_field.getText();

                            if(Integer.parseInt(port_field_entry) < 0 || Integer.parseInt(port_field_entry) > 65535) {
                                port_field.setBackground(Color.RED);
                                error_found++;
                            }
                            else {
                                port_field.setBackground(Color.WHITE);
                            }
                        }
                        catch(NumberFormatException ex) {
                            port_field.setBackground(Color.RED);
                            error_found++;
                        }
                        
                        if(error_found == 0) {
                            PORT = Integer.valueOf(port_field_entry);
                        }
                        else {
                            return;
                        }

                        pw_field_entry = String.copyValueOf(password.getPassword());
                        
                        try {

                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Attempting to connect to " + IP + " .\n");
                            textArea1.setCaretPosition(textArea1.getText().length());

                            connectionWithServer = new Socket(IP, PORT);

                            out = new PrintWriter(connectionWithServer.getOutputStream(), true);
                            in = new BufferedReader(new InputStreamReader(connectionWithServer.getInputStream()));
                            out.println(pw_field_entry);

                            try {
                                while((in.readLine()).equals("waitForPasswordVerification")) {}
                            }
                            catch(NullPointerException ex) {
                                calendar = new GregorianCalendar();
                                textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Connection Failed, the password is incorrect.\n");
                                textArea1.setCaretPosition(textArea1.getText().length());
                                out = null;
                                in = null;

                                if(enabled_sounds[1]) {
                                    InputStream sound_stream_in = null;
                                    try {
                                        sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[1] + ".wav");
                                    }
                                    catch(FileNotFoundException exc) {
                                        textField.requestFocus();
                                        menu_connect.setEnabled(true);
                                        connect.dispose();
                                        return;
                                    }

                                    AudioStream as = null;
                                    try {
                                        as = new AudioStream(sound_stream_in);
                                    }
                                    catch(IOException exc) {
                                        textField.requestFocus();
                                        menu_connect.setEnabled(true);
                                        connect.dispose();
                                        return;
                                    }

                                    AudioPlayer.player.start(as);
                                }

                                settings.setNameFieldEntry(name_field_entry);
                                settings.setIpField1Entry(ip_field1_entry);
                                settings.setIpField2Entry(ip_field2_entry);
                                settings.setIpField3Entry(ip_field3_entry);
                                settings.setIpField4Entry(ip_field4_entry);
                                settings.setPortEntry(port_field_entry);
                                saveSettings();

                                textField.requestFocus();
                                menu_connect.setEnabled(true);
                                connect.dispose();
                                return;
                            }

                            out.println(name_field_entry);
                            
                            try {
                                while((in.readLine()).equals("waitForNameChecking")) {}
                                connected_hand_shake = in.readLine();
                                disconnected_hand_shake = in.readLine();
                                whois_hand_shake = in.readLine();
                            }
                            catch(NullPointerException ex) {
                                calendar = new GregorianCalendar();
                                textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Connection Failed, the name " + name_field_entry + " already exists.\n");
                                textArea1.setCaretPosition(textArea1.getText().length());
                                out = null;
                                in = null;

                                if(enabled_sounds[1]) {
                                    InputStream sound_stream_in = null;
                                    try {
                                        sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[1] + ".wav");
                                    }
                                    catch(FileNotFoundException exc) {
                                        textField.requestFocus();
                                        menu_connect.setEnabled(true);
                                        connect.dispose();
                                        return;
                                    }

                                    AudioStream as = null;
                                    try {
                                        as = new AudioStream(sound_stream_in);
                                    }
                                    catch(IOException exc) {
                                        textField.requestFocus();
                                        menu_connect.setEnabled(true);
                                        connect.dispose();
                                        return;
                                    }

                                    AudioPlayer.player.start(as);
                                }

                                settings.setNameFieldEntry(name_field_entry);
                                settings.setIpField1Entry(ip_field1_entry);
                                settings.setIpField2Entry(ip_field2_entry);
                                settings.setIpField3Entry(ip_field3_entry);
                                settings.setIpField4Entry(ip_field4_entry);
                                settings.setPortEntry(port_field_entry);
                                saveSettings();

                                textField.requestFocus();
                                menu_connect.setEnabled(true);
                                connect.dispose();
                                return;
                            }   
                   
                        }
                        catch(UnknownHostException ex) {
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Connection Failed, could not connect to the requested server.\n");
                            textArea1.setCaretPosition(textArea1.getText().length());

                            if(enabled_sounds[1]) {
                                InputStream sound_stream_in = null;
                                try {
                                    sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[1] + ".wav");
                                }
                                catch(FileNotFoundException exc) {
                                    textField.requestFocus();
                                    menu_connect.setEnabled(true);
                                    connect.dispose();
                                    return;
                                }

                                AudioStream as = null;
                                try {
                                    as = new AudioStream(sound_stream_in);
                                }
                                catch(IOException exc) {
                                    textField.requestFocus();
                                    menu_connect.setEnabled(true);
                                    connect.dispose();
                                    return;
                                }

                                AudioPlayer.player.start(as);
                            }

                            settings.setNameFieldEntry(name_field_entry);
                            settings.setIpField1Entry(ip_field1_entry);
                            settings.setIpField2Entry(ip_field2_entry);
                            settings.setIpField3Entry(ip_field3_entry);
                            settings.setIpField4Entry(ip_field4_entry);
                            settings.setPortEntry(port_field_entry);
                            saveSettings();

                            textField.requestFocus();
                            menu_connect.setEnabled(true);
                            connect.dispose();
                            return;
                        }
                        catch(IOException ex) {
                            calendar = new GregorianCalendar();
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "Connection Failed, could not connect to the requested server.\n");
                            textArea1.setCaretPosition(textArea1.getText().length());
                            
                            if(enabled_sounds[1]) {
                                InputStream sound_stream_in = null;
                                try {
                                    sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[1] + ".wav");
                                }
                                catch(FileNotFoundException exc) {
                                    textField.requestFocus();
                                    menu_connect.setEnabled(true);
                                    connect.dispose();
                                    return;
                                }

                                AudioStream as = null;
                                try {
                                    as = new AudioStream(sound_stream_in);
                                }
                                catch(IOException exc) {
                                    textField.requestFocus();
                                    menu_connect.setEnabled(true);
                                    connect.dispose();
                                    return;
                                }

                                AudioPlayer.player.start(as);
                            }

                            settings.setNameFieldEntry(name_field_entry);
                            settings.setIpField1Entry(ip_field1_entry);
                            settings.setIpField2Entry(ip_field2_entry);
                            settings.setIpField3Entry(ip_field3_entry);
                            settings.setIpField4Entry(ip_field4_entry);
                            settings.setPortEntry(port_field_entry);
                            saveSettings();

                            textField.requestFocus();
                            menu_connect.setEnabled(true);
                            connect.dispose();
                            return;
                        }

                        state = StateOfClient.CONNECTED;

                        setTitle("Chat Client BETA" + " [ " + name_field_entry + " ] ");
                        textField.requestFocus();
                        menu_disconnect.setEnabled(true);
    
                        if(enabled_sounds[0]) {
                            InputStream sound_stream_in = null;
                            try {
                                sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[0] + ".wav");
                            }
                            catch(FileNotFoundException ex) {
                                connect.dispose();
                                return;
                            }

                            AudioStream as = null;
                            try {
                                as = new AudioStream(sound_stream_in);
                            }
                            catch(IOException ex) {
                                connect.dispose();
                                return;
                            }
                        
                            AudioPlayer.player.start(as);
                        }

                        settings.setNameFieldEntry(name_field_entry);
                        settings.setIpField1Entry(ip_field1_entry);
                        settings.setIpField2Entry(ip_field2_entry);
                        settings.setIpField3Entry(ip_field3_entry);
                        settings.setIpField4Entry(ip_field4_entry);
                        settings.setPortEntry(port_field_entry);
                        saveSettings();
                        
                        connect.dispose();
 
                    }
                });

                JButton close = new JButton("Cancel");
                close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        if(name_field_entry.length() < 1 || name_field_entry.length() > MAX_SIZE_OF_NAME) {
                            name_field_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field1_entry) < 0 || Integer.parseInt(ip_field1_entry) > 255) {
                                ip_field1_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field1_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field2_entry) < 0 || Integer.parseInt(ip_field2_entry) > 255) {
                                ip_field2_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field2_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field3_entry) < 0 || Integer.parseInt(ip_field3_entry) > 255) {
                                ip_field3_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field3_entry = "";
                        }

                        try {
                            if(Integer.parseInt(ip_field4_entry) < 0 || Integer.parseInt(ip_field4_entry) > 255) {
                                ip_field4_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            ip_field4_entry = "";
                        }

                        try {
                            if(Integer.parseInt(port_field_entry) < 0 || Integer.parseInt(port_field_entry) > 65535) {
                                port_field_entry = "";
                            }
                        }
                        catch(NumberFormatException ex) {
                            port_field_entry = "";
                        }

                        settings.setNameFieldEntry(name_field_entry);
                        settings.setIpField1Entry(ip_field1_entry);
                        settings.setIpField2Entry(ip_field2_entry);
                        settings.setIpField3Entry(ip_field3_entry);
                        settings.setIpField4Entry(ip_field4_entry);
                        settings.setPortEntry(port_field_entry);
                        saveSettings();

                        textField.requestFocus();
                        menu_connect.setEnabled(true);
                        connect.dispose();

                    }
                });

                JPanel panel4 = new JPanel();
                JPanel panel5 = new JPanel();
                JPanel panel6 = new JPanel();
                JPanel panel7 = new JPanel();
                
                panel4.setBackground(Color.LIGHT_GRAY);
                panel5.setBackground(Color.LIGHT_GRAY);
                panel6.setBackground(Color.LIGHT_GRAY);
                panel7.setBackground(Color.LIGHT_GRAY);
                panel4.add(new JLabel("        "));
                panel4.add(new JLabel("Name"));
                panel4.add(name_field);
                panel4.add(new JLabel("        "));
                panel5.add(new JLabel("IP Address"));
                panel5.add(ip_field1);
                panel5.add(new JLabel("."));
                panel5.add(ip_field2);
                panel5.add(new JLabel("."));
                panel5.add(ip_field3);
                panel5.add(new JLabel("."));
                panel5.add(ip_field4);
                panel6.add(new JLabel("           "));
                panel6.add(new JLabel("Port"));
                panel6.add(port_field);
                panel6.add(new JLabel("                          "));
                panel7.add(new JLabel(""));
                panel7.add(new JLabel("Password"));
                panel7.add(password);
                panel7.add(new JLabel("        "));
                connect.add(panel4);
                connect.add(panel5);
                connect.add(panel6);
                connect.add(panel7);
                connect.add(connectButton);
                connect.add(close);
                connect.setVisible(true);

            }
        });

        menu_disconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });

        

        addWindowListener( new WindowAdapter() {
            public void windowOpened( WindowEvent e ){
                textField.requestFocus();
            }
        });

        menu_about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              int about_window_width = 298;
              int about_window_height = 226;

                menu_about.setEnabled(false);
                about = new JFrame("About");
                about.setSize(about_window_width, about_window_height);
                about.setLocation((int)(getLocation().getX() + getSize().getWidth() / 2) - (about_window_width / 2), (int)(getLocation().getY() + getSize().getHeight() / 2) - (about_window_height / 2));
                about.getContentPane().setBackground(background_color);
                about.setResizable(false);

                about.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        menu_about.setEnabled(true);
                        textField.requestFocus();
                        about.dispose();

                    }
                });

                about.setLayout(new FlowLayout());
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(8, 1));
                panel.add(new JLabel(" "));
                panel.add(new JLabel("            Chat Client, beta version."));
                panel.add(new JLabel());
                panel.add(new JLabel("  Made by Chris Kalonakis using java  "));
                panel.add(new JLabel("  on NetBeans IDE 6.8"));
                panel.add(new JLabel());
                panel.add(new JLabel("       Contact: hrkalona@inf.uth.gr"));
                panel.add(new JLabel(" "));

                JButton close = new JButton("Close");
                close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        menu_about.setEnabled(true);
                        textField.requestFocus();
                        about.dispose();

                    }
                });

                about.add(new JLabel("                                                                                                          "));
                about.add(new JLabel("  "));
                about.add(panel);
                about.add(new JLabel("  "));
                about.add(close);
                about.setVisible(true);
            }
        });

    }

    private void disconnect() {

        try {
            state = StateOfClient.DISCONNECTED;
            out.close();
            in.close();
            in = null;
            out = null;
            connectionWithServer.close();
            menu_disconnect.setEnabled(false);
            menu_connect.setEnabled(true);
            calendar = new GregorianCalendar();
            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " +  "Disconnected from the Server.\n");
            textArea1.setCaretPosition(textArea1.getText().length());
            textArea2.setText("");
            setTitle("Chat Client BETA");

            if(enabled_sounds[1]) {
                InputStream sound_stream_in = null;
                try {
                    sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[1] + ".wav");
                }
                catch(FileNotFoundException ex) {
                    textField.requestFocus();
                    return;
                }

                AudioStream as = null;
                try {
                    as = new AudioStream(sound_stream_in);
                }
                catch(IOException ex) {
                    textField.requestFocus();
                    return;
                }

                AudioPlayer.player.start(as);
            }
        }
        catch(Exception ex) {}
        textField.requestFocus();
        
    }

    public void run() {

        while(running) {

            checkIfServerDisconnected();
            waitForConnection();
            refreshAfterConnection();
            waitForMessages();

        }

    }

  
    private void checkIfServerDisconnected() {

        if(state == StateOfClient.CONNECTED) {
            try {
                in.readLine();
            }
            catch(IOException ex) {
                disconnect();
            }
        }

    }

    private void waitForConnection(){

        while(state == StateOfClient.DISCONNECTED) {
           System.out.println("");//BUG
        }

    }


    private void refreshAfterConnection() {
      int count;

        try {
            if(state == StateOfClient.CONNECTED) {
                calendar = new GregorianCalendar();
                textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " +  "Connected to the Server.\n");
                textArea1.setCaretPosition(textArea1.getText().length());
                count = Integer.parseInt(in.readLine());

                for(i = 0; i < count; i++)  {
                    textArea2.setText(textArea2.getText() + in.readLine() + "\n");
                }
            }
        }
        catch(NullPointerException ex) {}
        catch(SocketException ex) {}
        catch(IOException ex) {}


    }
    
    private void waitForMessages() {
      String Temp;
      int count;
        
        try {
            while(state == StateOfClient.CONNECTED && (SocketEntryMessage = in.readLine()) != null) {
                    if(SocketEntryMessage.equals(connected_hand_shake)) {
                        textArea2.setText("");
                        SocketEntryName = in.readLine();
                        calendar = new GregorianCalendar();
                        textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + SocketEntryName + " has connected.\n");
                        textArea1.setCaretPosition(textArea1.getText().length());

                        count = Integer.parseInt(in.readLine());
                        for(i = 0; i < count; i++)  {
                            textArea2.setText(textArea2.getText() + in.readLine() + "\n");
                        }

                        if(enabled_sounds[2]) {
                            InputStream sound_stream_in = null;
                            try {
                                sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[2] + ".wav");
                            }
                            catch(FileNotFoundException ex) {}

                            AudioStream as = null;
                            try {
                               as = new AudioStream(sound_stream_in);
                            }
                            catch(IOException ex) {}

                            AudioPlayer.player.start(as);
                        }
                    }
                    else {
                        if(SocketEntryMessage.equals(disconnected_hand_shake)) {
                            textArea2.setText("");
                            SocketEntryName = in.readLine();
                            calendar = new GregorianCalendar();           
                            textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + SocketEntryName + " has disconnected.\n");
                            textArea1.setCaretPosition(textArea1.getText().length());
                            count = Integer.parseInt(in.readLine());
                            for(i = 0; i < count; i++)  {
                                textArea2.setText(textArea2.getText() + in.readLine() + "\n");
                            }

                            if(enabled_sounds[3]) {
                                InputStream sound_stream_in = null;
                                try {
                                    sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[3] + ".wav");
                                }
                                catch(FileNotFoundException ex) {}

                                AudioStream as = null;
                                try {
                                   as = new AudioStream(sound_stream_in);
                                }
                                catch(IOException ex) {}

                                AudioPlayer.player.start(as);
                            }

                        }
                        else {
                            if(SocketEntryMessage.equals(whois_hand_shake)) {
                                 SocketEntryMessage = in.readLine();
                                 calendar = new GregorianCalendar();
                                 textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + "<Whois> " + SocketEntryMessage + "\n");
                                 textArea1.setCaretPosition(textArea1.getText().length());
                            }
                            else {
                                SocketEntryName = in.readLine();

                                if(SocketEntryName.length() > 10) {
                                    Temp = "";
                                    while(SocketEntryMessage.length() > 40) {
                                        Temp += SocketEntryMessage.substring(0, 40) + "\n";
                                        SocketEntryMessage = SocketEntryMessage.substring(40, SocketEntryMessage.length());
                                    }
                                    SocketEntryMessage = Temp + SocketEntryMessage;
                                }
                                else {
                                    Temp = "";
                                    while(SocketEntryMessage.length() > 44) {
                                        Temp += SocketEntryMessage.substring(0, 44) + "\n";
                                        SocketEntryMessage = SocketEntryMessage.substring(44, SocketEntryMessage.length());
                                    }
                                    SocketEntryMessage = Temp + SocketEntryMessage;
                                }

                                calendar = new GregorianCalendar();
                                textArea1.setText(textArea1.getText() + "<" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)) + "> " + SocketEntryName + " : " + SocketEntryMessage + "\n");
                                textArea1.setCaretPosition(textArea1.getText().length());

                                if(!SocketEntryName.equals(name_field_entry)) {
                                    if(enabled_sounds[4]) {
                                        InputStream sound_stream_in = null;
                                        try {
                                            sound_stream_in = new FileInputStream("./Sounds/" + selected_sounds[4] + ".wav");
                                        }
                                        catch(FileNotFoundException ex) {}

                                        AudioStream as = null;
                                        try {
                                            as = new AudioStream(sound_stream_in);
                                        }
                                        catch(IOException ex) {}

                                        AudioPlayer.player.start(as);
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
            catch(Exception ex) {}
        
        
    }

    private void saveSettings() {

        ObjectOutputStream file = null;

        try {
            file = new ObjectOutputStream(new FileOutputStream("settings.dat"));
            file.writeObject(settings);
            file.flush();
        }
        catch(IOException ex) {}

        try {
            file.close();
        }
        catch(Exception ex) {}

    }

    private void loadSettings() {

        ObjectInputStream file = null;
        try {
           file = new ObjectInputStream(new FileInputStream("settings.dat"));
           settings = (Settings) file.readObject();
        }
        catch(IOException ex) {
            settings = new Settings();
            saveSettings();
        }
        catch(ClassNotFoundException ex) {
            settings = new Settings();
            saveSettings();
        }

        try {
            file.close();
        }
        catch(Exception ex) {}

    }

    private void lastUsedListInsertion() {

        if(last_used.size() == NUMBER_OF_LAST_USED) {
            last_used.remove(0);
            last_used.add(new String(chat_entry));
            last_used_index = last_used.size() - 1;
        }
        else {
            last_used.add(new String(chat_entry));
            last_used_index = last_used.size() - 1;
        }
        
    }

    public static void main(String[] args) throws IOException {

        ChatClient client = new ChatClient();
        client.setVisible(true);
        client.run();

    }
    
}


