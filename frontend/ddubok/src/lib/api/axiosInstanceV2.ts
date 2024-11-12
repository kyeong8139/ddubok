import axios from "axios";
import Router from "next/router";

import { checkRefreshToken, reissue } from "@lib/api/login-api";
import useAuthStore from "@store/auth-store";

const axiosInstanceV2 = axios.create({
	baseURL: process.env.NEXT_PUBLIC_BASE_URL_V2,
	withCredentials: true,
	headers: {
		"Content-Type": "application/json",
	},
});

// 요청 인터셉터
axiosInstanceV2.interceptors.request.use(
	(config) => {
		const { accessToken } = useAuthStore.getState();
		if (accessToken) config.headers["Authorization"] = accessToken;
		return config;
	},
	(error) => Promise.reject(error),
);

// 응답 인터셉터
axiosInstanceV2.interceptors.response.use(
	(response) => response,
	async (error) => {
		const originalRequest = error.config;

		if (error.response && !originalRequest._retry && error.response.status === 803) {
			originalRequest._retry = true;

			try {
				const refreshResponse = await checkRefreshToken();

				if (refreshResponse.data.code === "200") {
					const response = await reissue();
					const newAccessToken = `Bearer ${response.headers.authorization}`;

					const setAccessToken = useAuthStore.getState().setAccessToken;
					setAccessToken(newAccessToken);

					originalRequest.headers["Authorization"] = newAccessToken;
					return axiosInstanceV2(originalRequest);
				} else if (refreshResponse.data.code === "800") {
					const clearAccessToken = useAuthStore.getState().clearAccessToken;
					clearAccessToken();
					console.log("refreshToken 없음, 로그인 필요");
					Router.push("/login");
				}
			} catch (error) {
				const clearAccessToken = useAuthStore.getState().clearAccessToken;
				clearAccessToken();
				console.error("accessToken 재발급 실패");
				Router.push("/login");
			}
		}

		return Promise.reject(error);
	},
);

export default axiosInstanceV2;
