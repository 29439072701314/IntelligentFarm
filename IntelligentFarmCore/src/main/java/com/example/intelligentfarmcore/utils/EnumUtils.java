package com.example.intelligentfarmcore.utils;

import com.example.intelligentfarmcore.pojo.enums.ICodeEnum;

import java.util.function.Function;

public class EnumUtils {
    public static <T extends Enum<T>> T fromValue(Object input, Class<T> enumClass, Function<T, Integer> idExtractor) {
        int value = 0;
        if (input instanceof Number) {
            value = ((Number) input).intValue();
        } else if (input instanceof String) {
            try {
                value = Integer.parseInt((String) input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID必须是数字或数字字符串");
            }
        }

        for (T enumValue : enumClass.getEnumConstants()) {
            if (idExtractor.apply(enumValue) == value) {
                return enumValue;
            }
        }
        // 如果没有找到匹配的枚举值，返回第一个枚举值作为默认值
        if (enumClass.getEnumConstants().length > 0) {
            return enumClass.getEnumConstants()[0];
        }
        throw new IllegalArgumentException("无效的ID: " + value);
    }
    // 从枚举的ID获取枚举实例
    public static <E extends Enum<E> & ICodeEnum> E getEnumFromId(Class<E> enumClass, int id) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getId() == id) {
                return e;
            }
        }
        // 如果没有找到匹配的枚举值，返回第一个枚举值作为默认值
        if (enumClass.getEnumConstants().length > 0) {
            return enumClass.getEnumConstants()[0];
        }
        throw new IllegalArgumentException("Invalid code: " + id + " for enum " + enumClass.getSimpleName());
    }
}
