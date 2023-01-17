public class Sentence_test {
	String 		data;
	public Sentence_test() {
		data = new String("");
	}
	@Write
	public void write(String text) {
		data = text;
	}
	@Read
	public String read() {
		return data;	
	}
	
}