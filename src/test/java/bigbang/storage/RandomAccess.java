package bigbang.storage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        n();
    }
    
    public static void m(){

        try {
            RandomAccessFile ra = new RandomAccessFile("/tmp/myfiles.dat", "rw");
            byte[] b = new byte[100];
            for (int i = 0; i < 100; i++) {
                b[i] = 0x01;
            }
            ra.write(b, 20, 50);
            System.out.println(ra.getFilePointer());
            byte[] a = new byte[100];
            ra.seek(48);
            ra.read(a, 10, 4);
            for (int i = 0; i < 100; i++) {
                System.out.println(i + ":" + a[i]);
            }

            ra.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    
    }

    public static void n() throws IOException {
        File file = new File("/tmp/abb.a");
        file.createNewFile();
    }

}
