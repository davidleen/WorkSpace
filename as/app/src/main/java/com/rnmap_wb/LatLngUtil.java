package com.rnmap_wb;

import com.giants3.android.frame.util.StringUtil;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LatLngUtil {


    public static void getTileNumber(final double lat, final double lon, final int level, int[] tileXY) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << level));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << level));
        if (xtile < 0)
            xtile = 0;
        if (xtile >= (1 << level))
            xtile = ((1 << level) - 1);
        if (ytile < 0)
            ytile = 0;
        if (ytile >= (1 << level))
            ytile = ((1 << level) - 1);

        tileXY[0] = xtile;
        tileXY[1] = ytile;

    }

    /**
     * 经度到像素X值
     */


    public static double lngToPixel(double lng, int zoom) {

        return (lng + 180) * (256 * Math.pow(2, zoom)) / 360;

    }

    /**
     * 像素X到经度
     */


    public static double pixelToLng(double pixelX, int zoom) {

        return pixelX * 360 / (256 * Math.pow(2, zoom)) - 180;

    }

    /**
     * 纬度到像素Y
     */


    public static double latToPixel(double lat, int zoom) {

        double siny = Math.sin(lat * Math.PI / 180);

        double y = Math.log((1 + siny) / (1 - siny));

        return (128 * Math.pow(2, zoom)) * (1 - y / (2 * Math.PI));

    }

    /**
     * 像素Y到纬度
     */


    public static double pixelToLat(double pixelY, int zoom) {

        double y = 2 * Math.PI * (1 - pixelY / (128 * Math.pow(2, zoom)));

        double z = Math.pow(Math.E, y);

        double siny = (z - 1) / (z + 1);

        return Math.asin(siny) * 180 / Math.PI;

    }


    private static PointD leftDown = new PointD(0, 0);
    private static PointD leftUp = new PointD(0, 0);
    private static PointD rightUp = new PointD(0, 0);
    private static PointD rightDown = new PointD(0, 0);


    private static PointD linePointStart = new PointD(0, 0);
    private static PointD linePointEnd = new PointD(0, 0);


    public static boolean linesIntersectRect(List<GeoPoint> lines, BoundingBox latLngBounds) {
        GeoPoint nearst1 = null;
        GeoPoint nearst2 = null;

        double maxDis = Double.MAX_VALUE;
        double maxDis2 = Double.MAX_VALUE;
        double centerLat = latLngBounds.getCenterLatitude();
        double centerLng = latLngBounds.getCenterLongitude();
        for (GeoPoint latLng : lines) {
            if (latLngBounds.contains(latLng)) return true;
            double dis = Math.pow(latLng.getLatitude() - centerLat, 2) + Math.pow(latLng.getLongitude() - centerLng, 2);

            if (dis < maxDis) {


                nearst2 = nearst1;
                maxDis2 = maxDis;

                nearst1 = latLng;
                maxDis = dis;
                continue;

            }
            if (dis < maxDis2) {
                nearst2 = latLng;
                maxDis2 = dis;
            }


        }
        //Log.e("lineStart:"+nearst1+",lineEnd:"+nearst2+",size:"+lines.size());
        return LineIntersectRect(nearst1, nearst2, latLngBounds);

    }


    // 线与矩形是否相交
    public static boolean LineIntersectRect(GeoPoint lineStart, GeoPoint lineEnd, BoundingBox latLngBounds) {


        //先判断线两点是否在范围中。
        if (latLngBounds.contains(lineStart) || latLngBounds.contains(lineEnd)) return true;


        leftDown.x = latLngBounds.getLatSouth();
        leftDown.y = latLngBounds.getLonWest();


        leftUp.x = latLngBounds.getLatSouth();
        leftUp.y = latLngBounds.getLonEast();


        rightUp.x = latLngBounds.getLatNorth();
        rightUp.y = latLngBounds.getLonEast();

        rightDown.x = latLngBounds.getLatNorth();
        rightDown.y = latLngBounds.getLonWest();


        linePointStart.x = lineStart.getLatitude();
        linePointStart.y = lineStart.getLongitude();


        linePointEnd.x = lineEnd.getLatitude();
        linePointEnd.y = lineEnd.getLongitude();


        return LineIntersectRect(linePointStart, linePointEnd);


    }


    // 线与矩形是否相交
    static boolean LineIntersectRect(PointD lineStart, PointD lineEnd) {


        if (LineIntersectLine(lineStart, lineEnd, leftDown, leftUp))
            return true;
        if (LineIntersectLine(lineStart, lineEnd, leftUp, rightUp))
            return true;
        if (LineIntersectLine(lineStart, lineEnd, rightUp, rightDown))
            return true;
        if (LineIntersectLine(lineStart, lineEnd, rightDown, leftDown))
            return true;

        return false;
    }

    // 线与线是否相交
    static boolean LineIntersectLine(PointD l1Start, PointD l1End, PointD l2Start, PointD l2End) {
        return QuickReject(l1Start, l1End, l2Start, l2End) && Straddle(l1Start, l1End, l2Start, l2End);
    }

    // 快速排序。  true=通过， false=不通过
    static boolean QuickReject(PointD l1Start, PointD l1End, PointD l2Start, PointD l2End) {
        double l1xMax = Math.max(l1Start.x, l1End.x);
        double l1yMax = Math.max(l1Start.y, l1End.y);
        double l1xMin = Math.min(l1Start.x, l1End.x);
        double l1yMin = Math.min(l1Start.y, l1End.y);

        double l2xMax = Math.max(l2Start.x, l2End.x);
        double l2yMax = Math.max(l2Start.y, l2End.y);
        double l2xMin = Math.min(l2Start.x, l2End.x);
        double l2yMin = Math.min(l2Start.y, l2End.y);

        if (l1xMax < l2xMin || l1yMax < l2yMin || l2xMax < l1xMin || l2yMax < l1yMin)
            return false;

        return true;
    }

    // 跨立实验
    static boolean Straddle(PointD l1Start, PointD l1End, PointD l2Start, PointD l2End) {
        double l1x1 = l1Start.x;
        double l1x2 = l1End.x;
        double l1y1 = l1Start.y;
        double l1y2 = l1End.y;
        double l2x1 = l2Start.x;
        double l2x2 = l2End.x;
        double l2y1 = l2Start.y;
        double l2y2 = l2End.y;

        if ((((l1x1 - l2x1) * (l2y2 - l2y1) - (l1y1 - l2y1) * (l2x2 - l2x1)) *
                ((l1x2 - l2x1) * (l2y2 - l2y1) - (l1y2 - l2y1) * (l2x2 - l2x1))) > 0 ||
                (((l2x1 - l1x1) * (l1y2 - l1y1) - (l2y1 - l1y1) * (l1x2 - l1x1)) *
                        ((l2x2 - l1x1) * (l1y2 - l1y1) - (l2y2 - l1y1) * (l1x2 - l1x1))) > 0) {
            return false;
        }

        return true;
    }


    public static class PointD {
        public double x;
        public double y;

        public PointD(double x, double y) {
            this.x = x;
            ;
            this.y = y;
        }
    }


    public static List<GeoPoint> convertStringToGeoPoints(String string) {
        List<GeoPoint> latLngs = new ArrayList<>();

        if (StringUtil.isEmpty(string)) return latLngs;


        String[] split = string.split(";");
        int len = split.length;

        for (int i = 0; i < len; i++) {
            String[] split1 = split[i].split(",");
            GeoPoint latLng = null;
            try {
                latLng = new GeoPoint(Double.valueOf(split1[0]), Double.valueOf(split1[1]));
                latLngs.add(latLng);
            } catch (Throwable e) {
                e.printStackTrace();
            }


        }
        return latLngs;

    }

    public static String convertGeoPointToString(GeoPoint... latLngs) {
        return convertGeoPointToString(Arrays.asList(latLngs));

    }

    public static String convertGeoPointToString(List<GeoPoint> latLngs) {


        StringBuilder stringBuilder = new StringBuilder();
        for (GeoPoint latLng : latLngs) {
            stringBuilder.append(latLng.getLatitude()).append(",").append(latLng.getLongitude());
            stringBuilder.append(";");


        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();

    }


    public static double distance(GeoPoint latLng1, GeoPoint latLng2) {

        double result = Math.sqrt(Math.pow(latLng1.getAltitude() - latLng2.getAltitude(), 2) + Math.pow(latLng1.getLongitude() - latLng2.getLongitude(), 2));

        return result;
    }

    public static double distanceOnPixel(GeoPoint latLng1, GeoPoint latLng2, int zoom, float density) {

        return distanceOnDp(latLng1, latLng2, zoom) * density;
    }


    public static double distanceOnDp(GeoPoint latLng1, GeoPoint latLng2, int zoom) {
        double distance = distance(latLng1, latLng2) * (256 * (2 << zoom)) / 360;
        return distance;
    }

    public static final double distanceInMeter(GeoPoint start, GeoPoint end) {
        return
                LatLngUtil.distanceInMeter(start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude());
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
