package com.example.dicegame.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectUtil {

    public static <X, Y> Y mapObject(X x, Class<Y> clazz) {
        try {
            var t = clazz.newInstance();
            BeanUtils.copyProperties(x, t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("InstantiationException");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalAccessException");
        }
    }

    public static <X, Y> List<Y> mapList(List<X> list, Class<Y> clazz) {
        return list.parallelStream().map(e -> mapObject(e, clazz)).collect(Collectors.toList());
    }
}
