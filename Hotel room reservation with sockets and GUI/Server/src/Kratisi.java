//Εισαγωγή των βιβλιοθηκών που χρησιμοποιήθηκαν
import java.io.Serializable;
import java.util.Date;

public class Kratisi implements Serializable {

    
    private String first_name;
    private String last_name;
    private String phone_num;
    private Date date_afiksis;
    private Date date_anaxwrisis;
    private String type_room;
    private boolean prwino;
    private Double kostos;

    //Δημιουργία constractor για την εισαγωγή των στοιχείων στις μεταβλητές που αφορούν την κράτηση
    public Kratisi(String first_name, String last_name, String phone_num, Date date_afiksis, Date date_anaxwrisis, String type_room, boolean prwino) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_num = phone_num;
        this.date_afiksis = date_afiksis;
        this.date_anaxwrisis = date_anaxwrisis;
        this.type_room = type_room;
        this.prwino = prwino;
        
    }

    //Δημιουργία Setters και Getters συναρτήσεων
    public void setKostos(Double kostos)
    {
        this.kostos=kostos;
    }

    public Double getKostos() {
        return kostos;
    }
    
    
    
    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public Date getDate_afiksis() {
        return date_afiksis;
    }

    public Date getDate_anaxwrisis() {
        return date_anaxwrisis;
    }

    public String getType_room() {
        return type_room;
    }

    public boolean isPrwino() {
        return prwino;
    }

}
