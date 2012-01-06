package bigbang.storage.Thread;

import java.util.List;

import bigbang.storage.mem.SegmentManager;
import bigbang.storage.model.Parameter;

public class ScanThread extends Thread {

    List<SegmentManager> smList;
    long                 recentlyScanTime;
    int                  releaseThreathHold = 1;
    boolean              scanTag            = false;
    boolean              go                 = true;
    FlushScanner         fs                 = FlushScanner.getInstance();

    public ScanThread(List<SegmentManager> smList) {
        this.smList = smList;
        this.start();
    }

    public void run() {
        boolean on = true;// execute 1 times after go = false;
        while (go || on) {
            System.out.println(this+" dirty..."+smList.get(0).getAim().getDirtyNum());
            if (scanTag) {
                try {
                    System.out.println(this+" sleepping...");
                    sleep(1000);// nothing to do, wait 1s.
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            scanTag = true;
            boolean hasDirty = true;
            long currentTime = System.currentTimeMillis();
            if (  currentTime - recentlyScanTime > 60 * 1000) {
                System.out.println(this+" scan dirty...");
                scanDirty();
                hasDirty = false;
            }
            // scan and release could not be concurrent executed, keep it by logic
            // in fact scan dirty must execute before release
            if (smList.get(0).getAim().getDirtyNum() > Parameter.dirtyLimit) {
                System.out.println(this+" scan dirty..."+smList.get(0).getAim().getDirtyNum());
                scanDirty();
                hasDirty = false;
            }
            if (go) {
                System.out.println("load:"+smList.get(0).getAim().getLoadedNum());
                if (smList.get(0).getAim().getLoadedNum() > Parameter.loadTop) {
                    if (hasDirty) {
                        scanDirty();
                    }
                    release(smList.get(0).getAim().getLoadedNum() - Parameter.loadBottom);
                }
            }else{
                close();
                on = false;
            }
        }
    }

    public void scanDirty() {
//        scanTag = false;
//        recentlyScanTime = System.currentTimeMillis();
//        for (SegmentManager sm : smList) {
//            fs.scanSegmentDirty(sm);
//        }
    }

    public void release(int releaseSize) {
        scanTag = false;
        recentlyScanTime = System.currentTimeMillis();
        for (SegmentManager sm : smList) {
            fs.scanRelaseSegment(sm, releaseSize);
        }
    }
    
    public void close(){
//        scanDirty();
        release(1<<31);
        for (SegmentManager sm : smList) {
            fs.flushIndex(sm);
        }
    }

    public void finish() {
        go = false;
    }
}
