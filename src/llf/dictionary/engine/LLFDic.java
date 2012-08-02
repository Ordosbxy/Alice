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
	 * 函 数 名: init
	 * 创建日期: 2010-12-17
	 * 功    能: 词典引擎初始化
	 * 输入参数: sFileName: 词典文件名(带路径)
	 * 输出参数: 无
	 * 返    回: int: 0(DIC_OK): 成功, 其它: 失败, 参考dictengine.h中的E_DIC_ERROR定义
	 *******************************************************/
	public native static int init(String sFilename);

	/*******************************************************
	 * 函 数 名: free
	 * 创建日期: 2010-12-17
	 * 功    能: 词典引擎释放
	 * 输入参数: 无
	 * 输出参数: 无
	 * 返    回: void
	 *******************************************************/
	public native static void free();

	/*******************************************************
	 * 函 数 名: getWordCount
	 * 创建日期: 2010-12-17
	 * 功    能: 获取单词总数
	 * 输入参数: 无
	 * 输出参数: 无
	 * 返    回: long: 单词总数
	 *******************************************************/
	public native static long getWordCount();

	/*******************************************************
	 * 函 数 名: getIndexByInput
	 * 创建日期: 2010-12-17
	 * 功    能: 查询单词
	 * 输入参数: input: 输入的字符串
	 *        tMatch: 匹配方式:
	 *        FIND_EXACT_MATCH		= 0		//完全匹配(词典学上的, 用于跨词典查询中)
	 *        FIND_LEXICAL_NEAREST	= 1		//最接近匹配
	 * 输出参数: 无
	 * 返    回: long: 查询到的单词序号
	 *******************************************************/
	public native static long getIndexByInput(String input, int tMatch);

	/*******************************************************
	 * 函 数 名: getWordByIndex
	 * 创建日期: 2010-12-17
	 * 功    能: 根据序号获取词头
	 * 输入参数: index: 单词序号(0~count-1),
	 * 输出参数: 无
	 * 返    回: char[]: 词头数据(unicode编码)
	 *******************************************************/
	public native static char[] getWordByIndex(long index);

	/*******************************************************
	 * 函 数 名: getRealIndexByIndexAndOffset
	 * 创建日期: 2010-12-17
	 * 功    能: 根据单词A的序号与相对单词A的偏移获取单词B实际序号
	 * 输入参数: index: 单词A的序号(0~count-1),
	 *        offset: 单词B相对单词A在转换过后的序号偏移量(0,1,2...)
	 * 输出参数: 无
	 * 返    回: long: 单词B的序号
	 * 说    明: (一般只在跨词典查询重词时用到)由于词典引擎中涉及到书本序号与排序序号的互相转换问题,
	 *       故而需要用到本函数.
	 *******************************************************/
	public native static long getRealIndexByIndexAndOffset(long index, long offset);

	/*******************************************************
	 * 函 数 名: getWordByIndexAndOffset
	 * 创建日期: 2010-12-17
	 * 功    能: 根据单词A的序号与相对单词A的偏移获取单词B的词头
	 * 输入参数: index: 单词A的序号(0~count-1),
	 *        offset: 单词B相对单词A在转换过后的序号偏移量(0,1,2...)
	 * 输出参数: 无
	 * 返    回: char[]: 单词B的词头数据(unicode编码)
	 * 说    明: (一般只在跨词典查询重词时用到)由于词典引擎中涉及到书本序号与排序序号的互相转换问题,
	 *       故而不能直接由 getWordByIndex(index+offset)去获取另一个单词词头,
	 *       只能使用本函数, 当然还可以这样:
	 *       long realInd = getRealIndexByIndexAndOffset(index, offset);
	 *       char[] ba = getWordByIndex(realInd);
	 *******************************************************/
	public native static char[] getWordByIndexAndOffset(long index, long offset);

	/*******************************************************
	 * 函 数 名: getExplainByIndex
	 * 创建日期: 2010-12-17
	 * 功    能: 根据序号获取释义内容
	 * 输入参数: index: 单词序号(0~count-1),
	 * 输出参数: 无
	 * 返    回: char[]: 释义数据(unicode编码)
	 *******************************************************/
	public native static char[] getExplainByIndex(long index);

    /*******************************************************
     * 函 数 名: getAttrsByIndex
     * 创建日期: 2010-12-17
     * 功    能: 根据序号获取属性
     * 输入参数: index: 单词序号(0~count-1),
     * 输出参数: 无
     * 返    回: long[]: 属性数据, 共DIC_SUB_LIB_NUM*(1+DIC_MAX_WORDINDEX_ATT)个元素,
     *           相当于把二维数组改成一维数组, 格式如下：
     *           属性0的索引个数, DIC_MAX_WORDINDEX_ATT个索引,
     *           属性1的索引个数, DIC_MAX_WORDINDEX_ATT个索引,
     *           ......
     *           属性(DIC_SUB_LIB_NUM-1)的索引个数, DIC_MAX_WORDINDEX_ATT个索引
     * 公式: 属性x的索引个数:   Result[x*(1+DIC_MAX_WORDINDEX_ATT)+0]
     *       属性x的第一个索引: Result[x*(1+DIC_MAX_WORDINDEX_ATT)+1]
     *       属性x为(同义,反义,用法等,具体参考vh_data_engine.h中的E_SUB_FILE_TYPE)
     *******************************************************/
	public native static long[] getAttrsByIndex(long index);

	/*******************************************************
	 * 函 数 名: getAttrText
	 * 创建日期: 2010-12-17
	 * 功    能: 获取属性内容
	 * 输入参数: attr: 属性类型(同义,反义,用法等,具体参考vh_data_engine.h中的E_SUB_FILE_TYPE)
	 *        index: 在属性库中的序号
	 * 输出参数: 无
	 * 返    回: char[]: 属性内容
	 * 注    意: 此函数适用的属性: 用法, 出处, 典故等
	 *******************************************************/
	public native static char[] getAttrText(int attr, long index);

	/*******************************************************
	 * 函 数 名: getPicData
	 * 创建日期: 2010-12-24
	 * 功    能: 获取图片数据
	 * 输入参数: index: 在属性库中的序号
	 * 输出参数: 无
	 * 返    回: byte[]: 图片数据
	 *******************************************************/
	public native static byte[] getPicData(long index);

	/*******************************************************
	 * 函 数 名: getExampleText
	 * 创建日期: 2010-12-17
	 * 功    能: 获取例句内容
	 * 输入参数: index: 例句库中的序号
	 * 输出参数: 无
	 * 返    回: char[]: 例句数据(unicode编码)
	 *******************************************************/
	public native static char[] getExampleText(long index);

	/*******************************************************
	 * 函 数 名: getIntroText
	 * 创建日期: 2010-12-17
	 * 功    能: 获取词典介绍文本
	 * 输入参数: 无
	 * 输出参数: 无
	 * 返    回: char[]: 文本数据(unicode编码)
	 *******************************************************/
	public native static char[] getIntroText();

	/*******************************************************
	 * 函 数 名: hasSubAttr
	 * 创建日期: 2010-12-17
	 * 功    能: 判断本词典是否有某属性
	 * 输入参数: attr: 属性类型(具体参考vh_data_engine.h中的E_SUB_FILE_TYPE)
	 * 输出参数: 无
	 * 返    回: boolean
	 *******************************************************/
	public native static boolean hasSubAttr(int attr);
	
	/*******************************************************
	 * 函 数 名: isExactMatchWord
	 * 创建日期: 2011-2-8
	 * 功    能: 判断俩单词是否完全匹配(词典学上的)
	 * 输入参数: word1, word2: 单词
	 * 输出参数: 无
	 * 返    回: boolean
	 *******************************************************/
	public native static boolean isExactMatchWord(char[] word1, char[] word2);

	/*******************************************************
	 * 函 数 名: fuzzySearchReady
	 * 创建日期: 2011-1-6
	 * 功    能: 准备一轮模糊搜索
	 * 输入参数: sInput: 输入的字符串
	 * 输出参数: 无
	 * 返    回: boolean: 是否成功
	 *******************************************************/
	public native static boolean fuzzySearchReady(String sInput);

	/*******************************************************
	 * 函 数 名: fuzzySearchNext
	 * 创建日期: 2011-1-6
	 * 功    能: 模糊搜索下一个匹配项
	 * 输入参数: 无
	 * 输出参数: 无
	 * 返    回: long: 匹配的单词序号,
	 *           若值为 UINT_MAX 表示没有进行首次搜索或搜索完毕
	 *******************************************************/
	public native static long fuzzySearchNext();

	/*******************************************************
	 * 函 数 名: fuzzySearchPrev
	 * 创建日期: 2011-1-6
	 * 功    能: 模糊搜索上一个匹配项
	 * 输入参数: 无
	 * 输出参数: 无
	 * 返    回: long: 匹配的单词序号,
	 *           若值为 UINT_MAX 表示没有进行首次搜索或搜索完毕
	 *******************************************************/
	public native static long fuzzySearchPrev();
}

