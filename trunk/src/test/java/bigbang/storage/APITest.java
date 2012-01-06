package bigbang.storage;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import bigbang.storage.api.Storage;

public class APITest extends TestCase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // n();
         m();
//        k();
    }

    public static void n() {
        int k = 1000;
        int m = 5000;
        Map<String, String> map = new HashMap<String, String>();
        long b = System.currentTimeMillis();
        for (int j = 0; j < k; j++) {
            for (int i = 0; i < m; i++) {
                map.put("k" + i, "v" + i + j);
                map.get("k" + i);
                // System.out.println(s.get("k1"));
            }
        }
        long e = System.currentTimeMillis();
        System.out.println(k * m + " times hashmap set spend:" + (e - b));
    }

    public static void m() {
        long b1 = System.currentTimeMillis();
        Storage s = new Storage();
        s.init();
        int k = 50;
        int m = 1000;
        long b2 = System.currentTimeMillis();
        for (int j = 0; j < k; j++) {
            long b = System.currentTimeMillis();
            for (int i = 0; i < m; i++) {
                s.set("k" + i + j, "v" + i + j);
                // s.get("k"+i+j);
//                 System.out.println(s.get("k" + i + j));
            }
            long e = System.currentTimeMillis();
            System.out.println(j+":"+ m + " times bigbang set spend:" + (e - b));
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
        long e2 = System.currentTimeMillis();
        System.out.println(k*m+" times set spend:" + (e2 - b2));  
        s.close();
        long e1 = System.currentTimeMillis();
        System.out.println("all spend:" + (e1 - b1));
    }

    public static void k() {

        Storage s = new Storage();
        s.init();
        int k = 50;
        int m = 1000;
        long b = System.currentTimeMillis();
        for (int j = 0; j < k; j++) {
            for (int i = 0; i < m; i++) {
                // s.set("k"+i, "v"+i+j);
                // System.out.print(s.get("k"+i));
                try {
                    if (!s.get("k" + i + j).equals("v" + i + j)) System.err.println("-----------" + s.get("k1"));
                    System.out.println("k" + i + j+"==="+s.get("k" + i + j));
                    
                } catch (Throwable t) {

                    System.out.println(i + ":" + j+"---"+s.get("k" + i + j));

                }
            }
        }
        long e = System.currentTimeMillis();
        System.out.println(k * m + " times bigbang get spend ms:" + (e - b));
        s.close();

    }

}
