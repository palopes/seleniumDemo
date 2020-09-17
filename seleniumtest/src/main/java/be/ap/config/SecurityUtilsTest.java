package be.ap.config;

import org.testng.annotations.Test;


public class SecurityUtilsTest {


    @Test
    public void encryptAndPrint() throws java.lang.SecurityException, SecurityException {
        System.out.println(SecurityUtils.encrypt("GB142753$"));
        System.out.println(SecurityUtils.encrypt("DV741951$"));
    }

    @Test
    public void decryptAndPrint() throws java.lang.SecurityException, SecurityException {
        System.out.println(SecurityUtils.decrypt("kUPLOOi89MPIHaSY3R0yk2zHmlTwvp5oKyvidZ9m42o="));
        System.out.println(SecurityUtils.decrypt("67Y1j2tbG85+go8uI6BNw7HCnJ7tZl58sQYXDg9rxLA="));
    }
}
