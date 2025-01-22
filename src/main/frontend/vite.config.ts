import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import svgr from "vite-plugin-svgr";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), svgr()],
  server: {
    port: 5173,
    proxy: {
      "/csrf-token": {
        target: "http://localhost:8080",
        changeOrigin: true,
        xfwd: true,
      },
      "/login": {
        target: "http://localhost:8080",
        changeOrigin: true,
        xfwd: true,
      },
      "/logout": {
        target: "http://localhost:8080",
        changeOrigin: true,
        xfwd: true,
      },
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        xfwd: true,
      },
    },
  },
  build: {
    outDir: "../../../target/classes/static",
  },
});
