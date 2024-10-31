import axiosInstance from "@lib/api/axiosInstance";

// 토큰 재발급
export const reissue = async () => {
	return axiosInstance.post(`auth/reissue`);
};

// 로그아웃

// 회원 탈퇴
