package fi.utu.tech.distributed.mesh;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class VanhaClient {
    public static void main(String[] args) throws Exception {
        int port = 2000;
        Socket s = new Socket("localhost", port);
        System.out.println("Connection made ...");

        InputStream iS = s.getInputStream();
        OutputStream oS = s.getOutputStream();
        ObjectOutputStream oOut = new ObjectOutputStream(oS);
        ObjectInputStream oIn = new ObjectInputStream(iS);


        Scanner scan = new Scanner(System.in);

        while(true) {
            String msg = scan.nextLine();
            oOut.writeObject(msg);
            //oOut.flush();
            //s.getOutputStream().flush();
            String ack = (String)oIn.readObject();
            System.out.println(ack);
        }

        //oOut.close();
        //oS.close();


    }
}
