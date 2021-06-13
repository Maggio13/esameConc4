import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

public class CS {

    static HashMap<String, String> dati = new HashMap<String, String>();
    static InetAddress addr;
    static CS_Proxy cs_proxy;

    static private int ownPort = 9090;
    static private final int registroPort = 8085;


    public static void main(String[] args) {



        while(true) {
            FileReader reader;
            BufferedReader buff;
            try {
                addr = InetAddress.getByName(null);
                cs_proxy = new CS_Proxy(addr, registroPort);
            } catch (Exception e) {
                System.err.println("Server non trovato");
                return;
            }
            System.out.println("Collegato al Server");
            connetti();


            try {
                reader = new FileReader("funz_cs1.txt");
                buff = new BufferedReader(reader);
            } catch (FileNotFoundException e) {
                System.err.println("file non trovato");
                return;
            }

            String str;
            try {
                while ((str = buff.readLine()) != null) {
                    parseComando(str);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
            }

            System.out.println(Arrays.asList(dati));

            disconnetti();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseComando(String riga) {
        String[] linea;
        linea = riga.split(" ");

        if (linea.length == 2) {
            System.out.println(linea[0] + ", " + linea[1]);

            if (linea[0].equals("create")) {
                creaDato(linea[1]);

            } else if (linea[0].equals("download")) {
                downloadDato(linea[1]);
            }
        }
    }


    /*******************METODI LOCALI********************************/
    public static void creaDato(String nome) {
        dati.put(nome, "cs1DatoX");
        notificaServer(nome);
    }

    public static void downloadDato(String nome) {
        trovaDato(nome);
        getDato(nome);
        creaDato(nome);
    }


    /*******************METODI REMOTI********************************/

    public static void trovaDato(String nome) {
        String datoTrovato = cs_proxy.trovaDato(nome);
        System.out.println("TROVATO DATO: "+datoTrovato);
    }

    public static void getDato(String nome) {
    }

    public static void notificaServer(String nome) {
        cs_proxy.notificaServer(nome);
    }

    public static void connetti(){
        cs_proxy.connetti(":"+ownPort);
    }

    public static void disconnetti(){
        cs_proxy.disconnetti();
    }


}
