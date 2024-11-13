"use client";

import axiosInstance from "@lib/api/axiosInstance";

// 토큰 저장
export const insertToken = (token: string) => {
	return axiosInstance.post(`/notifications`, { token });
};

// 토큰 삭제
export const removeToken = () => {
	return axiosInstance.delete(`/notifications`);
};
