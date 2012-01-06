package bigbang.storage.persistence;

import bigbang.storage.model.Parameter;

/**
 * 磁盘页管理器，管理空闲和在用页面<br>
 * 最后面的页保留，不当作空闲页返回（避免边界条件检查）<br>
 * 一个二维数组实现两个双向链表
 * 
 * @author frank
 */
public class PageManager {

    int     pageLength = Parameter.fileSize / Parameter.pageSize;

    int[][] page       = new int[2][pageLength];                 // 2d array store double link table. 0:next 1:pre
    int     freeIndex  = 0;                                      // free head
    int     usedIndex  = -1;                                     // used head

    public PageManager(int[] pageIndex) {

        if (pageLength < 5) {
            throw new RuntimeException("too few pages:" + pageLength);
        }
        for (int i = 0; i < page[0].length - 1; i++) {
            page[0][i] = i + 1;
            page[1][i] = i - 1;
        }
        page[0][page[0].length - 1] = -1;
        page[1][page[0].length - 1] = page[0].length - 2;
        if (pageIndex != null) {
            for (int j = 0; j < pageIndex.length; j++) {
                if (pageIndex[j] != -1) {
                    if (pageIndex[j] < pageLength) {
                        page[1][page[0][pageIndex[j]]] = page[1][pageIndex[j]];// next pre= this pre
                    }
                    if (pageIndex[j] != freeIndex) {
                        page[0][page[1][pageIndex[j]]] = page[0][pageIndex[j]];// pre next=this next
                    }
                    if (freeIndex == pageIndex[j]) {
                        freeIndex = page[0][pageIndex[j]];// modify free head
                    }

                    if (usedIndex == -1) {
                        page[0][pageIndex[j]] = -1;
                        page[1][pageIndex[j]] = -1;
                        usedIndex = pageIndex[j];
                    } else {// add to head of used table
                        page[0][pageIndex[j]] = usedIndex;
                        page[1][usedIndex] = pageIndex[j];
                        page[1][pageIndex[j]] = -1;
                    }
                }
            }
        }
    }

    /**
     * get free page from free table, before return, remove from free table and add to used table<br>
     * needn't lock
     * @return
     */
    public int getFreePage() {
        if (freeIndex == pageLength - 1) {
            throw new RuntimeException("no free page in disk");
        }
        int freePage = freeIndex;
        freeIndex = page[0][freePage];

        page[1][freePage] = -1;
        if (usedIndex == -1) {
            page[0][freePage] = -1;
        } else {
            page[0][freePage] = usedIndex;
            page[1][usedIndex] = freePage;
        }
        usedIndex = freePage;

        page[1][freeIndex] = -1;

        return freePage;
    }

    public void returnFreePage(int p) {
        // now , p in used table
        if (usedIndex == p) {
            usedIndex = page[0][p];
        }
        if (page[0][p] != -1) {
            page[1][page[0][p]] = page[1][p];
        }
        if (page[1][p] != -1) {
            page[0][page[1][p]] = page[0][p];
        }

        page[1][freeIndex] = p;
        page[0][p] = freeIndex;
        page[1][p] = -1;
        freeIndex = p;
    }
}
