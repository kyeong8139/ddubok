import { NextRequest, NextResponse } from "next/server";
import { getTokenInfo } from "@lib/utils/authUtils";

export function middleware(request: NextRequest) {
	const { pathname } = request.nextUrl;

	const authHeader = request.headers.get("authorization");
	const accessToken = authHeader?.split(" ")[1];

	const decodedToken = accessToken ? getTokenInfo(accessToken) : null;
	const user = decodedToken
		? {
				memberId: decodedToken.memberId,
				nickname: "",
				role: decodedToken.role,
		  }
		: null;

	if (pathname.startsWith("/mypage") || pathname.startsWith("/fortune") || pathname.startsWith("/book")) {
		if (!accessToken) return NextResponse.redirect(new URL("/login", request.url));
	}

	if (pathname.startsWith("/admin") && user?.role !== "admin") {
		return NextResponse.redirect(new URL("/login", request.url));
	}

	return NextResponse.next();
}

export const config = {
	matcher: ["/mypage/:path*", "/fortune/:path*", "/book/:path*", "/admin/:path*"],
};
