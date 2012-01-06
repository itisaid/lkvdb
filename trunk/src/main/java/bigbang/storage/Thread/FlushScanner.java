package bigbang.storage.Thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bigbang.storage.mem.SegmentManager;
import bigbang.storage.model.NewNest;
import bigbang.storage.model.Parameter;
import bigbang.storage.model.PersistenceFormate;
import bigbang.storage.persistence.PageManager;

/**
 * 内存全部flush
 * 
 * @author frank
 */
public class FlushScanner {

    private static FlushScanner instance = new FlushScanner();

    // private List<SegmentManager> smList = new ArrayList<SegmentManager>();

    public FlushScanner() {
    }

    // public void addSegment(SegmentManager sm) {
    // this.smList.add(sm);
    // }

    public static FlushScanner getInstance() {
        return instance;
    }

    public void flushIndex(SegmentManager sm) {
        NewNest[] nest = sm.getNest();
        int[] i = new int[Parameter.bigSize];
        for (int k = 0; k < i.length; k++) {
            if (nest[k] != null) {
                i[k] = nest[k].getPage();
            } else {
                i[k] = -1;
            }
        }
        sm.getPagei().flushIndex(i);
    }

    public void flushNewNest(SegmentManager sm, NewNest nn) {
        PageManager pm = sm.getPagem();

//        synchronized (nn) {
            int freePage = pm.getFreePage();
            byte[] b = PersistenceFormate.formateKVEntry(nn.getKvEntry());// TODO nn.getKvEntry!=null 4 robust
            if (b != null && b.length > 0) {
                sm.getPersm().writePage(b, freePage);
                // System.out.println(nn.getKvEntry().getKey()+" is flush to "+sm.getPersm().getPath()+" at page "+freePage);
                int oldPage = nn.getPage();
                if (oldPage != -1) {
                    pm.returnFreePage(oldPage);
                }
                nn.setPage(freePage);
                nn.setDirty(false);
            } else {
                pm.returnFreePage(freePage);
            }
//        }

    }

    // public void flushAllNewNest() {
    // for (SegmentManager sm : smList) {
    // NewNest[] nn = sm.getNest();
    // for (int j = 0; j < nn.length; j++) {
    // if (nn[j] != null && nn[j].getDirty()) {
    // flushNewNest(sm, nn[j]);
    // }
    // }
    // }
    // }

    public void scanSegmentDirty(SegmentManager sm) {
        List<NewNest> dirtyList = new LinkedList<NewNest>();
        sm.getAim().scanNestDirty(dirtyList);
        for (NewNest nn : dirtyList) {
            flushNewNest(sm, nn);
        }
    }

    public void scanRelaseSegment(SegmentManager sm, int releaseThreathHold) {
        sm.getAim().releaseNest(releaseThreathHold);
    }
}
