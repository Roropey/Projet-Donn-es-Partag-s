public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
    public Sentence_stub(int id, Object obj) {
        super(id,obj);
    }
    public void write(String a0){
        this.lock_write();
        Sentence object = (Sentence) obj;
        object.write(a0);
        this.unlock();
    }
    public String read(){
        this.lock_read();
        Sentence object = (Sentence) obj;
        String r = object.read();
        this.unlock();
        return r;
    }
}