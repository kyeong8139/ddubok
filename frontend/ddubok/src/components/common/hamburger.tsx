"use client";

import { useContext } from "react";

import { MenuContext } from "@context/menu-context";

const Hamburger = () => {
	const { isMenuOpen, toggleMenu } = useContext(MenuContext);

	return (
		<button
			className="relative flex flex-col justify-evenly items-center w-6 h-6"
			onClick={toggleMenu}
		>
			<span
				className={`block h-0.375 w-5 rounded bg-white transition-transform duration-300 ${
					isMenuOpen ? "rotate-45 translate-y-1.5" : ""
				}`}
			/>
			<span
				className={`block h-0.375 w-5 rounded bg-white transition-opacity duration-300 ${
					isMenuOpen ? "opacity-0" : "opacity-100"
				}`}
			/>
			<span
				className={`block h-0.375 w-5 rounded bg-white transition-transform duration-300 ${
					isMenuOpen ? "-rotate-45 -translate-y-1.5" : ""
				}`}
			/>
		</button>
	);
};

export default Hamburger;
