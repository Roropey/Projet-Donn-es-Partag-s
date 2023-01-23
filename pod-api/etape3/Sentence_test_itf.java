public interface Sentence_test_itf extends SharedObject_itf {
	@Write
	public void write(String text);
    @Read
	public String read();
	
}