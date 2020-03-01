package me.xueyao.convert;

import org.modelmapper.ModelMapper;

import java.net.URLDecoder;


/**
 * 常用转换工具类
 * @author Simon.Xue
 * @date 2018.09.28
 */
public class ConvertUtils {
    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * 对象转换
     * @param object 转换前的对象
     * @param tClass 转换后的对象
     * @param <D>
     * @return
     */
    public static <D> D convert(Object object, Class<D> tClass) {
        //map是映射处理方法
        return modelMapper.map(object, tClass);
    }

    public static String getURLDecoder(String arg0) throws Exception {
        if (arg0 != null && !"".equals(arg0)) {
            arg0 = arg0.trim();
            return URLDecoder.decode(arg0, "UTF-8");
        }
        return "";
    }
}
