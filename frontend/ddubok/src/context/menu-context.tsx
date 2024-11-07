"use client";

import { createContext, ReactNode, useCallback, useState } from "react";

import { IMenuContextState } from "@interface/context/menu-context";

const MenuContext = createContext<IMenuContextState>({
	isMenuOpen: false,
	toggleMenu: () => {},
	closeMenu: () => {},
});

const MenuProvider = ({ children }: { children: ReactNode }) => {
	const [isMenuOpen, setIsMenuOpen] = useState(false);

	const toggleMenu = useCallback(() => {
		setIsMenuOpen((prev) => !prev);
	}, []);

	const closeMenu = useCallback(() => {
		setIsMenuOpen(false);
	}, []);

	return <MenuContext.Provider value={{ isMenuOpen, toggleMenu, closeMenu }}>{children}</MenuContext.Provider>;
};

export { MenuContext, MenuProvider };
