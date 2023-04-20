//package com.eluolang.physical.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.nacos.common.utils.CollectionUtils;
//import com.google.common.collect.Maps;
//import org.apache.commons.beanutils.PropertyUtilsBean;
//
//import java.beans.PropertyDescriptor;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static com.alibaba.fastjson.JSON.toJSONString;
//
////动态实体类生成
//public class BeanAddPropertiesUtil {
//    public static Object getTarget(Object dest, Map<String, Object> addProperties) {
//        // get property map
//        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
//        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
//        Map<String, Class> propertyMap = Maps.newHashMap();
//        for (PropertyDescriptor d : descriptors) {
//            if (!"class".equalsIgnoreCase(d.getName())) {
//                propertyMap.put(d.getName(), d.getPropertyType());
//            }
//        }
//        // add extra properties
//        addProperties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
//        // new dynamic bean
//        DynamicBean dynamicBean = new DynamicBean(dest.getClass(), propertyMap);
//        // add old value
//        propertyMap.forEach((k, v) -> {
//            try {
//                // filter extra properties
//                if (!addProperties.containsKey(k)) {
//                    dynamicBean.setValue(k, propertyUtilsBean.getNestedProperty(dest, k));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        // add extra value
//        addProperties.forEach((k, v) -> {
//            try {
//                dynamicBean.setValue(k, v);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        Object target = dynamicBean.getTarget();
//        return target;
//
//    }
//
//    //
//    public static void main(String[] args) {
//        OrderUserOrderMerchantDTO orderMerchantDTOs = new OrderUserOrderMerchantDTO();
//        List<String> headList=new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            headList.add("create"+i);
//
//        }
//        Map<String, Object> map = new HashMap<>();
////通过list动态添加属性
//        if (CollectionUtils.isNotEmpty(headList)) {
//            for (int i = 0, j = headList.size(); i < j; i++) {
//                map.put(headList.get(i), "");
//            }
//        }
//        OrderUserOrderMerchantDTO merchantDTO = (OrderUserOrderMerchantDTO) BeanAddPropertiesUtil.getTarget(orderMerchantDTOs, map);
////增加后的实体对象引用读取时候将对象转为json再转为map
//        String jsonStr = toJSONString(merchantDTO);
//        Map maps = (Map) JSON.parse(jsonStr);
//        System.out.println(jsonStr);
//    }
//}
//
