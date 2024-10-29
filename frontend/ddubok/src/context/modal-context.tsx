"use client";

import { createContext, ReactNode, useCallback, useState } from "react";

import { IModalContextState } from "@interface/context/modal-context";

const ModalContext = createContext<IModalContextState>({
	isModalOpen: false,
	openModal: () => {},
	closeModal: () => {},
});

const ModalProvider = ({ children }: { children: ReactNode }) => {
	const [isModalOpen, setIsModalOpen] = useState(false);

	const openModal = useCallback(() => {
		setIsModalOpen(true);
	}, []);

	const closeModal = useCallback(() => {
		setIsModalOpen(false);
	}, []);

	return <ModalContext.Provider value={{ isModalOpen, openModal, closeModal }}>{children}</ModalContext.Provider>;
};

export { ModalContext, ModalProvider };
