import axios from 'axios'
import type { AxiosError } from 'axios'

const BASE_URL = (import.meta.env.VITE_API_BASE_URL as string) || ''

// Replace or create axios instance used by the app
const request = axios.create({
	// use Vite proxy in dev when VITE_API_BASE_URL is '/api'
	baseURL: BASE_URL,
	timeout: 10000,
	headers: {
		'Content-Type': 'application/json'
	},
	withCredentials: false // 通常不需要跨域携带 cookie；如需请改为 true 并后端允许 credentials
})

// 请求拦截：添加 token（如有）
request.interceptors.request.use(
	(config) => {
		// ...existing code...
		try {
			const token = localStorage.getItem('token')
			if (token && config.headers) {
				config.headers.Authorization = `Bearer ${token}`
			}
		} catch (e) {
			// ignore
		}
		return config
	},
	(error) => Promise.reject(error)
)

// 响应拦截：处理网络错误、统一错误消息
request.interceptors.response.use(
	(response) => {
		// 如果后端在业务成功时也采用固定结构，可在这里做统一 unwrap
		return response
	},
	(error: AxiosError) => {
		// network / CORS / no response
		if (error && error.request && !error.response) {
			console.error('No response received (possible network/CORS):', error.message)
			// 给调用方一个明确的错误信息
			return Promise.reject(new Error('Network or CORS error: ' + error.message))
		}

		// 服务端返回了响应（可能是 4xx / 5xx）
		const resp = (error as any).response
		if (resp) {
			const status: number = resp.status
			const data = resp.data
			// 后端返回业务错误信息时（如 400 且包含 message），把业务信息暴露出来
			if (data && data.message) {
				console.error(`API response error: ${status} Business error: ${data.message}`)
				const bizErr = new Error(data.message)
				// 把后端返回的完整信息附加在 error 对象上，方便上层判断
				;(bizErr as any).status = status
				;(bizErr as any).data = data
				return Promise.reject(bizErr)
			}
			// 没有标准 message，按状态码分类提示
			console.error('API response error:', status, data)
			return Promise.reject(new Error(`HTTP ${status}`))
		}

		// 其它未知错误
		console.error('Request error:', error.message)
		return Promise.reject(error)
	}
)

export default request
