import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";

const pyeongChangPeaceBold = localFont({
	src: "./fonts/PyeongChangPeace-Bold.ttf",
	variable: "--font-pyeongchang-peace-bold",
	weight: "700",
});

const nexonGothicLight = localFont({
	src: "./fonts/NEXONLv1GothicLight.ttf",
	variable: "--font-nexon-gothic-light",
	weight: "300",
});

const nexonGothicRegular = localFont({
	src: "./fonts/NEXONLv1GothicRegular.ttf",
	variable: "--font-nexon-gothic-regular",
	weight: "400",
});

const nexonGothicBold = localFont({
	src: "./fonts/NEXONLv1GothicBold.ttf",
	variable: "--font-nexon-gothic-bold",
	weight: "700",
});

export const metadata: Metadata = {
	title: "뚜복 (DDUBOK)",
	description: "당신을 위한 행운 배달부🍀",
	manifest: "/manifest.json",
	icons: {
		icon: "icons/icon-512x512.png",
	},
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
	return (
		<html lang="en">
			<body
				className={`${pyeongChangPeaceBold.variable} ${nexonGothicLight.variable} ${nexonGothicRegular.variable} ${nexonGothicBold.variable} font-sans`}
			>
				{children}
			</body>
		</html>
	);
}
