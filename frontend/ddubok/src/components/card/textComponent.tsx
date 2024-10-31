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
		{ name: "NEXON Lv1 Gothic Bold", label: "넥슨 고딕 볼드" },
		{ name: "PyeongChangPeace-Bold", label: "평창평화체" },
	];

	const colors = ["#000000", "#ffffff", "#ff0000", "#00ff00", "#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#7e22ce"];

	useEffect(() => {
		if (!canvas) return;

		// 객체 선택 이벤트 핸들러
		const handleSelection = (e: any) => {
			const selectedObject = canvas.getActiveObject();
			if (selectedObject && selectedObject.type === "i-text") {
				setSelectedText(selectedObject as fabric.IText);
				setFontFamily((selectedObject as fabric.IText).get("fontFamily") || "Arial");
				setTextColor(((selectedObject as fabric.IText).get("fill") as string) || "#000000");
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

		const text = new fabric.IText("", {
			left: (canvas.width || 0) / 2,
			top: (canvas.height || 0) / 2,
			fontFamily: fontFamily,
			fontSize: 20,
			fill: textColor,
			padding: 10,
			cornerColor: "#7e22ce",
			cornerStyle: "circle",
			cornerSize: 8,
			transparentCorners: false,
			data: { type: "text" },
			originX: "center",
			originY: "center",
			cursorColor: "#7e22ce",
			cursorWidth: 2,
			editingBorderColor: "#7e22ce",
		});

		canvas.add(text);
		canvas.setActiveObject(text);

		// 텍스트 객체가 추가된 후 편집 모드 시작
		text.enterEditing();
		text.selectAll();

		// 커스텀 컨트롤 설정
		const baseControls = fabric.Object.prototype.controls;

		// 삭제 버튼용 커스텀 컨트롤 생성
		fabric.Object.prototype.controls.deleteControl = new fabric.Control({
			x: 0.5,
			y: -0.5,
			offsetY: 0,
			offsetX: 0,
			cursorStyle: "pointer",
			touchSizeX: 40,
			touchSizeY: 40,
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

				// 원형 배경
				ctx.beginPath();
				ctx.arc(0, 0, size / 2, 0, Math.PI * 2);
				ctx.fillStyle = "white";
				ctx.fill();
				ctx.strokeStyle = "#7e22ce";
				ctx.lineWidth = 1;
				ctx.stroke();

				// X 표시
				ctx.fillStyle = "#7e22ce";
				ctx.font = "bold 16px Arial";
				ctx.textAlign = "center";
				ctx.textBaseline = "middle";
				ctx.fillText("×", 0, 0);

				ctx.restore();
			},
		});

		// 컨트롤 설정
		text.controls = {
			...baseControls,
			mtr: new fabric.Control({ visible: false }), // 상단 회전 컨트롤 숨기기
			deleteControl: fabric.Object.prototype.controls.deleteControl, // 삭제 버튼
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
