package network;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        String port = "2001";
        System.out.println("Starting server at port " + port);
        // ...or at least somebody should be
        Mesh mesh = new Mesh(Integer.parseInt(port),null);
        mesh.start();
        String myName = Long.toString(mesh.getId());

        try {
            String address = "localhost";
            System.out.printf("Connecting to server at %s:%s\n", address, "2000");
            // ...or at least somebody should be
            mesh.connect(address, Integer.parseInt("2000"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(1000);

        String viesti = "Hello";
        Message msgObject = new Message(viesti, Message.Tyyppi.MSG);
        System.out.println("sending move(client) " + viesti);
        mesh.broadcast(msgObject);
    }
}
