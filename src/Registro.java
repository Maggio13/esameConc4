import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;



class RegistryData{
    String nome;
    String addr;
    RegistryData(String n, String a){
        nome = n;
        addr = a;
    }
    public String toString(){
        return nome+" "+addr;
    }

}

public class Registro extends Thread {
    static ArrayList<RegistryData> registro = new ArrayList<RegistryData>();


    private Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    private static String remoteAddr;

    private static boolean connected;


    public Registro(Socket s) throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        connected = true;
        start();
    }

    public void run() {
        System.out.println("Server accepts connection");
        try {
            while (connected) {
                String str = in.readLine();
                System.out.println("Received: " + str);
                parseComando(str);
                System.out.println(Arrays.asList(Registro.registro));
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("Client disconnesso");
        } finally {
            disconnettiCS();
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
        System.out.println("Connection Ended");
    }

    public static void parseComando(String riga) {
        String[] linea;
        if (riga != null && riga.length() > 2) {

            linea = riga.split(" ");

            if (linea.length >= 2) {
                System.out.println(linea[0] + ", " + linea[1]);

                if (linea[0].equals("connetti")) {
                    connettiCS(linea[1]);
                }else if (linea[0].equals("trovaDato")) {
                    trovaDato(linea[1]);
                } else if (linea[0].equals("memorizzaDato")) {
                    memorizzaDato(linea[1]);
                } else if (linea[0].equals("disconnetti")) {
                    disconnettiCS();
                }
            }
        }
    }



    /*******************METODI LOCALI********************************/
    public synchronized static void memorizzaDato(String nome) {
        System.out.println("REGISTRA DATO "+nome+" "+ remoteAddr);
        registro.add(new RegistryData( nome, remoteAddr));
    }

    private static void connettiCS(String addr) {
        remoteAddr = addr;
    }

    public synchronized static void disconnettiCS() {
        System.out.println("RIMUOVENDO DATI CS");
        connected = false;
        rimuoviCS(remoteAddr);
    }

    public synchronized static void rimuoviCS(String addr) {
        registro.removeIf(data -> data.addr.equals(addr));
    }

    public synchronized static void trovaDato(String nome) {
        for (RegistryData data : registro) {
            if (data.nome.equals(nome)) {
                System.out.println("TROVATO DATO " + data);
                out.println(data.addr);
            }
        }
        System.out.println("DATO NON TROVATO " );
        out.println("error");
    }
}
