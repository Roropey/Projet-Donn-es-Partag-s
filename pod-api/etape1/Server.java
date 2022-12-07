import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements Server_itf {

	//HashMap<Integer,ServerObject_itf> DictionnaireServerObject;
	private static int portServeurClient = 1234;
	public Server() throws RemoteException {
		super();
		//this.DictionnaireServerObject = new HashMap<>();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the server layer
	public static void init() {
		try {
			Registry registry = LocateRegistry.createRegistry(portServeurClient);
			Naming.rebind("//localhost:"+Integer.toString(portServeurClient)+"/serveur",new Server());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public int lookup(String name) {

	}		
	
	// binding in the name server
	public void register(String name, int id) {

	}

	// creation of a shared object
	public int create(Object o) {
		
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the client
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException{
	}

	// request a write lock from the client
	public Object lock_write (int id, Client_itf client) throws java.rmi.RemoteException{
	}

}
