public class Sentence implements java.io.Serializable {
	String 		data;
	public Sentence() {
		data = new String("");
	}
	
	public void write(String text) {
		data = text;
		System.out.println("Sentence write "+data);
	}
	public String read() {
		System.out.println("Sentence read "+data);
		return data;	
	}
	
}