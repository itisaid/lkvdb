package bigbang.storage.util;


public class HexUtil {

    private HexUtil(){}
    
    public static byte[] int2byte4(int i){
        byte[] r = new byte[4];
        r[0] = (byte) (0xff & i);
        r[1] = (byte) ((0xff00 & i) >> 8);
        r[2] = (byte) ((0xff0000 & i) >> 16);
        r[3] = (byte) ((0xff000000 & i) >> 24);
        return r;
    }
    
    public static byte[] int2byte2(int i){
        if(i>65533){
            throw new IllegalArgumentException("int more than 65535,can't be convert to 2 bytes");
        }
        byte[] r = new byte[2];
        r[0] = (byte) (0xff & i);
        r[1] = (byte) ((0xff00 & i) >> 8);
        return r;
    }
    
    public static int byte2int4(byte[] b){
        int r = b[0] & 0xff;
        r |= ((b[1] << 8) & 0xff00);
        r |= ((b[2] << 16) & 0xff0000);
        r |= ((b[3] << 24) & 0xff000000);
        return r;
    }
    
    public static int byte2int2(byte[] b){
        if(b.length>2){
            throw new IllegalArgumentException("bytes more than 2");
        }
        int r = b[0] & 0xff;
        r |= ((b[1] << 8) & 0xff00);
        return r;
    }
}
