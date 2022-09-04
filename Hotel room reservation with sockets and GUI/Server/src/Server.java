//Εισαγωγή των βιβλιοθηκών που χρησιμοποιήθηκαν
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) {

        double kostos = 0;
        int id_kratisis = 0;
        //Δημιουργία ενός Hashmap για να αποθηκεύονται τα στοιχεία των κρατήσεων
        //καθώς και το μοναδικό id της κάθε κράτησης 
        HashMap<Integer, Kratisi> book_list = new HashMap<Integer, Kratisi>();

        try {
            //Δημιουργία socket του server προκειμένου να επιτευχθεί η επικοινωνία
            //με τον client
            ServerSocket server = new ServerSocket(5555, 50);

            while (true) {

                //Αποδοχή του socket προκειμένου να ξενκινήσει η επικοινωνία 
                //μεταξύ client και server
                Socket sock = server.accept();

                //Δημιουργία ενός ObjectInputStream και ObjectOutputStream για την αποστολή 
                //μηνυμάτων προς τον client και αποδοχής μηνυμάτων από τον client αντίστοιχα
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());

                //Σε αυτό το σημείο γίνεται η αποδοχή του πρώτου μηνύματος απο τον 
                //client το οποίο είναι το START
                String s = ois.readUTF();
                System.out.println(s);
                //Και στην συνέχεια ο server απαντά στον client με WATING
                oos.writeUTF("WAITING");
                oos.flush();

                //Σε αυτό το σημείο διαβάζουμε την επιλογή του χρήστη δηλαδή
                //αν ο client θέλει να κάνει INSERT,SEARCH ή DELETE των
                //στοιχείων κάποιας κράτησης
                s = ois.readUTF();

                //Δημιουργία αντικειμένου τύπου κράτησησς
                Kratisi k;

                if (s.equals("INSERT")) { //Αν ο client επιθυμεί να κάνει εισαγωγή στοιχείων μιας κράτησης 
                    System.out.println(s);//τότε τυπώνεται το μήνυμα INSERT στον server
                    k = (Kratisi) ois.readObject();//και στην συνέχεια τα στοιχεία της κράτησης εκχωρούνται
                    //στο αντικείμενο k τύπου Kratisis

                    //Στην μεταβλητή diff αποθηκεύεται το χρονικό διάστημα,σε ημέρες, μιας κράτησης
                    int diff = (int) ((k.getDate_anaxwrisis().getTime() - k.getDate_afiksis().getTime()) / (1000 * 60 * 60 * 24));

                    Calendar cal = Calendar.getInstance();//Προκειμένου να γίνει ο έλεγχος του μήνα μιας κράτησης 
                    cal.setTime(k.getDate_afiksis()); //μετατρέπουμε την ημερομηνία άφιξης από date σε calendar
                    int month = cal.get(Calendar.MONTH);//και αποθηκεύουμε τον μήνα στην μεταβλητή month
                    System.out.println(k.getDate_afiksis());
                    System.out.println(month);

                    //Μετά την μετατροπή απο date σε calendar ο μήνας της κράτησης 
                    //είναι -1 από αυτόν που ήταν πριν (πχ αν είχαμε βάλει στο date_afiksis
                    //τον μήνα 9-δηλαδή Σεπτέμβρη- το calendar θα μας έδειχνε τον μήνα 8-Αύγουστο-)
                    //και για αυτό τον λόγο στον έλεγχο του μήνα προσθέτουμε +1 στην μεταβλήτή month
                    if (((month + 1) > 5) && ((month + 1) < 10)) {
                        //Έπειτα γίνεται έλεγχος για τον τύπου του δωματίου
                        //και για το αν ο χρήστης έχει επιλέξει πρωινό ή όχι 
                        //και υπολογίζεται το κόστος της κράτησης
                        if (k.getType_room().equals("Μονόκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (80 + 8);
                            } else {
                                kostos = diff * 80;
                            }
                        } else if (k.getType_room().equals("Δίκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (120 + 8);
                            } else {
                                kostos = diff * 120;
                            }
                        } else if (k.getType_room().equals("Τρίκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (150 + 8);
                            } else {
                                kostos = diff * 150;
                            }
                        }
                    } else {
                        if (k.getType_room().equals("Μονόκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (40 + 8);
                            } else {
                                kostos = diff * 40;
                            }
                        } else if (k.getType_room().equals("Δίκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (70 + 8);
                            } else {
                                kostos = diff * 70;
                            }
                        } else if (k.getType_room().equals("Τρίκλινο")) {
                            if (k.isPrwino()) {
                                kostos = diff * (85 + 8);
                            } else {
                                kostos = diff * 85;
                            }
                        }
                    }

                    k.setKostos(kostos);//Στην συνέχεια εισάγουμε το κόστος στο αντικείμενο k
                    book_list.put(id_kratisis, k);//και τοποθετούμε το αντικείμενο στην λίστα μας 
                    //δίνοτάς του ένα id

                    oos.writeDouble(k.getKostos());//Στέλνουμε το κόστος στον πελάτη
                    oos.flush();

                    oos.writeInt(id_kratisis);//Στέλνουμε το id στον πελάτη
                    oos.flush();

                    id_kratisis += 1;//Αυξάνουμε το id κατα ένα για την επόμενη κράτηση

                } else if (s.equals("SEARCH")) {

                    //Αν ο client θέλει να κάνει αναζήτηση τότε οι επιλογές είναι
                    //2 οι οποίες είναι ανζήτηση με βάση το όνομα,το επώνυμο και
                    //την ημερομηνία άφιξης ή αναζήτηση με βάση το όνομα και το επίθετο
                    System.out.println(s);

                    s = ois.readUTF();

                    if (s.equals("SEARCH WITH FIRST NAME, LAST NAME AND DATE")) {

                        System.out.println(s);

                        //Παίρνουμε το όνομα,το επίθετο και την ημερομηνία που μας στέλνει ο client
                        String first_name_for_search = ois.readUTF();
                        String last_name_for_search = ois.readUTF();
                        String search_date = ois.readUTF();

                        //Το SimpleDateFormat χρησιμοποιείται για την μετατροπή ενός String σε Date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date_afiksis = null;
                        try {
                            //Μετατρέπουμε την ημερομηνία απο String σε Date
                            date_afiksis = sdf.parse(search_date);

                        } catch (ParseException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //Δημιουργία Iterators στην λίστα των κρατήσεων δηλαδή στην book_list
                        Iterator<Entry<Integer, Kratisi>> iter = book_list.entrySet().iterator();

                        //Δημιουργία ενός ArrayList όπου θα αποθηκεύονται τα αποτελέσματα της αναζήτησης
                        ArrayList<Kratisi> lista_kratisewn = new ArrayList();

                        while (iter.hasNext()) {
                            Kratisi kr = iter.next().getValue();
                            //Έπειτα γίνεται έλγχος για το αν τα στοιχεία που έδωσε ο χρήστης υπαρχουν στις κρατήσεις
                            // και αν υπάρχουν τότε εκχωρούνται στην λίστα μας
                            if ((kr.getFirst_name().equals(first_name_for_search)) && (kr.getLast_name().equals(last_name_for_search)) && (kr.getDate_afiksis().compareTo(date_afiksis) == 0)) {
                                lista_kratisewn.add(kr);
                            }
                        }

                        //Στέλνουμε την λίστα των αποτελεσμάτων στον client
                        oos.writeObject(lista_kratisewn);
                        oos.flush();

                    } else if (s.equals("SEARCH WITH FIRST NAME AND LAST NAME")) {

                        //Η διαδικασία είναι παρόμοια με την παραπάνω και για αυτό δεν περιγράφεται
                        System.out.println(s);

                        String first_name_for_search = ois.readUTF();
                        String last_name_for_search = ois.readUTF();

                        Iterator<Entry<Integer, Kratisi>> iter = book_list.entrySet().iterator();

                        ArrayList<Kratisi> lista_kratisewn = new ArrayList();

                        while (iter.hasNext()) {
                            Kratisi kr = iter.next().getValue();
                            if (kr.getFirst_name().equals(first_name_for_search) && kr.getLast_name().equals(last_name_for_search)) {
                                lista_kratisewn.add(kr);
                            }
                        }
                        System.out.println(lista_kratisewn.getClass());
                        oos.writeObject(lista_kratisewn);
                        oos.flush();
                    }
                    //Λειτουργία διαγραφής
                } else if (s.equals("DELETE")) {

                    //Διαβάζουμε το id που δίνει ο χρήστης προς διαγραφή
                    //και το παιρναμε σε μία μεταβκλητή int 
                    int id_delet = ois.readInt();

                    //Ελέγχουμε αν το id υπάρχει στην λίστα 
                    if (book_list.containsKey(id_delet)) {
                        //Διαγραφή όλης της εγγραφής απο την λίστα
                        book_list.remove(id_delet);
                        //Μήνυμα επιβεβαίωσης, ότι η διαγραφή έγινε επιτυχώς
                        oos.writeUTF("DELETE OK");
                        oos.flush();
                    } else {
                        //Σε διαφορετική περίπτωση στέλνουμε στον χρήστη
                        //μήνυμα αποτυχίας 
                        oos.writeUTF("DELETE FAIL ");
                        oos.flush();
                    }
                }
                if (s.equals("END")) {
                    System.out.println(s);
                }
                //Κλείσιμο τον ροών και του socket
                oos.close();
                ois.close();
                sock.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
