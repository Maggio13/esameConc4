import java.io.*;
import java.net.Socket;

class CS_Client {
    public int error = 0;


    String remoteAddress;
    int remotePort;

    Socket c;
    private BufferedReader in;
    private PrintWriter out;

    CS_Client(String addr, int port){
        remoteAddress = addr;
        remotePort    = port;
    }

    /***********METODI DI CONNESSIONE****************/
    public void connect() throws IOException{
        System.out.println("CLIENT : connecting to " + remoteAddress+":"+remotePort);

        try {
            c = new Socket(remoteAddress, remotePort);
            in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(c.getOutputStream())), true);
        } catch (IOException e) {
            System.out.println("CLIENT : can't connect to " + remoteAddress+":"+remotePort);
            throw e;
        }
        System.out.println("CLIENT : Success!");
    }
    public void close() {
        System.out.println("CLIENT: closing");

        out.println("disconnect");
        try {
            c.close();
        } catch (IOException e) { }
    }


    /***********METODI VERSO REGISTRO****************/
    public void sendInfo(String addr, int port) {
        out.println("connetti " + addr+":"+port);
        System.out.println("> connetti " + addr+":"+port);
    }
    public void creaDato(String nome) {
        out.println("creaDato " + nome);
        System.out.println("> creaDato " + nome);
    }
    public String trovaDato(String nome) {
        out.println("trovaDato " + nome);
        System.out.println("> trovaDato " + nome);

        String txt = "error";
        try {
            txt = in.readLine();
            System.out.println("< " + txt);

        } catch (IOException e) {
            System.out.println("CLIENT: network error");
        }
        return txt;
    }


    /***********METODI VERSO CS**********************/
    public String copiaDato(String nome) throws IOException{
        out.println("copiaDato " + nome);
        System.out.println("> copiaDato " + nome);

        String txt = in.readLine();
        System.out.println("< " + txt);
        return txt;

    }
}