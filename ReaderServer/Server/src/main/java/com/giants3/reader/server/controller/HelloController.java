package com.giants3.reader.server.controller;


import com.giants3.crypt.CryptUtils;
import com.giants3.reader.entity.AuthCodes;
import com.giants3.reader.noEntity.AjaxData;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.service.AuthService;
import com.giants3.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class HelloController {



	@Autowired
	AuthService authService;
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
	public String getIds(  @RequestParam(value = "platform",required = false,defaultValue = "") String platform,@RequestParam(value = "key",required = false,defaultValue = "") String keyWord) {

		try {
			authService.logAuthCodeReadTime();
		}catch (Throwable t){}
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

		return null;


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




}