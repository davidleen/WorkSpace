package com.giants3.android.immersive;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.giants3.android.frame.R;
import com.giants3.android.frame.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 状态栏工具类
 * <p>
 */
public class SmartBarUtils {

    public static int getSysVersion() {
        int sysVersion = Integer.parseInt(VERSION.SDK);
        return sysVersion;
    }

    // #Begin Meizu
    private static Object getActionBar(Activity activity) {
        try {
            Method method = Class.forName("android.app.Activity").getMethod(
                    "getActionBar");
            return method.invoke(activity);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideActionBarByActivity(Activity activity) {
        Object atb = getActionBar(activity);
        if (atb != null) {
            hideActionBar(atb);
        }
    }

    private static void hideActionBar(Object actionBar) {
//    	((ActionBar) actionBar).hide();
        try {
            Method method = Class.forName("android.app.ActionBar").getMethod(
                    "hide");
            method.invoke(actionBar);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

//    public static void showActionBarByActivity(Activity activity) {
//        Object atb = getActionBar(activity);
//        if (atb != null) {
//            showActionBar(atb);
//        }
//    }

//    public static void showActionBar(Object actionBar) {
////    	((ActionBar) actionBar).hide();
//        try {
//            Method method = Class.forName("android.app.ActionBar").getMethod(
//                    "show");
//            method.invoke(actionBar);
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 调用 ActionBar.setTabsShowAtBottom(boolean) 方法。
     * <p>
     * <p>如果 android:uiOptions="splitActionBarWhenNarrow"，则可设置ActionBar Tabs显示在底栏。
     * <p>
     * <p>示例：</p>
     * <pre class="prettyprint">
     * public class MyActivity extends Activity implements ActionBar.TabListener {
     * <p>
     * protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * ...
     * <p>
     * final ActionBar bar = getActionBar();
     * bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
     * SmartBarUtils.setActionBarTabsShowAtBottom(bar, true);
     * <p>
     * bar.addTab(bar.newTab().setText("tab1").setTabListener(this));
     * ...
     * }
     * }
     * </pre>
     */
//    public static void setActionBarTabsShowAtBottom(Object actionbar, boolean showAtBottom) {
//        try {
//            Method method = Class.forName("android.app.ActionBar").getMethod(
//                    "setTabsShowAtBottom", new Class[]{boolean.class});
//            try {
//                method.invoke(actionbar, showAtBottom);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 调用 ActionBar.setActionBarViewCollapsable(boolean) 方法。
     * <p>
     * <p>设置ActionBar顶栏无显示内容时是否隐藏。
     * <p>
     * <p>示例：</p>
     * <pre class="prettyprint">
     * public class MyActivity extends Activity {
     * <p>
     * protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * ...
     * <p>
     * final ActionBar bar = getActionBar();
     * <p>
     * // 调用setActionBarViewCollapsable，并设置ActionBar没有显示内容，则ActionBar顶栏不显示
     * SmartBarUtils.setActionBarViewCollapsable(bar, true);
     * bar.setDisplayOptions(0);
     * }
     * }
     * </pre>
     */
//    public static void setActionBarViewCollapsable(Object actionbar, boolean collapsable) {
//        try {
//            Method method = Class.forName("android.app.ActionBar").getMethod(
//                    "setActionBarViewCollapsable", new Class[]{boolean.class});
//            try {
//                method.invoke(actionbar, collapsable);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 调用 ActionBar.setActionModeHeaderHidden(boolean) 方法。
     * <p>
     * <p>设置ActionMode顶栏是否隐藏。
     * <p>
     * <p>示例：</p>
     * <pre class="prettyprint">
     * public class MyActivity extends Activity {
     * <p>
     * protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * ...
     * <p>
     * final ActionBar bar = getActionBar();
     * <p>
     * // ActionBar转为ActionMode时，不显示ActionMode顶栏
     * SmartBarUtils.setActionModeHeaderHidden(bar, true);
     * }
     * }
     * </pre>
     */
//    public static void setActionModeHeaderHidden(Object actionbar, boolean hidden) {
//
//        try {
//            Method method = Class.forName("android.app.ActionBar").getMethod(
//                    "setActionModeHeaderHidden", new Class[]{boolean.class});
//            method.invoke(actionbar, true);
//
//            method = Class.forName("android.app.ActionBar").getMethod(
//                    "setDisplayShowHomeEnabled", new Class[]{boolean.class});
//            method.invoke(actionbar, false);
//
//            method = Class.forName("android.app.ActionBar").getMethod(
//                    "setDisplayShowTitleEnabled", new Class[]{boolean.class});
//            method.invoke(actionbar, false);
//
//            method = Class.forName("android.app.ActionBar").getMethod(
//                    "setDisplayUseLogoEnabled", new Class[]{boolean.class});
//            method.invoke(actionbar, false);
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 调用ActionBar.setBackButtonDrawable(Drawable)方法
     * <p>
     * <p>设置返回键图标
     * <p>
     * <p>示例：</p>
     * <pre class="prettyprint">
     * public class MyActivity extends Activity {
     * <p>
     * protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * ...
     * <p>
     * final ActionBar bar = getActionBar();
     * // 自定义ActionBar的返回键图标
     * SmartBarUtils.setBackIcon(bar, getResources().getDrawable(R.drawable.ic_back));
     * ...
     * }
     * }
     * </pre>
     */
//    public static void setBackIcon(Object actionbar, Drawable backIcon) {
//        try {
//            Method method = Class.forName("android.app.ActionBar").getMethod(
//                    "setBackButtonDrawable", new Class[]{Drawable.class});
//            try {
//                method.invoke(actionbar, backIcon);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 是否有只能导航条
     *
     * @return
     */
    public static boolean hasSmartBar() {
//    	return true;
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

//    public static void invalidatePanelMenu(Window wnd) {
//        try {
//            Method method = Class.forName("android.view.Window").getMethod(
//                    "invalidatePanelMenu", new Class[]{int.class});
//            try {
//                method.invoke(wnd, Window.FEATURE_OPTIONS_PANEL);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 判断是否透明系统状态栏  sdk19 以下不能透明状态栏。
     */
    private static boolean isStatusBarTransparent = VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public static int getNavigationBarPaddingTop(Context context) {

        //系统透明状态栏不生效 则，头部导航不增加额外空间
        if (!isTranslucentApply()) return 0;


        return getStatusBarHeight(context);

    }

    public static boolean isTranparentSupport() {
        return isStatusBarTransparent;
    }

    /**
     * 判断是否透明状态栏效果。
     *
     * @return
     */
    public static boolean isTranslucentApply() {
        return isStatusBarTransparent && statusBar != null;
    }


    private static int statusBarHeight = 0;

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight > 0) return statusBarHeight;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    /**
     * 获取系统导航条的高度。 一些机型在底部用虚拟导航条， 占用屏幕高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getSystemNavigationBarHeight(Activity activity) {

        //判断是否有导航条。 1 系统配置 false   或者 无物理键
        if (!hasNavBar(activity)) {
            return 0;
        }

        //判断是否导航条。是否隐藏
        if ((activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) { //判断是否隐藏系统导航条。
            return 0;
        }

        //判断是否 透明导航条
        WindowManager.LayoutParams winParams = activity.getWindow().getAttributes();
        if ((WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION & winParams.flags) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
            return 0;
        }


        return getConfigNavigationBarHeight(activity);
    }


    /**
     * 横屏时候，导航条的高度 并不一定是系统配置的高度。 通过计算获取。
     *
     * @param activity
     * @return
     */

