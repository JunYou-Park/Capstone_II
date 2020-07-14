package blog.practice.capstone_ii.Util;

public class StringUtility {

    // 난수 생성
    public static String create_str(String pattern, String str) {
        double dValue = Math.random();
        char cValue = (char)((dValue * 94) + 33);
        str += cValue;
        str = str.replaceAll(pattern, "");
        if(str.length()<15) {
            return create_str(pattern,str);
        }
        return str;
    }

    public static String create_int(String str) {
        double dValue = Math.random();
        char cValue = (char)((dValue * 10) + 48);
        str += cValue;
        if(str.length()<5) {
            return create_int(str);
        }
        return str;
    }

    public static String[] return_arr(String str, String regexp){
        String strArr[] = str.split(regexp);
        return strArr;
    }

}
