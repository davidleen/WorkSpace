package com.giants3.hd.server.controller;


import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.entity.MaterialEquation;
import com.giants3.hd.entity.MaterialType;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.MaterialRelateService;
import com.giants3.hd.server.service.MaterialService;
import com.giants3.hd.server.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Calendar;
import java.util.List;


/**
 * 材料信息控制类。
 */
@Controller
@RequestMapping("/material")
public class MaterialController extends BaseController {

    private static final String TAG = "MaterialController";
    private static final Logger logger = Logger.getLogger(MaterialController.class);


    @Autowired
    private MaterialRelateService materialRelateService;


    @Autowired
    private MaterialService materialService;


    @Value("${materialfilepath}")
    private String rootFilePath;


    public MaterialController() {


    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> list() {


        return wrapData(materialService.findAll());
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> searchMaterial(@RequestParam(value = "codeOrName", required = false, defaultValue = "") String codeOrName, @RequestParam(value = "classId", required = false, defaultValue = "") String classId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return materialService.searchMaterial(codeOrName, classId, pageIndex, pageSize);

    }

    @RequestMapping(value = "/searchInService", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> searchInService(@RequestParam(value = "codeOrName", required = false, defaultValue = "") String codeOrName, @RequestParam(value = "classId", required = false, defaultValue = "") String classId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {


        return materialService.searchInService(codeOrName, classId, pageIndex, pageSize);

    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<Void> saveList(@RequestBody List<Material> materials) {


        materialService.saveList(materials);


        return wrapData();
    }


    /**
     * 同步erp 数据
     *
     * @return
     */
    @RequestMapping(value = "/syncERP", method = {RequestMethod.GET, RequestMethod.POST})

    public
    @ResponseBody
    RemoteData<Void> syncERP() {

        try {
            return materialService.syncERP();
        } catch (Exception e) {

            logger.error(this, e);
            return wrapError(e.getMessage());
        }

    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<Material> save(@RequestBody Material material) {


        return materialService.saveOrUpdate(material);


    }


    @RequestMapping(value = "/findListByCodes", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByCodes(@RequestBody List<String> codes) {


        List<Material> materials = materialService.getMaterialsByCodes(codes);


        return wrapData(materials);
    }


    @RequestMapping(value = "/findListByNames", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByNames(@RequestBody List<String> names) {


        List<Material> materials = materialService.getMaterialsForNames(names);


        return wrapData(materials);
    }


    /**
     * 获取材料类型类别
     *
     * @return
     */

    @RequestMapping(value = "/listClass", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialClass> listClass() {


        return materialRelateService.listClass();


    }


    /**
     * 删除材料类型类别
     *
     * @return
     */

    @RequestMapping(value = "/deleteClass", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> deleteClass(@RequestParam(value = "materialClassId") long materialClassId) {


        return materialRelateService.deleteClass(materialClassId);


    }


    /**
     * 更新材料类型类别
     *
     * @return
     */

    @RequestMapping(value = "/updateClass", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<MaterialClass> updateClass(@RequestBody MaterialClass materialClass) {


        return materialService.updateClass(materialClass);


    }


    /**
     * 获取材料类型类别
     *
     * @return
     */

    @RequestMapping(value = "/listType", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialType> listType() {


        return wrapData(materialRelateService.listTypes());
    }


    /**
     * 获取材料计算公式。
     *
     * @return
     */

    @RequestMapping(value = "/listEquation", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialEquation> listEquation() {


        ;
        return wrapData(materialRelateService.listEquation());
    }


    /**
     * 删除产品信息
     *
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})

    public
    @ResponseBody
    RemoteData<Void> logicDelete(@RequestParam("id") long materialId) {


        return materialService.logicDelete(materialId);


    }


    /**
     * 同步材料图片数据
     *
     * @return
     */

    @RequestMapping(value = "/syncPhoto", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RemoteData<Void> syncPhoto() {


        return materialService.synPhoto();

    }


    /**
     * 更新材料的缩略图数据
     *
     * @param material
     */
    private final void updateMaterialPhoto(Material material) {


        String filePath = FileUtils.getMaterialPicturePath(rootFilePath, material.code, material.classId);


        String url;
        //如果tup图片文件不存在  则 设置photo为空。
        if (!new File(filePath).exists()) {
            material.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());

        } else {


        }

        url = FileUtils.getMaterialPictureURL(material.code, material.classId, material.lastPhotoUpdateTime);
        material.url = url;


    }


    @RequestMapping(value = "/saveClassList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<MaterialClass> saveClassList(@RequestBody List<MaterialClass> materialClasses) {


        return materialRelateService.saveClassList(materialClasses);

    }


    public void updateProductData() {
        materialService.updateProductData();
    }
}
