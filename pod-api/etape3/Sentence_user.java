public class Sentence_user implements java.io.Serializable{

    Sentence_user sentence;
    String 		data;
    public Sentence_user() {
        data = new String("");
        
    }
    public void write(String text) {
		data = text;
	}
	public String read() {
		return data;	
	}
    public Sentence_user sentence_used(){
        return this.sentence;
    }
    
    
}
