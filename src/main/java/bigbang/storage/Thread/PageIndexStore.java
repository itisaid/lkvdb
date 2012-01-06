package bigbang.storage.Thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import bigbang.storage.model.Parameter;
import bigbang.storage.util.HexUtil;

public class PageIndexStore {

    private String pageFile;

    public PageIndexStore(int num) {
        pageFile = "/tmp/page" + num + ".ind";
    }

    public int[] loadPageIndex() {
        File ind = new File(pageFile);
        if (!ind.exists()) {
            System.out.println("Store first run.");
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(ind);
            byte[] b = new byte[4 * Parameter.bigSize];
            fis.read(b);
            int[] r = new int[Parameter.bigSize];
            int k = 0;
            int m = 0;
            byte[] br = new byte[4];
            for (int i = 0; i < 4 * Parameter.bigSize; i++) {
                br[k] = b[i];
                if (k == 3) {
                    r[m++] = HexUtil.byte2int4(br);
                    k = 0;
                } else {
                    k++;
                }

            }
            fis.close();
            ind.delete();
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void flushIndex(int[] b) {
        File ind = new File(pageFile);
        if (ind.exists()) {
            System.out.println("page index file has existed. It's error");
            return;
        }
        try {
            ind.createNewFile();
            FileOutputStream fos = new FileOutputStream(ind);
            byte[] ob = new byte[b.length * 4];
            int c = 0;
            for (int i = 0; i < b.length; i++) {
                byte[] tb = HexUtil.int2byte4(b[i]);
                for (int j = 0; j < 4; j++) {
                    ob[c++] = tb[j];
                }
            }
            fos.write(ob);
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
