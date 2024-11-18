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
				} else {
				}

				setIsInitialized(true);
			} catch (error) {
				console.error("Kakao initialization error:", error);
				setIsInitialized(false);
			}
		};

		const checkAndInitialize = setInterval(() => {
			if (window.Kakao) {
				initializeKakao();
				clearInterval(checkAndInitialize);
			}
		}, 100);

		return () => clearInterval(checkAndInitialize);
	}, []);

	return isInitialized;
};

export default useKakaoInit;
