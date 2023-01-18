public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
public Sentence_stub(int id, Object obj) {
    super(id,obj);
}
public void write(String a0){
    Sentence object = (Sentence) obj;
    object.write(a0);
}
public String read(){
    Sentence object = (Sentence) obj;
  return object.read();
}
}