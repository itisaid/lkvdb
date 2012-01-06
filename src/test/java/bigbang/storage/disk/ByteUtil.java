package bigbang.storage.disk;


public class ByteUtil {

    private static final int s = 10240;
    
    public static byte[] getBytes(){
        byte[] a = new byte[]{1,2,3,4,5,6,7,8,9,0,'a','b','c','d','e','f'};
        byte[] b = new byte[s];
        for(int i=0;i<s;i++){
            b[i] = a[(int)(15*Math.random())];
        }
        return b;
    }
}
