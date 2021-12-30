package by.boginsky.util;

public class Utils {
    public static boolean isAllPositiveNumbers(String ... str){
        for (String var : str){
            if(!new StringUtils().isPositiveNumber(var)){
                return false;
            }
        }
        return true;
    }
}
