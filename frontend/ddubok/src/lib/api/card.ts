import axiosInstance from "@lib/api/axiosInstance";

export const sendCard = async (content: string, writerName: string, seasonId: number, image: string | null) => {
	try {
		const formData = new FormData();
		formData.append("content", content);
		formData.append("writerName", writerName);
		formData.append("seasonId", seasonId.toString());

		// 이미지가 base64 문자열이라면 File 객체로 변환
		if (image) {
			// base64 문자열에서 실제 바이너리 데이터 추출
			const base64Data = image.split(",")[1];
			const binaryString = window.atob(base64Data);
			const bytes = new Uint8Array(binaryString.length);

			for (let i = 0; i < binaryString.length; i++) {
				bytes[i] = binaryString.charCodeAt(i);
			}

			const imageBlob = new Blob([bytes], { type: "image/png" });
			const imageFile = new File([imageBlob], "card-image.png", { type: "image/png" });
			formData.append("image", imageFile);
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
