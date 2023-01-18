public class Sentence_user_stub extends SharedObject implements Sentence_user_itf, java.io.Serializable {
public Sentence_user_stub(int id, Object obj) {
    super(id,obj);
}
public void write(String a0){
    this.lock_write();
    Sentence_user object = (Sentence_user) obj;
    object.write(a0);
    this.unlock();
}
public String read(){
    this.lock_read();
    Sentence_user object = (Sentence_user) obj;
String    r = object.read();
    this.unlock();
    return r;
}
public int sentenceHashCode(){
    this.lock_write();
    Sentence_user object = (Sentence_user) obj;
int    r = object.sentenceHashCode();
    this.unlock();
    return r;
}
}