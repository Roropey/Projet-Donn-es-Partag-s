import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;


public class IrcSlow extends Frame {
	public TextArea		text;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java IrcSlow <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IrcSlow object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IrcSlow");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
			System.out.println("Création objet IrcSlow");
		}
		// create the graphical part
		new IrcSlow(s);
	}

	public IrcSlow(SharedObject s) {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data=new TextField(60);
		add(data);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerSlow(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerSlow(this));
		add(read_button);
		
		setSize(470,300);
		text.setBackground(Color.black); 
		show();
		
		sentence = s;
	}
}



class readListenerSlow implements ActionListener {
	IrcSlow irc;
	public readListenerSlow (IrcSlow i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// lock the object in read mode
		irc.sentence.lock_read();
		// Wait 10s
		try{
			Thread.sleep(10000);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		// invoke the method
		String s = ((Sentence)(irc.sentence.obj)).read();
		
		// unlock the object
		irc.sentence.unlock();
		
		// display the read value
		irc.text.append(s+"\n");
	}
}

class writeListenerSlow implements ActionListener {
	IrcSlow irc;
	public writeListenerSlow (IrcSlow i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// get the value to be written from the buffer
        	String s = irc.data.getText();
        	
        	// lock the object in write mode
		irc.sentence.lock_write();
		
		
		// invoke the method
		((Sentence)(irc.sentence.obj)).write(Irc.myName+" wrote "+s);
		// Wait 10s
		try{
			Thread.sleep(10000);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		irc.data.setText("");
		
		// unlock the object
		irc.sentence.unlock();
	}
}



