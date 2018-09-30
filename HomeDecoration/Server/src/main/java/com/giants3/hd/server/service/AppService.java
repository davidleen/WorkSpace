package com.giants3.hd.server.service;

import com.giants3.hd.entity.AppVersion;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Module;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.repository.CustomerRepository;
import com.giants3.hd.server.repository.ModuleRepository;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * app 版本
 * Created by david on 2016/2/15.
 */
@Service
public class AppService extends AbstractService {

    @Autowired
    AppVersionRepository appVersionRepository;
    @Autowired
    ModuleRepository moduleRepository;


    @Value("${appfilepath}")
    private String appFilePath;
    @Value("${serverVersion}")
    private int serverVersion;

   public   AppVersion getLatestVersion()
     {

        return appVersionRepository.findFirstByAppNameLikeOrderByVersionCodeDescUpdateTimeDesc("%%");
     }





    @Transactional
    public void checkVersions()
    {
        //版本数据初始化。

        AppVersion appVersion = null;

        File f = new File(appFilePath);
        if (f.isDirectory()) {

            File[] files = f.listFiles();

            for (File aFile : files) {

                if (aFile.getName().endsWith(".jar")) {
                    String fileName = aFile.getName();
                    //读取相关的版本配置文件
                    Properties properties = readZipFile(aFile.getAbsolutePath(), "version.properties");
                    appVersion = new AppVersion();
                    appVersion.memo = properties.getProperty("Version_Spec");
                    appVersion.versionName = properties.getProperty("Version_Name");
                    String versionCodeString = properties.getProperty("Version_Number");
                    appVersion.updateTime = Calendar.getInstance().getTimeInMillis();
                    appVersion.timeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(appVersion.updateTime));
                    appVersion.versionCode = StringUtils.isEmpty(versionCodeString) ? -1 : Integer.valueOf(versionCodeString);
                    appVersion.appName = fileName;
                    appVersion.fileSize = aFile.length();
                    break;


                }


            }

        }


        if (appVersion != null) {
            //核对客户端最新版本
            AppVersion oldVersion = appVersionRepository.findFirstByAppNameEqualsOrderByVersionCodeDescUpdateTimeDesc(appVersion.appName);

            if (oldVersion == null) {

                appVersionRepository.save(appVersion);
            } else if (oldVersion.versionCode < appVersion.versionCode) {
                //添加记录
                appVersionRepository.save(appVersion);

            } else if (oldVersion.versionCode == appVersion.versionCode) {
                //版本一致  文件大小不一致。  使用当前文件大小

                if (oldVersion.fileSize != appVersion.fileSize) {
                    appVersion.id = oldVersion.id;
                    //修改当前记录
                    appVersionRepository.save(appVersion);

                }


            }


        }
    }
    public static Properties readZipFile(String zipFilePath, String relativeFilePath) {

        Properties
                properties = new Properties();
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                // if the entry is not directory and matches relative file then extract it
                if (!entry.isDirectory() && entry.getName().equals(relativeFilePath)) {
                    InputStream bis = zipFile.getInputStream(entry);
                    Reader reader = new InputStreamReader(bis, "UTF-8");
                    properties.load(reader);
                    reader.close();
                    bis.close();
                    break;

                } else {
                    continue;
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return properties;
    }

    @Transactional
    public void updateModuleIfNeeded() {

        List<Module> moduleList = Module.getInitDataList();
        int newSize = moduleList.size();

        int existSize = moduleRepository.findAll().size();
        //模块数据初始化
        if (existSize != newSize) {

            for (Module module : Module.getInitDataList()) {

                Module oldModule = moduleRepository.findFirstByNameEquals(module.name);
                if (oldModule == null) {
                    oldModule = new Module();
                    oldModule.name = module.name;
                    oldModule.title = module.title;
                    moduleRepository.save(module);

                }


            }

            moduleRepository.flush();

        }
    }
}
