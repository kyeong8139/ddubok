import type { Metadata } from "next";
import localFont from "next/font/local";
import Script from "next/script";

import Header from "@components/common/header";
import { MenuProvider } from "@context/menu-context";
import { ModalProvider } from "@context/modal-context";

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
		<html lang="ko">
			<head>
				{/* Google Analytics Script */}
				<Script
					src="https://www.googletagmanager.com/gtag/js?id=G-776Q6TRWEQ"
					strategy="afterInteractive"
				/>
				<Script
					id="google-analytics"
					strategy="afterInteractive"
				>
					{`
						window.dataLayer = window.dataLayer || [];
						function gtag(){dataLayer.push(arguments);}
						gtag('js', new Date());
						gtag('config', 'G-776Q6TRWEQ');
					`}
				</Script>
				<meta
					http-equiv="Content-Security-Policy"
					content="upgrade-insecure-requests"
				></meta>
			</head>
			<body
				className={`${pyeongChangPeaceBold.variable} ${nexonGothicLight.variable} ${nexonGothicRegular.variable} ${nexonGothicBold.variable}`}
				suppressHydrationWarning={true}
			>
				<div
					className="font-sans min-h-screen mx-auto w-full max-w-[480px] bg-ddubokBackground relative overflow-hidden"
					style={{
						boxShadow: "0 14px 28px #0000001a, 0 10px 10px #0000001a",
					}}
				>
					<MenuProvider>
						<ModalProvider>
							<Header />
							<div className="mt-14">{children}</div>
						</ModalProvider>
					</MenuProvider>
				</div>
			</body>
		</html>
	);
}
