package com.example.responsefromiseries;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

//    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
//    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";


    private static String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCS3pY8NbDF+CwrG8x12L7YqDHFbolgTXRATXBl6DG6v/3NFM/M0oudo6+QxMENmAtL+K/vSrF17JdA+e/eTv8na59sWTeo77GChTqqXXvJVPaFJQ9xYUFCI4akh47zfSQu8DAgzGbX55odFmH3hPiHUf96TXbnilBB7G0Mmp4BCYK+OWaeRxzrF5LF1fSvKoDG4B9fOrjVi6ffZSPf7ohRq3aGMg1jirXLnarLCw9dQ8CwHfHB0+Gn2ss2+7Wtqw+kSZE+B777rezXeI+tj43laIHc4Gyqgm4TmVLPgcrlz+yScw+1j4YKofG3MsHy0KEqKpJqYigtM/S6nuBq5FI5AgMBAAECggEAZbzW6UJv/9MYFYyeJ4xLN53bTF5zv0goHVsHXzBnMmxIjcW65LF8kWiP94K0EIygVrIgfWyKXZvUcugv5xll2pLMzqtGyTmxBy7vIpC7bQ3z7utrNSkfoYcmf9Z/id/ILtOnIzE0lJ8UvM2Cc4mi92ovxTLgCShgzCnQqIh6p1Au/tDwaU7dloWU0SIOciF3IiNFMfnLonP0EtbcwqkUl+Y42Q8T0hMONHzTFmUas3+C/NLnGe3B3fcZ8ykPuz/6XD0GNqxmfuCZtQEEFTyBA3sTc1ZRMzi/LFRO0YBlE4Y6IP9P9l8EkREdsJOo919jUxus0YkR/DxGb7gR6RswGQKBgQD9Ry+WCzBX79n6vXl5Gxxo369V1jzWQbOx0OPJi8o/YGnVqcZk44htYM0hW3qv5hywBcGc55fUmzdDXA8jyCAx0HS0sqVjs4RAM/3pulUNGGbyPzW5bLlYG4mienFOMd0SHxsgsNu3Gw66Ul+pNrwh5rx0E4s82Ldk0YrRwktQwwKBgQCUcqbMSZr6DElQw0RiSryxVOamNI8O3Wx9ZlsZm1ReDKKDgiPsyxTySh0g2+kxR0x4udiU+YPWN5difVVTlXKQhtE6OLOV66G0AWA6nZ0bBESXpL1Laxy4sZCz9wKT+zIlx+jIhFdygClELHAoM8ta7DvroFmw0tJT6U/0+7AhUwKBgQDonCEi/yGmEX0zYCKYzAJaetzPy5NR/0NmUn9xUbYsYIKynVq8u49HTPj34skZlQ/RxWecj8IMm4S0k27AIQMYXOdiMRPeLjeOs3FF4EBOZ5PcdZsEscrDFX9hPmyjMP5krza93k/bqbjr+80pXkCILh3zySM+fZZovF1u6a87+wKBgQCB+wHhznFMkIWBmfi+uAIQo+5xiYPZ2DeudXk/KqrsztNdow4hwJBOtmZa5zOAWpWy/8eMag3t/C8ppeSYkzsFIYcCG7PjibbFDo9bw7ZUeaA1x6GX7+nuLkwtyBMRz9lRC4HgUUDgmSeI8gAis3dT3ZV586WVFoPjxV6le9v00QKBgQCxJmP27n2cnrtfNQr0L/6mQR/deuxtC9cVukYOPYJwjnGsbyrnQkHYv6K/WmjsGFNtWvucRRmsxabi3oY4wTsGOcLceJzX8FDKny9KHKQVO6nm0beDwPtn7XBubteI659aQfAmy3dVZe1NTPPoTzwpCsHIrrBAmIQyoWkzEWNrBg==";
    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkt6WPDWwxfgsKxvMddi+2KgxxW6JYE10QE1wZegxur/9zRTPzNKLnaOvkMTBDZgLS/iv70qxdeyXQPnv3k7/J2ufbFk3qO+xgoU6ql17yVT2hSUPcWFBQiOGpIeO830kLvAwIMxm1+eaHRZh94T4h1H/ek1254pQQextDJqeAQmCvjlmnkcc6xeSxdX0ryqAxuAfXzq41Yun32Uj3+6IUat2hjINY4q1y52qywsPXUPAsB3xwdPhp9rLNvu1rasPpEmRPge++63s13iPrY+N5WiB3OBsqoJuE5lSz4HK5c/sknMPtY+GCqHxtzLB8tChKiqSamIoLTP0up7gauRSOQIDAQAB";


    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws java.io.IOException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {

        String privateKey = new String(Files.readAllBytes(Paths.get("src/main/resources/privateKey")));
        String publicKey = new String(Files.readAllBytes(Paths.get("src/main/resources/publicKey1")));

        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("test-key", publicKey));
            System.out.println("ENCRYPT: " + encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            System.out.println("DECRYPT: " + decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
    }
}


