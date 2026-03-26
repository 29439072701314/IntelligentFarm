package com.example.intelligentfarmcore.pojo.enums;

import com.example.intelligentfarmcore.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender implements ICodeEnum {
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int genderId;

    private final String genderName;

    Gender(int genderId, String genderName) {
        this.genderId = genderId;
        this.genderName = genderName;
    }

    public int getGenderId() {
        return genderId;
    }

    public String getGenderName() {
        return genderName;
    }

    @JsonCreator
    public static Gender fromGenderId(Object input) {
        try {
            return EnumUtils.fromValue(input, Gender.class, Gender::getGenderId);
        } catch (Exception e) {
            // 如果输入无效，默认返回男
            return MALE;
        }
    }

    @Override
    public int getId() {
        return genderId;
    }
}
