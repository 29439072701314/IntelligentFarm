import React from "react";
import { Tag } from "antd";
import { GENDER } from "../GenderSelect/constant";

const TAG_COLOR = {
  [GENDER.NONE]: "default",
  [GENDER.MALE]: "blue",
  [GENDER.FEMALE]: "pink",
};

const GENDER_NAME = {
  [GENDER.NONE]: "未知",
  [GENDER.MALE]: "男",
  [GENDER.FEMALE]: "女",
};

/**
 * 性别标签组件
 * @param {Object} props
 * @param {number|Object} props.gender - 性别值(0/1/2)或包含genderId/genderName的对象
 */
export default function GenderTag({ gender }) {
  // 处理对象格式: { genderId: 1, genderName: "男" }
  if (gender && typeof gender === "object") {
    const genderId = gender.genderId ?? gender.gender;
    const genderName = gender.genderName ?? GENDER_NAME[genderId];
    return <Tag color={TAG_COLOR[genderId]}>{genderName}</Tag>;
  }

  // 处理数字格式: 0, 1, 2
  return <Tag color={TAG_COLOR[gender]}>{GENDER_NAME[gender]}</Tag>;
}
