package bigbang.storage.model;

public class KVEntry {

    String  key;
    String  value;
    KVEntry next;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public KVEntry getNext() {
        return next;
    }
    
    public void setNext(KVEntry next) {
        this.next = next;
    }
    
    @Override
    public String toString(){
        return key+":"+value+"-->"+next;
    }
    
}