    private static int getConfigNavigationBarHeight(Activity activity) {

        boolean isLandScape = false;
        Point realSize = new Point();
        Point screenSize = new Point();

        //判断 屏幕 的真实使用高度 跟 屏幕高度 比  如果差个默认的navigationbarheight  则认为 navigationbar 显示
        DisplayMetrics metrics = new DisplayMetrics();
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } else {
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
        } catch (NoSuchMethodError e) { //2.3版本无此方法
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }


        //查找contentView  如果contentView 全局位置.bottom>=metrics.heightPixels
        //也认为导航条不存在。
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView != null) {
            Rect rect = new Rect();
            contentView.getGlobalVisibleRect(rect);
            if (rect.bottom >= metrics.heightPixels) {
                return 0;
            }
        }


        isLandScape = metrics.widthPixels > metrics.heightPixels;
        //横屏处理。
        if (isLandScape) {

            realSize.x = metrics.widthPixels;
            realSize.y = metrics.heightPixels;
            try {
                activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
            } catch (NoSuchMethodError e) { //2.3版本无此方法
                return 0;
            }


            int difference = realSize.x - screenSize.x;
            return difference;
        }


        //竖屏 获取配置值
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean hasNavBar(Context context) {

//        //Emulator
//        if (Build.FINGERPRINT.startsWith("generic"))
//            return true;
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        boolean result = id > 0 && context.getResources().getBoolean(id);
        result = true;
        if (result) {
            result = false;
            Point realSize = new Point();
            Point screenSize = new Point();

            //判断 屏幕 的真实使用高度 跟 屏幕高度 比  如果差个默认的navigationbarheight  则认为 navigationbar 显示
            DisplayMetrics metrics = new DisplayMetrics();

            try {
                ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } catch (NoSuchMethodError e) { //2.3版本无此方法
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }

            realSize.x = metrics.widthPixels;
            realSize.y = metrics.heightPixels;
            try {
                ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
            } catch (NoSuchMethodError e) { //2.3版本无此方法
                return result;
            }

            if (realSize.y != screenSize.y) {
                int difference = realSize.y - screenSize.y;
                int navBarHeight = 0;
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    navBarHeight = resources.getDimensionPixelSize(resourceId);
                }
                if (navBarHeight != 0) {
                    if (difference == navBarHeight) {
                        result = true;
                    }
                }

            }

        }


        return result;
    }

    /**
     * 获取导航条高度  系统状态栏透明的情况下  默认导航条高度+系统状态栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {

        //系统透明状态栏不生效 则，头部导航不增加额外空间
        int navigationBarDefaultHeight = (int) context.getResources().getDimension(R.dimen.syt_top_bar_height);
        if (!(isTranslucentApply())) return navigationBarDefaultHeight;

        return getNavigationBarPaddingTop(context) + navigationBarDefaultHeight;

    }


    private static BarCompatible statusBar;

    static {
        String displayId = Build.DISPLAY;

        Log.e("display Id:" + displayId + ",Build.MANUFACTURER:" + Build.MANUFACTURER + ", Build.PRODUCT:" + Build.PRODUCT);

        if (VERSION.SDK_INT >= 19)
            if (RomUtils.isLightStatusBarAvailable()) {


                if (RomUtils.isMIUIV6OrAbove()) {
                    //生产厂家判断 小米ui  魅族ui
                    statusBar = new MiuiStatusBarCompatible();

                } else if (RomUtils.isFlymeV4OrAbove()) {
                    //生产厂家判断 小米ui  魅族ui
                    statusBar = new MeizuBarCompatible();
                } else if (RomUtils.isAndroidMOrAbove()) {
                    statusBar = new AndroidMUpBarCompatible();
                }
            }
//            else {
//
//                statusBar = new KitatUpStatusBarCompatible();
//            }
        Log.e("currentCompatable:" + statusBar);

    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {


        //on=false;
        if (statusBar != null) {

            statusBar.setBackgroundColor(getTopestActivity(activity).getWindow(), Color.TRANSPARENT);
            //深色皮肤包并且不是书籍阅读界面 切换状态栏字体颜色。
            boolean isDarkSkin = isDarkSkinApply(activity);
            //深色皮肤包，则切换系统状态栏的字体样式。
            statusBar.setTextMode(getTopestActivity(activity).getWindow(), (isDarkSkin ? !on : on) ? BarCompatible.BAR_MODE_DARK : BarCompatible.BAR_MODE_LIGHT);
        }


//        }
    }

    /**
     * 判断activity 是否受深色皮肤包影响
     * 切换沉浸式 头部 底色 与字体颜色
     *
     * @param activity
     * @return
     */
    private static boolean isDarkSkinApply(Activity activity) {

        return false;

    }

    /**
     * 黑字模式。
     *
     * @param activity
     */
    public static void setStatusBarDarkMode(Activity activity) {

        if (statusBar != null) {
            boolean isDarkSkin = isDarkSkinApply(activity);
            int textMode = isDarkSkin ? BarCompatible.BAR_MODE_LIGHT : BarCompatible.BAR_MODE_DARK;
            statusBar.setTextMode(getTopestActivity(activity).getWindow(), textMode);
        }
    }

    public static void setStatusBarLightMode(Activity activity) {
        if (statusBar != null) {
            boolean isDarkSkin = isDarkSkinApply(activity);
            int textMode = isDarkSkin ? BarCompatible.BAR_MODE_DARK : BarCompatible.BAR_MODE_LIGHT;
            statusBar.setTextMode(getTopestActivity(activity).getWindow(), textMode);
        }


    }


    public static Activity getTopestActivity(Activity activity) {
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        return activity;
    }


    /**
     * 判断是否有虚拟返回键盘  即 无实体键盘
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean checkDeviceHasNavigationBar(Context activity) {

        if (VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
            boolean hasMenuKey = ViewConfiguration.get(activity)
                    .hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap
                    .deviceHasKey(KeyEvent.KEYCODE_BACK);

            if (!hasMenuKey && !hasBackKey) {
                // 做任何你需要做的,这个设备有一个导航栏
                return true;
            }
        }
        return false;
    }

}
