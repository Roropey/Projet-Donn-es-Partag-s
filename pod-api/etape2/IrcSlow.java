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
	Sentence_test_itf		Sentence_test;
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
		Sentence_test_itf s = (Sentence_test_itf)Client.lookup("IRCSlow");
		if (s == null) {
			s = (Sentence_test_itf)Client.create(new Sentence_test());
			Client.register("IRCSlow", s);
		}
		// create the graphical part
		new IrcSlow(s);
	}

	public IrcSlow(Sentence_test_itf s) {
	
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
		
		Sentence_test = s;
	}
}



class readListenerSlow implements ActionListener {
	IrcSlow irc;
	public readListenerSlow (IrcSlow i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// lock the object in read mode
		//irc.Sentence_test.lock_read();
		
		// invoke the method
		String s = irc.Sentence_test.read();
		
		// unlock the object
		//irc.Sentence_test.unlock();
		
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
		//irc.Sentence_test.lock_write();
		
		// invoke the method
		irc.Sentence_test.write(Irc.myName+" wrote "+s);
		irc.data.setText("");
		
		// unlock the object
		//irc.Sentence_test.unlock();
	}
}



