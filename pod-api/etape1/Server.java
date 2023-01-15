import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements Server_itf {

	private static HashMap<Object,Integer> MapObjectToId = new HashMap<>();
	private static HashMap<Integer,ServerObject> DictionnaireServerObject = new HashMap<>();
	private static HashMap<Integer,Client_itf> UtilisationServerObject = new HashMap<>();
	private HashMap<String,Integer> NamesIds;
	private static Integer nbObj = 0;

	public Server() throws RemoteException {
		super();
		this.NamesIds = new HashMap<>();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the server layer
	public static void init() {
		try {
			Registry registry = LocateRegistry.createRegistry(4000);
			Naming.rebind("//localhost:4000/serveur",new Server());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public int lookup(String name) {
		System.out.println("Lookup serveur "+name);
		int id = this.NamesIds.get(name);
		System.out.println("Serveur renvoie "+Integer.toString(id));
		return id;

	}		
	
	// binding in the name server
	public void register(String name, int id) {
		this.NamesIds.put(name,id);

	}

	// creation of a shared object
	public int create(Object o) {
		
		if (MapObjectToId.get(o) == null) {
			nbObj +=1;
			ServerObject serverObject = new ServerObject(nbObj, o);
			MapObjectToId.put(o,nbObj);
			DictionnaireServerObject.put(nbObj,serverObject);
		}
		
		return MapObjectToId.get(o);
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the client
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException{
		System.out.println("Server lock_read sur "+Integer.toString(id));
		ServerObject serverObject = DictionnaireServerObject.get(id);
		System.out.println("Server lock_read sur objet "+serverObject.getObj().getClass().getName());
		//Client client_non_itf = (Client) client;
		//UtilisationServerObject.put(id,client_non_itf);
		Object objet = 	serverObject.lock_read(client);
		System.out.println("Retour server lock_read : "+objet.getClass().getName());
		return objet ;
		//return serverObject.getObj();
			
	}

	// request a write lock from the client
	public Object lock_write (int id, Client_itf client) throws java.rmi.RemoteException{
		
		
		System.out.println("Server lock_write sur "+Integer.toString(id));
		ServerObject serverObject = DictionnaireServerObject.get(id);
		
		System.out.println("Server lock_write sur objet "+serverObject.getObj().getClass().getName());
		//Client client_non_itf = (Client) client;
		//UtilisationServerObject.put(id,client_non_itf);	
		Object objet = 	serverObject.lock_write(client);
		System.out.println("Retour server lock_write : "+objet.getClass().getName());
		return objet ;		
		//return serverObject.getObj();
		
	}

	public static void reduce_lock(int id, Client_itf client) throws java.rmi.RemoteException{
		//Client clientUtilisateur = UtilisationServerObject.get(id);
		client.reduce_lock(id);
	}

	public static void invalidate_reader(int id, Client_itf client) throws java.rmi.RemoteException{
		//Client clientUtilisateur = UtilisationServerObject.get(id);
		client.invalidate_reader(id);
	}

	public static void invalidate_writer(int id, Client_itf client) throws java.rmi.RemoteException{
		//Client clientUtilisateur = UtilisationServerObject.get(id);
		client.invalidate_writer(id);
	}

	public static void main(String args[]) {
		Server.init();
		System.out.println("Serveur initialisé.");
		//Server.start();
		//System.out.println("Serveur tourne.");
	}

}
