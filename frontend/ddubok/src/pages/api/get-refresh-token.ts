import { NextApiRequest, NextApiResponse } from "next";
import nookies from "nookies";

export default function handler(req: NextApiRequest, res: NextApiResponse) {
	const cookies = nookies.get({ req });
	const refreshToken = cookies.refresh || null;

	res.status(200).json({ refreshToken });
}
