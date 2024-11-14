"use client";

import axiosInstance from "@lib/api/axiosInstance";

export const selectMainInfo = () => {
	return axiosInstance.get(`/main`);
};
