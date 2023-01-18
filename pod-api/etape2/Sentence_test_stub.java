public class Sentence_test_stub extends SharedObject implements Sentence_test_itf, java.io.Serializable {
    public Sentence_test_stub(int id, Object obj) {
        super(id,obj);
    }
    public void write(String a0){
        this.lock_write();
        Sentence_test object = (Sentence_test) obj;
        object.write(a0);
        this.unlock();
    }
    public String read(){
        this.lock_read();
        Sentence_test object = (Sentence_test) obj;
        String r = object.read();
        this.unlock();
        return r;
    }
}