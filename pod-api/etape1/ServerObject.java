import java.io.*;

public class ServerObject implements Serializable, ServerObject_itf {
	
	private int id;
	private Object obj;
	private enum lock {NL,
					RL,
					WL
	}
	private lock lock_state;

	public ServerObject(int id, Object objet){
		this.id = id;
		this.obj = objet;
		this.lock_state = lock.NL;
	}

	public int getId() {
		return id;
	}

	public Object getObj() {
		return obj;
	}

	

	// invoked by the user program on the client node
	public void lock_read() throws java.rmi.RemoteException{
		switch (this.lock_state){
			case WL :
				Server.reduce_lock(id);
			case NL :
				this.lock_state = lock.RL;
				break;
			case RL :
				Server.invalidate_reader(id);	
		}
	}

	// invoked by the user program on the client node
	public void lock_write() throws java.rmi.RemoteException{
		switch (this.lock_state){
			case RL : 
				Server.invalidate_reader(id);				
			case NL :
				this.lock_state = lock.WL;	
				break;
			case WL :
				Server.invalidate_writer(id);
		}
	}
}
