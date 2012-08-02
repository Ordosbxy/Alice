package llf.dictionary.engine;

import android.graphics.Color;

public class VHConst
{
	public static final boolean PCRE = true;	// 模糊搜索开关

	public static final long UINT_MAX = 0xFFFFFFFFL;
	public static final long INVALID_ADDR  = UINT_MAX;
	public static final long INVALID_INDEX = UINT_MAX;

	// 搜索方式
	public static final int FIND_EXACT_MATCH = 0;       //完全匹配(词典学上的, 用于跨词典查询中)
	public static final int FIND_LEXICAL_NEAREST = 1;	//最接近匹配

	// 自造字区域
	public static final int SELF_WORD_GSW_START = 0xD000;	//古诗文古汉语自造字库的起始位置
	public static final int SELF_WORD_START = 0xE000;
	public static final int SELF_WORD_END   = 0xE8FF;

	// 子文件(或: 属性)类型, E_SUB_FILE_TYPE
    public static final int SFT_NULL = -1;      // 空
    public static final int SFT_WORD_HEAD = 0;  // 词条
    public static final int SFT_PHONETIC = 1;       // 音标
    public static final int SFT_EXPLAIN = 2;        // 释义
    public static final int SFT_EXAMPLE = 3;        // 例句
    public static final int SFT_PHRASE = 4;         // 短语
    public static final int SFT_THESAURUS = 5;      // 同义
    public static final int SFT_ANTONYM = 6;        // 反义
    public static final int SFT_DERIV = 7;          // 派生
    public static final int SFT_IDIOM = 8;          // 习语
    public static final int SFT_COMPLEX = 9;        // 复合
    public static final int SFT_SEE_ALSO = 10;       // 另见
    public static final int SFT_DERIVATION = 11;     // 出处
    public static final int SFT_STORY = 12;          // 典故
    public static final int SFT_USAGE = 13;          // 用法
    public static final int SFT_MP3_NAME = 14;       // 声音文件名
    public static final int SFT_ENG_HFM_TABLE = 15;  // 英文压缩表
    public static final int SFT_CHN_HFM_TABLE = 16;  // 中文压缩表
    public static final int SFT_JAP_HFM_TABLE = 17;  // 日文压缩表
    public static final int SFT_KOR_HFM_TABLE = 18;  // 韩文压缩表
    public static final int SFT_INTRO = 19;          // 词典介绍
    public static final int SFT_PIC = 20;            // 图片
    public static final int SFT_UNIT = 21;           // 单元(考试词汇)
    public static final int SFT_GROUP = 22;          // 组别(考试词汇)
    public static final int SFT_SOUND = 23;          // 声音数据
    // 24~29    保留
    public static final int SFT_NUM = 24;            // 实际总数
    //public static final int DIC_SUB_LIB_NUM = SFT_NUM;	// 属性类型数
	public static final int DIC_MAX_WORDINDEX_ATT = 128;	// 最多的属性索引个数

    // 属性数据中的属性起始地址
	public static final long ATTR_EXAMPLE_START = SFT_EXAMPLE*(1+DIC_MAX_WORDINDEX_ATT);
	public static final long ATTR_IDIOM_START = SFT_IDIOM*(1+DIC_MAX_WORDINDEX_ATT);
	public static final long ATTR_PIC_START = SFT_PIC*(1+DIC_MAX_WORDINDEX_ATT);

