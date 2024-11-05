"use client";

import { IReportProps } from "@interface/components/card";
import axiosInstance from "@lib/api/axiosInstance";

// 신고하기
export const insertReport = (reportData: IReportProps) => {
	return axiosInstance.post(`/reports`, reportData);
};
