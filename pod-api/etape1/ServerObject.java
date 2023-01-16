import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class ServerObject implements Serializable, ServerObject_itf {
	
	private int id;
	private Object obj;
	private enum lock {NL,
					RL,
					WL
	}
	private List<Client_itf> clientsUtilisateurs;
	private lock lock_state;

	public ServerObject(int id, Object objet){
		this.id = id;
		this.obj = objet;
		this.lock_state = lock.NL;
		this.clientsUtilisateurs = new ArrayList<Client_itf>();
	}

	public int getId() {
		return this.id;
	}

	public Object getObj() {
		return this.obj;
	}

	

	// invoked by the user program on the client node
	public Object lock_read(Client_itf client) throws java.rmi.RemoteException{
		try {
			switch (this.lock_state){
				case WL :
					Client_itf ancienUtilisateur = clientsUtilisateurs.remove(0);
					this.obj = Server.reduce_lock(this.id,ancienUtilisateur);
				case NL :
					this.lock_state = lock.RL;
				case RL :
					clientsUtilisateurs.add(client);
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		
		return this.obj;
	}

	// invoked by the user program on the client node
	public Object lock_write(Client_itf client) throws java.rmi.RemoteException{
		switch (this.lock_state){
			case RL :
				while (!clientsUtilisateurs.isEmpty()) {
					Client_itf ancienUtilisateur = clientsUtilisateurs.remove(0);
					Server.invalidate_reader(this.id,ancienUtilisateur);	
				}							
			case NL :
				this.lock_state = lock.WL;	
				break;
			case WL :
				try {
					Client_itf ancienUtilisateur = clientsUtilisateurs.remove(0);
					this.obj = Server.invalidate_writer(this.id,ancienUtilisateur);
				} catch (NullPointerException e) {
					System.out.println(" No writers ");
				} catch (RemoteException ee) {
					System.out.println("Client disconnected");
				}
					
				
		}
		clientsUtilisateurs.add(client);
		return this.obj;
	}
}
