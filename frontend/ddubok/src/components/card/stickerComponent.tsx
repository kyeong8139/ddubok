"use client";
import React, { useState, useEffect } from "react";
import { fabric } from "fabric";
import { chunk } from "lodash";
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

	useEffect(() => {
		if (!canvas) return;

		// 기존의 삭제 버튼 제거
		const existingButton = document.querySelector(".delete-btn");
		if (existingButton) {
			existingButton.remove();
		}

		// 삭제 버튼 생성
		const deleteBtn = document.createElement("div");
		deleteBtn.innerHTML = "×";
		deleteBtn.style.position = "absolute";
		deleteBtn.style.zIndex = "999";
		deleteBtn.style.cursor = "pointer";
		deleteBtn.style.fontSize = "20px";
		deleteBtn.style.fontWeight = "bold";
		deleteBtn.style.color = "#7e22ce";
		deleteBtn.style.background = "white";
		deleteBtn.style.borderRadius = "50%";
		deleteBtn.style.width = "20px";
		deleteBtn.style.height = "20px";
		deleteBtn.style.lineHeight = "16px";
		deleteBtn.style.textAlign = "center";
		deleteBtn.style.boxShadow = "0 0 5px rgba(0,0,0,0.2)";
		deleteBtn.style.display = "none";
		deleteBtn.className = "delete-btn";

		const canvasContainer = document.querySelector(".canvas-container");
		if (canvasContainer) {
			canvasContainer.appendChild(deleteBtn);
		}

		// 삭제 버튼 위치 업데이트 함수
		const updateDeleteButtonPosition = (target: fabric.Object) => {
			if (!target.aCoords) return;

			const canvasEl = document.querySelector(".canvas-container");
			if (!canvasEl) return;

			const rect = canvasEl.getBoundingClientRect();
			const scale = rect.width / (canvas?.width || 1);
			const topRight = target.aCoords.tr;

			deleteBtn.style.left = `${topRight.x * scale}px`;
			deleteBtn.style.top = `${topRight.y * scale - 20}px`;
		};

		// 스티커 삭제 핸들러
		const handleStickerDelete = (target: fabric.Object) => {
			canvas.remove(target);
			deleteBtn.style.display = "none";
			canvas.renderAll();
			canvas.discardActiveObject();
		};

		// 객체 선택 이벤트 핸들러
		const handleObjectSelect = (e: fabric.IEvent) => {
			const target = canvas.getActiveObject();
			if (!target) {
				deleteBtn.style.display = "none";
				return;
			}

			if (target.data?.type === "sticker") {
				updateDeleteButtonPosition(target);
				deleteBtn.style.display = "block";

				// 이벤트 리스너 재설정
				deleteBtn.onclick = () => handleStickerDelete(target);
			} else {
				deleteBtn.style.display = "none";
			}
		};

		// 객체 수정 이벤트 핸들러
		const handleObjectModification = (e: fabric.IEvent) => {
			const target = e.target;
			if (!target || target.data?.type !== "sticker") return;

			updateDeleteButtonPosition(target);
		};

		// 선택 해제 이벤트 핸들러
		const handleSelectionClear = () => {
			deleteBtn.style.display = "none";
		};

		// 이벤트 리스너 등록
		canvas.on("selection:created", handleObjectSelect);
		canvas.on("selection:updated", handleObjectSelect);
		canvas.on("object:moving", handleObjectModification);
		canvas.on("object:scaling", handleObjectModification);
		canvas.on("object:rotating", handleObjectModification);
		canvas.on("selection:cleared", handleSelectionClear);

		// 현재 선택된 객체 확인 및 처리
		const activeObject = canvas.getActiveObject();
		if (activeObject?.data?.type === "sticker") {
			updateDeleteButtonPosition(activeObject);
			deleteBtn.style.display = "block";
			deleteBtn.onclick = () => handleStickerDelete(activeObject);
		}

		// 클린업
		return () => {
			// 개별 이벤트 리스너 제거
			canvas.off("selection:created", handleObjectSelect);
			canvas.off("selection:updated", handleObjectSelect);
			canvas.off("object:moving", handleObjectModification);
			canvas.off("object:scaling", handleObjectModification);
			canvas.off("object:rotating", handleObjectModification);
			canvas.off("selection:cleared", handleSelectionClear);

			if (deleteBtn && deleteBtn.parentNode) {
				deleteBtn.parentNode.removeChild(deleteBtn);
			}
		};
	}, [canvas]);

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
			img.controls = {
				...baseControls,
				mtr: new fabric.Control({ visible: false }),
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
								<img
									src={imageUrl}
									alt={`스티커 ${index + 1}`}
									className="w-full h-full object-contain"
								/>
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
