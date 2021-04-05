//package com.xxx.reader;
//
//import android.graphics.Typeface;
//import android.text.TextUtils;
//
//import com.changdu.ApplicationInit;
//import com.changdu.BaseActivity;
//import com.changdu.ActivityType;
//import com.changdu.BuildConfig;
//import com.changdu.R;
//import com.changdu.changdulib.common.sdk.Version4;
//import com.changdu.changdulib.util.Log;
//import com.changdu.changdulib.util.storage.StorageUtils;
//import com.changdu.common.ActivityManager;
//import com.changdu.common.ActivityManager.ActivityFilter;
//import com.changdu.database.DataBaseManager;
//import com.changdu.download.DownloadData;
//import com.changdu.netprotocol.NdDataHelper;
//import com.changdu.netprotocol.NdPlugInData;
//import com.changdu.netprotocol.ProtocolData;
//import com.changdu.netprotocol.ProtocolData.FontInfo;
//import com.changdu.setting.SettingContent;
//import com.changdu.util.Conver;
//import com.changdu.util.ResourceExtractor;
//import com.changdu.util.file.FileUtil;
//import com.giants3.android.frame.util.StringUtil;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static android.os.Build.VERSION.SDK;
//import static com.changdu.ApplicationInit.baseContext;
//import static com.changdu.ActivityType.typeface;
//import static com.changdu.SystemConst.SDK_VERSION_3;
//import static com.changdu.payment.PaymentEntity.STATE_PRICE_CHARGE;
//import static com.changdu.payment.PaymentEntity.STATE_PRICE_DOWNLOAD;
//import static com.changdu.payment.PaymentEntity.STATE_PRICE_FREE;
//import static com.changdu.payment.PaymentEntity.STATE_PRICE_NONE;
//import static com.changdu.payment.PaymentEntity.STATE_PRICE_PURCHASED;
//import static com.changdu.payment.PaymentEntity.TAG_LOCAL;
//import static com.changdu.payment.PaymentEntity.TAG_SEVICE;
//import static com.changdu.payment.PaymentEntity.TAG_SYSTEM;
//import static com.changdu.setting.color.TypefaceEntity.POSITION_DEFAULT;
//
//public class TypefaceHelper {
//    public static final int KEY_TYPEFACE_ENTITY = 4000;
//    public static final int KEY_TYPEFACE_POSITION = 4001;
//    public static final String NAME_TYPEFACE = ApplicationInit.baseContext.getString(R.string.path_font);
//    public static final String RELATIVE_PATH_TYPEFACE = NAME_TYPEFACE + File.separator;
//
//    public static final String EXTENSION_TYPEFACE = ".ttf";
//    public static final String EXTENSION_TYPEFACE_OTF = ".otf";
//    public static final String EXTENSION_TYPEFACE_UPPERCASE = ".TTF";
//
//    private static final String TAG_PRICE_FREE = "0";
//
//    private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();
//
//    public static Typeface getTypeface(String typefaceName) {
//        if(StringUtil.isEmpty(typefaceName)) return Typeface.SERIF;
//
//        return null;
//    }
//
//
//    private static File findTypefaceFile(String typefaceName, String relativePathTypeface) {
//        File typefaceFile1 = new File(StorageUtils.getAbsolutePathIgnoreExist(relativePathTypeface) + Conver.getInstance().ConverToSimplified(typefaceName) + EXTENSION_TYPEFACE);
//        File typefaceFile2 = new File(StorageUtils.getAbsolutePathIgnoreExist(relativePathTypeface) + Conver.getInstance().ConverToSimplified(typefaceName) + EXTENSION_TYPEFACE_UPPERCASE);
//        File typefaceFile3 = new File(StorageUtils.getAbsolutePathIgnoreExist(relativePathTypeface) + typefaceName + EXTENSION_TYPEFACE_OTF);
//        return typefaceFile1.exists() ? typefaceFile1 : (typefaceFile2.exists() ? typefaceFile2 : typefaceFile3.exists() ?typefaceFile3:null);
//    }
//
//    public static void clearTypefaceCache() {
//        if (typefaceCache != null && !typefaceCache.isEmpty()) {
//            typefaceCache.clear();
//        }
//    }
//
//    public static void checkTypefaceSetting() {
//        String typtfaceName = SettingContent.getInstance().getTextStyleName();
//        if (TextUtils.isEmpty(typtfaceName) || !defaultTypeface.equals(typtfaceName) && !isLocalTypeface(typtfaceName)) {
//            SettingContent.getInstance().setTextStyle(defaultTypeface, 0);
//        }
//    }
//
//    /**
//     * 判断是对应字体是否在本地sd 卡 目录下，  简繁体适配处理。
//     * @param typeName
//     * @return
//     */
//    public static boolean isLocalTypeface(String typeName) {
//
//
//        String fontTypePath = StorageUtils.getAbsolutePath(RELATIVE_PATH_TYPEFACE);
//        //字体简繁体  后缀名大小写 都判断过去。
//        //简体 后缀小写
//        String typeNameSimple = Conver.getInstance().ConverToSimplified(typeName);
//
//        File tempFile = new File(fontTypePath + typeNameSimple + EXTENSION_TYPEFACE);
//        if (tempFile.exists()) return true;
//        //简体 后缀大写
//        tempFile = new File(fontTypePath + typeNameSimple + EXTENSION_TYPEFACE_UPPERCASE);
//        if (tempFile.exists()) return true;
//
//
//        //繁体 后缀小写
//        String typeNameTraditional = Conver.getInstance().ConverToTraditianl(typeName);
//        tempFile = new File(fontTypePath + typeNameTraditional + EXTENSION_TYPEFACE);
//        if (tempFile.exists()) return true;
//
//        //繁体 后缀大写
//        tempFile = new File(fontTypePath + typeNameTraditional + EXTENSION_TYPEFACE_UPPERCASE);
//        if (tempFile.exists()) return true;
//
//
//        tempFile = new File(fontTypePath + typeName + EXTENSION_TYPEFACE_OTF);
//        if (tempFile.exists()) return true;
//
//        return false;
//
//    }
//
//    //每次退出字体Activity重置一次
//    public static void resetControl() {
//        curPI = DEFAULT_PI;
//        flag = DEFAULT_FLAG;
//        if (typefaceList != null && !typefaceList.isEmpty() && typefaceList.size() > 0) {
//            typefaceList.clear();
//            typefaceList = null;
//        }
//    }
//
//    //缓存
//    private static ArrayList<TypefaceEntity> typefaceList;
//
//    //字体数量控制
//    public synchronized static ArrayList<TypefaceEntity> getTypefaceList(int count, boolean needServer) {
//        ArrayList<TypefaceEntity> typefaceEntityList = new ArrayList<TypefaceEntity>();
//        if (needServer) {
//            //由于第一次进入Activity已经取得系统字体和本地字体，就不须累加了
//            while (typefaceList.size() < count && flag) {
//                List<TypefaceEntity> tmpList = getTypefaceEntityListFromSevice();
//                if (tmpList != null) {
//                    if (!tmpList.isEmpty()) {
//                        typefaceList.addAll(tmpList);
//                    }
//                } else {
//                    break;
//                }
//            }
//        } else {
//            typefaceList = new ArrayList<TypefaceEntity>();
//            typefaceList.addAll(getTypefaceEntityListFromSystem());
//            typefaceList.addAll(getTypefaceEntityListFromLocal());
//        }
//
//        if (typefaceList.size() > count) {
//            typefaceEntityList.addAll(typefaceList.subList(0, count));
//        } else {
//            typefaceEntityList.addAll(typefaceList);
//        }
//
//        return typefaceEntityList;
//    }
//
//    public static void shellTypefaceList(TypefaceEntity entity, int count, OnShellListener listener) {//暂时刷新手段
//        if (entity != null) {//未购买->已购买
//            for (int i = 0; i < typefaceList.size(); i++) {
//                TypefaceEntity tmp = typefaceList.get(i);
//                if (entity.getName().equals(tmp.getName())
//                        && entity.getItemId().equals(tmp.getItemId())) {
//                    tmp.setPriceState(entity.getPriceState());
//                }
//            }
//        }
//
//        ArrayList<DownloadData> lsdt = DataBaseManager.getEZineDB().getDownloadData();
//        for (int i = 0; i < lsdt.size(); i++) {
//            if (lsdt.get(i).getType() == DownloadData.RESOURCE_TYPE_TYPEFACE) {
//                for (int j = 0; j < typefaceList.size(); j++) {
//                    String tmpId = ApplicationInit.baseContext.getString(R.string.path_font) + "_" + typefaceList.get(j).getName();
//                    if (lsdt.get(i).getId().equals(tmpId)) {
//                        typefaceList.get(j).setDownloadState(lsdt.get(i).getDownloadState());
//                        int tmp = typefaceList.get(j).getProgress() > lsdt.get(i).getProcess() ?
//                                typefaceList.get(j).getProgress() : lsdt.get(i).getProcess();
//                        typefaceList.get(j).setProgress(tmp);
//                    }
//                }
//            }
//        }
//
//        ArrayList<TypefaceEntity> typefaceEntityList = new ArrayList<TypefaceEntity>();
//        if (typefaceList.size() > count) {
//            typefaceEntityList.addAll(typefaceList.subList(0, count));
//        } else {
//            typefaceEntityList.addAll(typefaceList);
//        }
//
//        listener.onFinish(typefaceEntityList);
//    }
//
//    public static void updateTypefaceList(int i, int downloadState, int percent) {
//        if (i >= 0 && i < typefaceList.size() && typefaceList.get(i) != null) {
//            TypefaceEntity entity = typefaceList.get(i);
//            entity.setDownloadState(downloadState);
//            entity.setProgress(percent);
//        }
//    }
//
//    public interface OnShellListener {
//        void onFinish(ArrayList<TypefaceEntity> typefaceEntityList);
//    }
//
//    //系统字体（用）
//    private static List<TypefaceEntity> getTypefaceEntityListFromSystem() {
//        TypefaceEntity entity = new TypefaceEntity();
//        entity.setName(defaultTypeface);
//        entity.setPriceState(STATE_PRICE_FREE);
//        entity.setTypefaceTag(TAG_SYSTEM);
//        entity.setPosition(POSITION_DEFAULT);
//        entity.setResourceType(DownloadData.RESOURCE_TYPE_TYPEFACE);
//        return Arrays.asList(entity);
//    }
//
//    //本地字体（用）
//    private static List<TypefaceEntity> getTypefaceEntityListFromLocal() {
//        List<TypefaceEntity> typefaceEntityList = new ArrayList<TypefaceEntity>(0);
//
//        File typefaceFile = null;
//        try {
//            typefaceFile = new File(StorageUtils.getAbsolutePath(RELATIVE_PATH_TYPEFACE));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (typefaceFile != null && typefaceFile.exists()) {
//            File[] fileArrays = FileUtil.listFiles(typefaceFile, new FileFilter() {
//                @Override
//                public boolean accept(File file) {
//                    return file.getName().toLowerCase().endsWith(EXTENSION_TYPEFACE) || file.getName().toLowerCase().endsWith(EXTENSION_TYPEFACE_OTF);
//                }
//            }, true);
//
//            if (fileArrays != null && fileArrays.length > 0) {
//                int arrayLength = fileArrays.length;
//                typefaceEntityList = new ArrayList<TypefaceEntity>(arrayLength);
//                for (File file : fileArrays) {
//                    String filename = file.getName();
//
//                    TypefaceEntity entity = new TypefaceEntity();
//                    String name ="";
//                    if (filename.contains(EXTENSION_TYPEFACE_OTF)){
//                        name = Conver.getInstance().ConverToSimplified(filename.substring(0, filename.toLowerCase().lastIndexOf(EXTENSION_TYPEFACE_OTF)));
//                    }else{
//                        name = Conver.getInstance().ConverToSimplified(filename.substring(0, filename.toLowerCase().lastIndexOf(EXTENSION_TYPEFACE)));
//                    }
//
//                    entity.setName(name);
//                    entity.setPriceState(STATE_PRICE_FREE);
//                    entity.setTypefaceTag(TAG_LOCAL);
//                    entity.setPosition(POSITION_DEFAULT);
//                    entity.setRelativePath(RELATIVE_PATH_TYPEFACE + Conver.getInstance().ConverToSimplified(filename));
//                    entity.setResourceType(DownloadData.RESOURCE_TYPE_TYPEFACE);
//
//                    typefaceEntityList.add(entity);
//                }
//            }
//        }
//
//        return typefaceEntityList;
//    }
//
//    //缓存页数信息
//    private static final int DEFAULT_PI = 0;
//    private static int curPI = DEFAULT_PI;
//    //控制可否继续从系统取得字体列表
//    private static final boolean DEFAULT_FLAG = true;
//    private static boolean flag = DEFAULT_FLAG;
//    private static String vipBaseUrl;
//
//    //服务器字体（用）
//    private static List<TypefaceEntity> getTypefaceEntityListFromSevice() {
//        List<TypefaceEntity> typefaceList = new ArrayList<TypefaceEntity>();
//        NdPlugInData ndData = NdDataHelper.getTypefaceList(curPI + 1, 10);
//        if (ndData != null) {
//            curPI++;
//            if (ndData.getPageIndex() == ndData.getPageNum()) {
//                flag = false;
//            }
//            vipBaseUrl = ndData.getVipBaseUrl();
//            ArrayList<NdPlugInData.PlugInData> dataList = ndData.getDataList();
//            //转换类型
//            for (int i = 0; i < dataList.size(); i++) {
//                NdPlugInData.PlugInData tmpData = dataList.get(i);
//                TypefaceEntity entity = new TypefaceEntity();
//                entity.setTypefaceTag(TAG_SEVICE);
//                entity.setId(Long.toString(tmpData.getId()));
//                entity.setItemId(tmpData.getItemId());
//                entity.setName(tmpData.getName());
//                entity.setSize(tmpData.getSize());
//                entity.setPosterUrl(tmpData.getPosterUrl());
//
//                //价格格式转换
//                Float price = tmpData.getPrice();
//                entity.setPrice(String.valueOf(price.intValue()));
//
//                if (isLocalTypeface(tmpData.getName())) {//跳过本地有的字体;
//                    continue;
//                } else if (tmpData.getPrice() == Float.parseFloat(TAG_PRICE_FREE)) {//免费
//                    entity.setBaseUrl(tmpData.getDownloadUrl());
//                    entity.setPriceState(STATE_PRICE_FREE);
//                } else if (tmpData.isPurchased()) {//已经付费过
//                    int stateDownload = isLocalTypeface(tmpData.getName()) ? STATE_PRICE_DOWNLOAD : STATE_PRICE_NONE;//是否已经下载的标识
//                    entity.setBaseUrl(vipBaseUrl);
//                    entity.setPriceState(STATE_PRICE_PURCHASED | stateDownload);
//                } else {//付费
//                    entity.setBaseUrl(vipBaseUrl);
//                    entity.setPriceState(STATE_PRICE_CHARGE);
//                }
//                entity.setResourceType(DownloadData.RESOURCE_TYPE_TYPEFACE);
//                typefaceList.add(entity);
//            }
//        } else {
//            //网络异常
//            BaseActivity baseActivity = ActivityManager.getInstance().peek(new ActivityFilter() {
//
//                @Override
//                public boolean accept(BaseActivity activity) {
//                    boolean result = false;
//                    ActivityType activityType = activity.getActivityType();
//                    if (typeface.equals(activityType)) {
//                        result = true;
//                    }
//
//                    return result;
//                }
//            });
//
//
//            typefaceList = null;
//        }
//        return typefaceList;
//    }
//
//    public static ArrayList<FontInfo> getLocalFont() {
//        ArrayList<FontInfo> info = new ArrayList<FontInfo>();
//        List<TypefaceEntity> typefaceList = getTypefaceEntityListFromLocal();
//        if (typefaceList.size() > 0) {
//            for (int i = 0; i < typefaceList.size(); i++) {
//                FontInfo mInfo = new ProtocolData().new FontInfo();
//                mInfo.fontName = typefaceList.get(i).getName();
//                //mInfo.fontId = -1;
//                //mInfo.hasBuy = true;
//                //mInfo.price = 0;
//                info.add(mInfo);
//            }
//        }
//
//        return info;
//    }
//
//
//    /**
//     * 查询指定的字体是否存在sd卡中
//     *
//     * @param name
//     * @return
//     */
//    public static boolean searchLocalFont(String name) {
//        ArrayList<FontInfo> mlocalFont = getLocalFont();
//        if (mlocalFont.size() > 0) {
//            for (int i = 0; i < mlocalFont.size(); i++) {
//                String localFontName = mlocalFont.get(i).fontName;
//                if (BuildConfig.DEBUG)
//                    Log.e("name:" + name + ",localFontName:" + localFontName + ",Conver.getInstance().ConverToSimplified(name):" + Conver.getInstance()
//                            .ConverToSimplified(name));
//                if (name.equals(localFontName)
//                        || Conver.getInstance()
//                        .ConverToSimplified(name)
//                        .equals(Conver.getInstance().ConverToSimplified(localFontName))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * 字体迁移，新代码 字体放在font文件夹下，
//     *
//     * 从旧的字体/字體 文件夹改名成 font
//     */
//    public static  void  correctFontPath()
//    {
//        String relativePathTypeface = ResourceExtractor.getString(R.string.path_font) ;
//        String fontPath= StorageUtils.getAbsolutePathIgnoreExist(relativePathTypeface);
////        Log.e("fontPath:"+fontPath);
//        File file = new File(fontPath);
//        if(file.exists()) return ;
//
//
//
//
//        String oldFontPath2=StorageUtils.getAbsolutePathIgnoreExist( "字體" );
//        Log.e("oldFontPath2:"+oldFontPath2);
//        File oldFontFile2 = new File(oldFontPath2);
//        if(oldFontFile2.exists())
//        {
//
//            boolean   result=oldFontFile2.renameTo(file);
//            if(result)
//            {
//                return;
//            }
//            Log.e("renameTo:"+result);
//        }
//        String oldFontPath=StorageUtils.getAbsolutePathIgnoreExist( "字体" );
//        Log.e("oldFontPath:"+oldFontPath);
//        File oldFontFile = new File(oldFontPath);
//        if(oldFontFile.exists())
//        {
//
//            boolean   result=oldFontFile.renameTo(file);
//            if(result)
//            {
//                return;
//            }
//            Log.e("renameTo:"+result);
//        }
//
//    }
//}
