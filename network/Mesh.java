package network;

/** 
 * @author ranzuh, genrr
 * 
 */

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import main.App2;

// Yhteyspyyntöjä odottava ja julkisen APIn tarjoava palvelinluokka/pääluokka
public class Mesh extends Thread {

    private Map<String, Handler> connections = new ConcurrentHashMap<>();
    private int PORT;
    private Set<Long> tokenit = Collections.synchronizedSet(new HashSet<>());
    private String name;
    private ServerSocket ss;
    private App2 app;
    private long id;

    @Override
    public long getId() {
        return id;
    }

    /**
     * Luo Mesh-palvelininstanssi
     * @param port Portti, jossa uusien vertaisten liittymispyyntöjä kuunnellaan
     */
    public Mesh(int port, App2 app) {
        this.PORT = port;
        this.app = app;
        Random random = new Random();
        id = random.nextLong();
        System.out.println("mesh with app ref: "+app);
    }

    /**
     *  Käynnistä uusien vertaisten kuuntelusäie
     */
    @Override
    public void run() {
        try {

            ss = new ServerSocket(PORT);
            name = "localhost" + ":" + ss.getLocalPort();
            
            System.out.println("server socket local port: "+ss.getLocalPort());
            System.out.println("host address: "+ss.getInetAddress().getHostAddress());
            
            while (!Thread.currentThread().isInterrupted()) {
                Socket cs = ss.accept();
                App2.statusConsole.appendText("Connection from " + cs.getInetAddress()  + " port " + cs.getPort()+"\n");

                String connectionIdentifier = cs.getInetAddress() + ":" + cs.getPort();
                Handler handler = new Handler(cs);
                connections.put(connectionIdentifier, handler);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lähetä hyötykuorma kaikille vastaanottajille
     * @param o Lähetettävä hyötykuorma
     */
    public void broadcast(Serializable o) {
        Message msg = (Message)o;
        addToken(msg.getToken());
        connections.forEach((k, v) -> v.send(o));
    }

    /**
     * Lähetä hyötykuorma valitulle vertaiselle
     * @param o Lähetettävä hyötykuorma
     * @param recipient Vastaanottavan vertaisen tunnus
     */
    public void send(Serializable o, String recipient) {
        Message msg = (Message)o;
        msg.setRecipient(recipient);
        broadcast(msg);
    }

    /**
     * Sulje mesh-palvelin ja kaikki sen yhteydet
     */
    public void close() {
        try {
            ss.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("closing mesh");
        connections.forEach((k, v) -> v.closeConnection());
        Thread.currentThread().interrupt();
    }

    /**
     * Lisää token, eli "viestitunniste"
     * Käytännössä merkkaa viestin tällä tunnisteella luetuksi
     * Määreenä private, koska tätä käyttävä luokka on sisäluokka (inner class)
     * Jos et käytä sisäluokkaa, pitää olla public
     * @param token Viestitunniste
     */
    private void addToken(long token) {
        tokenit.add(token);
    }

    /**
     * Tarkista, onko viestitunniste jo olemassa
     * Määreenä private, koska tätä käyttävä luokka on sisäluokka (inner class)
     * Jos et käytä sisäluokkaa, pitää olla public
     * @param token Viestitunniste
     */
    private boolean tokenExists(long token) {
        return tokenit.contains(token);
    }

    /**
     * Yhdistä tämä vertainen olemassaolevaan Mesh-verkkoon
     * @param addr Solmun ip-osoite, johon yhdistetään
     * @param port Portti, jota vastapuolinen solmu kuuntelee
     */
    public void connect(String addr, int port) throws Exception{
        Socket s = new Socket(addr, port);
        Handler h = new Handler(s);
        String connectionIdentifier = s.getInetAddress() + ":" + s.getPort();
        connections.put(connectionIdentifier, h);
        h.start();
    }

    //vertaiskommunikaatiosta huolehtiva luokka (voi olla sisäluokka, "inner class")
    class Handler extends Thread {
        private final Socket client;
        private ObjectInputStream objIn;
        private ObjectOutputStream objOut;

        public Handler(Socket s) {
            client = s;
            try {
                InputStream inStream = client.getInputStream();
                OutputStream outStream = client.getOutputStream();
                outStream.flush();
                objOut = new ObjectOutputStream(outStream);
                objOut.flush();
                objIn = new ObjectInputStream(inStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted() ) {
                    final Message msg = (Message) objIn.readObject();
                    Long token = msg.getToken();
                    System.out.println("Received move at mesh: " + msg.getPayload());
                    //System.out.println("Received token " + token);

                    if(!tokenExists(token)) {
                    	
                    	if(!msg.getSender().equals(app.toString())) {
                    		if(msg.getType()==Message.Tyyppi.MOVE) {
                    			Platform.runLater(() -> {
		                    		System.out.println("starting handle message() with app ref "+app);
		                        	app.handleMessage(msg);
		                        });
                    		}
                    		else if(msg.getType()==Message.Tyyppi.MSG) {
                    			Platform.runLater(() -> {
		                    		System.out.println("starting ready confirm with app ref "+app);
		                        	app.confirm(1, (int)msg.getPayload());
		                        });
                    		}
                    		else if(msg.getType()==Message.Tyyppi.RNG) {
                    			Platform.runLater(() -> {
		                    		System.out.println("rolled "+(int)msg.getPayload()+" with app ref "+app);
		                        	app.setColor((int)msg.getPayload());
		                        });
                    		}
                    		else if(msg.getType()==Message.Tyyppi.READY) {
                    			Platform.runLater(() -> {
                    				System.out.println("sending confirm to other player, game starting");
                    				app.confirm(2, (boolean)msg.getPayload() ? 1 : 0);
                    			});
                    		}
                    		else if(msg.getType()==Message.Tyyppi.BANAANI) {
                    			Platform.runLater(() -> {
                    				System.out.println("received resignation end game for winner, put data into console");
                    				app.resign((int)msg.getPayload());
                    			});
                    		}
	                    	
                    	}

//                    	
//                        if (msg.getRecipient() == null){
//                            System.out.println("Broadcast, lähetetään eteenpäin viesti: " + msg.getPayload());
//                            
//                            //broadcast(msg);
//                        }
//                        else if (msg.getRecipient().equals(name)) {
//                            System.out.println("Priva viesti minulle, ei lähetetä eteenpäin, viesti: " + msg.getPayload());
//                        }
//                        else {
//                            System.out.println("privaviesti jollenkkin muulle, lähetetään eteenpäin");
//                            broadcast(msg);
//                        }
                        System.out.println("ei löydy lisätään");
                        addToken(token);
                    }
                    else {
                        System.out.println("löytyy token");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    objIn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        public void closeConnection() {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
            System.out.println("closing handler");
        }

        private void send(Serializable msg) {
            try {
                //outStream.flush();
                objOut.writeObject(msg);
                objOut.flush();
                //System.out.println("Lähetetty " + ((Message) msg).getMsg());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

