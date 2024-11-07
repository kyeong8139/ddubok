"use client";

import React, { useState, useEffect, useRef } from "react";
import { ITextComponentProps } from "@interface/components/text";
import { fabric } from "fabric";

function TextComponent({ canvas }: ITextComponentProps) {
	const [fontFamily, setFontFamily] = useState("Arial");
	const [textColor, setTextColor] = useState("#000000");
	const [customColor, setCustomColor] = useState(
		"linear-gradient(90deg, #ff0000, #ff8000, #ffff00, #00ff00, #0000ff, #4b0082, #9400d3)",
	);
	const [selectedText, setSelectedText] = useState<fabric.IText | null>(null);
	const [isDragging, setIsDragging] = useState(false);
	const [startX, setStartX] = useState(0);
	const [scrollLeft, setScrollLeft] = useState(0);
	const sliderRef = useRef<HTMLDivElement>(null);

	const fonts = [
		{ id: 1, name: "NEXON Lv1 Gothic Bold", label: "화이팅" },
		{ id: 2, name: "PyeongChangPeace-Bold", label: "화이팅" },
		{ id: 3, name: "CookieRun-Regular", label: "화이팅" },
		{ id: 4, name: "GumiRomanceTTF", label: "화이팅" },
	];

	const colors = ["#000000", "#ffffff", "#ff0000", "#00ff00", "#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#ff9900"];

	useEffect(() => {
		if (!canvas) return;

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

	const handleFontChange = (newFont: string) => {
		setFontFamily(newFont);
		if (selectedText && canvas) {
			selectedText.set("fontFamily", newFont);
			canvas.renderAll();
		} else {
			addText(newFont);
		}
	};

	const handleColorChange = (newColor: string) => {
		setTextColor(newColor);
		if (selectedText && canvas) {
			selectedText.set("fill", newColor);
			canvas.renderAll();
		}
	};

	const handleCustomColorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newColor = e.target.value;
		setCustomColor(newColor);
		handleColorChange(newColor);
	};

	const handleMouseDown = (e: React.MouseEvent) => {
		setIsDragging(true);
		if (sliderRef.current) {
			setStartX(e.pageX - sliderRef.current.offsetLeft);
			setScrollLeft(sliderRef.current.scrollLeft);
		}
	};

	const handleMouseUp = () => {
		setIsDragging(false);
	};

	const handleMouseMove = (e: React.MouseEvent) => {
		if (!isDragging || !sliderRef.current) return;
		e.preventDefault();
		const x = e.pageX - sliderRef.current.offsetLeft;
		const walk = (x - startX) * 2;
		sliderRef.current.scrollLeft = scrollLeft - walk;
	};

	const addText = (font: string) => {
		if (!canvas) return;

		const text = new fabric.IText("", {
			left: (canvas.width || 0) / 2,
			top: (canvas.height || 0) / 2,
			fontFamily: font,
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

		text.enterEditing();
		text.selectAll();

		const baseControls = fabric.Object.prototype.controls;

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

				ctx.beginPath();
				ctx.arc(0, 0, size / 2, 0, Math.PI * 2);
				ctx.fillStyle = "white";
				ctx.fill();
				ctx.strokeStyle = "#7e22ce";
				ctx.lineWidth = 1;
				ctx.stroke();

				ctx.fillStyle = "#7e22ce";
				ctx.font = "bold 16px Arial";
				ctx.textAlign = "center";
				ctx.textBaseline = "middle";
				ctx.fillText("×", 0, 0);

				ctx.restore();
			},
		});

		text.controls = {
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

		canvas.renderAll();
	};

	return (
		<div className="w-full flex flex-col h-full">
			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-3">색상</p>
				<div className="grid grid-cols-5 gap-2">
					{colors.map((color) => (
						<button
							key={color}
							className={`w-10 h-10 rounded-lg border-2 ${
								textColor === color ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ backgroundColor: color }}
							onClick={() => handleColorChange(color)}
						/>
					))}
					<div className="relative w-10 h-10">
						<input
							type="color"
							value={customColor}
							onChange={handleCustomColorChange}
							className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
						/>
						<button
							className={`w-full h-full rounded-lg border-2 ${
								textColor === customColor ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ background: customColor }}
						/>
					</div>
				</div>
			</div>
			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">폰트</p>
				<div
					ref={sliderRef}
					className="flex overflow-x-auto space-x-2 whitespace-nowrap scrollbar-hide select-none cursor-grab active:cursor-grabbing"
					onMouseDown={handleMouseDown}
					onMouseUp={handleMouseUp}
					onMouseLeave={handleMouseUp}
					onMouseMove={handleMouseMove}
				>
					{fonts.map((font) => (
						<button
							key={font.id}
							className={`px-4 py-2 rounded-lg border-2 flex-shrink-0 ${
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
		</div>
	);
}

export default TextComponent;
