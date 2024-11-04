import axios from "axios";
import axiosInstance from "@lib/api/axiosInstance";
const baseURL = process.env.NEXT_PUBLIC_BASE_URL;

export const sendCard = async (content: string, writerName: string, seasonId: number, image: string | null) => {
	try {
		const formData = new FormData();

		formData.append(
			"req",
			JSON.stringify({
				content,
				writerName,
				seasonId,
				memberId: null,
			}),
		);

		if (image) {
			try {
				if (image.startsWith("data:image")) {
					if (!image.includes("base64,")) {
						throw new Error("Invalid base64 image format");
					}

					const base64Data = image.split(",")[1];
					let binaryString: string;

					try {
						binaryString = window.atob(base64Data);
					} catch (e) {
						console.error("Base64 디코딩 실패:", e);
						throw new Error("Invalid base64 encoding");
					}

					const bytes = new Uint8Array(binaryString.length);
					for (let i = 0; i < binaryString.length; i++) {
						bytes[i] = binaryString.charCodeAt(i);
					}

					const imageBlob = new Blob([bytes], { type: "image/png" });
					const imageFile = new File([imageBlob], "card-image.png", { type: "image/png" });
					formData.append("image", imageFile);
				} else {
					try {
						const response = await fetch(image);
						if (!response.ok) throw new Error("Failed to fetch image");
						const blob = await response.blob();
						const imageFile = new File([blob], "card-image.png", { type: blob.type });
						formData.append("image", imageFile);
					} catch (fetchError) {
						console.error("이미지 가져오기 실패:", fetchError);
						throw new Error("이미지 가져오기에 실패했습니다.");
					}
				}
			} catch (imageError) {
				console.error("이미지 처리 중 오류:", imageError);
				throw new Error("이미지 처리에 실패했습니다. 다시 시도해주세요.");
			}
		}

		if (process.env.NODE_ENV === "development") {
			Array.from(formData.entries()).forEach(([key, value]) => {
				console.log("FormData:", key, value);
			});
		}

		const response = await axios.post(`${baseURL}/cards`, formData, {
			headers: {
				"Content-Type": "multipart/form-data",
			},
			withCredentials: true,
		});

		if (process.env.NODE_ENV === "development") {
			console.log("Response:", response.data);
		}

		return response.data;
	} catch (error) {
		if (axios.isAxiosError(error)) {
			console.error("요청 에러:", {
				status: error.response?.status,
				data: error.response?.data,
				headers: error.response?.headers,
			});
		}
		console.error("카드 전송 실패:", error);
		throw error;
	}
};
export const saveCard = async (cardId: number) => {
	try {
		const response = await axiosInstance.post("/cards/save", {
			cardId: cardId,
		});
		return response.data;
	} catch (error) {
		console.error("카드 보관 실패:", error);
		throw error;
	}
};

export const getAllCards = async (lastCardId?: number, size: number = 4) => {
	try {
		const response = await axiosInstance.get("/cards", {
			params: {
				lastCardId,
				size,
			},
		});
		return response.data;
	} catch (error) {
		console.error("카드 목록 조회 실패:", error);
		throw error;
	}
};

export const getCardDetail = async (cardId: number) => {
	try {
		const response = await axiosInstance.get(`/cards/${cardId}`);
		return response.data;
	} catch (error) {
		console.error("카드 상세 조회 실패:", error);
		throw error;
	}
};

export const getCardsBySeason = async (seasonId: number, lastCardId?: number, size: number = 4) => {
	try {
		const response = await axiosInstance.get(`/cards/albums/${seasonId}`, {
			params: {
				lastCardId,
				size,
			},
		});
		return response.data;
	} catch (error) {
		console.error("시즌별 카드 목록 조회 실패:", error);
		throw error;
	}
};

export const getCardPreview = async (memberId: number) => {
	try {
		const response = await axiosInstance.get(`/cards/${memberId}/preview`);
		return response.data;
	} catch (error) {
		console.error("카드 프리뷰 조회 실패:", error);
		throw error;
	}
};

export const deleteCard = async (cardId: number) => {
	try {
		const response = await axiosInstance.delete(`/cards/${cardId}`);
		return response.data;
	} catch (error) {
		console.error("카드 삭제 실패:", error);
		throw error;
	}
};

export const receiveCard = async (cardId: number) => {
	try {
		const response = await axiosInstance.post(`/cards/${cardId}`);
		return response.data;
	} catch (error) {
		console.error("카드 수신 실패:", error);
		throw error;
	}
};
