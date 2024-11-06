// src/hooks/useKakaoInit.ts
import { useEffect, useState } from "react";

declare global {
	interface Window {
		Kakao: any;
	}
}

const useKakaoInit = () => {
	const [isInitialized, setIsInitialized] = useState(false);

	useEffect(() => {
		const initializeKakao = () => {
			if (typeof window === "undefined") return;

			if (!window.Kakao) {
				console.error("Kakao SDK not loaded yet");
				return;
			}

			try {
				if (!window.Kakao.isInitialized()) {
					const key = process.env.NEXT_PUBLIC_KAKAO_KEY;
					window.Kakao.init(key);
					console.log("Kakao initialized successfully");
				} else {
					console.log("Kakao was already initialized");
				}

				setIsInitialized(true);
			} catch (error) {
				console.error("Kakao initialization error:", error);
				setIsInitialized(false);
			}
		};

		// Script 로드 확인을 위한 인터벌
		const checkAndInitialize = setInterval(() => {
			if (window.Kakao) {
				initializeKakao();
				clearInterval(checkAndInitialize);
			}
		}, 100);

		// 컴포넌트 언마운트 시 인터벌 제거
		return () => clearInterval(checkAndInitialize);
	}, []);

	return isInitialized;
};

export default useKakaoInit;
