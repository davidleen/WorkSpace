package com.giants3.hd.controller;

import com.giants3.hd.domain.api.ApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 文件上传控制类
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
    @Value("${rootpath}")
    private String rootpath;

    @Value("${filepath}")
    private String productFilePath;

    @Value("${deletedProductPhotoFilePath}")
    private String deleteProductFilePath;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;
    //订单唛头文件夹
    @Value("${maitoufilepath}")
    private String maitoufilepath;

    @Value("${materialfilepath}")
    private String materialFilePath;
    @Value("${quotationfilepath}")
    private String quotationfilepath;


    @Value("${erpPhotolFilePath}")
    private String erpPhotolFilePath;

    @Value("${appfilepath}")
    private String appFilePath;


    private String serverBaseUrl;


    private ApiManager apiManager;


    @Override
    public void afterPropertiesSet() throws Exception {

        apiManager=new ApiManager();


        try {
            InetAddress addr = (InetAddress) InetAddress.getLocalHost();


            serverBaseUrl="http://"+addr.getHostAddress()+":8080/Server/";
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public void destroy() throws Exception {
        if(apiManager!=null)
            apiManager.close();
    }

    @RequestMapping(value = "/download/product/{name}/{pVersion}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @PathVariable String pVersion, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getProductPicturePath(productFilePath, name, pVersion, type));


        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }


    @RequestMapping(value = "/download/product/thumbnail/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductThumbnailFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {

        if (StringUtils.isEmpty(name)) return null;

        FileSystemResource resource = new FileSystemResource(productFilePath + "thumbnail" + FileUtils.SEPARATOR + name.replace(FileUtils.URL_PATH_SEPARATOR, FileUtils.SEPARATOR) + "." + type);

        return resource;
    }


    /**
         * 读取erp数据库的图片
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/download/erpProduct/id_no/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Resource getErpPhotoFile(HttpServletRequest request,@PathVariable String name, @RequestParam(value = "updateTime", defaultValue = "") String updateTime) {


//        if (StringUtils.isEmpty(name)) return null;
        //查找对应的文件存在 直接返回文件
        String fileName = (name + "XXXXX" + updateTime).replace("->", "-");
        String filePath = erpPhotolFilePath + fileName;

        final File file = new File(filePath);
        if(file.exists())
        return new FileSystemResource(file);

        //String serverUrl=request.getRequestURL().toString().replace("Resource","Server");
        String serverUrl=  serverBaseUrl+request.getServletPath()+request.getPathInfo()+"?"+request.getQueryString();

       apiManager.downloadUrlToFilePath(serverUrl,filePath);




        return new FileSystemResource(file);

    }

    @RequestMapping(value = "/download/product/{name}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        return getProductFile(name, "", type);


    }

    @RequestMapping(value = "/download/{category}/{name:.+}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadCategoryFile(@PathVariable String category,@PathVariable(value = "name") String fileName ) {


        return new FileSystemResource(rootpath+category+File.separator +fileName);

    }


    /**
     * 读取附件文件
     *
     * @param name
     * @param type
     * @return
     */

    @RequestMapping(value = "/download/attach/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAttachFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(attachfilepath + name.replace(AttachFileUtils.FILE_SEPARATOR, File.pathSeparator) + "." + type);
        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }

    /**
     * @param code   材料编码
     * @param mClass 材料类别   类别即为文件归类的文件夹
     * @param type   图片类型  默认jpg
     * @return
     */
    @RequestMapping(value = "/download/material/{code}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody

    public FileSystemResource getMaterialFile(@PathVariable String code, @RequestParam(value = "mClass", defaultValue = "") String mClass, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getMaterialPicturePath(materialFilePath, code, mClass, type));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }


    /**
     *
     *
     */
    @RequestMapping(value = "/download/quotation", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getQuotationFile(@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "appendix", defaultValue = "xls") String appendix) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getQuotationFile(quotationfilepath, name, appendix));

        return resource;
    }


    @RequestMapping(value = "/download/androidApp/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAndroidApp(@PathVariable String name) {


        FileSystemResource resource = new FileSystemResource(appFilePath + name + ".apk");
        return resource;


    }


    /**
     * 嘜頭文件上传
     *
     * @return
     */
    @RequestMapping(value = "/download/order/maitou", method = RequestMethod.GET)
    public
    @ResponseBody
    FileSystemResource downloadMaitouUpload(@RequestParam("os_no") String os_no) {


        return new FileSystemResource(maitoufilepath + os_no + ".xls");


    }

}
