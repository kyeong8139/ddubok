"use client";

import React, { useEffect, useRef, useState } from "react";
import { fabric } from "fabric";
import BackgroundComponent from "@components/card/backgroundComponent";
import StickerComponent from "@components/card/stickerComponent";
import TextComponent from "@components/card/textComponent";
import BrushComponent from "@components/card/brushComponent";
import BorderComponent from "@components/card/borderComponent";
import EffectComponent from "@components/card/effectComponent";

const CreateFront = () => {
	const [activeComponent, setActiveComponent] = useState<string>("background");
	const [canvas, setCanvas] = useState<fabric.Canvas | null>(null);
	const [showClearConfirm, setShowClearConfirm] = useState(false);

	// 캔버스 초기화
	useEffect(() => {
		const newCanvas = new fabric.Canvas("canvas", {
			width: 280,
			height: 495,
			isDrawingMode: false,
		});
		setCanvas(newCanvas);

		// 초기 흰색 배경 설정
		const whiteBg = new fabric.Rect({
			width: newCanvas.width,
			height: newCanvas.height,
			fill: "white",
			selectable: false,
			evented: false,
			data: { type: "background" },
		});

		newCanvas.insertAt(whiteBg, 0, false);
		newCanvas.renderAll();

		// 클린업: 컴포넌트 언마운트 시 캔버스 해제
		return () => {
			newCanvas.dispose();
		};
	}, []);

	useEffect(() => {
		if (canvas) {
			canvas.isDrawingMode = activeComponent === "brush";
			canvas.selection = true;
			canvas.off("mouse:down");
		}
	}, [activeComponent, canvas]);

	// 초기화 (Clear)
	const handleClear = () => {
		if (canvas) {
			canvas.clear();

			// 초기 흰색 배경 다시 설정
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
			setShowClearConfirm(false);
		}
	};

	// 기존 함수들...
	const saveToJSON = () => {
		if (canvas) {
			const json = canvas.toJSON(["data", "selectable", "evented"]);
			localStorage.setItem("canvasData", JSON.stringify(json));
			console.log("Canvas saved as JSON");
		}
	};

	const loadFromJSON = () => {
		if (canvas) {
			const savedData = localStorage.getItem("canvasData");
			if (savedData) {
				canvas.loadFromJSON(savedData, () => {
					// 배경과 테두리의 속성 복원
					canvas.getObjects().forEach((obj) => {
						if (obj.data?.type === "background" || obj.data?.type === "border") {
							obj.set({
								selectable: false,
								evented: false,
							});
						}
					});
					canvas.renderAll();
					console.log("Canvas loaded from JSON");
				});
			}
		}
	};

	const saveAsImage = () => {
		if (canvas) {
			const dataURL = canvas.toDataURL({
				format: "png",
				quality: 1,
			});

			const link = document.createElement("a");
			link.download = "canvas-image.png";
			link.href = dataURL;
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		}
	};

	const uploadToServer = async () => {
		if (canvas) {
			const dataURL = canvas.toDataURL({
				format: "png",
				quality: 1,
			});

			try {
				const response = await fetch("/api/upload", {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
					body: JSON.stringify({ image: dataURL }),
				});

				if (response.ok) {
					console.log("Image uploaded successfully");
				} else {
					console.error("Failed to upload image");
				}
			} catch (error) {
				console.error("Error uploading image:", error);
			}
		}
	};

	const renderActiveComponent = () => {
		switch (activeComponent) {
			case "background":
				return <BackgroundComponent canvas={canvas} />;
			case "sticker":
				return <StickerComponent canvas={canvas} />;
			case "text":
				return <TextComponent canvas={canvas} />;
			case "brush":
				return <BrushComponent canvas={canvas} />;
			case "border":
				return <BorderComponent canvas={canvas} />;
			// case "effect":
			// 	return <EffectComponent />;
			default:
				return null;
		}
	};

	return (
		<div className="flex flex-col items-center w-full h-full">
			<div className="flex justify-between w-[280px] mb-4">
				<button
					onClick={() => setShowClearConfirm(true)}
					className="p-2 rounded-lg bg-gray-100 hover:bg-gray-200 text-gray-600"
				>
					초기화
				</button>
			</div>

			<canvas
				id="canvas"
				className="rounded-lg"
			></canvas>

			<div className="mt-10 flex w-[320px]">
				{["background", "border", "sticker", "text", "brush"].map((value) => (
					<label
						key={value}
						className="flex items-center cursor-pointer"
					>
						<input
							type="radio"
							value={value}
							checked={activeComponent === value}
							onChange={() => setActiveComponent(value)}
							className="hidden"
						/>
						<p
							className={`p-2 border-2 border-black rounded-lg font-nexonBold text-xs ${
								activeComponent === value ? "bg-ddubokPurple" : "bg-white"
							}`}
						>
							{value === "background" && "배경"}
							{value === "border" && "테두리"}
							{value === "sticker" && "스티커"}
							{value === "text" && "텍스트"}
							{value === "brush" && "브러쉬"}
							{/* {value === "effect" && "효과"} */}
						</p>
					</label>
				))}
			</div>

			<div className="bg-white rounded-lg flex flex-col justify-center items-center w-[320px] h-[320px] pl-4 pr-4 pt-4 pb-4 mb-12">
				{renderActiveComponent()}
			</div>

			<div className="mt-4 flex gap-2">
				<button
					onClick={saveToJSON}
					className="px-4 py-2 bg-ddubokPurple text-white rounded-lg"
				>
					임시 저장
				</button>
				<button
					onClick={loadFromJSON}
					className="px-4 py-2 bg-ddubokPurple text-white rounded-lg"
				>
					불러오기
				</button>
				<button
					onClick={saveAsImage}
					className="px-4 py-2 bg-ddubokPurple text-white rounded-lg"
				>
					이미지로 저장
				</button>
				<button
					onClick={uploadToServer}
					className="px-4 py-2 bg-ddubokPurple text-white rounded-lg"
				>
					서버에 저장
				</button>
			</div>

			{/* 초기화 확인 모달 */}
			{showClearConfirm && (
				<div className="fixed inset-0 flex items-center justify-center z-50">
					<div
						className="fixed inset-0 bg-black bg-opacity-50"
						onClick={() => setShowClearConfirm(false)}
					/>
					<div className="relative bg-white rounded-lg p-6 max-w-[320px] w-full mx-4">
						<h3 className="text-lg font-semibold mb-4">정말 초기화하시겠습니까?</h3>
						<p className="text-gray-600 mb-6">모든 작업내용이 삭제됩니다.</p>
						<div className="flex justify-end gap-2">
							<button
								className="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg"
								onClick={() => setShowClearConfirm(false)}
							>
								취소
							</button>
							<button
								className="px-4 py-2 bg-red-500 text-white rounded-lg"
								onClick={handleClear}
							>
								초기화
							</button>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default CreateFront;
