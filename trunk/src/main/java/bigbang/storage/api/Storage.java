package bigbang.storage.api;

import bigbang.storage.mem.MemManager;
import bigbang.storage.model.KVEntry;

public class Storage {

    private MemManager mm = MemManager.getInstance();

    public void set(String key, String value) {
        mm.setKV(key, value);
    }

    public String get(String key) {
        KVEntry kve = mm.getKV(key);
        if(kve==null){
            return null;
        }
        return kve.getValue();
    }

    public void close() {
        mm.close();
    }

    public void init() {
        mm.init();
    }
}
