"use client";

import axios from "axios";

import axiosInstance from "@lib/api/axiosInstance";

const baseURL = process.env.NEXT_PUBLIC_BASE_URL;
const loginURL = process.env.NEXT_PUBLIC_LOGIN_URL;

// 토큰 재발급
export const reissue = async () => {
	return axios.post(`${baseURL}/auth/reissue`, {
		headers: { "Content-Type": "applicaion/json" },
	});
};

// 리프레시 토큰 확인
export const checkRefreshToken = async () => {
	return axiosInstance.get(`auth/check-refresh-token`);
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

// 회원 탈퇴
