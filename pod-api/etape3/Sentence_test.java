public class Sentence_test {
	String 		data;
	public Sentence_test() {
		data = new String("");
	}
	public void write(String text) {
		data = text;
	}
	public String read() {
		return data;	
	}
	
}