package com.kth.common.utils.etc;

public class Crc64Utils {

	private static boolean init = false;

	private static long[] CRCTable = new long[256];

	private static final long POLY64REV = 0x95AC9329AC4BC9B5L;

	private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;

	/**
	 * A function that returns a human readable hex string of a Crx64
	 * 
	 * @param in input string
	 * @return hex string of the 64-bit CRC value
	 */
	public static final String crc64(String in) {
		if (in == null) {
			return null;
		}
		long crc = crc64Long(in);

		/*
		 * The output is done in two parts to avoid problems with
		 * architecture-dependent word order
		 */
		int low = ((int) crc) & 0xffffffff;
		int high = ((int) (crc >> 32)) & 0xffffffff;
		String outVal = Integer.toHexString(high) + Integer.toHexString(low);
		return outVal;
	}

	/**
	 * A function thats returns a 64-bit crc for string
	 * 
	 * @param in input string
	 * @return 64-bit crc value
	 */
	public static final long crc64Long(String in) {
		if (in == null || in.length() == 0) {
			return 0;
		}
		// http://bioinf.cs.ucl.ac.uk/downloads/crc64/crc64.c
		long crc = INITIALCRC, part;
		if (!init) {
			for (int i = 0; i < 256; i++) {
				part = i;
				for (int j = 0; j < 8; j++) {
					int value = ((int) part & 1);
					if (value != 0) {
						part = (part >> 1) ^ POLY64REV;
					}
					else {
						part >>= 1;
					}
				}
				CRCTable[i] = part;
			}
			init = true;
		}
		int length = in.length();
		for (int k = 0; k < length; ++k) {
			char c = in.charAt(k);
			crc = CRCTable[(((int) crc) ^ c) & 0xff] ^ (crc >> 8);
		}
		return crc;
	}

}
