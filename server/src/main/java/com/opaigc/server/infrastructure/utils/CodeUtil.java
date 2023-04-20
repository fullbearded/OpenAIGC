package com.opaigc.server.infrastructure.utils;


import java.util.Random;
import java.util.UUID;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/4
 */
public class CodeUtil {

	private static final String BASE32_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
	private static final Random random = new Random();

	public static String generateNewCode(String prefix, int codeLen) {
		int remainingCodeLen = codeLen - prefix.length();
		return prefix + generateNewCode(remainingCodeLen, 0, 0, 7);
	}

	public static String generateNewCode(int codeLen, int flag, int flagBitLen, int checkBitLen) {
		int totalBitLen = codeLen * 5;
		int dataBitLen = calculateDataBitLen(totalBitLen, checkBitLen, flagBitLen);
		long randData = generateRandomData(dataBitLen);
		int checkModData = 1 << checkBitLen;

		long ret = addFlag(flag, flagBitLen, totalBitLen);
		ret = addRandomData(ret, randData, checkBitLen);
		ret = addCheckNumber(ret, checkBitLen, checkModData);

		return convertToBase32SerialCode(ret, codeLen);
	}

	public static String generateRandomUserCode() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	private static int calculateDataBitLen(int totalBitLen, int checkBitLen, int flagBitLen) {
		return totalBitLen - checkBitLen - flagBitLen;
	}

	private static long generateRandomData(int dataBitLen) {
		return (long) (1 + (1L << dataBitLen - 1) * random.nextDouble());
	}

	private static long addFlag(int flag, int flagBitLen, int totalBitLen) {
		if (flagBitLen > 0) {
			flag = flag & ((1 << flagBitLen) - 1);
			return (long) flag << (totalBitLen - flagBitLen);
		}
		return 0L;
	}

	private static long addRandomData(long ret, long randData, int checkBitLen) {
		return ret + (randData << checkBitLen);
	}

	private static long addCheckNumber(long ret, int checkBitLen, int checkModData) {
		long checkNum = (ret >> checkBitLen) % checkModData;
		return ret + checkNum;
	}

	private static String convertToBase32SerialCode(long longRandValue, int codeLen) {
		StringBuilder codeSerial = new StringBuilder(16);
		long tmpRandValue = longRandValue;
		for (int i = 0; i < codeLen; i++) {
			int code = (int) (tmpRandValue & 0x1F);
			char convertCode = BASE32_ALPHABET.charAt(code);
			codeSerial.append(convertCode);
			tmpRandValue = tmpRandValue >> 5;
		}
		return codeSerial.reverse().toString();
	}
}
