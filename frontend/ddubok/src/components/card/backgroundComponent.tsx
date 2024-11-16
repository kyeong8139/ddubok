import React, { useState, useRef, useEffect } from "react";
import Image from "next/image";
import { chunk } from "lodash";
import { IBackgroundComponentProps } from "@interface/components/background";
import { createPortal } from "react-dom";
import dynamic from "next/dynamic";

import "cropperjs/dist/cropper.css";
import { fabric } from "fabric";
import { Cropper } from "react-cropper";
import { CaretLeft, CaretRight, ImageSquare, X } from "@phosphor-icons/react";

type BackgroundUploadItem = {
	type: "upload";
};

type BackgroundWhiteItem = {
	type: "white";
};

type BackgroundImageItem = {
	type: "image";
	url: string;
};

type BackgroundItem = BackgroundUploadItem | BackgroundWhiteItem | BackgroundImageItem;

const backgroundImages = Array.from({ length: 27 }, (_, index) => `/assets/background/background (${index + 1}).JPG`);

function BackgroundComponent({ canvas }: IBackgroundComponentProps) {
	const [mounted, setMounted] = useState(false);
	const [selectedBackground, setSelectedBackground] = useState<string | null>("white");
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [image, setImage] = useState<string>("");
	const cropperRef = useRef<any>(null);
	const [currentPage, setCurrentPage] = useState(0);

	const [isWideScreen, setIsWideScreen] = useState(false);

	useEffect(() => {
		const checkScreenSize = () => {
			setIsWideScreen(window.innerWidth >= 420);
		};

		checkScreenSize();
		window.addEventListener("resize", checkScreenSize);

		return () => window.removeEventListener("resize", checkScreenSize);
	}, []);

	useEffect(() => {
		setMounted(true);
	}, []);

	const itemsPerPage = isWideScreen ? 10 : 8;

	const defaultItems: BackgroundItem[] = [
		{ type: "upload" },
		{ type: "white" },
		...backgroundImages.map((url): BackgroundImageItem => ({ type: "image", url })),
	];
	const totalPages = Math.ceil(defaultItems.length / itemsPerPage);
	const pages = chunk(defaultItems, itemsPerPage);

	useEffect(() => {
		if (!canvas) return;

		const updateSelectedBackground = () => {
			const background = canvas.getObjects().find((obj) => obj.data?.type === "background");
			if (!background) {
				setSelectedBackground("white");
				return;
			}

			if (background instanceof fabric.Rect && background.fill === "white") {
				setSelectedBackground("white");
			} else if (background instanceof fabric.Image) {
				const src = background.getSrc();
				if (src) {
					try {
						if (src.startsWith("data:")) {
							setSelectedBackground("custom");
							return;
						}

						const fullUrl = new URL(src, window.location.origin).href;
						const pathname = new URL(fullUrl).pathname;

						const matchingBackground = backgroundImages.find((bg) =>
							pathname.endsWith(bg.split("/").pop()!),
						);

						if (matchingBackground) {
							setSelectedBackground(matchingBackground);
						} else {
							setSelectedBackground("custom");
						}
					} catch (error) {
						setSelectedBackground("custom");
					}
				}
			}
		};

		updateSelectedBackground();
		canvas.on("object:added", updateSelectedBackground);
		canvas.on("object:removed", updateSelectedBackground);

		return () => {
			canvas.off("object:added", updateSelectedBackground);
			canvas.off("object:removed", updateSelectedBackground);
		};
	}, [canvas]);

	const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const file = e.target.files?.[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = () => {
				setImage(reader.result as string);
				setIsModalOpen(true);
			};
			reader.readAsDataURL(file);
		}
	};

	const handleCrop = () => {
		if (!cropperRef.current || !canvas) return;

		const croppedCanvas = cropperRef.current?.cropper.getCroppedCanvas();

		const finalCanvas = document.createElement("canvas");
		finalCanvas.width = croppedCanvas.width;
		finalCanvas.height = croppedCanvas.height;

		const ctx = finalCanvas.getContext("2d");
		if (ctx) {
			ctx.fillStyle = "white";
			ctx.fillRect(0, 0, finalCanvas.width, finalCanvas.height);
			ctx.drawImage(croppedCanvas, 0, 0);
		}

		fabric.Image.fromURL(finalCanvas.toDataURL(), (img) => {
			const existingBackground = canvas.getObjects().find((obj) => obj.data?.type === "background");
			if (existingBackground) {
				canvas.remove(existingBackground);
			}

			img.scaleToWidth(canvas.width!);
			img.scaleToHeight(canvas.height!);
			img.set({
				selectable: false,
				evented: false,
				data: { type: "background" },
			});

			canvas.insertAt(img, 0, false);
			canvas.renderAll();
			setSelectedBackground("custom");
		});

		setIsModalOpen(false);
	};

	const setBackground = (type: string, imageUrl?: string) => {
		if (!canvas) return;

		const existingBackground = canvas.getObjects().find((obj) => obj.data?.type === "background");
		if (existingBackground) {
			canvas.remove(existingBackground);
		}

		if (type === "white") {
			const whiteBg = new fabric.Rect({
				width: canvas.width,
				height: canvas.height,
				fill: "white",
				selectable: false,
				evented: false,
				data: { type: "background" },
			});

			canvas.insertAt(whiteBg, 0, false);
			canvas.renderAll();
			setSelectedBackground("white");
		} else if (imageUrl) {
			fabric.Image.fromURL(imageUrl, (img) => {
				img.scaleToWidth(canvas.width!);
				img.scaleToHeight(canvas.height!);

				img.set({
					selectable: false,
					evented: false,
					data: { type: "background" },
				});

				canvas.insertAt(img, 0, false);
				canvas.renderAll();
				setSelectedBackground(imageUrl);
			});
		}
	};

	const renderItem = (item: BackgroundItem, index: number) => {
		if (item.type === "upload") {
			return (
				<label
					key="upload"
					className="relative cursor-pointer flex justify-center"
				>
					<input
						type="file"
						accept="image/*"
						className="absolute opacity-0"
						onChange={handleFileChange}
					/>
					<div
						className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all bg-white flex items-center justify-center
             ${selectedBackground === "custom" ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}`}
					>
						<div className="absolute flex flex-col items-center justify-center">
							<ImageSquare
								size={32}
								color="#6b7280"
								weight="light"
							/>
							<span className="text-xs text-gray-500 font-nexonRegular">업로드</span>
						</div>
					</div>
				</label>
			);
		}

		if (item.type === "white") {
			return (
				<label
					key="white"
					className="relative cursor-pointer flex justify-center"
				>
					<input
						type="radio"
						className="absolute opacity-0"
						checked={selectedBackground === "white"}
						onChange={() => setBackground("white")}
					/>
					<div
						className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all bg-white
             ${selectedBackground === "white" ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}`}
					>
						<div className="absolute flex items-center justify-center"></div>
					</div>
				</label>
			);
		}

		if (item.type === "image") {
			return (
				<label
					key={index}
					className="relative cursor-pointer flex justify-center"
				>
					<input
						type="radio"
						className="absolute opacity-0"
						checked={selectedBackground === item.url}
						onChange={() => setBackground("image", item.url)}
					/>
					<div
						className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all
           ${selectedBackground === item.url ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}`}
					>
						<div className="relative w-full h-full">
							<Image
								src={item.url}
								alt={`배경 ${index + 1}`}
								fill
								sizes="64px"
								className="object-cover"
								style={{ width: "100%", height: "100%" }}
							/>
						</div>
					</div>
				</label>
			);
		}

		return null;
	};

	return (
		<div className="w-full">
			<div className="relative">
				<div className={`grid ${isWideScreen ? "grid-cols-5" : "grid-cols-4"} gap-4`}>
					{pages[currentPage]?.map((item, index) => renderItem(item, index))}
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

			{mounted &&
				isModalOpen &&
				createPortal(
					<div className="fixed inset-0 flex items-center justify-center z-[9999]">
						<div
							className="fixed inset-0 bg-black bg-opacity-50"
							onClick={() => setIsModalOpen(false)}
						/>
						<div className="relative bg-white rounded-lg p-6 max-w-[416px] w-full mx-4">
							<div className="flex justify-between items-center mb-4">
								<h3 className="text-lg font-semibold">이미지 자르기</h3>
								<button onClick={() => setIsModalOpen(false)}>
									<X
										size={24}
										color="#6b7280"
										weight="regular"
									/>
								</button>
							</div>
							<div className="mt-4">
								{image && (
									<Cropper
										ref={cropperRef}
										src={image}
										style={{ height: 400, width: "100%" }}
										aspectRatio={280 / 495}
										guides={true}
										viewMode={1}
										minCropBoxHeight={10}
										minCropBoxWidth={10}
										background={false}
										responsive={true}
										autoCropArea={1}
										checkOrientation={false}
									/>
								)}
							</div>
							<div className="mt-4 flex justify-end gap-2">
								<button
									className="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
									onClick={() => setIsModalOpen(false)}
								>
									취소
								</button>
								<button
									className="px-4 py-2 bg-ddubokPurple text-white rounded-lg hover:bg-opacity-90"
									onClick={handleCrop}
								>
									적용하기
								</button>
							</div>
						</div>
					</div>,
					document.body,
				)}
		</div>
	);
}

export default dynamic(() => Promise.resolve(BackgroundComponent), {
	ssr: false,
});
