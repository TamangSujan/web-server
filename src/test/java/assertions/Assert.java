package assertions;

public class Assert {
    public static void isEqual(String real, String actual){
        if(!real.equals(actual))
            throw new AssertException(actual + "is not equal to "+ real);
    }
    public static void print(String value){
        System.err.println(value);
    }
}
