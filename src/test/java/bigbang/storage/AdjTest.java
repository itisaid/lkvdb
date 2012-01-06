package bigbang.storage;

import java.util.concurrent.locks.ReentrantLock;

import junit.framework.TestCase;
import bigbang.storage.mem.AttachIndexManager;
import bigbang.storage.model.NewNest;


public class AdjTest extends TestCase{

    public void testRelease(){
        NewNest[] nn = new NewNest[5];
        ReentrantLock lock = new ReentrantLock();
        for(int i=0;i<5;i++){
            nn[i] = new NewNest();
        }
        AttachIndexManager aim = new AttachIndexManager(null,nn,lock);
        aim.adjLru(3);
        aim.adjLru(4);
        aim.adjLru(3);
        aim.adjLru(0);
        aim.adjLru(4);
        aim.adjLru(0);
        aim.adjLru(2);
        aim.adjLru(3);
        aim.adjLru(1);
        assertEquals(nn[0].getLruNext(),4);
        assertEquals(nn[3].getLruPre(),1);
        assertEquals(nn[2].getLruPre(),3);
        
    }
}
