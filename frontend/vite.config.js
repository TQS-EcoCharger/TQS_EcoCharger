import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 5000,
    allowedHosts: ['deti-tqs-19.ua.pt'], // ✅ Aqui, no nível do `server`
    proxy: {
      '/api': {
        target: 'http://backend:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
