"use client";

import { usePathname } from "next/navigation";

import { CaretLeft, List } from "@phosphor-icons/react";
import Image from "next/image";

const Header = () => {
	const pathname = usePathname();
	console.log(pathname);

	const renderHeader = () => {
		switch (pathname) {
			case "/":
				return (
					<div className="flex justify-end items-center h-full">
						<List
							size={24}
							color="white"
						/>
					</div>
				);
			case "/create":
				return (
					<div className="flex justify-center items-center h-full">
						<Image
							src="/assets/ddubok.png"
							alt="ddubok"
							width={56}
							height={56}
							objectFit="contain"
						/>
					</div>
				);
			case "/admin":
			case "/collection":
			case "/fortune":
				return (
					<div className="flex justify-between items-center h-full">
						<CaretLeft
							size={24}
							color="white"
						/>
						<List
							size={24}
							color="white"
						/>
					</div>
				);
			default:
				return <></>;
		}
	};

	return (
		<div
			id="header"
			className="h-14 px-4"
		>
			{renderHeader()}
		</div>
	);
};

export default Header;
