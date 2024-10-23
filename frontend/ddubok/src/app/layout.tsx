import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

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
			<body className={inter.className}>{children}</body>
		</html>
	);
}
