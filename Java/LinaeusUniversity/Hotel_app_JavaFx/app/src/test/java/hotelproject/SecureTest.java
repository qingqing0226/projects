/*
 * Secure test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.jupiter.api.Test;  // junit 5
// same as import org.junit.Test;  // junit 4

import static org.junit.jupiter.api.Assertions.assertEquals;  // junit 5
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SecureTest {
     // method: hash()
     // it tests if the hash function works
     @Test
     public void testHash() {
         String pass1 = "1234";
         String pass2 = "1234";
         assertNotEquals(pass1, pass2, "they are the same objects");
         assertEquals(Secure.hash(pass1), Secure.hash(pass2), "pass1 and pass2 are not equal");
     }
    
}
