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
	const [isLoading, setIsLoading] = useState(true);
	const canvasRef = useRef<fabric.Canvas | null>(null);

	// 캔버스 초기화
	useEffect(() => {
		// DOM이 완전히 로드된 후 캔버스 초기화
		const timer = setTimeout(() => {
			try {
				const canvasElement = document.getElementById("canvas");
				if (!canvasElement) {
					console.error("Canvas element not found");
					return;
				}

				// 이전 캔버스가 있다면 정리
				if (canvasRef.current) {
					canvasRef.current.dispose();
					canvasRef.current = null;
				}

				// 새 캔버스 생성
				const newCanvas = new fabric.Canvas("canvas", {
					width: 280,
					height: 495,
				});

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

				canvasRef.current = newCanvas;
				setCanvas(newCanvas);
				setIsLoading(false);
			} catch (error) {
				console.error("Canvas initialization error:", error);
				setIsLoading(false);
			}
		}, 100); // 100ms 딜레이 추가

		return () => {
			clearTimeout(timer);
			if (canvasRef.current) {
				try {
					canvasRef.current.dispose();
					canvasRef.current = null;
				} catch (error) {
					console.log("Cleanup error:", error);
				}
			}
		};
	}, []);

	const renderActiveComponent = () => {
		switch (activeComponent) {
			case "background":
				return <BackgroundComponent canvas={canvas} />;
			case "sticker":
				return <StickerComponent />;
			case "text":
				return <TextComponent />;
			case "brush":
				return <BrushComponent />;
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
			<div className="w-[280px] h-[495px] relative">
				<canvas
					id="canvas"
					className="rounded-lg absolute top-0 left-0"
					style={{ display: isLoading ? "none" : "block" }}
				/>
				{isLoading && (
					<div className="w-full h-full rounded-lg bg-gray-100 flex items-center justify-center">
						<div className="flex flex-col items-center gap-2">
							<div className="w-8 h-8 border-4 border-ddubokPurple border-t-transparent rounded-full animate-spin" />
							<p className="text-sm text-gray-500">행운카드 로딩중...</p>
						</div>
					</div>
				)}
			</div>

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
		</div>
	);
};

export default CreateFront;
