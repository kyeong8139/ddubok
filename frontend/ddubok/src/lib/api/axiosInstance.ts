import axios from "axios";
import { getCookie, setCookie } from "cookies-next";
import Router from "next/router";
import { reissue } from "./login-api";

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
		const accessToken = getCookie("accessToken");
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

		// 에러 처리 수정 필요
		if (error.response && !originalRequest._retry) {
			originalRequest._retry = true;
			const refreshToken = getCookie("refreshToken");

			if (refreshToken) {
				try {
					const response = await reissue();
					const { accessToken: newAcessToken } = response.data;

					setCookie("accessToken", newAcessToken);
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
