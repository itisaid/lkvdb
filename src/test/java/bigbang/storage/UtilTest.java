package bigbang.storage;

import bigbang.storage.util.HexUtil;
import junit.framework.TestCase;


public class UtilTest extends TestCase {

    public void testInt2byte4(){
        byte[] b = HexUtil.int2byte4(255);
        assertTrue(b[0]==-1);
        assertTrue(b[1]==0);
        assertTrue(b[2]==0);
        assertTrue(b[3]==0);
    }
    
    public void testByte2(){
        int i=65523;
        byte[] b = HexUtil.int2byte2(i);
        int x = HexUtil.byte2int2(b);
        assertEquals(i,x);
    }
    
    public void testByte4(){
        int i=1265523;
        byte[] b = HexUtil.int2byte4(i);
        int x = HexUtil.byte2int4(b);
        assertEquals(i,x);
    }
    
    public void testException(){
        int i=32365523;
        try{
        byte[] b = HexUtil.int2byte2(i);
        int x = HexUtil.byte2int2(b);
        fail();
        }catch(Exception e){
           
        }
     
    }
    
    public static void main(String args[]){
        System.out.println(HexUtil.int2byte2(9));
    }
}
