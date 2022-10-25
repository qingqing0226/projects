package hotelproject;

import java.util.Objects;

public class Secure {

  public static String hash(String string) {
    int hash = Objects.hashCode(string);
    Long complexHash = (long) hash;
    complexHash = complexHash * complexHash;
    String hashed = Long.toString(complexHash);
    return hashed;
  }

}
