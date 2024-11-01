import axiosInstance from "@lib/api/axiosInstance";

const baseURL = process.env.NEXT_PUBLIC_LOGIN_URL;

// 토큰 재발급
export const reissue = async () => {
	return axiosInstance.post(`auth/reissue`);
};

// 로그인
export const kakaoLogin = () => {
	console.log(`${baseURL}/kakao`);
	window.location.href = `${baseURL}/kakao`;
};

// 로그아웃

// 회원 탈퇴
