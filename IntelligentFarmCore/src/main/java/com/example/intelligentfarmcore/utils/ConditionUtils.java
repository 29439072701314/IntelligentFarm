/**
 * 处理查询条件工具类
 */
package com.example.intelligentfarmcore.utils;
import com.example.intelligentfarmcore.pojo.enums.ICodeEnum;
import java.time.LocalDate;
import java.util.Map;


public class ConditionUtils {
    private Map<String, Object> condition;

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public ConditionUtils(Map<String, Object> condition) {
        this.condition = condition;
    }

    public String getString(String key) {
        Object value = condition.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public Long getLong(String key){
        Object value = condition.get(key);
        if(value == null){
            return null;
        }
        try{
            return Long.parseLong(value.toString());
        }catch(NumberFormatException e){
            return null;
        }
    }

    public LocalDate getLocalDate(String key) {
        Object value = condition.get(key);
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof String) {
                return LocalDate.parse((String) value);
            } else if (value instanceof LocalDate) {
                return (LocalDate) value;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public <E extends Enum<E> & ICodeEnum> E getEnum(String key, Class<E> enumClass) {
        Object value = condition.get(key);
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return EnumUtils.getEnumFromId(enumClass, (Integer) value);
        } else if (value instanceof String) {
            try {
                int code = Integer.parseInt((String) value);
                return EnumUtils.getEnumFromId(enumClass, code);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
