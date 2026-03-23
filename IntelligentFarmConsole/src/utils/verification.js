/**
 * 校验工具（用于校验数据格式）
 */


/**
 * 验证手机号
 * @param {string} phone 手机号
 * @returns {boolean} 是否验证通过
 */
export const verifyPhone = (phone) => {
  const phoneRegex = /^1[3456789]\d{9}$/;
  return phoneRegex.test(phone);
}

/**
 * 验证通用格式（数字或字母）
 * @param {string} value 待验证值
 * @param {number} min 最小长度(默认8)
 * @param {number} max 最大长度(默认16)
 * @returns {boolean} 是否验证通过
 */
export const verifyGeneralFormat = (value, min = 8, max = 16) => {
  const generalFormatRegex = new RegExp(`^[a-zA-Z0-9]{${min},${max}}$`);
  return generalFormatRegex.test(value);
}
