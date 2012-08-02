package llf.dictionary.engine;

import android.graphics.Color;

public class VHConst
{
	public static final boolean PCRE = true;	// ģ����������

	public static final long UINT_MAX = 0xFFFFFFFFL;
	public static final long INVALID_ADDR  = UINT_MAX;
	public static final long INVALID_INDEX = UINT_MAX;

	// ������ʽ
	public static final int FIND_EXACT_MATCH = 0;       //��ȫƥ��(�ʵ�ѧ�ϵ�, ���ڿ�ʵ��ѯ��)
	public static final int FIND_LEXICAL_NEAREST = 1;	//��ӽ�ƥ��

	// ����������
	public static final int SELF_WORD_GSW_START = 0xD000;	//��ʫ�Ĺź��������ֿ����ʼλ��
	public static final int SELF_WORD_START = 0xE000;
	public static final int SELF_WORD_END   = 0xE8FF;

	// ���ļ�(��: ����)����, E_SUB_FILE_TYPE
    public static final int SFT_NULL = -1;      // ��
    public static final int SFT_WORD_HEAD = 0;  // ����
    public static final int SFT_PHONETIC = 1;       // ����
    public static final int SFT_EXPLAIN = 2;        // ����
    public static final int SFT_EXAMPLE = 3;        // ����
    public static final int SFT_PHRASE = 4;         // ����
    public static final int SFT_THESAURUS = 5;      // ͬ��
    public static final int SFT_ANTONYM = 6;        // ����
    public static final int SFT_DERIV = 7;          // ����
    public static final int SFT_IDIOM = 8;          // ϰ��
    public static final int SFT_COMPLEX = 9;        // ����
    public static final int SFT_SEE_ALSO = 10;       // ���
    public static final int SFT_DERIVATION = 11;     // ����
    public static final int SFT_STORY = 12;          // ���
    public static final int SFT_USAGE = 13;          // �÷�
    public static final int SFT_MP3_NAME = 14;       // �����ļ���
    public static final int SFT_ENG_HFM_TABLE = 15;  // Ӣ��ѹ����
    public static final int SFT_CHN_HFM_TABLE = 16;  // ����ѹ����
    public static final int SFT_JAP_HFM_TABLE = 17;  // ����ѹ����
    public static final int SFT_KOR_HFM_TABLE = 18;  // ����ѹ����
    public static final int SFT_INTRO = 19;          // �ʵ����
    public static final int SFT_PIC = 20;            // ͼƬ
    public static final int SFT_UNIT = 21;           // ��Ԫ(���Դʻ�)
    public static final int SFT_GROUP = 22;          // ���(���Դʻ�)
    public static final int SFT_SOUND = 23;          // ��������
    // 24~29    ����
    public static final int SFT_NUM = 24;            // ʵ������
    //public static final int DIC_SUB_LIB_NUM = SFT_NUM;	// ����������
	public static final int DIC_MAX_WORDINDEX_ATT = 128;	// ����������������

    // ���������е�������ʼ��ַ
	public static final long ATTR_EXAMPLE_START = SFT_EXAMPLE*(1+DIC_MAX_WORDINDEX_ATT);
	public static final long ATTR_IDIOM_START = SFT_IDIOM*(1+DIC_MAX_WORDINDEX_ATT);
	public static final long ATTR_PIC_START = SFT_PIC*(1+DIC_MAX_WORDINDEX_ATT);

	// ������
    public static final char FN_NO_START      = 0x0001;  // ��ſ�ʼ
    public static final char FN_NO_END        = 0x0002;
    public static final char FN_PHONE_START   = 0x0003;  // ���꿪ʼ
    public static final char FN_PHONE_END     = 0x0004;
    public static final char FN_BOLD_START    = 0x0005;  // ���忪ʼ
    public static final char FN_BOLD_END      = 0x0006;
    public static final char FN_ITALIC_START  = 0x0007;  // б��
    public static final char FN_ITALIC_END    = 0x0008;
    public static final char FN_EXAMPLE_START = 0x000B;  // ����
    public static final char FN_EXAMPLE_END   = 0x000C;
    public static final char FN_COLOR_START   = 0x000E;  // ��ɫ
    public static final char FN_COLOR_END     = 0x000F;
    public static final char FN_USAGE_START   = 0x0012;     // �÷�˵��
    public static final char FN_USAGE_END     = 0x0013;
    public static final char FN_PIC_START     = 0x0014;     // ͼƬ
    public static final char FN_PIC_END       = 0x0015;
    public static final char FN_ENG_START     = 0x0016;     // ˫��ʵ��е�Ӣ����
    public static final char FN_ENG_END       = 0x0017;
    public static final char FN_CHN_START     = 0x0018;     // ˫��ʵ��еĺ�����
    public static final char FN_CHN_END       = 0x0019;
    public static final char FN_SELFWORD1_START = 0x001E;   // ������1
    public static final char FN_SELFWORD1_END   = 0x001F;
    public static final char FN_SELFWORD2_START = 0x001C;   // ������2
    public static final char FN_SELFWORD2_END   = 0x001D;
    public static final char FN_SELEWROD3_START = 0x001A;	//������3
    public static final char FN_SELFWROD3_END = 0x001B;

    // ��ɫ��
    public static final char FN_DEFAULT = 0x0000;  // Ĭ��ɫ
    public static final char FN_RED   = 0x0001;  // ��ɫ
    public static final char FN_BLUE  = 0x0002;  // ��ɫ
    public static final char FN_GREEN = 0x0003;  // ��ɫ
    public static final char FN_BROWN = 0x0004;  // ��ɫ
    public static final char FN_GRAY  = 0x0005;  // ��ɫ

    // ��������
    public static final int LANG_ENG = 0;
    public static final int LANG_CHN = 1;
    public static final int LANG_JPN = 2;
    public static final int LANG_KOR = 3;
    public static final int LANG_RUS = 4;
    public static final int LANG_GER = 5;
    public static final int LANG_FRE = 6;

    // ����
    public static final char CHAR_EXAMPLE = 0xE7A1;	// �������ֵ�unicode��
    public static final char SYMBLE_EXAMPLE = 0x00B6;   // �������
    public static final char CHAR_0D = 0x000D;
    public static final char CHAR_0A = 0x000A;
    public static final char CHAR_09 = 0x0009;	//��ʾͬʱ����Ӣʽ�������ʽӢ��, ʹ������ַ��ָ�����
    public static final char CHAR_SPACE = 0x0020;   // �ո�

    // �������з�������
    public static final int DIC_OK = 0;

    // �Ƿ��־��
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

    // �Ƿ�������
    public static boolean isSelfWord(char c)
    {
    	int w = toUnsignedNum(c);
    	return (w>=SELF_WORD_START && w<=SELF_WORD_END);
    }

    // ���ַ�ת��Ϊ"�޷���"��
    public static int toUnsignedNum(char c)
    {
    	int n = (int)c;

    	if (n < 0)
    	{
    		n += 256;
    	}

    	return n;
    }

    // ��ɫֵת��
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
