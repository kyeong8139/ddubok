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

		// 캔버스 설정 초기화
		canvas.selection = true; // 다중 선택 활성화

		// 삭제 버튼 생성 함수
		const createDeleteButton = (left: number, top: number) => {
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
			deleteBtn.style.padding = "0 5px";
			deleteBtn.style.boxShadow = "0 0 5px rgba(0,0,0,0.2)";
			deleteBtn.style.display = "none";
			deleteBtn.className = "delete-btn";
			document.querySelector(".canvas-container")?.appendChild(deleteBtn);
			return deleteBtn;
		};

		// 삭제 버튼 위치 업데이트 함수
		const positionDeleteButton = (target: fabric.Object, deleteBtn: HTMLElement) => {
			const zoom = canvas.getZoom();
			const bound = target.getBoundingRect();
			deleteBtn.style.left = `${bound.left + bound.width - 10}px`;
			deleteBtn.style.top = `${bound.top - 10}px`;
		};

		let deleteBtn: HTMLElement | null = null;

		// 객체 선택 이벤트 처리 함수
		const handleSelection = (e: fabric.IEvent) => {
			const target = canvas.getActiveObject();
			if (!target) return;

			if (target.data?.type === "sticker") {
				if (!deleteBtn) {
					deleteBtn = createDeleteButton(0, 0);
				}
				positionDeleteButton(target, deleteBtn);
				deleteBtn.style.display = "block";

				// 삭제 버튼 클릭 이벤트
				deleteBtn.onclick = () => {
					canvas.remove(target);
					deleteBtn!.style.display = "none";
					canvas.renderAll();
				};
			}
		};

		// 이벤트 리스너 추가
		canvas.on("selection:created", handleSelection);
		canvas.on("selection:updated", handleSelection);

		// 객체 이동 이벤트
		canvas.on("object:moving", (e) => {
			if (!deleteBtn || !e.target) return;
			if (e.target.data?.type === "sticker") {
				positionDeleteButton(e.target, deleteBtn);
			}
		});

		// 객체 크기 변경 이벤트
		canvas.on("object:scaling", (e) => {
			if (!deleteBtn || !e.target) return;
			if (e.target.data?.type === "sticker") {
				positionDeleteButton(e.target, deleteBtn);
			}
		});

		// 객체 회전 이벤트
		canvas.on("object:rotating", (e) => {
			if (!deleteBtn || !e.target) return;
			if (e.target.data?.type === "sticker") {
				positionDeleteButton(e.target, deleteBtn);
			}
		});

		// 선택 해제 이벤트
		canvas.on("selection:cleared", () => {
			if (deleteBtn) {
				deleteBtn.style.display = "none";
			}
		});

		return () => {
			// 클린업: 이벤트 리스너 제거 및 삭제 버튼 제거
			if (deleteBtn && deleteBtn.parentNode) {
				deleteBtn.parentNode.removeChild(deleteBtn);
			}
			canvas.off("selection:created", handleSelection);
			canvas.off("selection:updated", handleSelection);
			canvas.off("object:moving");
			canvas.off("object:scaling");
			canvas.off("object:rotating");
			canvas.off("selection:cleared");
		};
	}, [canvas]);

	const addSticker = (imageUrl: string) => {
		if (!canvas) return;

		fabric.Image.fromURL(imageUrl, (img) => {
			console.log("Image loaded:", img);
			const defaultWidth = canvas.width! * 0.2;
			const scale = defaultWidth / img.width!;

			img.set({
				scaleX: scale,
				scaleY: scale,
				left: (canvas.width! - img.width! * scale) / 2,
				top: (canvas.height! - img.height! * scale) / 2,
				selectable: true,
				evented: true,
				hasRotatingPoint: true,
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

			canvas.add(img);
			canvas.bringToFront(img); // 스티커를 최상위로 이동
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
