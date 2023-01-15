import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;


public class IrcForced extends Frame {
	SharedObject		sentence;
	static String		myName;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
			System.out.println("Création objet IRC");
		}
		// create the graphical part
		new IrcForced(s);
	}

	public IrcForced(SharedObject s) {
		while (true){

			double choice = Math.random();

			if (choice < 0.5){
				// lock the object in read mode
				s.lock_read();
				// invoke the method
				String sentence = ((Sentence)(s.obj)).read();
				System.out.println("Read : "+sentence);
				
				// unlock the object
				s.unlock();
			} else {
				
				s.lock_write();
				
				// invoke the method
				((Sentence)(s.obj)).write(this.myName+" wrote");
				
				// unlock the object
				s.unlock();

			}

		}
		
		
	}
}





