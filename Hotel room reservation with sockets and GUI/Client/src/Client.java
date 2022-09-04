import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Client extends JFrame {

    //Η λίστα αυτή περιέχει τον τύπο των δωματίων και αφαρμόζεται σε ένα ComboBox
    String[] TypeRoom = {"Μονόκλινο", "Δίκλινο", "Τρίκλινο"};

    private JPanel panel_1 = new JPanel();
    private JPanel panel_2 = new JPanel();
    private JPanel panel_3 = new JPanel();
    private JPanel panel_4 = new JPanel();
    private JPanel panel_5 = new JPanel();
    private JPanel panel_6 = new JPanel();

    JButton kratisi = new JButton("Εισαγωγή κράτησης ");
    JButton pse_kratisi = new JButton("Εμφάνιση/αναζήτηση κράτσησης");
    JButton akirwsi_kratisis = new JButton("Ακύρωση κράτησης");
    JButton ins_elem_book = new JButton("Εισαγωγή των στοιχείων κράτησης");

    JButton search_date_name = new JButton("Αναζήτηση με ημερομηνία άφιξης και ονοματεπώνυμο πελάτη");
    JButton search_name = new JButton("Αναζήτηση με ονοματεπώνυμο του πελάτη");
    JButton search_button = new JButton("Αναζήτηση ");
    JButton delete_button = new JButton("Διαγραφή ");

    JLabel id_kratisis = new JLabel("Id κράτησης");
    JLabel fir_name = new JLabel("Όνομα: ");
    JLabel las_name = new JLabel("Επίθετο: ");
    JLabel phone_num = new JLabel("Τηλέφωνο: ");
    JLabel date_afiksis = new JLabel("Ημ/νία άφιξης: ");
    JLabel date_anaxwrisis = new JLabel("Ημ/νία αναχώρησης: ");
    JLabel type_room = new JLabel("Τύπος δωματίου: ");
    JLabel breakfast = new JLabel("Πρωινό γεύμα");
    JLabel first_name_for_search = new JLabel("Όνομα πελάτη");
    JLabel last_name_for_search = new JLabel("Επώνυμο πελάτη");
    JLabel date_for_search = new JLabel("Ημερομηνία κράτησης:");
    JLabel id_for_delete = new JLabel("Id πελάτη:");

    JTextField id_kratisi = new JTextField();
    JTextField fir_nam = new JTextField();
    JTextField las_nam = new JTextField();
    JTextField phon_num = new JTextField();
    JTextField date_afiksi = new JTextField();
    JTextField date_anaxwrisi = new JTextField();
    JComboBox type_roo = new JComboBox(TypeRoom);
    JCheckBox breakfas = new JCheckBox();
    JTextField first_name_for_searc = new JTextField();
    JTextField last_name_for_searc = new JTextField();
    JTextField date_for_searc = new JTextField();
    JTextField id_for_delet = new JTextField();

    //Δήλωση του socket,τηε ροής ObjectInputStream και της ροής ObjectOutputStream
    private Socket sock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    String s;

    public Client() {
        super(" Ξενοδοχείο ");

        try {
            //Αοίγουμε το socket για να επιτευχθεί η επικοινωνία
            sock = new Socket("localhost", 5555);

            oos = new ObjectOutputStream(sock.getOutputStream());

            ois = new ObjectInputStream(sock.getInputStream());

            //Αρχικά στέλνουμε στον server το μήνυμα START
            oos.writeUTF("START");
            oos.flush();

            //Και στην συνέχεια διαβάζουμε την απάντηση του Server η οποία έιναι WAITING
            s = ois.readUTF();
            System.out.println(s);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        setSize(500, 350);
        setVisible(true);
        Container pan = getContentPane();

        //Αν ο χρήστης κλείσει το frame τότε θα σταλεί κατάλληλο
        //μήνυμα στον server και θα διακοπεί η επικοινωνία
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    oos.writeUTF("END");
                    oos.flush();
                    oos.close();
                    ois.close();
                    sock.close();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //Σε αυτό το σημείο εμφανίζονται οι επιλογές
        //στον χρήστη για εισαγωγή ,αναζήτησ/εμφάνιση και διαγραφής 
        //κράτησης με τα κουμπιά kratisi, pse_kratisi και akirwsi_kratisis αντίστοιχα
        panel_1.add(kratisi);
        panel_1.add(pse_kratisi);
        panel_1.add(akirwsi_kratisis);

        pan.add(panel_1);
        setContentPane(pan);

        //Το κουμπι kratisi ενεργοποιείται όταν ο χρήστης επιλέξει την εισαγωγή
        //στοιχείων της κράτησης
        kratisi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    //Στέλνεται το μήνυμα INSERT στον server
                    //προκειμένου να ενεργοποιηθούν οι κατάλληλες λειτουργίες
                    oos.writeUTF("INSERT");
                    oos.flush();

                    //Δημιουργούμε ένα επιπλέον Frame στο οποίο θα φιλοξενούνται
                    //τα JTextField για την εισαγβγή των στοιχείων κράτησης
                    JFrame booking = new JFrame("Κράτηση δωματίου ");
                    booking.setSize(500, 350);
                    booking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    booking.setVisible(true);

                    GridLayout glo_2 = new GridLayout(8, 7);

                    //Τοποθετούμε τα JLabel και JTextField στο panel καθώς και 
                    //το κουμπί που θα πραγματοποιεί την αναζήτηση 
                    panel_2.add(fir_name);
                    panel_2.add(fir_nam);

                    panel_2.add(las_name);
                    panel_2.add(las_nam);

                    panel_2.add(phone_num);
                    panel_2.add(phon_num);

                    panel_2.add(date_afiksis);
                    panel_2.add(date_afiksi);

                    panel_2.add(date_anaxwrisis);
                    panel_2.add(date_anaxwrisi);

                    panel_2.add(type_room);
                    panel_2.add(type_roo);

                    panel_2.add(breakfast);
                    panel_2.add(breakfas);
                    breakfas.setSelected(false);

                    panel_2.add(ins_elem_book);

                    panel_2.setLayout(glo_2);

                    //Προσθέτουμε το panel στο Jframe 
                    booking.getContentPane().add(panel_2);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Το κουμπί  ins_elem_book ενεργοποιείται όταν ο χρήστης
                //επιθυμεί να εισαχθούν τα στοιχεία της κράτησης στην λίστα των 
                //κρατήσεων
                ins_elem_book.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        try {

                            JPanel panel_2_1 = new JPanel();
                            GridLayout glo_2_1 = new GridLayout(2, 2);

                            //Αποθηκεύουμε τα περιεχόμενα των JTextField σε μεταβλητές
                            String first_name = fir_nam.getText();
                            String last_name = las_nam.getText();
                            String phone_num = phon_num.getText();
                            String date_afiksis = date_afiksi.getText();
                            String date_anaxwrisis = date_anaxwrisi.getText();
                            String type_room = type_roo.getSelectedItem().toString();
                            boolean prwino = breakfas.isSelected();

                            ////Το SimpleDateFormat χρησιμοποιείται για την μετατροπή ενός String σε Date
                            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            //Μετατρέπουμε τα String date_afiksis και date_anaxwrisis σε τύπο Date
                            Date date_afiksi = dt.parse(date_afiksis);
                            Date date_anaxwrisi = dt.parse(date_anaxwrisis);

                            //Δημιουργούμε το αντικείμενο Kratisi
                            Kratisi k = new Kratisi(first_name, last_name, phone_num, date_afiksi, date_anaxwrisi, type_room, prwino);

                            //Στέλνουμε το αντικείμενο Kratisi στον server
                            oos.writeObject(k);
                            oos.flush();

                            //Τυπώνουμε το κόστος και το μοναδικό id της κράτησης στο panel
                            panel_2_1.add(new JLabel("Το κόστος είναι: " + ois.readDouble() + "€"));
                            panel_2_1.add(new JLabel("Το id  είναι:    " + ois.readInt()));
                            panel_2_1.setLayout(glo_2_1);

                            panel_2.add(panel_2_1);
                            panel_2.revalidate();
                            panel_2.repaint();

                        } catch (ParseException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });
            }
        });

        //Το κουμπί pse_kratisi ενεργοποιείται όταν ο χρήστης επιλέξει να αναζητήσει
        //ή να εμφανίσει τα στοιχεία μιας κρατησης
        pse_kratisi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    //Στέλνεται το μήνυμα SEARCH στον server
                    //προκειμένου να ενεργοποιηθούν οι κατάλληλες λειτουργίες
                    oos.writeUTF("SEARCH");
                    oos.flush();

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Δημιουργία νέου JFrame 
                JFrame search = new JFrame("Εμφάνιση/Αναζήτηση στοιχείων ");
                search.setSize(500, 350);
                search.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                search.setVisible(true);
                //Εμφάνιση επιλεγών για αναζήτησς με όνομα,επίθετο και ημερομηνία 
                //άφιξης ή αναζήτηση με όνομα και επίθετο με τα κουμπία search_date_name
                //και search_name αντίστοιχα
                panel_3.add(search_date_name);
                panel_3.add(search_name);
                search.getContentPane().add(panel_3);

                //Το κουμπί αυτό ενεργοποιείται όταν ο χρήστης επιλέξει αναζήτηση με 
                //όνομα,επίθετο και ημερομηνία άφιξης
                search_date_name.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        try {
                            oos.writeUTF("SEARCH WITH FIRST NAME, LAST NAME AND DATE");
                            oos.flush();

                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //Δημιουργία ένος μέου JFrame που θα φιλοξενεί τα στοιχεία
                        //της αναζήτησης και τα αποτελέσματα της αναζήτησης
                        JFrame search_1 = new JFrame("Αναζήτηση με ημερομηνία άφιξης και ονοματεπώνυμο πελάτη");
                        search_1.setSize(500, 350);
                        search_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        search_1.setVisible(true);

                        GridLayout glo_3 = new GridLayout(12, 2);

                        //Προσθέτουμε στο panel τα JLabel και JTextField
                        panel_4.add(first_name_for_search);
                        panel_4.add(first_name_for_searc);

                        panel_4.add(last_name_for_search);
                        panel_4.add(last_name_for_searc);

                        panel_4.add(date_for_search);
                        panel_4.add(date_for_searc);

                        panel_4.add(search_button);

                        panel_4.add(new JLabel(""));
                        panel_4.setLayout(glo_3);

                        //Προσθέτουμε το panel στο Frame
                        search_1.getContentPane().add(panel_4);

                        //Το κουμπί search_button ενεγοποιείται όταν ο χρήστης
                        //συμπληρώσει τα κατάλληλα JtextField της αναζήτησης και
                        //πατήσει το κουπί Αναζήτηση
                        search_button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                //Δημιουργούμε μία λιστα η οποία θα φιλοξενήσει 
                                //τα αποτελέσματα της αναζήτησης
                                ArrayList<Kratisi> lista_kratisewn = new ArrayList<>();

                                try {
                                    //Παίρνουμε τα στοιχεία των JTextFields και 
                                    //τα αποθηκεύουμε σε μεταβλητές
                                    String search_first_name = first_name_for_searc.getText();
                                    String search_last_name = last_name_for_searc.getText();
                                    String search_date = date_for_searc.getText();

                                    //Στέλνουμε τα στοιχεία της αναζήτησς στον server
                                    oos.writeUTF(search_first_name);
                                    oos.flush();
                                    oos.writeUTF(search_last_name);
                                    oos.flush();
                                    oos.writeUTF(search_date);
                                    oos.flush();

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                                    //Διαβάζουμε και εχωρούμε τα αποτελέσματα της
                                    //αναζήτησης στην λίστα μας
                                    lista_kratisewn = (ArrayList<Kratisi>) ois.readObject();

                                    //Τυπώνουμε την λίστα που περιέχει τα αποτελέσματα της
                                    //αναζήτησης στο panel
                                    for (int i = 0; i < lista_kratisewn.size(); i++) {
                                        panel_4.add(new JLabel("Όνομα: "));
                                        panel_4.add(new JLabel(lista_kratisewn.get(i).getFirst_name()));
                                        panel_4.add(new JLabel("Επίθετο: "));
                                        panel_4.add(new JLabel(lista_kratisewn.get(i).getLast_name()));
                                        panel_4.add(new JLabel("Τηλέφωνο: "));
                                        panel_4.add(new JLabel(lista_kratisewn.get(i).getPhone_num()));
                                        panel_4.add(new JLabel("Ημ/νία άφιξης: "));
                                        panel_4.add(new JLabel(sdf.format(lista_kratisewn.get(i).getDate_afiksis())));
                                        panel_4.add(new JLabel("Ημ/νία αναχώρισης: "));
                                        panel_4.add(new JLabel(sdf.format(lista_kratisewn.get(i).getDate_anaxwrisis())));
                                        panel_4.add(new JLabel("Τύπος δωματίου: "));
                                        panel_4.add(new JLabel(lista_kratisewn.get(i).getType_room()));
                                        panel_4.add(new JLabel("Πρωινό: "));
                                        panel_4.add(new JLabel(Boolean.toString(lista_kratisewn.get(i).isPrwino())));
                                        panel_4.add(new JLabel("Κόστος: "));
                                        panel_4.add(new JLabel(Double.toString(lista_kratisewn.get(i).getKostos())));

                                    }

                                    panel_4.revalidate();
                                    panel_4.repaint();

                                } catch (IOException ex) {
                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        });

                    }
                });

                //Το κουμπί αυτό ενεργοποιείται όταν ο χρήστης επιλέξει αναζήτηση με 
                //όνομα και επίθετο 
                //Οι λειτουργίες της αναζήτησης με βάση το όνομα και το 
                //επίθετο είναι παρόποιες με την αναζήτηση βάσει το όνομα,το επίθετο
                // και της ημερομηνίας άφιξης που
                //αναλύθηκαν παραπάνω και για αυτό δεν περιγράφονται 
                search_name.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        try {
                            oos.writeUTF("SEARCH WITH FIRST NAME AND LAST NAME");
                            oos.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        JFrame search_2 = new JFrame("Αναζήτηση με ονοματεπώνυμο ");
                        search_2.setSize(500, 350);
                        search_2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        search_2.setVisible(true);

                        GridLayout glo_3 = new GridLayout(11, 2);

                        panel_5.add(first_name_for_search);
                        panel_5.add(first_name_for_searc);

                        panel_5.add(last_name_for_search);
                        panel_5.add(last_name_for_searc);

                        panel_5.add(search_button);

                        panel_5.add(new JLabel(""));
                        panel_5.setLayout(glo_3);

                        search_2.getContentPane().add(panel_5);

                        search_button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                ArrayList<Kratisi> lista_kratisewn = new ArrayList<>();

                                try {
                                    String search_first_name = first_name_for_searc.getText();
                                    String search_last_name = last_name_for_searc.getText();

                                    oos.writeUTF(search_first_name);
                                    oos.flush();
                                    oos.writeUTF(search_last_name);
                                    oos.flush();

                                    lista_kratisewn = (ArrayList<Kratisi>) ois.readObject();

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                                    for (int i = 0; i < lista_kratisewn.size(); i++) {

                                        panel_5.add(new JLabel("Όνομα: "));
                                        panel_5.add(new JLabel(lista_kratisewn.get(i).getFirst_name()));
                                        panel_5.add(new JLabel("Επίθετο: "));
                                        panel_5.add(new JLabel(lista_kratisewn.get(i).getLast_name()));
                                        panel_5.add(new JLabel("Τηλέφωνο: "));
                                        panel_5.add(new JLabel(lista_kratisewn.get(i).getPhone_num()));
                                        panel_5.add(new JLabel("Ημ/νία άφιξης: "));
                                        panel_5.add(new JLabel((sdf.format(lista_kratisewn.get(i).getDate_afiksis()))));
                                        panel_5.add(new JLabel("Ημ/νία αναχώρισης: "));
                                        panel_5.add(new JLabel(sdf.format(lista_kratisewn.get(i).getDate_anaxwrisis())));
                                        panel_5.add(new JLabel("Τύπος δωματίου: "));
                                        panel_5.add(new JLabel(lista_kratisewn.get(i).getType_room()));
                                        panel_5.add(new JLabel("Πρωινό: "));
                                        panel_5.add(new JLabel(Boolean.toString(lista_kratisewn.get(i).isPrwino())));
                                        panel_5.add(new JLabel("Κόστος: "));
                                        panel_5.add(new JLabel(Double.toString(lista_kratisewn.get(i).getKostos())));

                                    }

                                    panel_5.revalidate();
                                    panel_5.repaint();

                                } catch (IOException ex) {
                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        });

                    }
                });

            }
        });

        //Το κουμπί akirwsi_kratisis ενεργοποιείται όταν πατήσει το κουμπί
        //Ακύρωση κράτησης
        akirwsi_kratisis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    //Στέλνουμε στον Server το μήνυμα DELETE
                    oos.writeUTF("DELETE");
                    oos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Δημιουργία ενός JFrame που θα φιλεξενήσει ένα JLabel
                //ένα JTextfild και το κουμπί της διαγραφής  
                JFrame search_2 = new JFrame("Διαγραφή κράτησης");
                search_2.setSize(500, 350);
                search_2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                search_2.setVisible(true);

                GridLayout glo_3 = new GridLayout(8, 8);

                //Προσθέτυμε στο panel το JLabel και JTextField και 
                //συγχρόνως το κουμπί της διαγραφής
                panel_5.add(id_for_delete);
                panel_5.add(id_for_delet);

                panel_5.add(delete_button);

                panel_5.setLayout(glo_3);

                //Παίρνουμε το id που έδωσε ο χρήστης στο JTextFiled id_for_delet
                //και το αποθηκεύουμε σε μία μεταβλητή
                search_2.getContentPane().add(panel_5);

                delete_button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        String id_delete = id_for_delet.getText();

                        int id_del = Integer.parseInt(id_delete);

                        String msg;
                        try {
                            //Στέλνουμε στον server το id τηε κράτησης που έδωσε ο 
                            //χρήστης προς διαγραφή
                            oos.writeInt(id_del);
                            oos.flush();

                            //Διαβάζουμε και τυπώνουμε το μήνυμα του εξυπηρετητή, το 
                            //οποίο θα ειναι έιτε DELETE OK είτε DELETE FAIL
                            msg = ois.readUTF();
                            System.out.println(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });

    }

    public static void main(String[] args) {

        Client client = new Client();

    }

}
