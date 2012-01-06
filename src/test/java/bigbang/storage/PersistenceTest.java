package bigbang.storage;

import junit.framework.TestCase;
import bigbang.storage.model.KVEntry;
import bigbang.storage.model.PersistenceFormate;
import bigbang.storage.persistence.PersistenceManager;


public class PersistenceTest extends TestCase {
    PersistenceManager pm = new PersistenceManager(0);
    
    public void testPersistence(){
        KVEntry kve = new KVEntry();
        kve.setKey("k");
        kve.setValue("v");
        
        KVEntry kven = new KVEntry();
        kven.setKey("k1");
        kven.setValue("v1");
        kve.setNext(kven);
        byte[] b = PersistenceFormate.formateKVEntry(kve);
        pm.writePage(b, 1);
        
        byte[] e = pm.readPage(1);
        KVEntry en = PersistenceFormate.formateByte(e);
        System.out.println(en);
    }
}
