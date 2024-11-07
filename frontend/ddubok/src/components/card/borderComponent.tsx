"use client";

import React, { useState, useEffect, useCallback } from "react";
import Image from "next/image";
import { IBorderComponentProps } from "@interface/components/border";
import { fabric } from "fabric";
import { chunk } from "lodash";
import { CaretLeft, CaretRight, X } from "@phosphor-icons/react";

const borderImages = Array.from({ length: 13 }, (_, index) => `/assets/border/border (${index + 1}).png`);

function BorderComponent({ canvas }: IBorderComponentProps) {
	const [selectedBorder, setSelectedBorder] = useState<string | null>(null);
	const [currentPage, setCurrentPage] = useState(0);
	const [isProcessing, setIsProcessing] = useState(false);

	const itemsPerPage = 8;
	const pages = chunk([null, ...borderImages], itemsPerPage);
	const totalPages = Math.ceil((borderImages.length + 1) / itemsPerPage);

	useEffect(() => {
		if (!canvas) return;

		const updateSelectedBorder = () => {
			const border = canvas.getObjects().find((obj) => obj.data?.type === "border");
			if (!border) {
				setSelectedBorder(null);
				return;
			}

			if (border instanceof fabric.Image) {
				const src = border.getSrc();
				if (src) {
					try {
						const fullUrl = src.startsWith("data:") ? src : new URL(src, window.location.origin).href;
						const pathname = new URL(fullUrl).pathname;
						const matchingBorder = borderImages.find((bg) => pathname.includes(bg));
						if (matchingBorder) {
							setSelectedBorder(matchingBorder);
						}
					} catch (error) {}
				}
			}
		};

		updateSelectedBorder();
		canvas.on("object:added", updateSelectedBorder);
		canvas.on("object:removed", updateSelectedBorder);

		return () => {
			canvas.off("object:added", updateSelectedBorder);
			canvas.off("object:removed", updateSelectedBorder);
		};
	}, [canvas]);

	const setBorder = useCallback(
		async (imageUrl: string | null) => {
			if (!canvas || isProcessing) return;

			setIsProcessing(true);

			try {
				const existingBorder = canvas.getObjects().find((obj) => obj.data?.type === "border");
				if (existingBorder) {
					canvas.remove(existingBorder);
				}

				if (imageUrl === null) {
					setSelectedBorder(null);
					canvas.renderAll();
					return;
				}

				await new Promise((resolve) => {
					fabric.Image.fromURL(imageUrl, (img) => {
						img.set({
							scaleX: canvas.width! / img.width!,
							scaleY: canvas.height! / img.height!,
							left: 0,
							top: 0,
							selectable: false,
							evented: false,
							data: { type: "border" },
						});

						const backgroundIndex = canvas.getObjects().findIndex((obj) => obj.data?.type === "background");
						canvas.insertAt(img, backgroundIndex + 1, false);
						canvas.renderAll();
						setSelectedBorder(imageUrl);
						resolve(null);
					});
				});
			} finally {
				setTimeout(() => {
					setIsProcessing(false);
				}, 300);
			}
		},
		[canvas, isProcessing],
	);

	return (
		<div className="w-full">
			<div className="relative">
				<div className="grid grid-cols-4 gap-4">
					<label className="relative cursor-pointer">
						<input
							type="radio"
							className="absolute opacity-0"
							checked={selectedBorder === null}
							onChange={() => !isProcessing && setBorder(null)}
							disabled={isProcessing}
						/>
						<div
							className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all bg-white flex items-center justify-center
               ${selectedBorder === null ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}
               ${isProcessing ? "opacity-50" : ""}`}
						>
							<X
								size={24}
								weight="bold"
								className="text-gray-400"
							/>
						</div>
					</label>

					{borderImages
						.slice(currentPage * (itemsPerPage - 1), (currentPage + 1) * (itemsPerPage - 1))
						.map((imageUrl, index) => (
							<label
								key={index}
								className="relative cursor-pointer"
							>
								<input
									type="radio"
									className="absolute opacity-0"
									checked={selectedBorder === imageUrl}
									onChange={() => !isProcessing && setBorder(imageUrl)}
									disabled={isProcessing}
								/>
								<div
									className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all
                 ${selectedBorder === imageUrl ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}
                 ${isProcessing ? "opacity-50" : ""}`}
								>
									<div className="relative w-full h-full">
										<Image
											src={imageUrl}
											alt={`테두리 ${index + 1}`}
											fill
											sizes="64px"
											className="object-contain"
										/>
									</div>
								</div>
							</label>
						))}
				</div>

				<div className="flex justify-between items-center mt-4">
					<button
						onClick={() => setCurrentPage((prev) => Math.max(0, prev - 1))}
						disabled={currentPage === 0}
						className={`p-2 rounded-full hover:bg-gray-100 transition-colors
             ${currentPage === 0 ? "text-gray-300" : "text-gray-600"}`}
					>
						<CaretLeft
							size={24}
							color="#6b7280"
							weight="light"
						/>
					</button>

					<div className="flex gap-1">
						{Array.from({ length: totalPages }, (_, i) => (
							<div
								key={i}
								className={`w-2 h-2 rounded-full transition-colors ${
									i === currentPage ? "bg-ddubokPurple" : "bg-gray-200"
								}`}
							/>
						))}
					</div>

					<button
						onClick={() => setCurrentPage((prev) => Math.min(totalPages - 1, prev + 1))}
						disabled={currentPage === totalPages - 1}
						className={`p-2 rounded-full hover:bg-gray-100 transition-colors
             ${currentPage === totalPages - 1 ? "text-gray-300" : "text-gray-600"}`}
					>
						<CaretRight
							size={24}
							color="#6b7280"
							weight="light"
						/>
					</button>
				</div>
			</div>
		</div>
	);
}

export default BorderComponent;
