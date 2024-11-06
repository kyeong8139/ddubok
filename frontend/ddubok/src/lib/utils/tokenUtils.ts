"use client";

import Router from "next/router";
import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/navigation";
import { checkRefreshToken, reissue } from "@lib/api/login-api";
import useAuthStore from "@store/auth-store";

const useAuthToken = () => {
	const router = useRouter();
	const accessToken = useAuthStore((state) => state.accessToken);
	const setAccessToken = useAuthStore((state) => state.setAccessToken);
	const clearAccessToken = useAuthStore((state) => state.clearAccessToken);
	const [isTokenReady, setIsTokenReady] = useState(false);
	const refreshAttempted = useRef(false); // refresh 시도 여부를 추적

	useEffect(() => {
		const initializeAccessToken = async () => {
			if (!accessToken && !refreshAttempted.current) {
				refreshAttempted.current = true; // refresh 시도 표시
				try {
					const refreshResponse = await checkRefreshToken();

					if (refreshResponse.data.code === "200") {
						const response = await reissue();
						const newAccessToken = `Bearer ${response.headers.authorization}`;
						setAccessToken(newAccessToken);
						router.refresh(); // 토큰 재발급 성공 시에만 refresh
					} else if (refreshResponse.data.code === "800") {
						clearAccessToken();
						if (typeof window !== "undefined" && !window.location.pathname.includes("/login")) {
							const currentPath = window.location.pathname + window.location.search;
							localStorage.setItem("redirectAfterLogin", currentPath);
							Router.push("/login");
						}
					}
				} catch (error) {
					clearAccessToken();
				}
			}

			setIsTokenReady(true);
		};

		initializeAccessToken();

		// cleanup function에서 refreshAttempted 초기화
		return () => {
			refreshAttempted.current = false;
		};
	}, [accessToken, setAccessToken, clearAccessToken, router]);

	return { accessToken, isTokenReady, clearAccessToken };
};

export default useAuthToken;
