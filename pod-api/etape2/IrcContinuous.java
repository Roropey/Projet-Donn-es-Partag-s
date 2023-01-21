import java.util.*;



public class IrcContinuous {
	public static void main(String args[]) {
		if (args.length != 2) {
			System.out.println("java IrcContinuous <nbClient> <nbSharedObject>");
			return;
		}
		int nbClient = Integer.parseInt(args[0]);
		System.out.println("Création de "+nbClient+" clients dans des threads");
		Thread[] pool = new Thread[nbClient];
		for (int i = 0; i<nbClient;i++){
			System.out.println("Lancement thread Client"+i);
			Thread t = new IrcForced("Client"+i,Integer.parseInt(args[1]));
			t.start();
			pool[i] = t;
		}

		for (Thread t : pool){
			try {
				t.join();
			} catch (Exception e){
				e.printStackTrace();
			}
		}


	}
}



class IrcForced extends Thread {
	private ArrayList<Sentence_itf>	listeSharedObject;
	private String			myName;
	private Random			random;
	private int				nbSO;

	public IrcForced(String name, int numSharedObject) {
		
		
		this.myName = name;
		this.random = new Random();
		this.nbSO = numSharedObject;
		// initialize the system
		Client.init();
		this.listeSharedObject = new ArrayList<Sentence_itf>();
		
		for (int i = 0; i<this.nbSO; i++){
			
			System.out.println("Création SO"+i+" pour "+myName);
			String nameSO = "IRC"+i;
			Sentence_itf s = (Sentence_itf)Client.lookup(nameSO);
			if (s == null) {
				s = (Sentence_itf)Client.create(new Sentence());
				Client.register(nameSO, s);
			}
			this.listeSharedObject.add(s);
		}
		System.out.println("Init "+myName+" terminé");
		
		
	}

	public void run(){
		while (true){
			
			int choiceSO = this.random.nextInt(this.nbSO);
			double choiceWOrR = this.random.nextDouble();

			if (choiceWOrR < 0.5){
				try {
					//System.out.println(myName+" décide de lire IRC"+choiceSO);
					//System.out.println(myName+" lit IRC"+choiceSO);
					// invoke the method
					/*
					try{
						Thread.sleep(5000);
					} catch (Exception exc) {
						exc.printStackTrace();
					} */
					String sentence = (this.listeSharedObject.get(choiceSO)).read();
					System.out.println(this.myName+" read : "+sentence);
				
				//System.out.println(this.myName+" a fini de lire IRC"+choiceSO);
				} catch(Exception e){
					System.out.println("Echec lecture de "+this.myName+" dans IRC"+choiceSO);
				}
				
			} else {
				try{
				//System.out.println(this.myName+" décide d'écrire dans IRC"+choiceSO);
				//System.out.println(this.myName+" écrit dans IRC"+choiceSO);
				// invoke the method
				/*
				try{
					Thread.sleep(5000);
				} catch (Exception exc) {
					exc.printStackTrace();
				} */
				(this.listeSharedObject.get(choiceSO)).write(myName+" wrote in IRC"+choiceSO);
				
				//System.out.println(this.myName+" a fini d'écrire dans IRC"+choiceSO);
				} catch(Exception e){
					System.out.println("Echec écriture de "+this.myName+" dans IRC"+choiceSO);
				}

			}
			try {				
				Thread.sleep(10);
			} catch (Exception e){
				e.printStackTrace();
			}

		}
		
		
	}
}





