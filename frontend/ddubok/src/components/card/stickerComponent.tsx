"use client";

import React, { useState } from "react";
import { chunk } from "lodash";
import Image from "next/image";

import { fabric } from "fabric";
import { CaretLeft, CaretRight } from "@phosphor-icons/react";

const stickerImages = [
	"/assets/stickers/sticker_1.jpg",
	"/assets/stickers/sticker_2.jpg",
	"/assets/stickers/sticker_3.png",
	"/assets/stickers/sticker_4.png",
	"/assets/stickers/sticker_5.png",
	"/assets/stickers/sticker_6.png",
	"/assets/stickers/sticker_7.png",
	"/assets/stickers/sticker_8.png",
	"/assets/stickers/sticker_9.png",
	"/assets/stickers/sticker_10.png",
	"/assets/stickers/sticker_11.png",
	"/assets/stickers/sticker_12.png",
];

interface StickerComponentProps {
	canvas?: fabric.Canvas | null;
}

const StickerComponent: React.FC<StickerComponentProps> = ({ canvas }) => {
	const [currentPage, setCurrentPage] = useState(0);
	const itemsPerPage = 8;
	const pages = chunk(stickerImages, itemsPerPage);
	const totalPages = Math.ceil(stickerImages.length / itemsPerPage);

	const addSticker = (imageUrl: string) => {
		if (!canvas) return;

		fabric.Image.fromURL(imageUrl, (img) => {
			const defaultWidth = canvas.width! * 0.2;
			const scale = defaultWidth / img.width!;

			img.set({
				scaleX: scale,
				scaleY: scale,
				left: (canvas.width! - img.width! * scale) / 2,
				top: (canvas.height! - img.height! * scale) / 2,
				selectable: true,
				evented: true,
				hasBorders: true,
				hasControls: true,
				lockUniScaling: true,
				cornerSize: 8,
				cornerStyle: "circle",
				transparentCorners: false,
				padding: 10,
				borderColor: "#7e22ce",
				cornerColor: "#7e22ce",
				data: { type: "sticker" },
			});

			const baseControls = fabric.Object.prototype.controls;

			fabric.Object.prototype.controls.deleteControl = new fabric.Control({
				x: 0.5,
				y: -0.5,
				offsetY: 0,
				offsetX: 0,
				cursorStyle: "pointer",
				mouseUpHandler: function (eventData, transform) {
					const target = transform.target;
					canvas.remove(target);
					canvas.requestRenderAll();
					return true;
				},
				render: function (ctx, left, top, styleOverride, fabricObject) {
					const size = 24;
					ctx.save();
					ctx.translate(left, top);

					ctx.beginPath();
					ctx.arc(0, 0, size / 2, 0, Math.PI * 2);
					ctx.fillStyle = "white";
					ctx.fill();
					ctx.strokeStyle = "#7e22ce";
					ctx.lineWidth = 1;
					ctx.stroke();

					ctx.fillStyle = "#7e22ce";
					ctx.font = "16px Arial";
					ctx.textAlign = "center";
					ctx.textBaseline = "middle";
					ctx.fillText("×", 0, 0);

					ctx.restore();
				},
			});

			img.controls = {
				...baseControls,
				mtr: new fabric.Control({ visible: false }),
				deleteControl: fabric.Object.prototype.controls.deleteControl,
				br: new fabric.Control({
					x: 0.5,
					y: 0.5,
					actionHandler: function (eventData, transform, x, y) {
						const rotateChange = baseControls.mtr.actionHandler(eventData, transform, x, y);
						const scaleChange = baseControls.br.actionHandler(eventData, transform, x, y);
						return rotateChange && scaleChange;
					},
					cursorStyle: "se-resize",
					actionName: "resize_rotate",
					render: function (ctx, left, top, styleOverride, fabricObject) {
						const size = 8;
						ctx.save();
						ctx.fillStyle = "#6EFFBF";
						ctx.beginPath();
						ctx.arc(left, top, size / 2, 0, Math.PI * 2);
						ctx.fill();
						ctx.restore();
					},
				}),
			};

			canvas.add(img);
			canvas.setActiveObject(img);
			canvas.renderAll();
		});
	};

	return (
		<div className="w-full">
			<div className="relative">
				<div className="grid grid-cols-4 gap-4">
					{pages[currentPage]?.map((imageUrl, index) => (
						<div
							key={index}
							className="cursor-pointer"
							onClick={() => addSticker(imageUrl)}
						>
							<div className="w-16 h-16 rounded-lg overflow-hidden border-2 border-gray-200 hover:border-ddubokPurple transition-all">
								<div className="relative w-full h-full">
									<Image
										src={imageUrl}
										alt={`스티커 ${index + 1}`}
										fill
										sizes="64px"
										className="object-contain"
									/>
								</div>
							</div>
						</div>
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
};

export default StickerComponent;
