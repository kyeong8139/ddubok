`use client`;
import React, { useState, useEffect } from "react";
import { fabric } from "fabric";
import { chunk } from "lodash";
import { CaretLeft, CaretRight } from "@phosphor-icons/react";

// 테두리 이미지 URL 목록
const borderImages = [
	"/assets/border/border_1.png",
	"/assets/border/border_2.png",
	"/assets/border/border_3.png",
	"/assets/border/border_4.png",
	"/assets/border/border_5.png",
	"/assets/border/border_6.png",
	"/assets/border/border_7.png",
	"/assets/border/border_8.png",
	"/assets/border/border_9.png",
	"/assets/border/border_10.png",
	"/assets/border/border_11.png",
	"/assets/border/border_12.png",
];

interface BorderComponentProps {
	canvas?: fabric.Canvas | null;
}

const BorderComponent: React.FC<BorderComponentProps> = ({ canvas }) => {
	const [selectedBorder, setSelectedBorder] = useState<string | null>(null);
	const [currentPage, setCurrentPage] = useState(0);

	// 페이지네이션 설정
	const itemsPerPage = 8;
	const pages = chunk(borderImages, itemsPerPage);
	const totalPages = Math.ceil(borderImages.length / itemsPerPage);

	// 현재 선택된 테두리 업데이트
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
					} catch (error) {
						console.log("Error parsing border URL:", error);
					}
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

	// const setBorder = (imageUrl: string) => {
	// 	if (!canvas) return;

	// 	// 기존 테두리 제거
	// 	const existingBorder = canvas.getObjects().find((obj) => obj.data?.type === "border");
	// 	if (existingBorder) {
	// 		canvas.remove(existingBorder);
	// 	}

	// 	// 새 테두리 추가
	// 	fabric.Image.fromURL(imageUrl, (img) => {
	// 		// 이미지와 캔버스의 비율 계산
	// 		const scaleX = canvas.width! / img.width!;
	// 		const scaleY = canvas.height! / img.height!;

	// 		// 더 작은 비율을 사용하여 이미지가 캔버스를 벗어나지 않도록 함
	// 		const scale = Math.min(scaleX, scaleY);

	// 		img.set({
	// 			scaleX: scale,
	// 			scaleY: scale,
	// 			left: (canvas.width! - img.width! * scale) / 2, // 중앙 정렬
	// 			top: (canvas.height! - img.height! * scale) / 2, // 중앙 정렬
	// 			selectable: false,
	// 			evented: false,
	// 			data: { type: "border" },
	// 		});

	// 		// 테두리는 항상 배경 바로 위에 위치하도록 설정
	// 		const backgroundIndex = canvas.getObjects().findIndex((obj) => obj.data?.type === "background");
	// 		canvas.insertAt(img, backgroundIndex + 1, false);
	// 		canvas.renderAll();
	// 		setSelectedBorder(imageUrl);
	// 	});
	// };

	const setBorder = (imageUrl: string) => {
		if (!canvas) return;

		// 기존 테두리 제거
		const existingBorder = canvas.getObjects().find((obj) => obj.data?.type === "border");
		if (existingBorder) {
			canvas.remove(existingBorder);
		}

		// 새 테두리 추가
		fabric.Image.fromURL(imageUrl, (img) => {
			// 캔버스 크기에 맞게 강제로 늘림
			img.set({
				scaleX: canvas.width! / img.width!,
				scaleY: canvas.height! / img.height!,
				left: 0,
				top: 0,
				selectable: false,
				evented: false,
				data: { type: "border" },
			});

			// 테두리는 항상 배경 바로 위에 위치하도록 설정
			const backgroundIndex = canvas.getObjects().findIndex((obj) => obj.data?.type === "background");
			canvas.insertAt(img, backgroundIndex + 1, false);
			canvas.renderAll();
			setSelectedBorder(imageUrl);
		});
	};

	return (
		<div className="w-full">
			<div className="relative">
				<div className="grid grid-cols-4 gap-4">
					{pages[currentPage]?.map((imageUrl, index) => (
						<label
							key={index}
							className="relative cursor-pointer"
						>
							<input
								type="radio"
								className="absolute opacity-0"
								checked={selectedBorder === imageUrl}
								onChange={() => setBorder(imageUrl)}
							/>
							<div
								className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-all
                  ${selectedBorder === imageUrl ? "border-ddubokPurple" : "border-gray-200 hover:border-ddubokPurple"}`}
							>
								<img
									src={imageUrl}
									alt={`테두리 ${index + 1}`}
									className="w-full h-full object-contain"
								/>
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
};

export default BorderComponent;
