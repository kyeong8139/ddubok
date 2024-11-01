"use client";

import React, { useState, useEffect } from "react";
import { IBrushComponentProps, IPathCreatedEvent } from "@interface/components/brush";

import { Eraser, PaintBrush } from "@phosphor-icons/react";
import { fabric } from "fabric";

function BrushComponent({ canvas }: IBrushComponentProps) {
	const [brushColor, setBrushColor] = useState("#000000");
	const [brushSize, setBrushSize] = useState(5);
	const [isErasing, setIsErasing] = useState(false);

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

	useEffect(() => {
		if (canvas) {
			const baseControls = fabric.Object.prototype.controls;

			fabric.Object.prototype.controls.deleteControl = new fabric.Control({
				x: 0.5,
				y: -0.5,
				offsetY: 0,
				offsetX: 0,
				cursorStyle: "pointer",
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
					ctx.font = "16px Arial";
					ctx.textAlign = "center";
					ctx.textBaseline = "middle";
					ctx.fillText("×", 0, 0);

					ctx.restore();
				},
			});

			canvas.on("path:created", function (e: IPathCreatedEvent) {
				const path = e.path;

				path.controls = {
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

				path.set({
					selectable: true,
					evented: true,
					hasBorders: true,
					hasControls: true,
					lockUniScaling: true,
					cornerSize: 8,
					cornerStyle: "circle",
					transparentCorners: false,
					padding: 10,
					borderColor: "#7e22ce",
					cornerColor: "#7e22ce",
				});

				canvas.renderAll();
			} as any);

			return () => {
				canvas.off("path:created");
				if (canvas) {
					canvas.isDrawingMode = true;
					canvas.off("mouse:down");
					canvas.off("mouse:move");
					canvas.off("mouse:up");
				}
			};
		}
	}, [canvas]);

	useEffect(() => {
		if (canvas) {
			canvas.freeDrawingBrush.color = brushColor;
			canvas.freeDrawingBrush.width = brushSize;
		}
	}, [canvas, brushColor, brushSize]);

	const handleColorChange = (color: string) => {
		setBrushColor(color);
		setIsErasing(false);
		if (canvas) {
			canvas.isDrawingMode = true;
			canvas.freeDrawingBrush.color = color;
			canvas.freeDrawingBrush.width = brushSize;
			canvas.off("mouse:down");
			canvas.off("mouse:move");
			canvas.off("mouse:up");
		}
	};

	const handleSizeChange = (size: number) => {
		setBrushSize(size);
		setIsErasing(false);
		if (canvas) {
			canvas.isDrawingMode = true;
			canvas.freeDrawingBrush.width = size;
			canvas.off("mouse:down");
			canvas.off("mouse:move");
			canvas.off("mouse:up");
		}
	};

	const sizes = [2, 5, 8, 12, 16];

	const toggleEraser = () => {
		if (!canvas) return;

		setIsErasing(!isErasing);

		if (!isErasing) {
			canvas.isDrawingMode = false;

			canvas.on("mouse:down", () => {
				canvas.selection = false;
			});

			canvas.on("mouse:move", (options) => {
				if (!options.e.buttons) return;
				const pointer = canvas.getPointer(options.e);
				const objects = canvas.getObjects();

				objects.forEach((obj) => {
					if (obj.type === "path") {
						const objectLeft = obj.left || 0;
						const objectTop = obj.top || 0;
						const objectWidth = obj.width || 0;
						const objectHeight = obj.height || 0;

						if (
							pointer.x >= objectLeft &&
							pointer.x <= objectLeft + objectWidth &&
							pointer.y >= objectTop &&
							pointer.y <= objectTop + objectHeight
						) {
							canvas.remove(obj);
						}
					}
				});
				canvas.renderAll();
			});

			canvas.on("mouse:up", () => {
				canvas.selection = true;
			});
		} else {
			canvas.isDrawingMode = true;
			canvas.off("mouse:down");
			canvas.off("mouse:move");
			canvas.off("mouse:up");
		}
	};

	return (
		<div className="w-full flex flex-col h-full">
			<div className="space-y-2">
				<p className="text-base font-nexonRegular ">색상</p>
				<div className="grid grid-cols-5 gap-2">
					{colors.map((color) => (
						<button
							key={color}
							className={`w-8 h-8 rounded-lg border-2 ${
								brushColor === color && !isErasing ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ backgroundColor: color }}
							onClick={() => handleColorChange(color)}
						/>
					))}
				</div>
			</div>

			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">크기</p>
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

			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">도구</p>
				<div className="flex gap-2 mb-4">
					<button
						className={`px-4 py-2 rounded-lg border-2 ${
							!isErasing ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
						}`}
						onClick={() => {
							if (isErasing) toggleEraser();
						}}
					>
						<PaintBrush size={32} />
					</button>
					<button
						className={`px-4 py-2 rounded-lg border-2 ${
							isErasing ? "border-ddubokPurple bg-ddubokPurple/10" : "border-gray-200"
						}`}
						onClick={toggleEraser}
					>
						<Eraser size={32} />
					</button>
				</div>
			</div>
		</div>
	);
}

export default BrushComponent;
