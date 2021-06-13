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
    static int commandDelay = 1500;
    static int maxRetries   = 10;


    public static void main(String[] args){
        try {
            registro.connect();
        }catch (IOException e){
            return;
        }

        server.start();
        registro.sendInfo(server.addr, server.port);

        //il CS cerca di aprire il file con nome = alla propria porta (es: funz_cs9001.txt)
        readCommandFile("funz_cs"+server.port+".txt");


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
            System.err.println("can't find file "+ fileName);
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
        String[] words = str.split(" ");

        if (words.length == 2) {
            System.out.println();
            System.out.println("input: "+ words[0] + ", " + words[1]);

            if (words[0].equals("create")) {
                creaDato(words[1], "test_"+words[1]+server.port);

            } else if (words[0].equals("download")) {
                downloadDato(words[1]);
            }
        }

    }


    /*******************METODI REGISTRO******************************/
    private static void creaDato(String nome, String dataContent) {
        dati.add(new CS_Data(nome, dataContent));
        registro.creaDato(nome);
    }
    private static void downloadDato(String nome) {
        for (int i = 0; i< maxRetries; i++) {
            String location = registro.trovaDato(nome);
            if (location.equals("error")) return;

            String[] words = location.split(":");
            String addr = words[0];
            int    port = Integer.parseInt(words[1]);

            CS_Client server = new CS_Client(addr, port);
            String dataContent = "";
            try {
                server.connect();
                dataContent = server.copiaDato(nome);
            } catch (IOException e) {
                System.out.println("CLIENT: could not find server " + location );
                continue;
            }

            creaDato(nome, dataContent);
            break;
        }
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