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
				try {
					const refreshResponse = await checkRefreshToken();

					if (refreshResponse.data.code === "200") {
						const response = await reissue();
						const newAccessToken = `Bearer ${response.headers.authorization}`;
						setAccessToken(newAccessToken);
					} else if (refreshResponse.data.code === "800") {
						clearAccessToken();
						Router.push("/login");
					}
				} catch (error) {
					clearAccessToken();
					console.error("accessToken 재발급 실패");
					Router.push("/");
				}
			}

			setIsTokenReady(true);
		};

		initializeAccessToken();
	}, [accessToken, setAccessToken, clearAccessToken]);

	return { accessToken, isTokenReady, clearAccessToken };
};

export default useAuthToken;
