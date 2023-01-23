import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static HashMap<Integer,SharedObject> MapIntegerToSharedObject;
	private static Server_itf serveur;
	private static Client clientActuel = null;

	public Client() throws RemoteException {
		super();
	}

	

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		
		if (clientActuel == null){
			try{
				clientActuel = new Client();
			} catch (RemoteException e){
				e.printStackTrace();
			}

			try {
				serveur = (Server_itf) Naming.lookup("//localhost:4000/serveur");
				MapIntegerToSharedObject = new HashMap<Integer,SharedObject>();
			} catch (Exception e){
				System.out.println("Echec connection serveur");
				e.printStackTrace();
			}
		}
		
		

	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		SharedObject sharedObject = null;
		try {
			int id = serveur.lookup(name);
			

			if (id>=0) {
				if (MapIntegerToSharedObject.get(id) == null){
					
					System.out.println("Objet existant dans serveur mais pas client => création objet vide client");
					SharedObject sharedObjectVide = new SharedObject(id, null);
					MapIntegerToSharedObject.put(id,sharedObjectVide);
				}
				sharedObject = MapIntegerToSharedObject.get(id);
			}
		} catch (Exception e){

			System.out.println("Echec Lookup");
			e.printStackTrace();
			
		}
		return sharedObject;
		
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so_itf) {
		try {
			SharedObject so = (SharedObject) so_itf;
			serveur.register(name,so.getId());
		} catch (Exception e){
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		SharedObject sharedObject = null;
		try {
			int id = serveur.create(o);
			sharedObject = new SharedObject(id, o);
			
			MapIntegerToSharedObject.put(id,sharedObject);

			
		} catch (RemoteException e){
			e.printStackTrace();
		}
		return sharedObject;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		Object objet = null;
		try {
			objet = serveur.lock_read(id,clientActuel);			
		} catch (Exception e){
			
			e.printStackTrace();
		}
		return objet;
	}

	// request a write lock from the server
	public static Object lock_write (int id) {		
		Object objet = null;
		try {
			objet = serveur.lock_write(id,clientActuel);					
		} catch (Exception e){
			e.printStackTrace();
		}
		return objet;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		SharedObject sharedObject = MapIntegerToSharedObject.get(id);
		return sharedObject.reduce_lock();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		SharedObject sharedObject = MapIntegerToSharedObject.get(id);
		sharedObject.invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		
		SharedObject sharedObject = MapIntegerToSharedObject.get(id);
		return sharedObject.invalidate_writer();
	}
}
