"use client";

import axiosInstance from "@lib/api/axiosInstance";

// 사용자 조회
export const selectUser = () => {
	return axiosInstance.get(`/members`);
};

// 닉네임 변경
export const updateUser = (nickname: string) => {
	return axiosInstance.patch(`/members`, { nickname: nickname });
};
