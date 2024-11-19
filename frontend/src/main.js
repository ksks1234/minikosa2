// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

// import 'bootstrap/dist/css/bootstrap.min.css'
// import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import './assets/css/headerFooter.css'

// 중앙집중식 Axios 인스턴스 임포트
import api from './axios'

// Vue 애플리케이션 생성
const app = createApp(App)

// Pinia 인스턴스 생성 및 설치
const pinia = createPinia()
app.use(pinia)

// 라우터 설치
app.use(router)

// 전역 속성으로 Axios 설정 (선택 사항)
app.config.globalProperties.$axios = api

// Vue 애플리케이션 마운트
app.mount('#app')
