"use client";

import { useContext } from "react";

import { ModalContext } from "@context/modal-context";

const Modal = ({ children }: { children: React.ReactNode }) => {
	const { closeModal } = useContext(ModalContext);

	return (
		<div
			id="modal"
			className="absolute inset-0 flex items-center justify-center"
		>
			<div
				id="overlay"
				className="fixed bottom-0 w-screen max-w-[480px] z-10 h-full bg-black bg-opacity-30 backdrop-blur-sm"
				onClick={closeModal}
			></div>
			<div
				id="content"
				className="fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[calc(100%-64px)] max-w-[416px] bg-white rounded-lg p-8 z-10 font-nexonRegular"
			>
				{children}
			</div>
		</div>
	);
};

export default Modal;
