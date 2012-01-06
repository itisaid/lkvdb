package bigbang.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CreateFile {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MappedByteBuffer mbb;
        FileChannel fc;
        int length = 1 << 29; // 512 MB
        System.out.println(length);
        try {
            fc = new RandomAccessFile("/tmp/myfilee.dat", "rw").getChannel();
            mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
            fc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
