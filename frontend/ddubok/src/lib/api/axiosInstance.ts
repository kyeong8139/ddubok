import axios from "axios";
import Router from "next/router";

import { checkRefreshToken, reissue } from "@lib/api/login-api";
import useAuthStore from "@store/auth-store";

const axiosInstance = axios.create({
	baseURL: process.env.NEXT_PUBLIC_BASE_URL,
	withCredentials: true,
	headers: {
		"Content-Type": "application/json",
	},
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
	(config) => {
		const { accessToken } = useAuthStore.getState();
		if (accessToken) config.headers["Authorization"] = `Bearer ${accessToken}`;
		return config;
	},
	(error) => Promise.reject(error),
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
	(response) => response,
	async (error) => {
		const originalRequest = error.config;

		if (error.response && !originalRequest._retry && error.response.status === 803) {
			originalRequest._retry = true;

			const refreshResponse = await checkRefreshToken();

			if (refreshResponse.status === 200) {
				try {
					const response = await reissue();
					const newAcessToken = response.headers.authorization;

					useAuthStore.getState().setAccessToken(newAcessToken);

					originalRequest.headers["Authorization"] = `Bearer ${newAcessToken}`;
					return axiosInstance(originalRequest);
				} catch (error) {
					console.error("accessToken 재발급 실패");
				}
			} else if (refreshResponse.status === 800) {
				console.log("refreshToken 없음");
				// 로그인이 만료되었으니 다시 하라고 해야함
				Router.push("/login");
			}
		}

		return Promise.reject(error);
	},
);

export default axiosInstance;
