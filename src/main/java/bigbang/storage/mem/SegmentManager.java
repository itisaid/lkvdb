package bigbang.storage.mem;

import java.util.concurrent.locks.ReentrantLock;

import bigbang.storage.Thread.PageIndexStore;
import bigbang.storage.model.KVEntry;
import bigbang.storage.model.NewNest;
import bigbang.storage.model.Parameter;
import bigbang.storage.model.PersistenceFormate;
import bigbang.storage.persistence.PageManager;
import bigbang.storage.persistence.PersistenceManager;

public class SegmentManager {

    NewNest[]          nest;
    int                no;

    PersistenceManager persm;
    PageIndexStore     pagei;
    PageManager        pagem;
    AttachIndexManager aim;
    ReentrantLock      lock = new ReentrantLock();

    public SegmentManager(int num) {
        this.no = num;
        persm = new PersistenceManager(num);
        pagei = new PageIndexStore(num);

    }

    public void init() {
        int[] pageIndex = pagei.loadPageIndex();
        nest = new NewNest[Parameter.bigSize];
        if (pageIndex != null) {
            for (int i = 0; i < pageIndex.length; i++) {
                if (pageIndex[i] != -1) {
                    nest[i] = new NewNest();
                    nest[i].setPage(pageIndex[i]);
                    // nest[i].setKvEntry(PersistenceFormate.formateByte(persm.readPage(pageIndex[i])));
                }
            }
        }
        persm.init();
        pagem = new PageManager(pageIndex);
        aim = new AttachIndexManager(this,nest, lock);
    }

    private int makeInd(int hashCode) {
        int index = hashCode % Parameter.bigSize;

        // System.out.println(no+":"+index+",");
        return index;
    }

    private NewNest getNest(int hashCode) {
        int index = makeInd(hashCode);
        if (nest[index] != null) {
            aim.adjLru(index);
        }
        return nest[index];

    }

    private void addNest(NewNest nn, int hashCode) {
        int index = makeInd(hashCode);
        nest[index] = nn;
        if (nest[index] != null) {
            aim.adjLru(index);
        }
    }

    /**
     * load kventry from disk if it's not in memory
     * 
     * @param nn
     * @return
     */
    private KVEntry fetchNewNest(NewNest nn) {
        KVEntry kve = nn.getKvEntry();
        if (kve != null) {
            return kve;
        }
        synchronized (this) {
            kve = nn.getKvEntry();
            if (kve != null) {
                return kve;
            }
            kve = PersistenceFormate.formateByte((persm.readPage(nn.getPage())));
            aim.adjLoadNum();
            nn.setKvEntry(kve);
            return kve;
        }
    }

    public KVEntry getKV(String key, int hashCode) {
        NewNest nn = getNest(hashCode);
        if (nn == null) {
            return null;
        }
        KVEntry kve = fetchNewNest(nn);
        KVEntry tmp = kve;
        int i = 0;
        while (kve != null && !key.equals(kve.getKey())) {
            kve = kve.getNext();
            i++;
        }
        if (i > 6) {
            System.out.println(tmp);
        }
        return kve;
    }

    public void setKV(String key, String value, int hashCode) {// TODO to optimize

        KVEntry kve = null;
        NewNest nn = getNest(hashCode);
        if (nn != null) {
            kve = fetchNewNest(nn);
            while (kve != null && !key.equals(kve.getKey())) {
                kve = kve.getNext();
            }
        }

        if (kve == null) {
            kve = new KVEntry();
            kve.setKey(key);
            kve.setValue(value);
            synchronized (this) {
                nn = getNest(hashCode);
                if (nn == null) {
                    aim.adjLoadNum();
                    nn = new NewNest();
                    lock.lock();
                    try {
                        aim.adjDirty(nn);
                        nn.setKvEntry(kve);
                        addNest(nn, hashCode);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    KVEntry okve = fetchNewNest(nn);
                    lock.lock();
                    try {
                        aim.adjDirty(nn);
                        kve.setNext(okve);
                        nn.setKvEntry(kve);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } else {
            if (!value.equals(kve.getValue())) {
                lock.lock();
                try {
                    aim.adjDirty(nn);
                    kve.setValue(value);
                } finally {
                    lock.unlock();
                }
            } else {
                return;
            }
        }

    }

//    public void close() {
//
//        int[] i = new int[Parameter.bigSize];
//        for (int k = 0; k < i.length; k++) {
//            if (nest[k] != null) {
//                i[k] = nest[k].getPage();
//            } else {
//                i[k] = -1;
//            }
//        }
//        pagei.flushIndex(i);
//
//    }

    public PersistenceManager getPersm() {
        return persm;
    }

    public PageIndexStore getPagei() {
        return pagei;
    }

    public PageManager getPagem() {
        return pagem;
    }

    public NewNest[] getNest() {
        return nest;
    }

    public AttachIndexManager getAim() {
        return aim;
    }
    
    @Override
    public String toString(){
        return String.valueOf(no);
    }

}
