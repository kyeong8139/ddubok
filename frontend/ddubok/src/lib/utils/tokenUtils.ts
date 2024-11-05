"use client";

import Router from "next/router";
import { useEffect, useState } from "react";

import { checkRefreshToken, reissue } from "@lib/api/login-api";
import useAuthStore from "@store/auth-store";

const useAuthToken = () => {
	const accessToken = useAuthStore((state) => state.accessToken);
	const setAccessToken = useAuthStore((state) => state.setAccessToken);
	const clearAccessToken = useAuthStore((state) => state.clearAccessToken);

	const [isTokenReady, setIsTokenReady] = useState(false);

	useEffect(() => {
		const initializeAccessToken = async () => {
			if (!accessToken) {
				const refreshResponse = await checkRefreshToken();

				if (refreshResponse.data.code === "200") {
					try {
						const response = await reissue();
						const newAccessToken = `Bearer ${response.headers.authorization}`;
						setAccessToken(newAccessToken);
					} catch (error) {
						clearAccessToken();
						console.error("accessToken 재발급 실패");
					}
				} else if (refreshResponse.data.code === "800") {
					clearAccessToken();
					console.log("refreshToken 없음, 로그인 필요");
					Router.push("/login");
				}
			}
			setIsTokenReady(true);
		};
		initializeAccessToken();
	}, [accessToken, setAccessToken, clearAccessToken]);

	return { accessToken, isTokenReady };
};

export default useAuthToken;
