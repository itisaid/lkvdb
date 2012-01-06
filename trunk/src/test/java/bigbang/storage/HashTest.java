package bigbang.storage;

import bigbang.storage.model.Parameter;
import bigbang.storage.util.MD5Util;


public class HashTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println(MD5Util.md5("k381732").hashCode());
//        System.out.println("k765571".hashCode());
        int hashCode = MD5Util.md5HashCode("k12200");
        hashCode+=hashCode/(Parameter.bigSize*Parameter.segSize);
        System.out.println(hashCode);
        

    }

}
