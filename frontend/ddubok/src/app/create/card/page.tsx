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

	// 캔버스 초기화
	useEffect(() => {
		const newCanvas = new fabric.Canvas("canvas", {
			width: 280,
			height: 495,
			isDrawingMode: false,
		});
		setCanvas(newCanvas);

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

	// JSON으로 저장
	const saveToJSON = () => {
		if (canvas) {
			const json = canvas.toJSON();
			// JSON 데이터를 서버에 저장하거나 로컬 스토리지에 저장
			localStorage.setItem("canvasData", JSON.stringify(json));
			console.log("Canvas saved as JSON");
		}
	};

	// JSON에서 불러오기
	const loadFromJSON = () => {
		if (canvas) {
			const savedData = localStorage.getItem("canvasData");
			if (savedData) {
				canvas.loadFromJSON(savedData, () => {
					canvas.renderAll();
					console.log("Canvas loaded from JSON");
				});
			}
		}
	};

	// 이미지로 저장 (PNG)
	const saveAsImage = () => {
		if (canvas) {
			const dataURL = canvas.toDataURL({
				format: "png",
				quality: 1,
			});

			// 이미지 다운로드
			const link = document.createElement("a");
			link.download = "canvas-image.png";
			link.href = dataURL;
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		}
	};

	// 서버에 이미지 업로드
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
				return <TextComponent />;
			case "brush":
				return <BrushComponent canvas={canvas} />;
			case "border":
				return <BorderComponent canvas={canvas} />;
			case "effect":
				return <EffectComponent />;
			default:
				return null;
		}
	};

	return (
		<div className="flex flex-col items-center w-full h-full">
			{/* Canvas 요소 */}
			<canvas
				id="canvas"
				className=" rounded-lg"
			></canvas>

			<div className="mt-10 flex w-[320px]">
				{["background", "border", "sticker", "effect", "text", "brush"].map((value) => (
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
							{value === "effect" && "효과"}
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
		</div>
	);
};

export default CreateFront;
