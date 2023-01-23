public interface Sentence_user_itf extends SharedObject_itf {
    @Write
    public void write(String text);
    @Read
	public String read();
    public Sentence_user sentence_used();
    
    
    
}