	// 功能码
    public static final char FN_NO_START      = 0x0001;  // 编号开始
    public static final char FN_NO_END        = 0x0002;
    public static final char FN_PHONE_START   = 0x0003;  // 音标开始
    public static final char FN_PHONE_END     = 0x0004;
    public static final char FN_BOLD_START    = 0x0005;  // 粗体开始
    public static final char FN_BOLD_END      = 0x0006;
    public static final char FN_ITALIC_START  = 0x0007;  // 斜体
    public static final char FN_ITALIC_END    = 0x0008;
    public static final char FN_EXAMPLE_START = 0x000B;  // 例句
    public static final char FN_EXAMPLE_END   = 0x000C;
    public static final char FN_COLOR_START   = 0x000E;  // 颜色
    public static final char FN_COLOR_END     = 0x000F;
    public static final char FN_USAGE_START   = 0x0012;     // 用法说明
    public static final char FN_USAGE_END     = 0x0013;
    public static final char FN_PIC_START     = 0x0014;     // 图片
    public static final char FN_PIC_END       = 0x0015;
    public static final char FN_ENG_START     = 0x0016;     // 双解词典中的英语标记
    public static final char FN_ENG_END       = 0x0017;
    public static final char FN_CHN_START     = 0x0018;     // 双解词典中的汉语标记
    public static final char FN_CHN_END       = 0x0019;
    public static final char FN_SELFWORD1_START = 0x001E;   // 自造字1
    public static final char FN_SELFWORD1_END   = 0x001F;
    public static final char FN_SELFWORD2_START = 0x001C;   // 自造字2
    public static final char FN_SELFWORD2_END   = 0x001D;
    public static final char FN_SELEWROD3_START = 0x001A;	//自造字3
    public static final char FN_SELFWROD3_END = 0x001B;

    // 颜色码
    public static final char FN_DEFAULT = 0x0000;  // 默认色
    public static final char FN_RED   = 0x0001;  // 红色
    public static final char FN_BLUE  = 0x0002;  // 蓝色
    public static final char FN_GREEN = 0x0003;  // 绿色
    public static final char FN_BROWN = 0x0004;  // 棕色
    public static final char FN_GRAY  = 0x0005;  // 灰色

    // 语言类型
    public static final int LANG_ENG = 0;
    public static final int LANG_CHN = 1;
    public static final int LANG_JPN = 2;
    public static final int LANG_KOR = 3;
    public static final int LANG_RUS = 4;
    public static final int LANG_GER = 5;
    public static final int LANG_FRE = 6;

    // 符号
    public static final char CHAR_EXAMPLE = 0xE7A1;	// 例自造字的unicode码
    public static final char SYMBLE_EXAMPLE = 0x00B6;   // 例句符号
    public static final char CHAR_0D = 0x000D;
    public static final char CHAR_0A = 0x000A;
    public static final char CHAR_09 = 0x0009;	//表示同时存在英式音标和美式英标, 使用这个字符分隔开来
    public static final char CHAR_SPACE = 0x0020;   // 空格

    // 函数运行返回类型
    public static final int DIC_OK = 0;

    // 是否标志符
    public static boolean isFlagChar(char c)
    {
    	if (c>(char)0x0000 && c<(char)0x0020)
    	{
    		if (c!=CHAR_0A && c!=CHAR_0D)	// c!=CHAR_09 && 
    		{
    			return true;
    		}
    	}

    	return false;
    }

    // 是否自造字
    public static boolean isSelfWord(char c)
    {
    	int w = toUnsignedNum(c);
    	return (w>=SELF_WORD_START && w<=SELF_WORD_END);
    }

    // 将字符转换为"无符号"数
    public static int toUnsignedNum(char c)
    {
    	int n = (int)c;

    	if (n < 0)
    	{
    		n += 256;
    	}

    	return n;
    }

    // 颜色值转换
    public static final int DEFAULT_COLOR = Color.BLACK;

    public static int getColor(char fnColor)
    {
    	switch (fnColor)
    	{
    	case FN_RED:
    		return Color.RED;
    	case FN_GREEN:
    		return Color.GREEN;
    	case FN_BLUE:
    		return Color.BLUE;
    	case FN_BROWN:
    		return 0x00B25D25;
    	case FN_GRAY:
    		return Color.GRAY;
    	default:
    		return DEFAULT_COLOR;
    	}
    }
}
