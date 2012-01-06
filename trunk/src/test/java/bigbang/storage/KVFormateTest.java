package bigbang.storage;

import junit.framework.TestCase;
import bigbang.storage.model.KVEntry;
import bigbang.storage.model.PersistenceFormate;


public class KVFormateTest extends TestCase{

    public void testFormate(){
        KVEntry kve = new KVEntry();
        kve.setKey("k");
        kve.setValue("v");
        byte[] b = PersistenceFormate.formateKVEntry(kve);
        KVEntry e = PersistenceFormate.formateByte(b);
        System.out.println(e);
    }
}
