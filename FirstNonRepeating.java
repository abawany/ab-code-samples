import java.util.HashMap;
import java.util.Map;

class FirstNonRepeating {
  /**
   * Iterate over the input string and store all found characters
   * uniquely in map with count of times found. Iterate over string again
   * and lookup char in map - if count==1, it is the first non-repeating
   * character
   * @param in
   * @return
   */
  public static char getFirstNonRepeating(String in) {
    char rtn=0;
    Map<Character, Integer> map=new HashMap<Character, Integer>();

    for (char a: in.toCharArray()) {
      if (map.containsKey(a)) { // map contained it, increment value
        Integer x=map.get(a);
        x=x+1;
        map.put(a, x);
      }
      else { // first time this char found
        map.put(a, Integer.valueOf(1));
      }
    }

    for (char a: in.toCharArray()) {
      Integer x=map.get(a);
      if (x.equals(1)) {
        rtn=a;
        break;
      }
    }

    return rtn;
  }

  public static void main(String[] sArgs) {
    String in="character";
    System.out.println("got " + FirstNonRepeating.getFirstNonRepeating(in)
            + " for " + in);
    in="abcdefg";
    System.out.println("got " + FirstNonRepeating.getFirstNonRepeating(in)
            + " for " + in);
  }
}