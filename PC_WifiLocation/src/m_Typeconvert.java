

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class m_Typeconvert {
	 public char[] getChars (byte[] bytes) { ////byte[]��ת����char����
         Charset cs = Charset.forName ("UTF-8");
         ByteBuffer bb = ByteBuffer.allocate (bytes.length);
         bb.put (bytes);
                    bb.flip ();
          CharBuffer cb = cs.decode (bb);
     
      return cb.array();
   }
	  public  byte[] intToBytes(int num) {////C++ �������ݴ洢��ʽΪС�� JavaΪ���
         
         
         
     	   byte[] b = new byte[4];
     	   for (int i = 0; i < 4; i++) {
     	    b[i] = (byte) (num >>> ( i * 8));
     	   }
     	   return b;
     	}
	  public  void long2Byte(byte[] bb, long x) { ////C++ �������ݴ洢��ʽΪС�� JavaΪ���
         bb[ 7] = (byte) (x >> 56); 
         bb[6] = (byte) (x >> 48); 
         bb[5] = (byte) (x >> 40); 
         bb[4] = (byte) (x >> 32); 
         bb[3] = (byte) (x >> 24); 
         bb[2] = (byte) (x >> 16); 
         bb[ 1] = (byte) (x >> 8); 
         bb[ 0] = (byte) (x >> 0); 
   } 
	  public  double byteToDouble(byte[] b){
     	  long l;
     	  l=b[0];
     	  l&=0xff;
     	  l|=((long)b[1]<<8);
     	  l&=0xffff;
     	  l|=((long)b[2]<<16);
     	  l&=0xffffff;
     	  l|=((long)b[3]<<24);
     	  l&=0xffffffffl;
     	  l|=((long)b[4]<<32);
     	  l&=0xffffffffffl;
     	  l|=((long)b[5]<<40);
     	  l&=0xffffffffffffl;
     	  l|=((long)b[6]<<48);
     	  l|=((long)b[7]<<56);
     	  return Double.longBitsToDouble(l);
     	 }
	  public  int byteToInt(byte[] b) {////C++ �������ݴ洢��ʽΪС�� JavaΪ���
     	  int s = 0;
     	  for (int i = 3; i >0; i--) {
     	   if (b[i] >= 0)
     	    s = s + b[i];
     	   else
     	    s = s + 256 + b[i];  //�˴�����ʹ��mod 256��ʡȥif�ж�
     	   s = s * 256;
     	  }
     	  if (b[0] >= 0) //���һ��֮���Բ��ˣ�����Ϊ���ܻ����
     	   s = s + b[0];
     	  else
     	   s = s + 256 + b[0];  //ͬ��
     	  return s;
     	 }
	  public  long getLong(byte[] bb) { 
         return ((((long) bb[ 7] & 0xff) << 56) 
                 | (((long) bb[ 6] & 0xff) << 48) 
                 | (((long) bb[ 5] & 0xff) << 40) 
                 | (((long) bb[ 4] & 0xff) << 32) 
                 | (((long) bb[ 3] & 0xff) << 24) 
                 | (((long) bb[ 2] & 0xff) << 16) 
                 | (((long) bb[ 1] & 0xff) << 8) | (((long) bb[ 0] & 0xff) << 0)); 
    }
	  public  byte[] getBytes(long data)
	    {
	        byte[] bytes = new byte[8];
	        bytes[0] = (byte) (data & 0xff);
	        bytes[1] = (byte) ((data >> 8) & 0xff);
	        bytes[2] = (byte) ((data >> 16) & 0xff);
	        bytes[3] = (byte) ((data >> 24) & 0xff);
	        bytes[4] = (byte) ((data >> 32) & 0xff);
	        bytes[5] = (byte) ((data >> 40) & 0xff);
	        bytes[6] = (byte) ((data >> 48) & 0xff);
	        bytes[7] = (byte) ((data >> 56) & 0xff);
	        return bytes;
	    }
	  public  byte[] getBytes(double data)
	    {
	        long intBits = Double.doubleToLongBits(data);
	        return getBytes(intBits);
	    }
	  
	  public byte[] getBytes (char[] chars) {
		   Charset cs = Charset.forName ("UTF-8");
		   CharBuffer cb = CharBuffer.allocate (chars.length);
		   cb.put (chars);
		                 cb.flip ();
		   ByteBuffer bb = cs.encode (cb);
		  
		   return bb.array();
		         }

}

