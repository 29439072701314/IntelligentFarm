import React from "react";
import styles from "./index.module.less";
export default function Content({ title, children, extra, className, style }) {
  return (
    <div className={`${styles.content} ${className || ''}`} style={style}>
      {title && (
        <div className={styles.title}>
          <h2>{title}</h2>
          {extra && <div>{extra}</div>}
        </div>
      )}
      <div className={styles.children}>{children}</div>
    </div>
  );
}
