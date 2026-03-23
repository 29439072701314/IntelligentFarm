import React from "react";
import styles from "./index.module.less";
export default function Content(props) {
  const { title, children, extra } = props;
  return (
    <div className={styles.content} {...props}>
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
