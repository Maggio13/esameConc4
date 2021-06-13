


class CS_Server {
    public String addr;
    String nome;
    String data;
    CS_Server(){
    }

    public void start() {
        System.out.println("Starting server");

    }
}

class CS_Client {
    String nome;
    String data;
    CS_Client(String addr){
        data=addr;
    }

    public void connect() {
    }

    public void sendInfo(Object addr) {
    }
}