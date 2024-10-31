"use client";

import React, { useState } from "react";
import { Canvas } from "fabric/fabric-impl";

interface BrushComponentProps {
	canvas: Canvas | null;
}

const BrushComponent: React.FC<BrushComponentProps> = ({ canvas }) => {
	const [brushColor, setBrushColor] = useState("#000000");
	const [brushSize, setBrushSize] = useState(5);

	const colors = [
		"#000000",
		"#ffffff",
		"#ff0000",
		"#00ff00",
		"#0000ff",
		"#ffff00",
		"#ff00ff",
		"#00ffff",
		"#ff9900",
		"#9900ff",
	];

	const handleColorChange = (color: string) => {
		setBrushColor(color);
		if (canvas) {
			canvas.freeDrawingBrush.color = color;
			canvas.freeDrawingBrush.width = brushSize;
		}
	};

	const handleSizeChange = (size: number) => {
		setBrushSize(size);
		if (canvas) {
			canvas.freeDrawingBrush.width = size;
		}
	};

	const sizes = [2, 5, 8, 12, 16];

	React.useEffect(() => {
		if (canvas) {
			canvas.freeDrawingBrush.color = brushColor;
			canvas.freeDrawingBrush.width = brushSize;
		}
	}, [canvas]);

	return (
		<div className="w-full space-y-6">
			<div className="space-y-2">
				<p className="text-sm font-medium">브러시 색상</p>
				<div className="grid grid-cols-5 gap-2">
					{colors.map((color) => (
						<button
							key={color}
							className={`w-8 h-8 rounded-lg border-2 ${
								brushColor === color ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ backgroundColor: color }}
							onClick={() => handleColorChange(color)}
						/>
					))}
				</div>
			</div>

			<div className="space-y-2">
				<p className="text-sm font-medium">브러시 크기</p>
				<div className="flex gap-2">
					{sizes.map((size) => (
						<button
							key={size}
							className={`w-8 h-8 rounded-lg border-2 flex items-center justify-center ${
								brushSize === size ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
							}`}
							onClick={() => handleSizeChange(size)}
						>
							<div
								className="rounded-full bg-black"
								style={{ width: size, height: size }}
							/>
						</button>
					))}
				</div>
			</div>
		</div>
	);
};

export default BrushComponent;
