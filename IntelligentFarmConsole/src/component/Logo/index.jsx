import React from "react";
import styles from "./index.module.less";
export default function Logo({ className, isHideTitle = true }) {
  return (
    <div className={className + " " + styles.logo}>
      {!isHideTitle && <h1>智能农场养殖系统</h1>}
    </div>
  );
}
