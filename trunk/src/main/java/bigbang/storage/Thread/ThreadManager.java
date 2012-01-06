package bigbang.storage.Thread;

import java.util.ArrayList;
import java.util.List;

import bigbang.storage.mem.SegmentManager;
import bigbang.storage.model.Parameter;

public class ThreadManager {

    SegmentManager[] smList;
    ScanThread       st[];

    public ThreadManager(SegmentManager[] smList) {
        this.smList = smList;
    }

    public void initScan() {
        st = new ScanThread[Parameter.threadNum];
        List<SegmentManager>[] segList = new List[Parameter.threadNum];
        for (int i = 0; i < Parameter.threadNum; i++) {
            segList[i] = new ArrayList<SegmentManager>();
        }
        int cur = 0;
        for (int i = 0; i < smList.length; i++) {
            segList[cur++].add(smList[i]);
            if (cur == Parameter.threadNum) {
                cur = 0;
            }
        }
        for (int i = 0; i < Parameter.threadNum; i++) {
            st[i] = new ScanThread(segList[i]);
        }
    }

    public void finishScan() {
        for (int i = 0; i < st.length; i++) {
            st[i].finish();
        }
    }

}
