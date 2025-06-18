import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/    local deployment
// export default defineConfig({
  // plugins: [react()],
  // server: {
    // proxy: {
      // '/stations': 'http://localhost:8080',
      // '/availability': 'http://localhost:8080',
      // '/weather': 'http://localhost:8080',
    // },
  // },
// });

// For Github pages deployment.

export default defineConfig({
  plugins: [react()],
  base: '/Dublin_Bikes_App/',
});