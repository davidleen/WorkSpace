package com.google.maps.android.data.kml;

import com.google.android.gms.maps.model.LatLng;

public class LatLngPixel {


    public static double distance(LatLng latLng1, LatLng latLng2) {

        double result = Math.sqrt(Math.pow(latLng1.latitude - latLng2.latitude, 2) + Math.pow(latLng1.longitude - latLng2.longitude, 2));

        return result;
    }

    public static double distanceOnPixel(LatLng latLng1, LatLng latLng2, int zoom, float density) {

        return distanceOnDp(latLng1, latLng2, zoom) * density;
    }


    public static double distanceOnDp(LatLng latLng1, LatLng latLng2, int zoom) {
        double distance = distance(latLng1, latLng2) * (256 * (2 << zoom)) / 360;
        return distance;
    }

    public static double distanceInMeter(double longitude1, double latitude1, double longitude2, double latitude2) {

        double Lat1 = rad(latitude1); // 纬度

        double Lat2 = rad(latitude2);

        double a = Lat1 - Lat2;//两点纬度之差

        double b = rad(longitude1) - rad(longitude2); //经度之差

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式

        s = s * 6378137.0;//弧长乘地球半径（半径为米）

        s = Math.round(s * 10000d) / 10000d;//精确距离的数值

        return s;

    }


    private static double rad(double d) {

        return d * Math.PI / 180.00; //角度转换成弧度

    }


}