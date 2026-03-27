import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // 配置路径别名
  resolve: {
    alias: {
      "@": "/src",
      "@pages": "/src/pages",
      "@assets": "/src/assets",
    },
  },
  // 配置服务器
  server: {
    host: "0.0.0.0",
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      },
    },
  },
  // 解决sockjs-client的兼容性问题
  define: {
    global: "window"
  },
});
