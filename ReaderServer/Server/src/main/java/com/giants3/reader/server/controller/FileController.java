package com.giants3.reader.server.controller;

import com.giants3.utils.Assets;
import com.giants3.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by davidleen29 on 2018/5/19.
 */
@Controller
@RequestMapping("/file")
public class FileController extends  BaseController  {

    @Value("${rootPath}")
    private String rootPath;



    @RequestMapping(value = "/{filePath:.+}", method = RequestMethod.GET )
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable("filePath") String filePath  ) {


        logger.info("filePath====:"+filePath);

        logger.info("filePath:"+filePath);
        String[] fileName=filePath.split("__");
      String assetSourceRelativePath=  Assets.urlToPath(filePath);

        logger.info(filePath);
        String destFilePath=rootPath+assetSourceRelativePath;

        FileSystemResource resource = new FileSystemResource(destFilePath);

        if(resource.exists()) return resource;
        return null;




    }

}
