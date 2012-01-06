package bigbang.storage.model;

import java.util.ArrayList;
import java.util.List;

import bigbang.storage.util.HexUtil;

public class PersistenceFormate {

    // temp,一页写完
    public static byte[] formateKVEntry(KVEntry entry) {
        List<byte[]> l = new ArrayList<byte[]>();
        if (entry == null) {
            return null;
        }

        do {
            byte[] keyb = entry.key.getBytes();
            byte[] valueb = entry.value.getBytes();
            l.add(keyb);
            l.add(valueb);
            entry = entry.next;

        } while (entry != null);

        int size = 2 + 4;// magic code+size
        for (byte[] b : l) {
            size += 4;// key || value length
            size += b.length;
        }
        // int fd = size/Parameter.pageSize+1;
        // size += fd*2;
        byte[] r = new byte[size];
        r[0] = 12;
        r[1] = 34;
        byte[] s = HexUtil.int2byte4(size);
        for (int i = 0; i < 4; i++) {
            r[i + 2] = s[i];
        }
        int cursor = 6;
        for (byte[] b : l) {
            byte[] ls = HexUtil.int2byte4(b.length);
            for (int i = 0; i < 4; i++) {
                r[cursor++] = ls[i];
            }
            for (int j = 0; j < b.length; j++) {
                r[cursor++] = b[j];
            }
        }
        return r;
    }

    public static KVEntry formateByte(byte[] b) {
        if (b[0] != 12 || b[1] != 34) {
            throw new RuntimeException("page bytes's magic code error.");
        }

        int cursor = 2;
        byte[] s = new byte[4];
        for (int i = 0; i < 4; i++) {
            s[i] = b[cursor++];
        }
        List<KVEntry> r = new ArrayList<KVEntry>();
        int kvs = 0;
        while (cursor < HexUtil.byte2int4(s)) {
            byte[] bl = new byte[4];
            for (int i = 0; i < 4; i++) {
                bl[i] = b[cursor++];
            }
            int kvl = HexUtil.byte2int4(bl);
            byte[] kvb = new byte[kvl];
            for (int j = 0; j < kvl; j++) {
                try {
                    kvb[j] = b[cursor++];
                } catch (RuntimeException e) {
                        for (int bi = 0; bi < b.length; bi++) {
                            System.out.print(b[bi]);
                    }
                }
            }
            if (kvs == 0) {
                KVEntry kve = new KVEntry();
                kve.setKey(new String(kvb));
                r.add(kve);
                kvs++;
            } else {
                KVEntry kve = r.get(r.size() - 1);
                kve.setValue(new String(kvb));
                kvs = 0;
            }
        }
        if (r.size() == 1) {
            return r.get(0);
        }
        for (int k = 0; k < r.size() - 1; k++) {
            r.get(k).setNext(r.get(k + 1));
        }
        return r.get(0);

    }

}
