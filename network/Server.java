package network;

public class Server {
    public static void main(String[] args) {
        String port = "2000";
        System.out.println("Starting server at port " + port);
        // ...or at least somebody should be
        Mesh mesh = new Mesh(Integer.parseInt(port),null);
        mesh.start();
        String myName = Long.toString(mesh.getId());
    }
}
