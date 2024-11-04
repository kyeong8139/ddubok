"use client";

import axiosInstance from "@lib/api/axiosInstance";

// 카드 전체 조회
export const selectCardList = () => {
	return axiosInstance.get(`/cards`);
};

// 카드 시즌별 조회
export const selectCardSeasonList = (seasonId: number) => {
	return axiosInstance.get(`/cards/albums/${seasonId}`);
};

// 카드 상세 조회
export const selectCardDetail = (cardId: number) => {
	return axiosInstance.get(`/cards/${cardId}`);
};

// 카드 정보 미리보기 (조르기 화면)
export const selectPreviewList = (memberId: number) => {
	return axiosInstance.get(`/cards/${memberId}/preview`);
};
