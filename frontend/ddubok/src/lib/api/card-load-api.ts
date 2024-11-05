"use client";

import axiosInstance from "@lib/api/axiosInstance";

// 카드 전체 조회
export const selectCardList = (size: number, page: number) => {
	return axiosInstance.get(`/cards`, {
		params: { size, page },
	});
};

// 카드 시즌별 조회
export const selectCardSeasonList = (size: number, page: number, seasonId: number) => {
	return axiosInstance.get(`/cards/albums/${seasonId}`, {
		params: { size, page },
	});
};

// 카드 상세 조회
export const selectCardDetail = (cardId: number) => {
	return axiosInstance.get(`/cards/${cardId}`);
};

// 카드 정보 미리보기 (조르기 화면)
export const selectPreviewList = (memberId: number) => {
	return axiosInstance.get(`/cards/${memberId}/preview`);
};

// 카드 숨김 (사실상 삭제)
export const deleteCard = (cardId: number) => {
	return axiosInstance.delete(`/cards/${cardId}`);
};
