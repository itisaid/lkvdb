package bigbang.storage.mem;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import bigbang.storage.Thread.FlushScanner;
import bigbang.storage.model.NewNest;

public class AttachIndexManager {

    private int           loadedNum;
    private int           dirtyNum;
    private int           lruHead = -1;
    private int           lruTail = -1;
    private NewNest[]     nest;
    private ReentrantLock lock;
    private SegmentManager sm;

    public AttachIndexManager(SegmentManager sm,NewNest[] nest, ReentrantLock lock) {
        this.nest = nest;
        this.lock = lock;
        this.sm=sm;
    }

    public void adjLoadNum() {
        lock.lock();
        loadedNum++;
        lock.unlock();
    }

    public void adjLru(int index) {
        lock.lock();
        if (lruHead == -1 && lruTail == -1) {
            lruHead = index;
            lruTail = index;
            lock.unlock();
            return;
        }
        if (lruHead == index) {
            lock.unlock();
            return;
        }
        if (nest[index].getLruNext() == nest[index].getLruPre()) {// ==-1
            nest[index].setLruNext(lruHead);
            nest[lruHead].setLruPre(index);
            lruHead = index;
            lock.unlock();
            return;
        }
        if (lruTail == index) {
            lruTail = nest[index].getLruPre();
            nest[index].setLruNext(lruHead);
            nest[index].setLruPre(-1);
            nest[lruTail].setLruNext(-1);
            nest[lruHead].setLruPre(index);
            lruHead = index;
            lock.unlock();
            return;
        }
        nest[nest[index].getLruPre()].setLruNext(nest[index].getLruNext());
        nest[nest[index].getLruNext()].setLruPre(nest[index].getLruPre());
        nest[index].setLruNext(lruHead);
        nest[index].setLruPre(-1);
        nest[lruHead].setLruPre(index);
        lruHead = index;
        lock.unlock();
        return;
    }

    public void adjDirty(NewNest nn) {
        lock.lock();
//        if (!nn.getDirty()) {
//            dirtyNum++;
            nn.setDirty(true);
//        }
        lock.unlock();
    }

    public void scanNestDirty(List<NewNest> dirtyList) {
        lock.lock();
        for (int i = 0; i < nest.length; i++) {
            if (nest[i] != null && nest[i].getDirty()) {
                dirtyList.add(nest[i]);
//                nest[i].setDirty(false);
                dirtyNum--;
            }
        }
        lock.unlock();
    }

    public void releaseNest(int releaseThresHold) {

        int count = 0;
        System.out.println(sm + " will be release" + releaseThresHold);
        if (lruTail == -1) {
            return;
        }

        while (count++ < releaseThresHold && lruTail != lruHead) {
//            System.out.print(lruTail + ",");
            
            if(nest[lruTail].getDirty()){//FIXME 并发可能还是有问题
                FlushScanner.getInstance().flushNewNest(sm, nest[lruTail]);
            }
            lock.lock();
            nest[lruTail].setKvEntry(null);
            loadedNum--;
            lruTail = nest[lruTail].getLruPre();
            nest[nest[lruTail].getLruNext()].setLruPre(-1);

            nest[lruTail].setLruNext(-1);
            lock.unlock();

        }
        
    }

    public int getLoadedNum() {
        return loadedNum;
    }

    public int getDirtyNum() {
        return dirtyNum;
    }

}
