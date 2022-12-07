import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static HashMap<Integer,SharedObject> DictionnaireIntegerSharedObject;
	private static HashMap<SharedObject,Integer> DictionnaireSharedObjectInteger;
	private static int portServeurClient = 1234;
	private static Client clientActuel;
	public Client() throws RemoteException {
		super();
		DictionnaireIntegerSharedObject = new HashMap<>();		
		DictionnaireSharedObjectInteger = new HashMap<>();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		try {			
			Registry registry = LocateRegistry.createRegistry(portServeurClient);
			clientActuel = new Client()
			Naming.rebind("//localhost:"+Integer.toString(portServeurClient)+"/client",clientActuel);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		Server_itf serveur = (Server_itf) Naming.lookup("//localhost:"+Integer.toString(portServeurClient)+"/serveur")
		int id = serveur.lookup(name);
		return DictionnaireIntegerSharedObject.get(id);
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		Server_itf serveur = (Server_itf) Naming.lookup("//localhost:"+Integer.toString(portServeurClient)+"/serveur")
		int id = DictionnaireSharedObjectInteger.get(so);
		serveur.register(name,id);
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		Server_itf serveur = (Server_itf) Naming.lookup("//localhost:"+Integer.toString(portServeurClient)+"/serveur")
		int id = serveur.create(o);
		SharedObject sharedObject = new SharedObject(id, o);
		DictionnaireSharedObjectInteger.put(sharedObject,id);		
		DictionnaireIntegerSharedObject.put(id,sharedObject);

		return sharedObject;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		Server_itf serveur = (Server_itf) Naming.lookup("//localhost:"+Integer.toString(portServeurClient)+"/serveur")
		Object objet = serveur.lock_read(id,clientActuel);
		SharedObject sharedObject = DictionnaireIntegerSharedObject.get(id);
		sharedObject.lock_read();
		return objet;
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		Server_itf serveur = (Server_itf) Naming.lookup("//localhost:"+Integer.toString(portServeurClient)+"/serveur")
		Object objet = serveur.lock_write(id,clientActuel);
		SharedObject sharedObject = DictionnaireIntegerSharedObject.get(id);
		sharedObject.lock_write();
		return objet;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		SharedObject sharedObject = DictionnaireIntegerSharedObject.get(id);
		sharedObject.reduce_lock();
		return sharedObject.getObj();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		SharedObject sharedObject = DictionnaireIntegerSharedObject.get(id);
		sharedObject.invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		SharedObject sharedObject = DictionnaireIntegerSharedObject.get(id);
		sharedObject.invalidate_reader();
	}
}
