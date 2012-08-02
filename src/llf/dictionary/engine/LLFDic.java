package llf.dictionary.engine;

import android.util.Log;

public class LLFDic {

	private static final String TAG = "LLFDic";
	private static final String SO_NAME = "LlfDicEngineJNI";

	public static final int FIND_EXACT_MATCH = 0; 
	public static final int FIND_LEXICAL_NEAREST = 1; 

	static {
		try {
			System.loadLibrary(SO_NAME);
		} catch (UnsatisfiedLinkError ule) {
			System.out.println("Could not load libLlfDicEngineJNI: " + SO_NAME + "\n"
					+ ule);
			Log.e(TAG, "Could not load libLlfDicEngineJNI: " + SO_NAME + "\n"
					+ ule);
		}
	}


	/*******************************************************
	 * �� �� ��: init
	 * ��������: 2010-12-17
	 * ��    ��: �ʵ������ʼ��
	 * �������: sFileName: �ʵ��ļ���(��·��)
	 * �������: ��
	 * ��    ��: int: 0(DIC_OK): �ɹ�, ����: ʧ��, �ο�dictengine.h�е�E_DIC_ERROR����
	 *******************************************************/
	public native static int init(String sFilename);

	/*******************************************************
	 * �� �� ��: free
	 * ��������: 2010-12-17
	 * ��    ��: �ʵ������ͷ�
	 * �������: ��
	 * �������: ��
	 * ��    ��: void
	 *******************************************************/
	public native static void free();

	/*******************************************************
	 * �� �� ��: getWordCount
	 * ��������: 2010-12-17
	 * ��    ��: ��ȡ��������
	 * �������: ��
	 * �������: ��
	 * ��    ��: long: ��������
	 *******************************************************/
	public native static long getWordCount();

	/*******************************************************
	 * �� �� ��: getIndexByInput
	 * ��������: 2010-12-17
	 * ��    ��: ��ѯ����
	 * �������: input: ������ַ���
	 *        tMatch: ƥ�䷽ʽ:
	 *        FIND_EXACT_MATCH		= 0		//��ȫƥ��(�ʵ�ѧ�ϵ�, ���ڿ�ʵ��ѯ��)
	 *        FIND_LEXICAL_NEAREST	= 1		//��ӽ�ƥ��
	 * �������: ��
	 * ��    ��: long: ��ѯ���ĵ������
	 *******************************************************/
	public native static long getIndexByInput(String input, int tMatch);

	/*******************************************************
	 * �� �� ��: getWordByIndex
	 * ��������: 2010-12-17
	 * ��    ��: ������Ż�ȡ��ͷ
	 * �������: index: �������(0~count-1),
	 * �������: ��
	 * ��    ��: char[]: ��ͷ����(unicode����)
	 *******************************************************/
	public native static char[] getWordByIndex(long index);

	/*******************************************************
	 * �� �� ��: getRealIndexByIndexAndOffset
	 * ��������: 2010-12-17
	 * ��    ��: ���ݵ���A���������Ե���A��ƫ�ƻ�ȡ����Bʵ�����
	 * �������: index: ����A�����(0~count-1),
	 *        offset: ����B��Ե���A��ת����������ƫ����(0,1,2...)
	 * �������: ��
	 * ��    ��: long: ����B�����
	 * ˵    ��: (һ��ֻ�ڿ�ʵ��ѯ�ش�ʱ�õ�)���ڴʵ��������漰���鱾�����������ŵĻ���ת������,
	 *       �ʶ���Ҫ�õ�������.
	 *******************************************************/
	public native static long getRealIndexByIndexAndOffset(long index, long offset);

	/*******************************************************
	 * �� �� ��: getWordByIndexAndOffset
	 * ��������: 2010-12-17
	 * ��    ��: ���ݵ���A���������Ե���A��ƫ�ƻ�ȡ����B�Ĵ�ͷ
	 * �������: index: ����A�����(0~count-1),
	 *        offset: ����B��Ե���A��ת����������ƫ����(0,1,2...)
	 * �������: ��
	 * ��    ��: char[]: ����B�Ĵ�ͷ����(unicode����)
	 * ˵    ��: (һ��ֻ�ڿ�ʵ��ѯ�ش�ʱ�õ�)���ڴʵ��������漰���鱾�����������ŵĻ���ת������,
	 *       �ʶ�����ֱ���� getWordByIndex(index+offset)ȥ��ȡ��һ�����ʴ�ͷ,
	 *       ֻ��ʹ�ñ�����, ��Ȼ����������:
	 *       long realInd = getRealIndexByIndexAndOffset(index, offset);
	 *       char[] ba = getWordByIndex(realInd);
	 *******************************************************/
	public native static char[] getWordByIndexAndOffset(long index, long offset);

	/*******************************************************
	 * �� �� ��: getExplainByIndex
	 * ��������: 2010-12-17
	 * ��    ��: ������Ż�ȡ��������
	 * �������: index: �������(0~count-1),
	 * �������: ��
	 * ��    ��: char[]: ��������(unicode����)
	 *******************************************************/
	public native static char[] getExplainByIndex(long index);

