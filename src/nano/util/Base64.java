package nano.util;

/*****************************************************************
 * @description : Base64 encoding / decoding 하기위한 class
 * $Id: Base64.java,v 1.1 2011/10/02 08:12:00 $
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


public class Base64 { 
    private static char alphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static byte codes[];

    static 
    {
        codes = new byte[256];
        for(int i = 0; i < 256; i++)
            codes[i] = -1;

        for(int i = 65; i <= 90; i++)
            codes[i] = (byte)(i - 65);

        for(int i = 97; i <= 122; i++)
            codes[i] = (byte)((26 + i) - 97);

        for(int i = 48; i <= 57; i++)
            codes[i] = (byte)((52 + i) - 48);

        codes[43] = 62;
        codes[47] = 63;
    }
	
    /**
     * byte[] 데이타를 Base64 로 변환
     * @param data
     * @return
     */
    public static String encode(byte data[])
    {
        char out[] = new char[((data.length + 2) / 3) * 4];
        int i = 0;
        for(int index = 0; i < data.length; index += 4)
        {
            boolean quad = false;
            boolean trip = false;
            int val = 0xff & data[i];
            val <<= 8;
            if(i + 1 < data.length)
            {
                val |= 0xff & data[i + 1];
                trip = true;
            }
            val <<= 8;
            if(i + 2 < data.length)
            {
                val |= 0xff & data[i + 2];
                quad = true;
            }
            out[index + 3] = alphabet[quad ? val & 0x3f : 64];
            val >>= 6;
            out[index + 2] = alphabet[trip ? val & 0x3f : 64];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3f];
            val >>= 6;
            out[index] = alphabet[val & 0x3f];
            i += 3;
        }

        return new String(out);
    }

    
    /**
     * Base64 데이타를 byte[] 로 변환
     * 
     * @param hex
     * @return
     */
    public static byte[] decode(String hex)
    {
    	byte data[] = hex.getBytes();
        int len = ((data.length + 3) / 4) * 3;
        if(data.length > 0 && data[data.length - 1] == 61)
            len--;
        if(data.length > 1 && data[data.length - 2] == 61)
            len--;
        byte out[] = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for(int ix = 0; ix < data.length; ix++)
        {
            int value = codes[data[ix] & 0xff];
            if(value >= 0)
            {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if(shift >= 8)
                {
                    shift -= 8;
                    out[index++] = (byte)(accum >> shift & 0xff);
                }
            }
        }

        if(index != out.length)
            throw new RuntimeException("Error decoding BASE64 element: miscalculated data length!");
        else
            return out;
    }

}
