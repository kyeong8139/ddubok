import { IUserDto } from "@interface/components/user";
import { jwtDecode } from "jwt-decode";

export const getTokenInfo = (token: string): IUserDto => {
	try {
		const parts = token.split(" ");
		if (parts.length === 2 && parts[0] === "Bearer") {
			const jwtToken = parts[1];
			const decodedToken = jwtDecode<{ memberId: number; role: string }>(jwtToken);
			return { memberId: decodedToken.memberId, role: decodedToken.role };
		} else {
			throw new Error("Invalid token format");
		}
	} catch (error) {
		console.error("Error decoding token:", error);
		return { memberId: 0, role: "" };
	}
};
