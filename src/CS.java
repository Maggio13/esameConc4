import java.util.ArrayList;


public class CS{
    static CS_Server server = new CS_Server();
    static CS_Client client = new CS_Client(Registro.INDIRIZZO);

    static ArrayList<CS_Data> dati = new ArrayList<CS_Data>();




    public static void main(String[] args){
        server.start();
        client.sendInfo(server.addr);


    }

    private static void startServer() {




    }


}

class CS_Data {
    String nome;
    String data;
    CS_Data(String n, String d){
        nome=n;
        data=d;
    }
}