    /*******************************************************
     * �� �� ��: getAttrsByIndex
     * ��������: 2010-12-17
     * ��    ��: ������Ż�ȡ����
     * �������: index: �������(0~count-1),
     * �������: ��
     * ��    ��: long[]: ��������, ��DIC_SUB_LIB_NUM*(1+DIC_MAX_WORDINDEX_ATT)��Ԫ��,
     *           �൱�ڰѶ�ά����ĳ�һά����, ��ʽ���£�
     *           ����0����������, DIC_MAX_WORDINDEX_ATT������,
     *           ����1����������, DIC_MAX_WORDINDEX_ATT������,
     *           ......
     *           ����(DIC_SUB_LIB_NUM-1)����������, DIC_MAX_WORDINDEX_ATT������
     * ��ʽ: ����x����������:   Result[x*(1+DIC_MAX_WORDINDEX_ATT)+0]
     *       ����x�ĵ�һ������: Result[x*(1+DIC_MAX_WORDINDEX_ATT)+1]
     *       ����xΪ(ͬ��,����,�÷���,����ο�vh_data_engine.h�е�E_SUB_FILE_TYPE)
     *******************************************************/
	public native static long[] getAttrsByIndex(long index);

	/*******************************************************
	 * �� �� ��: getAttrText
	 * ��������: 2010-12-17
	 * ��    ��: ��ȡ��������
	 * �������: attr: ��������(ͬ��,����,�÷���,����ο�vh_data_engine.h�е�E_SUB_FILE_TYPE)
	 *        index: �����Կ��е����
	 * �������: ��
	 * ��    ��: char[]: ��������
	 * ע    ��: �˺������õ�����: �÷�, ����, ��ʵ�
	 *******************************************************/
	public native static char[] getAttrText(int attr, long index);

	/*******************************************************
	 * �� �� ��: getPicData
	 * ��������: 2010-12-24
	 * ��    ��: ��ȡͼƬ����
	 * �������: index: �����Կ��е����
	 * �������: ��
	 * ��    ��: byte[]: ͼƬ����
	 *******************************************************/
	public native static byte[] getPicData(long index);

	/*******************************************************
	 * �� �� ��: getExampleText
	 * ��������: 2010-12-17
	 * ��    ��: ��ȡ��������
	 * �������: index: ������е����
	 * �������: ��
	 * ��    ��: char[]: ��������(unicode����)
	 *******************************************************/
	public native static char[] getExampleText(long index);

	/*******************************************************
	 * �� �� ��: getIntroText
	 * ��������: 2010-12-17
	 * ��    ��: ��ȡ�ʵ�����ı�
	 * �������: ��
	 * �������: ��
	 * ��    ��: char[]: �ı�����(unicode����)
	 *******************************************************/
	public native static char[] getIntroText();

	/*******************************************************
	 * �� �� ��: hasSubAttr
	 * ��������: 2010-12-17
	 * ��    ��: �жϱ��ʵ��Ƿ���ĳ����
	 * �������: attr: ��������(����ο�vh_data_engine.h�е�E_SUB_FILE_TYPE)
	 * �������: ��
	 * ��    ��: boolean
	 *******************************************************/
	public native static boolean hasSubAttr(int attr);
	
	/*******************************************************
	 * �� �� ��: isExactMatchWord
	 * ��������: 2011-2-8
	 * ��    ��: �ж��������Ƿ���ȫƥ��(�ʵ�ѧ�ϵ�)
	 * �������: word1, word2: ����
	 * �������: ��
	 * ��    ��: boolean
	 *******************************************************/
	public native static boolean isExactMatchWord(char[] word1, char[] word2);

	/*******************************************************
	 * �� �� ��: fuzzySearchReady
	 * ��������: 2011-1-6
	 * ��    ��: ׼��һ��ģ������
	 * �������: sInput: ������ַ���
	 * �������: ��
	 * ��    ��: boolean: �Ƿ�ɹ�
	 *******************************************************/
	public native static boolean fuzzySearchReady(String sInput);

	/*******************************************************
	 * �� �� ��: fuzzySearchNext
	 * ��������: 2011-1-6
	 * ��    ��: ģ��������һ��ƥ����
	 * �������: ��
	 * �������: ��
	 * ��    ��: long: ƥ��ĵ������,
	 *           ��ֵΪ UINT_MAX ��ʾû�н����״��������������
	 *******************************************************/
	public native static long fuzzySearchNext();

	/*******************************************************
	 * �� �� ��: fuzzySearchPrev
	 * ��������: 2011-1-6
	 * ��    ��: ģ��������һ��ƥ����
	 * �������: ��
	 * �������: ��
	 * ��    ��: long: ƥ��ĵ������,
	 *           ��ֵΪ UINT_MAX ��ʾû�н����״��������������
	 *******************************************************/
	public native static long fuzzySearchPrev();
}

