package com.k3k.unplat.utils.callbackapi.tool;

import java.io.UnsupportedEncodingException;

public class NetDataTypeTransform
{
	
//	/**
//	 *  long类型转成byte数组
//	 * @param number
//	 * @return
//	 */
//	public byte[] LongToByteArray(long number) {
//		long temp = number;
//		byte[] b = new byte[8];
//		for (int i = 0; i < b.length; i++) {
//			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
//			temp = temp >> 8; // 向右移8位
//		}
//		return b;
//	}
//	
//    /**
//     * byte数组转成long 
//     * @param bArr
//     * @return
//     */
//	public long ByteArrayToLong(byte[] b) {
//		long s = 0;
//		long s0 = b[0] & 0xff;// 最低位
//		long s1 = b[1] & 0xff;
//		long s2 = b[2] & 0xff;
//		long s3 = b[3] & 0xff;
//		long s4 = b[4] & 0xff;// 最低位
//		long s5 = b[5] & 0xff;
//		long s6 = b[6] & 0xff;
//		long s7 = b[7] & 0xff;
//
//		// s0不变
//		s1 <<= 8;
//		s2 <<= 16;
//		s3 <<= 24;
//		s4 <<= 8 * 4;
//		s5 <<= 8 * 5;
//		s6 <<= 8 * 6;
//		s7 <<= 8 * 7;
//		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
//		return s;
//	}
	
	/**
	 * 将int转为低字节在前，高字节在后的byte数组
	 */
	public byte[] IntToByteArray(int n)
	{
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * byte数组转化为int 将低字节在前转为int，高字节在后的byte数组
	 */
	public int ByteArrayToInt(byte[] bArr)
	{
		 int  s =  0 ; 
		   for  ( int  i =  0 ; i <  3 ; i++) { 
		     if  (bArr[ 3 -i] >=  0 ) { 
		     s = s + bArr[3 -i]; 
		     } else  { 
		     s = s + 256  + bArr[ 3 -i]; 
		     } 
		     s = s * 256 ; 
		   } 
		   if  (bArr[ 0 ] >=  0 ) { 
		     s = s + bArr[0 ]; 
		   } else  { 
		     s = s + 256  + bArr[ 0 ]; 
		   } 
		   return  s; 
//		int n = 0;
//		for (int i = 0; i < bArr.length && i < 4; i++)
//		{
//			int left = i * 8;
//			n += (bArr[i] << left);
//		}
//		return n;
	}

	/**
	 * 将byte数组转化成String
	 */
	public String ByteArraytoString(byte[] valArr, int maxLen)
	{
		String result = null;
		int index = 0;
		while (index < valArr.length && index < maxLen)
		{
			if (valArr[index] == 0)
			{
				break;
			}
			index++;
		}
		byte[] temp = new byte[index];
		System.arraycopy(valArr, 0, temp, 0, index);
		try
		{
			result = new String(temp, "GBK");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public byte[] StringToByteArray(String str)
	{
		byte[] temp = null;
		try
		{
			temp = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
}
