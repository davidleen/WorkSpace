package com.giants3.reader.server.controller;


import com.giants3.hd.domain.api.Client;
import com.giants3.reader.entity.AuthCodes;
import com.giants3.reader.noEntity.AjaxData;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.service.AuthService;
import com.giants3.thread.ThreadConst;
import com.giants3.utils.GsonUtils;
import com.giants3.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class HelloController  extends BaseController{


Executor threadPoolExecutor= ThreadConst.create();

	Client client ;
	Map<String,String > headers;
	String requestIpUrl;
	@Autowired
	AuthService authService;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		client=new Client();
		String appcode = "2cd138aaedc741bcb8bd1b708a5c2d32";
		headers=new HashMap<>();
		headers.put("Authorization", "APPCODE " + appcode);

		requestIpUrl=	"http://hcapi20.market.alicloudapi.com/ip?ip=%s";

	}

	/**

	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "authcodes",method = RequestMethod.GET)
//	public AjaxData<String[]> getAuthCodes( ) {
//
//
//		Resource resource = new ClassPathResource("test.json");
//		try {
//
//
//			String json = de.greenrobot.common.io.FileUtils.readUtf8(resource.getFile());
//
//		AjaxData<String[]> ajaxData=GsonUtils.fromJson(json, new ParameterizedType() {
//				@Override
//				public Type[] getActualTypeArguments() {
//					return new Type[]{String[].class};
//				}
//
//				@Override
//				public Type getRawType() {
//					return AjaxData.class;
//				}
//
//				@Override
//				public Type getOwnerType() {
//					return null;
//				}
//			});
//		return ajaxData;
//			//return  sql;
////			String s = new JsonParser().parse(sql).toString();
////			return s;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	 	return null;
//	}
	/**
	 *
	 *

	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "authcodes",method = RequestMethod.GET)
	public AjaxData<AuthCodes> getAuthCodes( @RequestParam(value = "draw" ,required = false ,defaultValue = "0")int drawCount,@RequestParam(value = "platform",required = false,defaultValue = "") String platform,@RequestParam(value = "search[value]",required = false,defaultValue = "") String keyWord) {



		RemoteData<AuthCodes> allAuthCodes = authService.getAuthCodesByKey(keyWord,platform);

		AjaxData<AuthCodes> result=new AjaxData<>();
		result.draw=drawCount+1;
		result.recordsFiltered=allAuthCodes.totalCount;
		result.recordsTotal=allAuthCodes.totalCount;
		result.data=allAuthCodes.datas;

		return result;
	}



	@ResponseBody
	@RequestMapping(value = "msg",method = RequestMethod.GET)
	public String getIds(  @RequestParam(value = "platform",required = false,defaultValue = "") String platform,@RequestParam(value = "key",required = false,defaultValue = "") String keyWord,@RequestParam(value = "aka",required = false,defaultValue = "0") int packageHashCode) {

			if (
					packageHashCode==-1434608682 //com.changdu.portugalreader
						//	||packageHashCode==-191994250  //com.jiasoft.swreader
				//	||packageHashCode==2012078905 //com.changdud
					||packageHashCode==1042834355 //com.changdu.ereader
					||packageHashCode==1204462450 //com.changdu.frenchreader
					||packageHashCode==-1214014313 //com.changdu.spainreader

			)
			{
				return "";
			}



		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					authService.logAuthCodeReadTime();
				}catch (Throwable t){}

			}
		});

		RemoteData<AuthCodes> allAuthCodes = authService.getAuthCodesByKey(keyWord,platform);

		int size = allAuthCodes.datas.size();
		String[] result=new String[size+1];
		for (int i = 0; i < size; i++) {

			result[i]=allAuthCodes.datas.get(i).code;
		}
		result[size]=String.valueOf(authService.getSettings().rate);
		String combine = StringUtils.combine(result,";;;");
		try {


			byte[] bytes =  combine .getBytes("UTF-8");
			String s = new String(Base64.getEncoder().encode(bytes), "UTF-8");

			return s;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";


	}


	@ResponseBody
	@RequestMapping(value = "addCode",method =  RequestMethod.POST)
	public AjaxData<Void> addAuthCode( @RequestParam(value = "code"   ) String code  , @RequestParam(value = "platform"   ) String platform   ) {


		 authService.addNewCode(code,platform);
		return new AjaxData<>();
	}

	@ResponseBody
	@RequestMapping(value = "setRate",method =  RequestMethod.POST)
	public AjaxData<Void> setRate( @RequestParam(value = "rate"   ) int rate     ) {


		authService.setRate(rate);
		return new AjaxData<>();
	}

	@ResponseBody
	@RequestMapping(value = "deleteAuthCode",method =  RequestMethod.POST)
	public AjaxData<Void> addAuthCode( @RequestParam(value = "id"   ) long id     ) {


		 authService.deleteAuthCode(id);
		return new AjaxData<>();
	}

	@ResponseBody
	@RequestMapping(value = "location",method =  RequestMethod.GET)
	public IPResult  location(  HttpServletRequest request   ) {


		String url=String.format(requestIpUrl,request.getRemoteAddr());
		//String url=String.format(requestIpUrl,"localhost");
		String result = "";
		try {
		  result=	client.getWithStringReturned(url, headers );
		} catch (Throwable e) {
			e.printStackTrace();
		}
		IPResult ipResult=null;
		if(!StringUtils.isEmpty(result))
		{
			  ipResult=GsonUtils.fromJson(result,IPResult.class);
			return ipResult;

		}

		return ipResult;
	}


	@ResponseBody
	@RequestMapping(value = "cid",method =  RequestMethod.GET)
	public String  getCityId(  HttpServletRequest request   ) {

		IPResult location = location(request);
		if(location!=null&&location.ret==200)
		{
			return location.data==null?"":location.data.city_id;
		}


		return "";
	}





	public class IPResult
	{
		public String msg;
		public int ret;
		public String log_id;
		public IPInfo data;
	}


	public class IPInfo
	{
		//Long 类型 IP 地址
		public String long_ip;
		//IP 地址
		public String ip;
		//运营商
		public String isp;
		//国家
		public String country;
		//国家编号
		public String country_id;
		//地区
		public String area;
		//省份
		public String region;
		//省份编号
		public String region_id;
		//城市
		public String city;
		//城市编号
		public String city_id;
	}


}