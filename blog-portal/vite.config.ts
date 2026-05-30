import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  build: {
    // Increase chunk size warning limit
    chunkSizeWarningLimit: 600,
    rollupOptions: {
      output: {
        // Manual chunks configuration for better code splitting
        manualChunks: (id) => {
          // Vue ecosystem
          if (id.includes('node_modules/vue/') ||
              id.includes('node_modules/@vue/') ||
              id.includes('node_modules/pinia/') ||
              id.includes('node_modules/vue-router/')) {
            return 'vue-vendor'
          }

          // Markdown and syntax highlighting
          if (id.includes('node_modules/markdown-it/') ||
              id.includes('node_modules/highlight.js/')) {
            return 'markdown'
          }

          // Mermaid diagrams - large library, split separately
          if (id.includes('node_modules/mermaid/')) {
            return 'mermaid'
          }

          // Chart and diagram libraries
          if (id.includes('node_modules/d3/') ||
              id.includes('node_modules/dagre/') ||
              id.includes('node_modules/cytoscape/')) {
            return 'diagrams'
          }

          // Lucide icons
          if (id.includes('node_modules/@lucide/')) {
            return 'icons'
          }

          // DOMPurify
          if (id.includes('node_modules/dompurify/')) {
            return 'dompurify'
          }

          // Other node_modules
          if (id.includes('node_modules/')) {
            return 'vendor'
          }
        },
        // Optimize chunk file naming
        chunkFileNames: (chunkInfo) => {
          const facadeModuleId = chunkInfo.facadeModuleId
            ? chunkInfo.facadeModuleId.split('/').pop()
            : 'chunk'

          // Group related chunks
          if (chunkInfo.name?.startsWith('mermaid')) {
            return 'assets/mermaid/[name]-[hash].js'
          }
          if (chunkInfo.name?.startsWith('diagrams')) {
            return 'assets/diagrams/[name]-[hash].js'
          }

          return `assets/${chunkInfo.name || facadeModuleId}-[hash].js`
        },
      },
    },
    // Use default minifier
    // CSS code splitting
    cssCodeSplit: true,
    // Target modern browsers for better optimization
    target: 'esnext',
    // Enable source maps for debugging (disable in production if needed)
    sourcemap: false,
  },
  // Optimize dependencies pre-bundling
  optimizeDeps: {
    include: ['vue', 'vue-router', 'pinia', '@vueuse/motion'],
    exclude: ['mermaid'], // Exclude large libraries from pre-bundling
  },
})
