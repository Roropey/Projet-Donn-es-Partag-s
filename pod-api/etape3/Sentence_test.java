public class Sentence_test implements java.io.Serializable{
	String 		data;
	public Sentence_test() {
		data = new String("");
	}
	public void write(String text) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		data = text;
	}
	public String read() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return data;	
	}
	
}