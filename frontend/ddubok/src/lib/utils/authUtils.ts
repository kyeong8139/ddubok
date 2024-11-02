import { jwtDecode } from "jwt-decode";

export const decodeAccessToken = (token: string) => {
	try {
		const decoded = jwtDecode<{ [key: string]: any }>(token);
		return decoded;
	} catch (error) {
		console.error(error);
		return null;
	}
};
