import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements Server_itf {

	private static HashMap<Integer,ServerObject> DictionnaireServerObject = new HashMap<>();
	private static HashMap<Integer,Client> UtilisationServerObject = new HashMap<>();
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
		return this.NamesIds.get(name);

	}		
	
	// binding in the name server
	public void register(String name, int id) {
		this.NamesIds.put(name,id);

	}

	// creation of a shared object
	public int create(Object o) {
		nbObj +=1;
		ServerObject serverObject = new ServerObject(nbObj, o);
		DictionnaireServerObject.put(nbObj,serverObject);
		return nbObj;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the client
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException{
		ServerObject serverObject = DictionnaireServerObject.get(id);
		Client client_non_itf = (Client) client;
		UtilisationServerObject.put(id,client_non_itf);	
		serverObject.lock_read();
		return serverObject.getObj();
			
	}

	// request a write lock from the client
	public Object lock_write (int id, Client_itf client) throws java.rmi.RemoteException{
		
		
		ServerObject serverObject = DictionnaireServerObject.get(id);
		Client client_non_itf = (Client) client;
		UtilisationServerObject.put(id,client_non_itf);			
		serverObject.lock_write();
		return serverObject.getObj();
		
	}

	public static void reduce_lock(int id) throws java.rmi.RemoteException{
		Client clientUtilisateur = UtilisationServerObject.get(id);
		clientUtilisateur.reduce_lock(id);
	}

	public static void invalidate_reader(int id) throws java.rmi.RemoteException{
		Client clientUtilisateur = UtilisationServerObject.get(id);
		clientUtilisateur.invalidate_reader(id);
	}

	public static void invalidate_writer(int id) throws java.rmi.RemoteException{
		Client clientUtilisateur = UtilisationServerObject.get(id);
		clientUtilisateur.invalidate_writer(id);
	}

}
