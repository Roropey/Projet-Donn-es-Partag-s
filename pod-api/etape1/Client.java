import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static HashMap<Integer,SharedObject> DictionnaireIntegerSharedObject = new HashMap<>();
	private static HashMap<SharedObject,Integer> DictionnaireSharedObjectInteger = new HashMap<>();
	private static Client clientActuel;

	public Client() throws RemoteException {
		super();
	}

	

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		;;
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		try {
			Server_itf serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
			int id = serveur.lookup(name);
			return DictionnaireIntegerSharedObject.get(id);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		try {
			Server_itf serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
			int id = DictionnaireSharedObjectInteger.get(so);
			serveur.register(name,id);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		try {
			Server_itf serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
			int id = serveur.create(o);
			SharedObject sharedObject = new SharedObject(id, o);
			DictionnaireSharedObjectInteger.put(sharedObject,id);		
			DictionnaireIntegerSharedObject.put(id,sharedObject);

			return sharedObject;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		try {
			Server_itf serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
			Object objet = serveur.lock_read(id,clientActuel);
			return objet;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		try {
			Server_itf serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
			Object objet = serveur.lock_write(id,clientActuel);		
			return objet;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
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
		return sharedObject.getObj();
	}
}
