package nano.util;



import java.io.*;
import java.security.*;

import javax.crypto.*;

/*****************************************************************
 * @description : 트리블 DES 알고리즘 암호화/복호화 class
 *                사용자 정보의 비밀번호를 암/복호화 한다.
 *                
 * $Id: DESUtil.java,v 1.3 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 10. 2	한성확		최초작성
 * 
 * ※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
 *  주의:src/SecretKey.ser  파일을 분실하면 복호화 할 수 없으므로 주의바람 
 * ※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
 * 
 *****************************************************************/

public class DESUtil {
    private static Key key = null;
    private static Cipher cipher = null;

    static {
        try {
        	cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        	
            //키 파일을 연다 ==> 키파일 위치는 war/WEB-INF/classe/SecretKey.ser
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("SecretKey.ser"));
            key = (Key)in.readObject(); // 키 파일로부터 키 객체를 가져온다.
            in.close();
        } catch (FileNotFoundException fe) { // 파일이 없으면

        	/**
        	 * 
        	 * 이하는 키 파일이 없을경우 키 파일을 만들긴 하지만 
        	 * 이미 암호화된 내용을 복호화 할 수 없으므로 매우 주의 바람
        	 * 
        	 * 
        	 * 
			 */
        	try {
        	KeyGenerator generator = KeyGenerator.getInstance("DES"); // DES 키 객체를 생성한다.
            generator.init(new SecureRandom()); // 의사난수에 근거하여 키 생성기를 초기화시킨다.
            // 초기화하는데 몇초정도의 시간이 걸린다.
            key = generator.generateKey(); // 키를 생성한다.
            // 키 파일을 만든다.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SecretKey.ser"));
            out.writeObject(key); // 키 파일에 기록한다.
            out.close();
        	}catch (Exception ex){
    			// TODO Auto-generated catch block
    			ex.printStackTrace();
        	}




			fe.printStackTrace();
        	
        } catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
        }


    	
    }
    
    /**
     * 
     * SecretKey.ser 키 파일을 이용해서 문자열을 암호화 한다.
     * 
     * @param text
     * @return 암호화된 Base64 문자열
     */
    public static String encode (String text){
    	
    	if (text == null){
    		return "";
    	}
        try {
			cipher.init(Cipher.ENCRYPT_MODE, key); // cipher객체를 암호모드로 만든다.

			// 주어진 스트링을 인자에 해당하는 형태의 바이트 배열로 바꾼다.
			byte[] stringBytes = text.getBytes("KSC5601");
			byte[] raw = cipher.doFinal(stringBytes); // 암호문을 만든다.
			String hexString = Base64.encode(raw);
			return hexString;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }
    
    /**
     * 암호화된 Base64문자열을 평문 문자열로 복호화 한다 
     * 
     * @param hextext
     * @return 복호화 평문
     */
    public static String decode (String hextext){
    	if (hextext == null){
    		return "";
    	}
        try {
			cipher.init(Cipher.DECRYPT_MODE, key);  // cipher객체를 복호화 모드로 만든다.
			byte[] raw = Base64.decode(hextext);
			byte[] stringBytes = cipher.doFinal(raw); // 복호화한다.
			String result = new String(stringBytes, "KSC5601");
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

    }
    

    /**
     * 키가 없으면 이 프로그램 실행
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {

    	Key key;
        try {
            //키 파일을 연다
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("SecretKey.ser"));
            key = (Key)in.readObject(); // 키 파일로부터 키 객체를 가져온다.
            in.close();
        } catch (FileNotFoundException fe) { // 파일이 없으면
                KeyGenerator generator = KeyGenerator.getInstance("DES"); // DES 키 객체를 생성한다.
                generator.init(new SecureRandom()); // 의사난수에 근거하여 키 생성기를 초기화시킨다.
                // 초기화하는데 몇초정도의 시간이 걸린다.
                key = generator.generateKey(); // 키를 생성한다.
                // 키 파일을 만든다.
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SecretKey.ser"));
                out.writeObject(key); // 키 파일에 기록한다.
                out.close();
        }



        // DES의 암호화 모드와 패딩 모드를 결정한다.

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String hexString;
		byte[] raw = null;

        while(true) {
            System.out.println("암호 : E / 복호 : D / 종료 : Q ");
            System.out.print("Select Mode : ");
            String msg = in.readLine();
            if(msg.toUpperCase().equals("Q")) {
                    break;
            } else    if(msg.toUpperCase().equals("E")) {
                cipher.init(Cipher.ENCRYPT_MODE, key); // cipher객체를 암호모드로 만든다.
                System.out.print("평문 입력 : ");
                String plain_text = in.readLine(); // 평문을 입력한다.

                // 주어진 스트링을 인자에 해당하는 형태의 바이트 배열로 바꾼다.
                byte[] stringBytes = plain_text.getBytes("KSC5601");
                raw = cipher.doFinal(stringBytes); // 암호문을 만든다.
                hexString = Base64.encode(raw);
                System.out.println("암호문 : " + hexString);
            } else if(msg.toUpperCase().equals("D")) {
                cipher.init(Cipher.DECRYPT_MODE, key);  // cipher객체를 복호화 모드로 만든다.
                byte[] stringBytes = cipher.doFinal(raw); // 복호화한다.
                String result = new String(stringBytes, "KSC5601");
                System.out.print("평문 : " + result);
                System.out.println();
            }

        }

    }

}