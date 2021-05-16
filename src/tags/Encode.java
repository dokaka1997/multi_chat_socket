package tags;

import java.math.BigInteger;
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

        String hashtext;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(result.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

//		System.out.println("(encode)Dau ra message: " + Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG);
        return Tags.CHAT_MSG_OPEN_TAG + hashtext + Tags.CHAT_MSG_CLOSE_TAG;
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

