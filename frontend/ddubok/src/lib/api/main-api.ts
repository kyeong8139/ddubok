"use client";

import axios from "axios";

export const selectMainInfo = () => {
	return axios.get(`/main`, {
		headers: { "Content-Type": "application/json" },
		withCredentials: true,
	});
};
