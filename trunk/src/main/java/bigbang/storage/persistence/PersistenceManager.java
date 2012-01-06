package bigbang.storage.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import bigbang.storage.model.Parameter;

public class PersistenceManager {

    private String path;

    public PersistenceManager(int num) {
        path="/tmp/bb_"+num+".dat";
        init();
    }



    RandomAccessFile raf;

    public void init(){
        File f = new File(path);
        if(!f.exists()){
            create();
        }else{
            try {
                raf = new RandomAccessFile(path, "rw");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void create() {
        MappedByteBuffer mbb;
        FileChannel fc;
        
        try {
            raf = new RandomAccessFile(path, "rw");
            fc = raf.getChannel();
            mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, Parameter.fileSize);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] readPage(int page) {

        byte[] b = new byte[Parameter.pageSize];
        try {
            raf.seek(calculatePos(page));
            raf.read(b, 0, Parameter.pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;

    }

    public void writePage(byte[] b, int page) {

        try {
            raf.seek(calculatePos(page));
            raf.write(b, 0, b.length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int calculatePos(int page) {
        return page * Parameter.pageSize;//TODO,pos is not begin from 0;
    }
    
    public String getPath(){
        return this.path;
    }
}
