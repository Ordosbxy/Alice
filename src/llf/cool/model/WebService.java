package llf.cool.model;

import org.json.JSONException;

/**
* @author  bxy
* @version ����ʱ�䣺2012-7-26 ����10:25:02
* 
*/
public interface WebService {
	
	/**
	 * �ӿ�����: GetSubResClass
	 * ��������: ��ȡ��Դ����
	 * �������: class_id: ���������ĸ�����id, 0��ʾ������
	 * ��ϸ����: ��ȡclass_id����Դ������Ϣ
	 * �������: ��
	 * �� �� ֵ  : ��Դ������Ϣ�б�
	 *        �б����ʽ:array('id',        // ����id
	                       'title'      // ����, GBK2HexString
	                       'icon_name'  // ͼ���ļ���, GBK2HexString
	                       'res_num);   // �˷����µ���Դ����
     */
	RootResource[] getSubResClass(int classId) throws JSONException, WebServiceError;
	
	/** 
	 * �ӿ�����: GetResInfo
	 * ��������: ��ȡ��Դ��Ϣ
	 * �������: class_id: ��Դ�����ķ���id, 0��ʾ����
	 *           start: ��ʼ��Դ���(begin from 0, ������class_id��conditions)
	 *             num: Ҫ��ȡ����Դ����(������class_id��conditions)
	 *      conditions: ��Ҫ��ȡ�������б�  (��ʽ: array('time'=>'20120524203454'): ���ڵ��ڴ�ʱ������Դ)
	 * ��ϸ����: ��ȡclass_id����������conditions�Ĵ�start��ʼ��num����Դ��Ϣ
	 */

	Track[] getResInfo(int classId, int start, int num, String conditions) throws JSONException, WebServiceError;
	
}
