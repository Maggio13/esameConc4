import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;


public class CS{
    static InetAddress addr;

    static CS_Server server   = new CS_Server();
    static CS_Client registro = new CS_Client(Register.INDIRIZZO, Register.PORTA);

    static ArrayList<CS_Data> dati = new ArrayList<CS_Data>();

    /***********DEBUG VARIABLES*******************/
    static int commandDelay = 500;

    public static void main(String[] args){
        try {
            registro.connect();
        }catch (IOException e){
            return;
        }

        server.start();
        registro.sendInfo(server.addr, server.port);

        readCommandFile("funz_cs1.txt");


        System.out.println(Arrays.asList(dati));

        registro.close();
        server.close();

        System.out.println("programma terminato");
        System.exit(0);
    }

    private static void readCommandFile(String fileName) {
        FileReader reader;
        BufferedReader buff;

        System.out.println("Leggendo file istruzione: "+fileName);

        try {
            reader = new FileReader(fileName);
            buff = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            System.err.println("file non trovato");
            return;
        }

        String str;
        try {
            while ((str = buff.readLine()) != null) {
                parseCommands(str);
                try {
                    Thread.sleep(commandDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
        }
    }
    private static void parseCommands(String str) {
        String[] words;
        words = str.split(" ");

        if (words.length == 2) {
            System.out.println();
            System.out.println("> " + words[0] + ", " + words[1]);

            if (words[0].equals("create")) {
                creaDato(words[1]);

            } else if (words[0].equals("download")) {
                downloadDato(words[1]);
            }
        }

    }


    /*******************METODI REGISTRO******************************/
    private static void creaDato(String nome) {
        dati.add(new CS_Data(nome, "test"));
        registro.creaDato(nome);
    }
    private static void downloadDato(String nome) {
        registro.trovaDato(nome);
        //download dato;
        creaDato(nome);
    }
}

class CS_Data {
    String nome;
    String data;
    CS_Data(String n, String d){
        nome=n;
        data=d;
    }
    public String toString(){
        return nome+" "+data;
    }
}