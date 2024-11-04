"use client";

import axiosInstance from "@lib/api/axiosInstance";

// 운세 출석 현황 조회
export const selectFortuneList = () => {
	return axiosInstance.get(`/attendances`);
};

// 운세 출석 체크
export const insertFortune = () => {
	return axiosInstance.post(`/attendances`);
};
