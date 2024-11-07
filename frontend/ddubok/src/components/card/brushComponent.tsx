"use client";

import React, { useState, useEffect } from "react";
import { IBrushComponentProps, IPathCreatedEvent } from "@interface/components/brush";
import { fabric } from "fabric";
import { Eraser } from "@phosphor-icons/react";

// fabric.js Path 타입 확장
declare module "fabric" {
	namespace fabric {
		interface Path {
			isBrushPath?: boolean;
		}
	}
}

function BrushComponent({ canvas }: IBrushComponentProps) {
	const [brushColor, setBrushColor] = useState("#000000");
	const [brushSize, setBrushSize] = useState(5);
	const [customColor, setCustomColor] = useState(
		"linear-gradient(90deg, #ff0000, #ff8000, #ffff00, #00ff00, #0000ff, #4b0082, #9400d3)",
	);
	const [isEraser, setIsEraser] = useState(false);

	const colors = ["#000000", "#ffffff", "#ff0000", "#00ff00", "#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#ff9900"];

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

				// Set custom property
				if (path instanceof fabric.Path) {
					path.isBrushPath = true;
				}

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

	// 지우개 모드 이벤트 핸들러
	useEffect(() => {
		if (!canvas) return;

		const handleMouseDown = (e: fabric.IEvent) => {
			if (!isEraser || !e.pointer) return;

			const pointer = canvas.getPointer(e.e);
			const objects = canvas.getObjects();

			for (let i = objects.length - 1; i >= 0; i--) {
				const obj = objects[i];
				if (
					obj instanceof fabric.Path &&
					obj.isBrushPath &&
					obj.containsPoint(new fabric.Point(pointer.x, pointer.y))
				) {
					canvas.remove(obj);
					canvas.requestRenderAll();
					break; // 가장 위에 있는 객체만 지우기
				}
			}
		};

		if (isEraser) {
			canvas.isDrawingMode = false;
			canvas.defaultCursor = "crosshair";
			canvas.on("mouse:down", handleMouseDown);
		} else {
			canvas.isDrawingMode = true;
			canvas.defaultCursor = "default";
			canvas.off("mouse:down", handleMouseDown);
		}

		return () => {
			if (canvas) {
				canvas.off("mouse:down", handleMouseDown);
			}
		};
	}, [canvas, isEraser]);

	useEffect(() => {
		if (canvas) {
			canvas.freeDrawingBrush.color = brushColor;
			canvas.freeDrawingBrush.width = brushSize;
		}
	}, [canvas, brushColor, brushSize]);

	const handleColorChange = (color: string) => {
		if (isEraser) {
			setIsEraser(false);
		}
		setBrushColor(color);
		if (canvas) {
			canvas.isDrawingMode = true;
			canvas.freeDrawingBrush.color = color;
			canvas.freeDrawingBrush.width = brushSize;
		}
	};

	const handleCustomColorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newColor = e.target.value;
		setCustomColor(newColor);
		handleColorChange(newColor);
	};

	const handleSizeChange = (size: number) => {
		if (isEraser) {
			setIsEraser(false);
		}
		setBrushSize(size);
		if (canvas) {
			canvas.isDrawingMode = true;
			canvas.freeDrawingBrush.width = size;
			canvas.defaultCursor = "default";
		}
	};

	const toggleEraser = () => {
		setIsEraser(!isEraser);
	};

	const sizes = [2, 5, 8, 12, 16];

	return (
		<div className="w-full flex flex-col h-full">
			<div className="space-y-2">
				<div className="flex items-center justify-between">
					<p className="text-base font-nexonRegular mt-3">색상</p>
					<button
						onClick={toggleEraser}
						className={`p-2 rounded-lg ${
							isEraser
								? "bg-ddubokPurple/10 border-2 border-ddubokPurple"
								: "bg-gray-100 border-2 border-gray-200"
						}`}
						title="지우개"
					>
						<Eraser />
					</button>
				</div>
				<div className="grid grid-cols-5 gap-2 ">
					{colors.map((color) => (
						<button
							key={color}
							className={`w-10 h-10 rounded-lg border-2 ${
								brushColor === color && !isEraser ? "border-ddubokPurple" : "border-gray-200"
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
								brushColor === customColor && !isEraser ? "border-ddubokPurple" : "border-gray-200"
							}`}
							style={{ background: customColor }}
						/>
					</div>
				</div>
			</div>

			<div className="space-y-2">
				<p className="text-base font-nexonRegular mt-4">크기</p>
				<div className="flex gap-2">
					{sizes.map((size) => (
						<button
							key={size}
							className={`w-10 h-10 rounded-lg border-2 flex items-center justify-center ${
								brushSize === size && !isEraser
									? "border-ddubokPurple bg-ddubokPurple/10"
									: "border-gray-200"
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
}

export default BrushComponent;
