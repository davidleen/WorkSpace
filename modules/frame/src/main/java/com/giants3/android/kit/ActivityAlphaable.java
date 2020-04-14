package com.giants3.android.kit;

import android.app.Activity;
import android.view.Window;


/**
 * 接口定义， 设置act 的透明度
 * 在Popupwindow 情况下使用。
 * 
 * 实现该该接口的act，其顶级布局元素必须为FrameLayout
 * 
 * 通过设置FramleLayout foreGround 来实现dim 效果。
 * 
 *  
 * PS 由于activity 顶层元素PhoneWindow#DectorView 就是Framelayout ,
 * 所以activity 布局上也无需改变， 只需实现该接口代码即可。
 *  @see Activity#getWindow() 
 * @see Window#getDecorView() 
 *
 *  
 * 
 * <br>Created 2016年8月8日 下午3:39:59
 * @version  
 * @author   davidleen29		
 *
 * @see
 */
public interface ActivityAlphaable {
    
    /**
     * 设置alpha 值， [0,255]
     * 
     * <br>Created 2016年8月8日 下午3:41:47
     * @param alpha
     * @author       davidleen29
     */
    void setAlpha(int alpha);

}
