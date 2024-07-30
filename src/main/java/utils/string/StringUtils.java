package utils.string;

public class StringUtils {
    public static int getCharCount(String string, char c){
        int count = 0;
        for(int index=0; index<string.length(); index++){
            if(c == string.charAt(index))
                count++;
        }
        return count;
    }
}
