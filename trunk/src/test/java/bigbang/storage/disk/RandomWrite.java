package bigbang.storage.disk;

import bigbang.storage.persistence.PageManager;
import bigbang.storage.persistence.PersistenceManager;

public class RandomWrite {

    public static void main(String[] args) throws Exception {
        int s = 100;
        PersistenceManager persm[] = new PersistenceManager[s];
        PageManager pagem[] = new PageManager[s];
        for (int i = 0; i < s; i++) {
            persm[i] = new PersistenceManager(i);
            pagem[i] = new PageManager(null);
        }
        byte[] b = ByteUtil.getBytes();
        long begin = System.currentTimeMillis();
        
        for (int j = 0; j < 250; j++) {
            for (int i = 0; i < s; i++) {
                int p = pagem[i].getFreePage();
               persm[i].writePage(b, p);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("time:"+(end-begin));
    }
}
