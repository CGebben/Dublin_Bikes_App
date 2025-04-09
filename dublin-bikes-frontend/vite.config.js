import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/stations': 'http://localhost:8080',
      '/availability': 'http://localhost:8080',
      '/weather': 'http://localhost:8080',
    },
  },
});