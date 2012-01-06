package bigbang.storage.util;

public class HashCodeUtil {

    /**
     * 冲突不多，散列不够。。。
     * @param s
     * @return
     */
    public static int times33(String s) {
        int hashCode = 0;
        int len = s.length();
        for (int i = 0; i < len ; i++) {
            hashCode += (hashCode * 33 + s.codePointAt(i)) & 0x7fffffff;
        }
        return hashCode;
    }
    
    public static void main(String args[]){
        System.out.println(HashCodeUtil.times33("2faewfdsfsf"));
    }
}
