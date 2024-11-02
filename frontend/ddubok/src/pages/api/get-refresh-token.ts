import { NextApiResponse } from "next";
import { cookies } from "next/headers";

export default function handler(res: NextApiResponse) {
	try {
		const cookieStore = cookies();
		const refreshToken = cookieStore.get("refresh")?.value || null;

		if (refreshToken) {
			res.status(200).json({ refreshToken });
		} else {
			res.status(404).json({ message: "Refresh token not found" });
		}
	} catch (error) {
		console.error("Error fetching refresh token:", error);
		res.status(500).json({ message: "Internal server error" });
	}
}
