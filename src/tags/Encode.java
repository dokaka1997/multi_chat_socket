package tags;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encode {

    private static Pattern checkMessage = Pattern.compile("[^<>]*[<>]");

    public static String getCreateAccount(String name, String port) {
        return Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
                + Tags.PEER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG + port
                + Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG;
    }

    public static String sendRequest(String name) {
        return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
                + name + Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
                + Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG
                + Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
    }

    public static String sendMessage(String message) {
//		System.out.println("(encode)Dau vao message: " + message);


        Matcher findMessage = checkMessage.matcher(message);
        String result = "";
        while (findMessage.find()) {
            String subMessage = findMessage.group(0);
            System.out.println("subMessage: " + subMessage);
            int begin = subMessage.length();            //do dai chuoi con
            char nextChar = message.charAt(subMessage.length() - 1); //ky tu cuoi cung cua message
            System.out.println("nextChar: " + nextChar);
            result += subMessage; // + nextChar
            subMessage = message.substring(begin, message.length());
            message = subMessage;
            findMessage = checkMessage.matcher(message);
        }
        result += message;


        try {

            String key = "Key12345Key12345"; // 128 bit key
            Cipher cipher = Cipher.getInstance("AES");
            // Encode the string into bytes using utf-8
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] utf8 = result.getBytes("UTF8");

            // Encrypt
            byte[] enc = cipher.doFinal(utf8);

//		System.out.println("(encode)Dau ra message: " + Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG);
            return Tags.CHAT_MSG_OPEN_TAG + new sun.misc.BASE64Encoder().encode(enc) + Tags.CHAT_MSG_CLOSE_TAG;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static String sendRequestChat(String name) {
        return Tags.CHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
                + Tags.PEER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG;
    }

    public static String sendFile(String name) {
        return Tags.FILE_REQ_OPEN_TAG + name + Tags.FILE_REQ_CLOSE_TAG;
    }

    public static String exit(String name) {
        return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
                + name + Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
                + Tags.SERVER_OFFLINE + Tags.STATUS_CLOSE_TAG
                + Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
    }
}

