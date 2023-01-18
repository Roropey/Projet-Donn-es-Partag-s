public class Sentence_user implements java.io.Serializable{

    private Sentence sentenceUsed;

    public Sentence_user(Sentence sentenceUsed) {
        this.sentenceUsed = sentenceUsed;
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
