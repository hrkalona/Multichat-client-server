import java.awt.Color;
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Settings implements Serializable {
  private String name_field_entry;
  private String ip_field1_entry;
  private String ip_field2_entry;
  private String ip_field3_entry;
  private String ip_field4_entry;
  private String port_field_entry;
  private Color background_color;
  private Color font_color;
  private Boolean[] enabled_sounds;
  private int[] selected_sounds_index;
  private String[] selected_sounds;


  public Settings() {

      this.name_field_entry = "";
      this.ip_field1_entry = "";
      this.ip_field2_entry = "";
      this.ip_field3_entry = "";
      this.ip_field4_entry = "";
      this.port_field_entry = "";
      this.background_color = Color.DARK_GRAY;
      this.font_color = Color.BLACK;
      this.enabled_sounds = new Boolean[ChatClient.NUM_OF_SOUNDS];
      this.selected_sounds_index = new int[ChatClient.NUM_OF_SOUNDS];
      this.selected_sounds = new String[ChatClient.NUM_OF_SOUNDS];

      for(int i = 0; i < ChatClient.NUM_OF_SOUNDS; i++) {
            selected_sounds_index[i] = 0;
            enabled_sounds[i] = true;
      }

      selected_sounds[0] = "connect1";
      selected_sounds[1] = "disconnect1";
      selected_sounds[2] = "userConnect1";
      selected_sounds[3] = "userDisconnect1";
      selected_sounds[4] = "newMessage1";
      selected_sounds[5] = "warning1";
      
  }

  public Color getBackgroundColor() {

      return this.background_color;

  }

  public void setBackgroundColor(Color obj) {

      this.background_color = obj;

  }

  public Color getFontColor() {

      return this.font_color;

  }

  public void setFontColor(Color obj) {

      this.font_color = obj;

  }

  public String getNameFieldEntry() {

      return this.name_field_entry;

  }

  public void setNameFieldEntry(String obj) {

      this.name_field_entry = obj;

  }

  public String getIpField1Entry() {

      return this.ip_field1_entry;

  }

  public void setIpField1Entry(String obj) {

      this.ip_field1_entry = obj;

  }

  public String getIpField2Entry() {

      return this.ip_field2_entry;

  }

  public void setIpField2Entry(String obj) {

      this.ip_field2_entry = obj;

  }

  public String getIpField3Entry() {

      return this.ip_field3_entry;

  }

  public void setIpField3Entry(String obj) {

      this.ip_field3_entry = obj;

  }

  public String getIpField4Entry() {

      return this.ip_field4_entry;

  }
  
  public void setIpField4Entry(String obj) {
      
      this.ip_field4_entry = obj;
      
  }

  public String getPortEntry() {

      return this.port_field_entry;

  }

  public void setPortEntry(String obj) {

      this.port_field_entry = obj;

  }

  public Boolean[] getEnabledSounds() {

      return this.enabled_sounds;

  }

  public void setEnabledSounds(Boolean[] obj) {

      this.enabled_sounds = obj;

  }

  public int[] getSelectedSoundsIndex() {

      return this.selected_sounds_index;

  }

  public void setSelectedSoundsIndex(int[] obj) {

      this.selected_sounds_index = obj;

  }

  public String[] getSelectedSounds() {

      return this.selected_sounds;

  }

  public void setSelectedSounds(String[] obj) {

      this.selected_sounds = obj;
      
  }

}
