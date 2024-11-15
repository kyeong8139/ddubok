"use client";

import React, { useState, useEffect, useRef } from "react";
import { ITextComponentProps } from "@interface/components/text";
import { fabric } from "fabric";
import { TextAlignCenter, TextAlignLeft, TextAlignRight } from "@phosphor-icons/react";

function TextComponent({ canvas, onPanelClose }: ITextComponentProps) {
	const [fontFamily, setFontFamily] = useState("Arial");
	const [textColor, setTextColor] = useState("#000000");
	const [textAlign, setTextAlign] = useState("center");
	const [customColor, setCustomColor] = useState(
		"linear-gradient(90deg, #ff0000, #ff8000, #ffff00, #00ff00, #0000ff, #4b0082, #9400d3)",
	);
	const [selectedText, setSelectedText] = useState<fabric.IText | null>(null);
	const [isDragging, setIsDragging] = useState(false);
	const [startX, setStartX] = useState(0);
	const [scrollLeft, setScrollLeft] = useState(0);
	const sliderRef = useRef<HTMLDivElement>(null);
	const colorSliderRef = useRef<HTMLDivElement>(null);

	const fonts = [
		{ id: 1, name: "NEXON Lv1 Gothic Bold", label: "화이팅" },
		{ id: 2, name: "PyeongChangPeace-Bold", label: "화이팅" },
		{ id: 3, name: "CookieRun-Regular", label: "화이팅" },
		{ id: 4, name: "GumiRomanceTTF", label: "화이팅" },
		{ id: 5, name: "UhBeeSe_hyun", label: "화이팅" },
		{ id: 6, name: "omyu_pretty", label: "화이팅" },
		{ id: 7, name: "kdg_Medium", label: "화이팅" },
		{ id: 8, name: "KCC-Ganpan", label: "화이팅" },
		{ id: 9, name: "SDSamliphopangche_Outline", label: "화이팅" },
		{ id: 10, name: "YClover-Bold", label: "화이팅" },
		{ id: 11, name: "Giants-Inline", label: "화이팅" },
	];

	const colors = [
		"#000000",
		"#DFE6E9",
		"#FF7675",
		"#FAB1A0",
		"#FD79A8",
		"#A855F7",
		"#8B5CF6",
		"#0984E3",
		"#74B9FF",
		"#55EFC4",
		"#00B894",
		"#FFEAA7",
		"#E17055",
		"#D63031",
	];

	useEffect(() => {
		if (!canvas) return;

		const handleSelection = (e: any) => {
			const selectedObject = canvas.getActiveObject();
			if (selectedObject && selectedObject.type === "i-text") {
				setSelectedText(selectedObject as fabric.IText);
				setFontFamily((selectedObject as fabric.IText).get("fontFamily") || "Arial");
				setTextColor(((selectedObject as fabric.IText).get("fill") as string) || "#000000");
				setTextAlign(((selectedObject as fabric.IText).get("textAlign") as string) || "center");
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

	const handleFontChange = (newFont: string, label: string) => {
		setFontFamily(newFont);
		if (selectedText && canvas) {
			selectedText.set("fontFamily", newFont);
			canvas.renderAll();
		} else {
			addText(newFont, label);
		}
		onPanelClose();
	};

	const handleColorChange = (newColor: string) => {
		setTextColor(newColor);
		if (selectedText && canvas) {
			selectedText.set("fill", newColor);
			canvas.renderAll();
		}
	};

	const handleAlignChange = (alignment: string) => {
		setTextAlign(alignment);
		if (selectedText && canvas) {
			selectedText.set("textAlign", alignment);
			canvas.renderAll();
		}
	};

	const handleCustomColorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newColor = e.target.value;
		setCustomColor(newColor);
		handleColorChange(newColor);
	};

	const handleMouseDown = (e: React.MouseEvent, ref: React.RefObject<HTMLDivElement>) => {
		setIsDragging(true);
		if (ref.current) {
			setStartX(e.pageX - ref.current.offsetLeft);
			setScrollLeft(ref.current.scrollLeft);
		}
	};

	const handleMouseUp = () => {
		setIsDragging(false);
	};

	const handleMouseMove = (e: React.MouseEvent, ref: React.RefObject<HTMLDivElement>) => {
		if (!isDragging || !ref.current) return;
		e.preventDefault();
		const x = e.pageX - ref.current.offsetLeft;
		const walk = (x - startX) * 2;
		ref.current.scrollLeft = scrollLeft - walk;
	};

	const addText = (font: string, initialText: string) => {
		if (!canvas) return;

		const text = new fabric.IText(initialText, {
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
			textAlign: textAlign,
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
				<p className="text-base font-nexonRegular mt-2">색상</p>
				<div
					ref={colorSliderRef}
					className="flex overflow-x-auto space-x-1 whitespace-nowrap scrollbar-hide select-none cursor-grab active:cursor-grabbing pb-2"
					onMouseDown={(e) => handleMouseDown(e, colorSliderRef)}
					onMouseUp={handleMouseUp}
					onMouseLeave={handleMouseUp}
					onMouseMove={(e) => handleMouseMove(e, colorSliderRef)}
				>
					<div
						className="relative w-10 h-10 flex-shrink-0"
						onDragStart={(e) => e.preventDefault()}
					>
						<input
							type="color"
							value={customColor}
							onChange={handleCustomColorChange}
							className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
						/>
						<button
							className={`w-full h-full rounded-lg border-2 transition-all duration-200 ${
								textColor === customColor
									? "border-ddubokPurple shadow-lg"
									: "border-gray-200 hover:border-gray-300"
							}`}
							style={{ background: customColor }}
							onDragStart={(e) => e.preventDefault()}
						/>
					</div>
					{colors.map((color) => (
						<button
							key={color}
							className={`w-10 h-10 rounded-lg border-2 flex-shrink-0 transition-all duration-200 ${
								textColor === color
									? "border-ddubokPurple shadow-sm"
									: "border-gray-200 hover:border-gray-300"
							}`}
							style={{ backgroundColor: color }}
							onClick={() => handleColorChange(color)}
							onDragStart={(e) => e.preventDefault()}
							draggable={false}
						/>
					))}
				</div>
			</div>
			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">정렬</p>
				<div className="flex space-x-2">
					<button
						className={`px-4 py-2 rounded-lg border-2 ${
							textAlign === "left" ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
						}`}
						onClick={() => handleAlignChange("left")}
					>
						<TextAlignLeft />
					</button>
					<button
						className={`px-4 py-2 rounded-lg border-2 ${
							textAlign === "center" ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
						}`}
						onClick={() => handleAlignChange("center")}
					>
						<TextAlignCenter />
					</button>
					<button
						className={`px-4 py-2 rounded-lg border-2 ${
							textAlign === "right" ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
						}`}
						onClick={() => handleAlignChange("right")}
					>
						<TextAlignRight />
					</button>
				</div>
			</div>
			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">폰트</p>
				<div
					ref={sliderRef}
					className="flex overflow-x-auto space-x-2 whitespace-nowrap scrollbar-hide select-none cursor-grab active:cursor-grabbing"
					onMouseDown={(e) => handleMouseDown(e, sliderRef)}
					onMouseUp={handleMouseUp}
					onMouseLeave={handleMouseUp}
					onMouseMove={(e) => handleMouseMove(e, sliderRef)}
				>
					{fonts.map((font) => (
						<button
							key={font.id}
							className={`px-4 py-2 rounded-lg border-2 flex-shrink-0 ${
								fontFamily === font.name ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
							}`}
							style={{ fontFamily: font.name }}
							onClick={() => handleFontChange(font.name, font.label)}
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
