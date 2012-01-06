package bigbang.storage;

import junit.framework.TestCase;
import bigbang.storage.persistence.PageManager;

public class PageManagerTest extends TestCase {

    public void testGetFree1() {
        PageManager pm = new PageManager(new int[] { -1, 2, 3, -1, -1 });// pageSize=1000;fileSize=5000;
        int f = pm.getFreePage();
        assertEquals(f, 0);
        f = pm.getFreePage();
        assertEquals(f, 1);
        try {
            f = pm.getFreePage();
            fail();
        } catch (Throwable t) {
        }
        pm.returnFreePage(1);
        pm.returnFreePage(0);
        pm.returnFreePage(3);
        pm.returnFreePage(2);
        f = pm.getFreePage();
        assertEquals(f, 2);
        f = pm.getFreePage();
        assertEquals(f, 3);
        f = pm.getFreePage();
        assertEquals(f, 0);
        pm.returnFreePage(2);
        f = pm.getFreePage();
        assertEquals(f, 2);

    }

    public void testGetFree2() {
        PageManager pm = new PageManager(new int[] { -1, 0, 3, -1, -1 });// pageSize=1000;fileSize=5000;
        int f = pm.getFreePage();
    }
}
