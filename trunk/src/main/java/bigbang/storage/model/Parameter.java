package bigbang.storage.model;


public class Parameter {

    public static final int pageSize   = 10*1024;   // 1KB
    public static final int fileSize   = 1 << 25; // 32MB
    public static final int bigSize    = 100000;
    public static final int segSize    = 100;
    public static final int loadTop    = 1000;   // per segment
    public static final int loadBottom = 500;    // per segment
    public static final int dirtyLimit = 300;    // //per segment
    public static final int threadNum  = 20;      // back scan thread number
}
