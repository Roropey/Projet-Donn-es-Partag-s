import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	private int id;
	public Object obj;
	public enum lock {NL,
					RLC,
					WLC,
					RLT,
					WLT,
					RLT_WLC,
	}
	private lock lock_state;
	private Boolean waiting;

	public SharedObject(int id, Object objet){
		this.id = id;
		this.obj = objet;
		this.lock_state = lock.NL;
		this.waiting = false;
	}

	public int getId() {
		return id;
	}

	public Object getObj() {
		return obj;
	}
	public lock getLock() {
		return this.lock_state;
	}

	protected Object readResolve(){

		SharedObject sharedObject = null;
		if (!RoleJVM.isServer()){
			sharedObject = Client.idToSharedObjectMemorized(id);
			if (sharedObject == null) {
				Client.create_memorize_stub(id, obj);
			}
		}	
		return sharedObject;
	}

	
	

	// invoked by the user program on the client node
	public void lock_read() {
		
		Boolean lockRead = false;
		synchronized (this) {
			while (this.waiting){
				try {
					wait();
				} catch ( InterruptedException e ) {
					e.printStackTrace ();
				}
			} 
			switch (this.lock_state){
				case NL :
					lockRead = true;
				case RLC :
					this.lock_state = lock.RLT;
					break;
				case WLC :			
					this.lock_state = lock.RLT_WLC;
					break;
				default :
					;;			
			}
		}
		if (lockRead){
			this.obj = Client.lock_read(id);
		}
		
	}

	// invoked by the user program on the client node
	public void lock_write() {
		
		Boolean lockWrite = false;
		synchronized (this) {
			// tant que l’attribut "attente" est vrai , on attend
			while (this.waiting){
				try {
					wait();
				} catch(InterruptedException e ) {
					e.printStackTrace ();
				}
			}
			switch (this.lock_state){
				case NL : 				
				case RLC :
					lockWrite = true;					
				case WLC :
					this.lock_state = lock.WLT;
					break;
				default:
					;;
			}
		}
		if (lockWrite){
			this.obj = Client.lock_write(id);
		}
	}


	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (this.lock_state){
			case RLT : 
				this.lock_state = lock.RLC;
				break;
			case WLT :
			case RLT_WLC:
				this.lock_state = lock.WLC;
				break;
			default:
				;;
		}
		try {
			notify();
		} catch(Exception e){
			e.printStackTrace();
		}
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		this.waiting = true;
		switch (this.lock_state){
			case WLT :
				while (this.lock_state==lock.WLT) {
					try {
						wait();
					} catch (InterruptedException e ) {
						e.printStackTrace();
					}
				}
			case WLC :
				this.lock_state = lock.RLC;
				break;
			case RLT_WLC :
				this.lock_state = lock.RLT;
				break;
			default :
				;;
		}
		this.waiting = false;
		try {
			notify();
		} catch(Exception e){
			e.printStackTrace();
		}
		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		this.waiting = true;
		switch (this.lock_state){
			case RLT :
				while (this.lock_state==lock.RLT) {
					try {
						wait();
					} catch (InterruptedException e ) {
						e.printStackTrace();
					}
				}
			case RLC :			
				this.lock_state = lock.NL;
				break;
			default :
				;;
		}
		this.waiting = false;
		try {
			notify();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized Object invalidate_writer() {
		
		this.waiting = true;
		switch (this.lock_state){
			case WLT :
				while (this.lock_state==lock.WLT) {
					try {
						wait();
					} catch (InterruptedException e ) {
						e.printStackTrace();
					}
				}
			case RLT_WLC :
				while (this.lock_state==lock.RLT_WLC) {
					try {
						wait();
					} catch (InterruptedException e ) {
						e.printStackTrace();
					}
				}
			case WLC :			
				this.lock_state = lock.NL;
				break;
			default :
				;;
		}
		this.waiting = false;
		try {
			notify();
		} catch(Exception e){
			e.printStackTrace();
		}
		return this.obj;
	}
}
