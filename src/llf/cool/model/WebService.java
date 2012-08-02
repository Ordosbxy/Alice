package llf.cool.model;

import org.json.JSONException;

/**
* @author  bxy
* @version 创建时间：2012-7-26 上午10:25:02
* 
*/
public interface WebService {
	
	/**
	 * 接口名称: GetSubResClass
	 * 功能描述: 获取资源分类
	 * 输入参数: class_id: 分类所属的父分类id, 0表示根分类
	 * 详细描述: 获取class_id下资源分类信息
	 * 输出参数: 无
	 * 返 回 值  : 资源分类信息列表
	 *        列表项格式:array('id',        // 分类id
	                       'title'      // 名称, GBK2HexString
	                       'icon_name'  // 图标文件名, GBK2HexString
	                       'res_num);   // 此分类下的资源数量
     */
	RootResource[] getSubResClass(int classId) throws JSONException, WebServiceError;
	
	/** 
	 * 接口名称: GetResInfo
	 * 功能描述: 获取资源信息
	 * 输入参数: class_id: 资源所属的分类id, 0表示所有
	 *           start: 起始资源序号(begin from 0, 受限于class_id与conditions)
	 *             num: 要获取的资源数量(受限于class_id与conditions)
	 *      conditions: 需要获取的条件列表  (格式: array('time'=>'20120524203454'): 大于等于此时间点的资源)
	 * 详细描述: 获取class_id下满足条件conditions的从start起始的num条资源信息
	 */

	Track[] getResInfo(int classId, int start, int num, String conditions) throws JSONException, WebServiceError;
	
}
