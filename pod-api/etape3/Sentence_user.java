public class Sentence_user implements java.io.Serializable{

    SharedObject sentence;

    public Sentence_user() {
        
    }

    @Write
    public void write(String text) {
		this.sentenceUsed.write(text);
	}
    @Read
	public String read() {
		return this.sentenceUsed.read();	
	}
    @Write
    public int sentenceHashCode(){
        return this.sentenceUsed.hashCode();
    }
    
    
    
}
