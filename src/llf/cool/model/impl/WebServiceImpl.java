package llf.cool.model.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import llf.cool.model.RootResource;
import llf.cool.model.Track;
import llf.cool.model.WebService;
import llf.cool.model.WebServiceError;
import llf.cool.model.util.WebServiceCaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class WebServiceImpl implements WebService{

	//命名空间
	private static final String SERVICE_NAME_SPACE = "http://bigtree12.w61.mc-test.com/";
	//调用方法名称（获取资源分类）
	private static final String GetSubResClass = "GetSubResClass";
	//调用方法名称（获取资源信息）
	private static final String GetResInfo = "GetResInfo";
	//投递SOAP数据的目标地址
	private static final String SERVICE_URL = "http://bigtree12.w61.mc-test.com/yytt/soap_server.php";

	public static String[] from;
	
	private String doGet(String name){
		return null;
		//return WebServiceCaller.doGet(name);
	}
	
	/**
	 * 获取资源分类
	 * @param classId 分类所属的父分类id, 0表示根分类
	 * @return
	 */
	public RootResource[] getSubResClass(int classId){
		
		//1.实例化soap对象
		SoapObject request = new SoapObject(SERVICE_NAME_SPACE, GetSubResClass);
		//2.请求获取根分类
		request.addProperty("class_id", classId);
		//3.获得序列化的Envelop
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = false; //指定webservice的类型的（java，PHP，dotNet）
		//4.注册Envelope
		(new MarshalBase64()).register(envelope);
		
		//5.构建Android传输对象
		HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		//6.调用WebService. 参数一 : 命名空间+调用方法名称
		
		try {
			transport.call(SERVICE_NAME_SPACE + GetSubResClass, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//7.解析返回数据
		try {
			if(envelope.getResponse()==null){
				return null;
			}
		} catch (SoapFault e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SoapObject result = (SoapObject)envelope.bodyIn;
		if(result.toString()!=null && result.toString().length()>0){
			String temp = "";
			temp = result.getProperty(0).toString();
			try {
				JSONArray array = new JSONArray(temp);
				int len = array.length();
				RootResource[] resources = new RootResource[len];
				RootResourceBuilder builder = new RootResourceBuilder();
				for(int i=0; i<len; i++){
					resources[i] = builder.build( array.getJSONObject(i));
				}				
				return resources;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end if	
		
		return null;
	}

	@Override
	public Track[] getResInfo(int classId, int start, int num, String conditions) 
													throws JSONException, WebServiceError {
		// TODO Auto-generated method stub
		
		//1.实例化soap对象
		SoapObject request = new SoapObject(SERVICE_NAME_SPACE, GetResInfo);
		//2.请求获取根分类
		log(classId+", "+start+", "+num+", "+conditions+".");
		request.addProperty("class_id", classId);
		request.addProperty("start", start);
		request.addProperty("num", num);
		request.addProperty("conditions", conditions);
		
		//3.获得序列化的Envelop
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = false; //指定webservice的类型的（java，PHP，dotNet）
		//4.注册Envelope
		(new MarshalBase64()).register(envelope);
		
		//5.构建Android传输对象
		HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		//6.调用WebService. 参数一 : 命名空间+调用方法名称
		
		try {
			transport.call(SERVICE_NAME_SPACE + GetResInfo, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//7.解析返回数据
		try {
			if(envelope.getResponse()==null){
				return null;
			}
		} catch (SoapFault e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SoapObject result = (SoapObject)envelope.bodyIn;
		if(result.toString()!=null && result.toString().length()>0){
			String temp = "";
			log(">>> Property.count="+result.getPropertyCount());
			temp = result.getProperty(0).toString();
			try {
				JSONArray array = new JSONArray(temp);
				int len = array.length();
				log(">>> JSONArray.len="+len);
				if(len<=0) return null;
				
				Track[] tracks = new Track[len];
				TrackBuilder builder = new TrackBuilder();
				for(int i=0; i<len; i++){
					tracks[i] = builder.build(array.getJSONObject(i));
				}				
				return tracks;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end if	
		
		return null;
	}
	
	
	private static void log(String str) {
		// TODO Auto-generated method stub	
		Log.i("WebServiceHelper", str);
	}

}
