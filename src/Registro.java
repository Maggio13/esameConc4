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


    public Registro(Socket s) throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                System.out.println("Received: " + str);
                parseComando(str);
            }

        } catch (IOException e) {
            System.out.println("Client disconnesso");
        } finally {
            try {
                socket.close();
                System.out.println(Arrays.asList(Registro.registro));

            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }

    public static void parseComando(String riga) {
        String[] linea;
        linea = riga.split(" ");

        System.out.println(linea[0] + ", " + linea[1]);

        if (linea.length >= 2) {
            if (linea[0].equals("trovaDato")) {
                trovaDato(linea[1]);
            } else if (linea[0].equals("memorizzaDato")) {
                memorizzaDato(linea[1], linea[2]);
            } else if (linea[0].equals("cancellaDato")) {
                cancellaDato(linea[1]);
            }
        }
    }

    /*******************METODI LOCALI********************************/
    public static void memorizzaDato(String nome, String cs) {
        System.out.println("REGISTRA DATO "+nome+" "+ cs);
        registro.add(new RegistryData( nome, cs));
    }

    public static void cancellaDato(String addr) {
        for (RegistryData data : registro) {
            if (data.addr.equals(addr)) {
                registro.remove(data);
            }
        }
    }

    public static void trovaDato(String nome) {
        for (RegistryData data : registro) {
            if (data.nome.equals(nome)) {
                System.out.println("TROVATO DATO " + data);
                out.println(data);
            }
        }
        System.out.println("DATO NON TROVATO " );
        out.println("error");
    }
}
