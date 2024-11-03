"use client";

import axios from "axios";

import axiosInstance from "@lib/api/axiosInstance";

const baseURL = process.env.NEXT_PUBLIC_BASE_URL;
const loginURL = process.env.NEXT_PUBLIC_LOGIN_URL;

// 토큰 재발급
export const reissue = async () => {
	return axios.post(
		`${baseURL}/auth/reissue`,
		{},
		{
			headers: { "Content-Type": "application/json" },
			withCredentials: true,
		},
	);
};

// 리프레시 토큰 확인
export const checkRefreshToken = async () => {
	return axios.get(`${baseURL}/auth/check-refresh-token`, {
		headers: { "Content-Type": "application/json" },
		withCredentials: true,
	});
};

// 로그인
export const kakaoLogin = () => {
	window.location.href = `${loginURL}/kakao`;
};

export const naverLogin = () => {
	window.location.href = `${loginURL}/naver`;
};

export const googleLogin = () => {
	window.location.href = `${loginURL}/google`;
};

export const facebookLogin = () => {
	window.location.href = `${loginURL}/facebook`;
};

// 로그아웃
export const logout = async () => {
	return axiosInstance.post(`/auth/logout`);
};

// 회원 탈퇴
export const deleteUser = async () => {
	return axiosInstance.delete(`/members`);
};
