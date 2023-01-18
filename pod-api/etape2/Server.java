import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class Server extends UnicastRemoteObject implements Server_itf {

	private Lock moniteurLookupCreator;
	private static HashMap<Integer, Lock> moniteursServerObject;
	private static HashMap<Integer,ServerObject> MapIntegerToServerObject;
	private static HashMap<String,Integer> MapStringToInteger;
	private static Integer nbObj = 0;

	public Server() throws RemoteException {
		super();
		MapStringToInteger = new HashMap<>();
		MapIntegerToServerObject = new HashMap<>();
		moniteursServerObject = new HashMap<>();
		moniteurLookupCreator = new ReentrantLock();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the server layer
	public static void init() {
		try {
			LocateRegistry.createRegistry(4000);
			Naming.rebind("//localhost:4000/serveur",new Server());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public int lookup(String name) {
		moniteurLookupCreator.lock();
		
		if (MapStringToInteger.get(name)!=null){
			System.out.println("Débloquer pour prochain client");
			moniteurLookupCreator.unlock();
			return MapStringToInteger.get(name);
		} else {
			
			System.out.println("Bloquer pour prochain client, attente réalisation create et register");
			return -1 ;
		}

	}	
	
	public Object getObject(int id){
		Object obj = null;
		if (MapIntegerToServerObject.get(id)!=null){
			obj = MapIntegerToServerObject.get(id).getObj();
		}
		return obj;

	}
	
	// binding in the name server
	public void register(String name, int id) {
		MapStringToInteger.put(name,id);
		moniteurLookupCreator.unlock();

	}

	// creation of a shared object
	public int create(Object o) {
		
		
		nbObj +=1;
		ServerObject serverObject = new ServerObject(nbObj, o);
		MapIntegerToServerObject.put(nbObj,serverObject);
		
		Lock moniteur = new ReentrantLock();
		moniteursServerObject.put(nbObj,moniteur);
		
		return nbObj;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the client
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException{

		Lock moniteur = moniteursServerObject.get(id);
		moniteur.lock();
		ServerObject serverObject = MapIntegerToServerObject.get(id);
		Object objet = 	serverObject.lock_read(client);
		moniteur.unlock();
		return objet ;
			
	}

	// request a write lock from the client
	public Object lock_write (int id, Client_itf client) throws java.rmi.RemoteException{

		Lock moniteur = moniteursServerObject.get(id);
		moniteur.lock();
		ServerObject serverObject = MapIntegerToServerObject.get(id);
		Object objet = 	serverObject.lock_write(client);
		moniteur.unlock();
		return objet ;		
		
	}

	public static Object reduce_lock(int id, Client_itf client) throws java.rmi.RemoteException{
		return client.reduce_lock(id);
	}

	public static void invalidate_reader(int id, Client_itf client) throws java.rmi.RemoteException{
		client.invalidate_reader(id);
	}

	public static Object invalidate_writer(int id, Client_itf client) throws java.rmi.RemoteException{
		return client.invalidate_writer(id);
	}
	
/////////////////////////////////////////////////////////////
//    Methode d'execution du serveur
////////////////////////////////////////////////////////////

public static void main(String args[]) {
		
	System.out.println("Initialisation du serveur...");
	Server.init();
	System.out.println("Serveur initialisé.");
}

}
