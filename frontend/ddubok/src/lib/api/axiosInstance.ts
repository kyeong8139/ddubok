import axios from "axios";
import Router from "next/router";

import { reissue } from "@lib/api/login-api";
import useAuthStore from "@store/auth-store";
import Cookies from "js-cookie";

const axiosInstance = axios.create({
	baseURL: process.env.NEXT_PUBLIC_BASE_URL,
	withCredentials: true,
	headers: {
		"Content-Type": "applicaion/json",
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

			if (Cookies.get("refresh")) {
				try {
					const response = await reissue();
					const newAcessToken = response.headers.authorization;

					useAuthStore.getState().setAccessToken(newAcessToken);

					originalRequest.headers["Authorization"] = `Bearer ${newAcessToken}`;
					return axiosInstance(originalRequest);
				} catch (error) {
					console.error("accessToken 재발급 실패");
					Router.push("/login");
				}
			} else {
				console.log("refreshToken 없음");
				Router.push("/login");
			}
		}

		return Promise.reject(error);
	},
);

export default axiosInstance;
