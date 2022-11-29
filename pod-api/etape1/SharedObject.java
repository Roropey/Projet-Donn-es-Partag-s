import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	private int id;
	private enum lock {NL,
					RLC,
					WLC,
					RLT,
					WLT,
					RLT_WLC,
	}
	private lock lock_state;
	// 0 : NL
	// 1 : RLC
	// 2 : WLC
	// 3 : RLT
	// 4 : WLT
	// 5 : RLT_wLC

	public SharedObject(int id){
		this.id = id;
		this.lock_state = lock.NL;
	}

	public int getId() {
		return id;
	}

	

	// invoked by the user program on the client node
	public void lock_read() {
		switch (this.lock_state){
			case NL :
			case RLC :
				this.lock_state = lock.RLT;
				break;
			case WLC :
				this.lock_state = lock.RLT_WLC;
				break;
			
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		switch (this.lock_state){
			case NL : 				
			case RLC :				
			case WLC :
				this.lock_state = lock.WLT;
				break;
			default:
				;;
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
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
	}

	public synchronized Object invalidate_writer() {
	}
}
