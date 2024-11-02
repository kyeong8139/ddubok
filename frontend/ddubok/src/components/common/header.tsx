"use client";

import Image from "next/image";
import { usePathname, useRouter } from "next/navigation";
import { useContext, useEffect } from "react";

import Hamburger from "@components/common/hamburger";
import Menu from "@components/common/menu";
import { MenuContext } from "@context/menu-context";

import { CaretLeft } from "@phosphor-icons/react";

const Header = () => {
	const pathname = usePathname();
	const { isMenuOpen, closeMenu } = useContext(MenuContext);
	const router = useRouter();

	useEffect(() => {
		closeMenu();
	}, [pathname, closeMenu]);

	const renderHeaderContent = () => {
		switch (pathname) {
			case "/":
				return (
					<div className="flex justify-end items-center h-full">
						<Hamburger />
					</div>
				);
			case "/create":
			case "/share":
				return (
					<div className="flex justify-center items-center h-full">
						<Image
							src="/assets/ddubok.png"
							alt="ddubok"
							width={72}
							height={72}
						/>
					</div>
				);
			case "/admin/user":
			case "/admin/report":
			case "/admin/setting":
			case "/book":
			case "/fortune":
				return (
					<div className="flex justify-between items-center h-full">
						<CaretLeft
							size={24}
							color="white"
							onClick={() => router.back()}
						/>
						<Hamburger />
					</div>
				);
			case "/login":
				return (
					<div className="flex justify-start items-center h-full">
						<CaretLeft
							size={24}
							color="white"
							onClick={() => router.back()}
						/>
					</div>
				);
			default:
				return null;
		}
	};

	return (
		<div
			id="header"
			className="max-w-[480px] w-full fixed top-0 bg-ddubokBackground z-10"
		>
			<div className="h-14 px-4">{renderHeaderContent()}</div>
			<div
				className={`transition-opacity duration-300
					${isMenuOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
			>
				<Menu />
			</div>
		</div>
	);
};

export default Header;
