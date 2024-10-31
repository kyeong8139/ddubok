"use client";

import React, { useState, useEffect } from "react";
import { fabric } from "fabric";

interface TextComponentProps {
	canvas?: fabric.Canvas | null;
}

const TextComponent: React.FC<TextComponentProps> = ({ canvas }) => {
	const [fontFamily, setFontFamily] = useState("Arial");
	const [textColor, setTextColor] = useState("#000000");
	const [selectedText, setSelectedText] = useState<fabric.IText | null>(null);

	const fonts = [
		{ name: "Arial", label: "기본" },
		{ name: "Times New Roman", label: "명조" },
		{ name: "Courier New", label: "고딕" },
		{ name: "Comic Sans MS", label: "손글씨" },
	];

	const colors = ["#000000", "#ffffff", "#ff0000", "#00ff00", "#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#7e22ce"];

	useEffect(() => {
		if (!canvas) return;

		// 객체 선택 이벤트 핸들러
		const handleSelection = (e: any) => {
			const selectedObject = canvas.getActiveObject();
			if (selectedObject && selectedObject.type === "i-text") {
				setSelectedText(selectedObject as fabric.IText);
				setFontFamily(selectedObject.get("fontFamily") || "Arial");
				setTextColor((selectedObject.get("fill") as string) || "#000000");
			} else {
				setSelectedText(null);
			}
		};

		// 선택 해제 이벤트 핸들러
		const handleDeselection = () => {
			setSelectedText(null);
		};

		canvas.on("selection:created", handleSelection);
		canvas.on("selection:updated", handleSelection);
		canvas.on("selection:cleared", handleDeselection);

		return () => {
			canvas.off("selection:created", handleSelection);
			canvas.off("selection:updated", handleSelection);
			canvas.off("selection:cleared", handleDeselection);
		};
	}, [canvas]);

	// 폰트 변경 함수
	const handleFontChange = (newFont: string) => {
		setFontFamily(newFont);
		if (selectedText && canvas) {
			selectedText.set("fontFamily", newFont);
			canvas.renderAll();
		}
	};

	// 색상 변경 함수
	const handleColorChange = (newColor: string) => {
		setTextColor(newColor);
		if (selectedText && canvas) {
			selectedText.set("fill", newColor);
			canvas.renderAll();
		}
	};

	const addText = () => {
		if (!canvas) return;

		const text = new fabric.IText("텍스트를 입력하세요", {
			left: 50,
			top: 50,
			fontFamily: fontFamily,
			fontSize: 20,
			fill: textColor,
			padding: 10,
			cornerColor: "#7e22ce",
			cornerStyle: "circle",
			cornerSize: 8,
			transparentCorners: false,
			data: { type: "text" },
		});

		canvas.add(text);
		canvas.setActiveObject(text);
		text.enterEditing();
		canvas.renderAll();
	};

	return (
		<div className="w-full space-y-6">
			<div className="space-y-2">
				<p className="text-sm font-medium">폰트 선택</p>
				<div className="grid grid-cols-2 gap-2">
					{fonts.map((font) => (
						<button
							key={font.name}
							className={`px-4 py-2 rounded-lg border-2 ${
								fontFamily === font.name ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
							}`}
							style={{ fontFamily: font.name }}
							onClick={() => handleFontChange(font.name)}
						>
							{font.label}
						</button>
					))}
				</div>
			</div>

			<div className="space-y-2">
				<p className="text-sm font-medium">글자 색상</p>
				<div className="grid grid-cols-5 gap-2">
					{colors.map((color) => (
						<button
							key={color}
							className={`w-8 h-8 rounded-lg border-2 ${
								textColor === color ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ backgroundColor: color }}
							onClick={() => handleColorChange(color)}
						/>
					))}
				</div>
			</div>

			<button
				onClick={addText}
				className="w-full px-4 py-2 bg-ddubokPurple text-white rounded-lg"
			>
				텍스트 추가
			</button>
		</div>
	);
};

export default TextComponent;
