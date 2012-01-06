package bigbang.storage.disk;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class SequenceWrite {

    public static void main(String[] args) throws Exception {
        //BufferedOutputStream osw = new BufferedOutputStream(new FileOutputStream("/tmp/hello"));
        FileOutputStream osw = new FileOutputStream("/tmp/hello");
        byte[] b = ByteUtil.getBytes();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 25500; i++) {
            osw.write(b, 0, b.length);
        }
        osw.flush();
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - begin));
    }
}
