package com.giants3.yourreader.text;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class TXTtypeset {

	public static final int LAST_PARAGRAH_LAST_LINE_EMPTY = 1;
	public static final int LAST_PARAGRAH_LAST_LINE_NO_EMPTY = 0;
	public static final int NEXT_PARAGRAH_FIRST_LINE_EMPTY = -1;
	public static final int NEXT_PARAGRAH_FIRST_LINE_NO_EMPTY = 0;
	public static final int UNKNOW = 2;
	public static final float CHAPR_REPLACE_FLAG = 0.01f;
	public static final int MAX_CHANGE = 3;

	private static byte[] charWidths = null;


	private int h_spacing;
	public static int TYPESET_FLAG = 9853;
	public static int TYPESET_FLAG_IMG = 9955;
	public static int TYPESET_FLAG_TAG_IMG = 9900;
	public static int TYPESET_FLAG_TAG_COMPRESS = 9909;

	private int screenWidth;
	private boolean measureDirect = false;
	private static int chineseCharWidth = 0;
}
