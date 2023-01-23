import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;


public class Irc_using_sentence_user extends Frame {
	public TextArea		text;
	public TextField	data;
	Sentence_user_itf	sentence;
	static String		myName;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		

		Sentence_user_itf s = (Sentence_user_itf) Client.lookup("IRC using IRC");
		if (s == null) {
			s = (Sentence_user_itf) Client.create(new Sentence_user());
			Client.register("IRC using IRC", s);
		}
		// create the graphical part
		new Irc_using_sentence_user(s);
	}

	public Irc_using_sentence_user(Sentence_user_itf s) {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data=new TextField(60);
		add(data);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener_user(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener_user(this));
		add(read_button);
		
		setSize(470,300);
		text.setBackground(Color.black); 
		show();		
		
		sentence = s;
	}
}



class readListener_user implements ActionListener {
	Irc_using_sentence_user irc;
	public readListener_user (Irc_using_sentence_user i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// lock the object in read mode
		//irc.sentence.lock_read();
		
		// invoke the method
		String s = irc.sentence.read();
		
		// unlock the object
		//irc.sentence.unlock();
		
		// display the read value
		irc.text.append(s+" \n");
	}
}

class writeListener_user implements ActionListener {
	Irc_using_sentence_user irc;
	public writeListener_user (Irc_using_sentence_user i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// get the value to be written from the buffer
        	String s = irc.data.getText();
        	
        	// lock the object in write mode
		//irc.sentence.lock_write();
		
		// invoke the method
		irc.sentence.write(Irc_using_sentence_user.myName+" wrote "+s);
		irc.data.setText("");
		
		// unlock the object
		//irc.sentence.unlock();
	}
}



