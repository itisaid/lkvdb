package bigbang.storage.mem;

import bigbang.storage.Thread.FlushScanner;
import bigbang.storage.Thread.ThreadManager;
import bigbang.storage.model.KVEntry;
import bigbang.storage.model.Parameter;
import bigbang.storage.util.MD5Util;

public class MemManager {

    SegmentManager[]          seg      = new SegmentManager[Parameter.segSize];

    // FlushScanner fb = new FlushScanner();
    private ThreadManager     tm;

    private static MemManager instance = new MemManager();

    private MemManager() {
    }

    public static MemManager getInstance() {
        return instance;
    }

    public void init() {
        for (int i = 0; i < Parameter.segSize; i++) {
            seg[i] = new SegmentManager(i);
            seg[i].init();
            // fb.addSegment(seg[i]);

        }
        tm = new ThreadManager(seg);
        tm.initScan();

    }

    private int makeHashcode(String key) {
        int hashCode = MD5Util.md5HashCode(key);
        hashCode += hashCode / (Parameter.bigSize * Parameter.segSize);
        return hashCode > 0 ? hashCode : -hashCode;
    }

    private int getSegIndex(int hashCode) {
        int index = (hashCode / Parameter.bigSize) % Parameter.segSize;
        // System.out.print(index+",");
        return index;
    }

    public KVEntry getKV(String key) {
        int hashCode = makeHashcode(key);
        return seg[getSegIndex(hashCode)].getKV(key, hashCode);
    }

    public void setKV(String key, String value) {
        int hashCode = makeHashcode(key);
        seg[getSegIndex(hashCode)].setKV(key, value, hashCode);
    }

    /**
     * data will be lost after close method was called.
     */
    public void close() {
        // fb.flushAllNewNest();
        tm.finishScan();
//        for (int i = 0; i < Parameter.segSize; i++) {
//            seg[i].close();
//        }

    }
}
