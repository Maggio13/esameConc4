import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

class Register_Thread extends Thread {
    static ArrayList<RegisterData> registro = new ArrayList<RegisterData>();

    private Socket socket;
    private BufferedReader   in;
    private PrintWriter      out;

    private String remoteAddr;

    public Register_Thread(Socket s)  throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();

    }
    public void run() {
        System.out.println("REGISTER: accept connection");
        try {
            String incomingText = "";
            while (incomingText != null && !incomingText.equals("disconnect")) {
                incomingText = in.readLine();
                parseComando(incomingText);
                System.out.println(Arrays.asList(Register_Thread.registro));
                System.out.println();
             }

        } catch (IOException e) {
            System.out.println("REGISTER: client error");
        }

        disconnettiCS();
        try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        System.out.println(Arrays.asList(Register_Thread.registro));
        System.out.println("REGISTER: closing connection");
    }

    public void parseComando(String riga) {
        String[] linea;

        if (riga != null && riga.length() > 2) {
            linea = riga.split(" ");

            if (linea.length >= 2) {
                System.out.println("> " + linea[0] + ", " + linea[1]);

                switch (linea[0]){
                    case "connetti":
                        connettiCS(linea[1]);
                        break;
                    case  "creaDato":
                        creaDato(linea[1]);
                        break;
                    case  "trovaDato":
                        trovaDato(linea[1]);
                        break;
                }
            }
        }
    }



    /*******************METODI LOCALI********************************/
    private void connettiCS(String addr) {
        remoteAddr = addr;
    }
    public synchronized void rimuoviCS(String addr) {
        registro.removeIf(data -> data.addr.equals(addr));
    }
    public synchronized void disconnettiCS() {
        System.out.println("REGISTER: rimuovendo "+ remoteAddr);
        rimuoviCS(remoteAddr);
    }


    public synchronized void creaDato(String nome) {
        System.out.println("REGISTRA DATO "+nome+" "+ remoteAddr);
        registro.add(new RegisterData( nome, remoteAddr));
    }
    public synchronized void trovaDato(String nome) {
        for (RegisterData data : registro) {
            if (data.nome.equals(nome)) {
                System.out.println("TROVATO DATO " + data);
                out.println(data.addr);
                return;
            }
        }
        System.out.println("DATO NON TROVATO " );
        out.println("error");
    }
}