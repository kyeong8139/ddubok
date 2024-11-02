"use client";

import axiosInstance from "@lib/api/axiosInstance";

const baseURL = process.env.NEXT_PUBLIC_LOGIN_URL;

// 토큰 재발급
export const reissue = async () => {
	return axiosInstance.post(`auth/reissue`);
};

// 로그인
export const kakaoLogin = () => {
	window.location.href = `${baseURL}/kakao`;
};

export const naverLogin = () => {
	window.location.href = `${baseURL}/naver`;
};

export const googleLogin = () => {
	window.location.href = `${baseURL}/google`;
};

export const facebookLogin = () => {
	window.location.href = `${baseURL}/facebook`;
};

// 로그아웃

// 회원 탈퇴
