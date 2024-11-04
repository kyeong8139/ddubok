import axiosInstance from "@lib/api/axiosInstance";

export const sendCard = async (content: string, writerName: string, seasonId: number, image: string | null) => {
	try {
		const formData = new FormData();
		formData.append("content", content);
		formData.append("writerName", writerName);
		formData.append("seasonId", seasonId.toString());

		if (image) {
			try {
				// base64 형식 검증
				if (!image.includes("base64,")) {
					throw new Error("Invalid base64 image format");
				}

				// base64 문자열에서 실제 바이너리 데이터 추출
				const base64Data = image.split(",")[1];

				// base64 디코딩 시도
				let binaryString: string;
				try {
					binaryString = window.atob(base64Data);
				} catch (e) {
					console.error("Base64 디코딩 실패:", e);
					throw new Error("Invalid base64 encoding");
				}

				// 바이너리 데이터를 Uint8Array로 변환
				const bytes = new Uint8Array(binaryString.length);
				for (let i = 0; i < binaryString.length; i++) {
					bytes[i] = binaryString.charCodeAt(i);
				}

				// Blob 및 File 객체 생성
				const imageBlob = new Blob([bytes], { type: "image/png" });
				const imageFile = new File([imageBlob], "card-image.png", { type: "image/png" });
				formData.append("image", imageFile);
			} catch (imageError) {
				console.error("이미지 처리 중 오류:", imageError);
				throw new Error("이미지 처리에 실패했습니다. 다시 시도해주세요.");
			}
		}

		const response = await axiosInstance.post("/api/cards", formData, {
			headers: {
				"Content-Type": "multipart/form-data",
			},
		});

		return response.data;
	} catch (error) {
		console.error("카드 전송 실패:", error);
		throw error;
	}
};

export const saveCard = async (cardId: number) => {
	try {
		const response = await axiosInstance.post("/api/cards/save", {
			cardId: cardId,
		});
		return response.data;
	} catch (error) {
		console.error("카드 보관 실패:", error);
		throw error;
	}
};
