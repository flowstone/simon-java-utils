package me.xueyao;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon.Xue
 * @date 2019-12-10 21:33
 **/
public class BeanCompareUtils {
    /**
     * 获得对象的Null或者空字符串的属性名，适用于BeanUtils复制
     * @param source 对象源
     * @return
     */
    public static String[] getEmptyPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else if (srcValue instanceof String && ((String) srcValue).isEmpty()) {
                emptyNames.add(pd.getName());
            }

        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
