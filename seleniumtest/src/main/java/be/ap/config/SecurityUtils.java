package be.ap.config;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public enum SecurityUtils {
    ;

    private static final int KEY_SIZE = 16;
    private static final int INITIALIZATION_VECTOR_SIZE = 16;

    private static final String DIGEST_ALGORYTHM = "SHA-256";
    private static final String SECRET_KEY_SPEC_ALGORYTHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final String DEFAULT_DIGEST_INPUT = "UjM1dXJyM2M3MTBuRjMkNw==";

    private static SecretKeySpec getSecretKeySpec(final String digestInput) throws SecurityException {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(DIGEST_ALGORYTHM);
            messageDigest.update(Base64.getDecoder().decode(digestInput));
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException(String.format("Invalid algorythm %s", DIGEST_ALGORYTHM), e);
        } catch (final IllegalArgumentException e) {
            throw new SecurityException(String.format("Invalid digest input %s", digestInput), e);
        }

        final byte[] keyBytes = new byte[KEY_SIZE];
        System.arraycopy(messageDigest.digest(), 0, keyBytes, 0, KEY_SIZE);

        return new SecretKeySpec(keyBytes, SECRET_KEY_SPEC_ALGORYTHM);
    }

    private static byte[] applyCipher(final int cipherMode, final SecretKeySpec secretKeySpec, final IvParameterSpec ivParameterSpec, final byte[] bytes) throws SecurityException {
        final byte[] cipherBytes;

        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(cipherMode, secretKeySpec, ivParameterSpec);
            cipherBytes = cipher.doFinal(bytes);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new SecurityException(e);
        }

        return cipherBytes;
    }

    public static String encrypt(final String plainText) throws SecurityException {
        return encrypt(plainText, DEFAULT_DIGEST_INPUT);
    }

    public static String encrypt(final String plainText, final String digestInput) throws SecurityException {
        final byte[] iv = new byte[INITIALIZATION_VECTOR_SIZE];
        SECURE_RANDOM.nextBytes(iv);

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        final SecretKeySpec secretKeySpec = getSecretKeySpec(digestInput);
        final byte[] encryptedBytes = applyCipher(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec, plainText.getBytes(ISO_8859_1));

        final byte[] encryptedTextIV = new byte[INITIALIZATION_VECTOR_SIZE + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedTextIV, 0, INITIALIZATION_VECTOR_SIZE);
        System.arraycopy(encryptedBytes, 0, encryptedTextIV, INITIALIZATION_VECTOR_SIZE, encryptedBytes.length);

        return new String(Base64.getEncoder().encode(encryptedTextIV), ISO_8859_1);
    }

    public static String decrypt(final String encryptedIvTextBytes) throws SecurityException {
        return decrypt(encryptedIvTextBytes, DEFAULT_DIGEST_INPUT);
    }

    public static String decrypt(final String encryptedText, final String digestInput) throws SecurityException {
        final byte[] encryptedTextIV;
        try {
            encryptedTextIV = Base64.getDecoder().decode(encryptedText);
            if (encryptedTextIV.length < INITIALIZATION_VECTOR_SIZE) {
                throw new IllegalArgumentException(String.format("%s is Base64 compliant but does not respect implemented policy", encryptedText));
            }
        } catch (final IllegalArgumentException e) {
            throw new SecurityException(String.format("Invalid encrypted text %s", encryptedText), e);
        }

        final byte[] iv = new byte[INITIALIZATION_VECTOR_SIZE];
        System.arraycopy(encryptedTextIV, 0, iv, 0, INITIALIZATION_VECTOR_SIZE);
       
        final int encryptedBytesSize = encryptedTextIV.length - INITIALIZATION_VECTOR_SIZE;
        final byte[] encryptedBytes = new byte[encryptedBytesSize];
        System.arraycopy(encryptedTextIV, INITIALIZATION_VECTOR_SIZE, encryptedBytes, 0, encryptedBytesSize);

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        final SecretKeySpec secretKeySpec = getSecretKeySpec(digestInput);
        final byte[] decryptedBytes = applyCipher(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec, encryptedBytes);
        return new String(decryptedBytes, ISO_8859_1);
    }

}
