"use client";

import { ISeasonProps } from "@interface/components/season";
import axiosInstance from "@lib/api/axiosInstance";

const baseURL = process.env.NEXT_PUBLIC_BASE_URL;

// 신고 목록 조회
export const selectReportList = (state: string | null, page: number, size: number) => {
	return axiosInstance.post(`/admins/reports`, {
		state,
		page,
		size,
	});
};

// 신고 상세 조회
export const selectReport = (reportId: number) => {
	return axiosInstance.get(`/admins/reports/${reportId}`);
};

// 신고 처리
export const updateReport = (reportId: number, state: string) => {
	return axiosInstance.patch(`admins/reports/${reportId}`, { state });
};

// 사용자 목록 조회
export const selectMemberList = (state: string | null, searchName: string) => {
	return axiosInstance.post(`admins/members`, {
		state,
		searchName,
	});
};

// 사용자 상세 조회
export const selectMember = (memberId: number) => {
	return axiosInstance.get(`/admins/members/${memberId}`);
};

// 사용자 상태 변경
export const updateMemberState = (memberId: number) => {
	return axiosInstance.patch(`/admins/members/state/${memberId}`);
};

// 사용자 역할 변경
export const updateMemberRole = (memberId: number) => {
	return axiosInstance.patch(`/admins/members/role/${memberId}`);
};

// 시즌 등록
export const insertSeason = (images: File[], seasonData: ISeasonProps) => {
	const formData = new FormData();

	images.forEach((image) => {
		formData.append("image", image);
	});

	formData.append("req", new Blob([JSON.stringify(seasonData)], { type: "application/json" }));

	return axiosInstance.post(`${baseURL}/admins/seasons`, formData, {
		withCredentials: true,
		headers: {
			"Content-Type": "multipart/form-data",
		},
	});
};

// 시즌 전체 조회
export const selectSeasonList = () => {
	return axiosInstance.get(`/admins/seasons`);
};

// 시즌 상세 조회
export const selectSeason = (seasonId: number) => {
	return axiosInstance.get(`/admins/seasons/${seasonId}`);
};

// 시즌 정보 수정
export const updateSeason = (seasonId: number, images: File[], seasonData: ISeasonProps) => {
	const formData = new FormData();

	images.forEach((image) => {
		formData.append("image", image);
	});

	formData.append("req", new Blob([JSON.stringify(seasonData)], { type: "application/json" }));

	return axiosInstance.put(`/admins/seasons/${seasonId}`, formData, {
		withCredentials: true,
		headers: {
			"Content-Type": "multipart/form-data",
		},
	});
};
