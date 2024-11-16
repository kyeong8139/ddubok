"use client";

import axios from "axios";

const baseURL = process.env.NEXT_PUBLIC_BASE_URL;

export const selectMainInfo = () => {
	return axios.get(`${baseURL}/main`, {
		headers: { "Content-Type": "application/json" },
		withCredentials: true,
	});
};
