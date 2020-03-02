package me.xueyao.map;


import me.xueyao.http.HttpRequestUtils;

import java.util.*;

/**
 * 地图计算
 * @author simonxue
 */
public class Geohash {

    public final static String TENCENT_MAP_URL = "https://apis.map.qq.com/ws/place/v1/suggestion";

    public final static String TENCENT_MAP_CODE = "腾讯地图提供的key";

    private static int numbits = 6 * 5;
    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();

    static {
        int i = 0;
        for (char c : digits) {
            lookup.put(c, i++);
        }
    }

    public double[] decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {

            int i = lookup.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }

        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        // even bits
        int j = 0;
        for (int i = 0; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            lonset.set(j++, isSet);
        }

        // odd bits
        j = 0;
        for (int i = 1; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            latset.set(j++, isSet);
        }

        double lon = decode(lonset, -180, 180);
        double lat = decode(latset, -90, 90);

        return new double[]{lat, lon};
    }

    private double decode(BitSet bs, double floor, double ceiling) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            mid = (floor + ceiling) / 2;
            if (bs.get(i)) {
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return mid;
    }

    public String encode(String lat, String lon) {

        return encode(Double.parseDouble(lat), Double.parseDouble(lon));

    }

    public String encode(double lat, double lon) {
        BitSet latbits = getBits(lat, -90, 90);
        BitSet lonbits = getBits(lon, -180, 180);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < numbits; i++) {
            buffer.append((lonbits.get(i)) ? '1' : '0');
            buffer.append((latbits.get(i)) ? '1' : '0');
        }
        return base32(Long.parseLong(buffer.toString(), 2));
    }

    private BitSet getBits(double lat, double floor, double ceiling) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            double mid = (floor + ceiling) / 2;
            if (lat >= mid) {
                buffer.set(i);
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return buffer;
    }

    public static String base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative) {
            i = -i;
        }
        while (i <= -32) {
            buf[charPos--] = digits[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }

    /*********************** 获取九个的矩形编码 ****************************************/
    public static String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    public static Map<String, String> BORDERS = new HashMap<String, String>();
    public static Map<String, String> NEIGHBORS = new HashMap<String, String>();

    public static void setMap() {
        NEIGHBORS.put("right:even", "bc01fg45238967deuvhjyznpkmstqrwx");
        NEIGHBORS.put("left:even", "238967debc01fg45kmstqrwxuvhjyznp");
        NEIGHBORS.put("top:even", "p0r21436x8zb9dcf5h7kjnmqesgutwvy");
        NEIGHBORS.put("bottom:even", "14365h7k9dcfesgujnmqp0r2twvyx8zb");

        NEIGHBORS.put("right:odd", "p0r21436x8zb9dcf5h7kjnmqesgutwvy");
        NEIGHBORS.put("left:odd", "14365h7k9dcfesgujnmqp0r2twvyx8zb");
        NEIGHBORS.put("top:odd", "bc01fg45238967deuvhjyznpkmstqrwx");
        NEIGHBORS.put("bottom:odd", "238967debc01fg45kmstqrwxuvhjyznp");

        BORDERS.put("right:even", "bcfguvyz");
        BORDERS.put("left:even", "0145hjnp");
        BORDERS.put("top:even", "prxz");
        BORDERS.put("bottom:even", "028b");

        BORDERS.put("right:odd", "prxz");
        BORDERS.put("left:odd", "028b");
        BORDERS.put("top:odd", "bcfguvyz");
        BORDERS.put("bottom:odd", "0145hjnp");

    }

    /**
     * 150      * 获取九个点的矩形编码
     * 151      *
     * 152      * @param geohash
     * 153      * @return
     * 154
     */
    public String[] getGeoHashExpand(String geohash) {
        try {
            String geohashTop = calculateAdjacent(geohash, "top");
            String geohashBottom = calculateAdjacent(geohash, "bottom");
            String geohashRight = calculateAdjacent(geohash, "right");
            String geohashLeft = calculateAdjacent(geohash, "left");
            String geohashTopLeft = calculateAdjacent(geohashLeft, "top");
            String geohashTopRight = calculateAdjacent(geohashRight, "top");
            String geohashBottomRight = calculateAdjacent(geohashRight,
                    "bottom");
            String geohashBottomLeft = calculateAdjacent(geohashLeft, "bottom");
            String[] expand = {geohash, geohashTop, geohashBottom,
                    geohashRight, geohashLeft, geohashTopLeft, geohashTopRight,
                    geohashBottomRight, geohashBottomLeft};
            return expand;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 分别计算每个点的矩形编码
     *
     * @param srcHash
     * @param dir
     * @return
     */
    public static String calculateAdjacent(String srcHash, String dir) {
        srcHash = srcHash.toLowerCase();
        char lastChr = srcHash.charAt(srcHash.length() - 1);
        int a = srcHash.length() % 2;
        String type = (a > 0) ? "odd" : "even";
        String base = srcHash.substring(0, srcHash.length() - 1);
        if (BORDERS.get(dir + ":" + type).indexOf(lastChr) != -1) {
            base = calculateAdjacent(base, dir);
        }
        base = base
                + BASE32.toCharArray()[(NEIGHBORS.get(dir + ":" + type)
                .indexOf(lastChr))];
        return base;
    }

    // 赤道半径(单位m)
    private static final double EARTH_RADIUS = 6371;

    /**
     * * 转化为弧度(rad)
     * *
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public double getDistance(double lon1, double lat1, double lon2,
                              double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    /**
     * 筛选出在范围里的经纬度集合
     * @param lon      经度
     * @param lat      纬度
     * @param lonLats  经纬度集合 筛选
     * @return
     */
    public List<Map<String, Double>> screenLonLat(Double lon, Double lat, List<Map<String, Double>> lonLats) {
        if (null == lonLats) {
            return null;
        }
        List<Map<String, Double>> lonLatList = new ArrayList<>();
        Geohash geohash = new Geohash();
        for (Map<String, Double> lonLat : lonLats) {
            Double longitude = lonLat.get("longitude");
            Double latitude = lonLat.get("latitude");
            Double distance = lonLat.get("distance");
            //计算两个经纬度之间距离
            double dist = geohash.getDistance(lon, lat, longitude, latitude);
            if (dist <= distance) {
                lonLat.put("distance", (double) Math.round(dist / 1000 * 10) / 10);
                lonLatList.add(lonLat);
            }
        }
        Geohash.sortById(lonLatList);
        return lonLatList;
    }

    /**
     * 按照集合id升序排序
     *
     * @param list
     */
    public static void sortById(List<Map<String, Double>> list) {
        Collections.sort(list, new Comparator<Map<String, Double>>() {
            @Override
            public int compare(Map<String, Double> u1, Map<String, Double> u2) {
                Double id1 = u1.get("distance");
                Double id2 = u2.get("distance");
                return id1.compareTo(id2);
            }
        });
    }

    /**
     * 查询腾讯地图 关键字信息
     * @param keyword
     * @param region
     * @return
     */
    public static String tencentMapList(String keyword,String region){
        Geohash geohash = new Geohash();
        StringBuffer stringBuffer = new StringBuffer(TENCENT_MAP_URL);
        stringBuffer.append("?").append("region=").append(region).append("&keyword=").
                append(keyword).append("&key=").append(TENCENT_MAP_CODE);
        return HttpRequestUtils.get(stringBuffer.toString());
    }

}
