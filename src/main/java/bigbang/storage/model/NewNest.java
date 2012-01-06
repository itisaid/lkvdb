package bigbang.storage.model;

/**
 * 注意和AttachIndexManager有死锁
 * @author frank
 *
 */
public class NewNest {

    KVEntry kvEntry;
    int     page    = -1;
    int     lruPre  = -1;
    int     lruNext = -1;   // lru is 2D link table, scan & release pointer in one synchronized block.
    boolean dirty   = false; // dirty is a tag, release lock after scanned. time exchange space with boolean.
//    int accessCounter=0;//replace lru pointer. (use basic int or atomicInteger?)

    
    public NewNest() {

    }
    
    
//    
//    public synchronized void accessCounterPlusPlus(){
//        accessCounter++;
//    }
//    
//    public synchronized void clearAccessCounter(){
//        accessCounter=0;
//    }

    public  KVEntry getKvEntry() {
        return kvEntry;
    }

    public  void setKvEntry(KVEntry kvEntry) {
        this.kvEntry = kvEntry;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLruPre() {
        return lruPre;
    }

    public void setLruPre(int lruPre) {
        this.lruPre = lruPre;
    }

    public int getLruNext() {
        return lruNext;
    }

    public void setLruNext(int lruNext) {
        this.lruNext = lruNext;
    }

    public boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean dirtyNext) {
        this.dirty = dirtyNext;
    }

    
//    public int getAccessCounter() {
//        return accessCounter;
//    }
    
    
}
