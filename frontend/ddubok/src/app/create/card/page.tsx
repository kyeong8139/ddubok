"use client";

import React, { useEffect, useRef, useState } from "react";
import { fabric } from "fabric";
import BackgroundComponent from "@components/card/backgroundComponent";
import StickerComponent from "@components/card/stickerComponent";
import TextComponent from "@components/card/textComponent";
import BrushComponent from "@components/card/brushComponent";
import BorderComponent from "@components/card/borderComponent";
import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { Trash } from "@phosphor-icons/react";
import { useRouter } from "next/navigation";

const CreateFront = () => {
	const router = useRouter();
	const [activeComponent, setActiveComponent] = useState<string>("background");
	const [canvas, setCanvas] = useState<fabric.Canvas | null>(null);
	const [showClearConfirm, setShowClearConfirm] = useState(false);
	const [showNextConfirm, setShowNextConfirm] = useState(false);

	// 캔버스 초기화
	useEffect(() => {
		const newCanvas = new fabric.Canvas("canvas", {
			width: 280,
			height: 495,
			isDrawingMode: false,
		});

		// 선택 스타일 설정
		const setSelectionStyles = () => {
			// 기본 선택 스타일 설정
			(newCanvas as any).set({
				borderColor: "#7e22ce",
				cornerColor: "#7e22ce",
				cornerSize: 8,
				cornerStyle: "circle",
				transparentCorners: false,
				padding: 10,
			});

			// 삭제 버튼용 커스텀 컨트롤 생성
			const deleteControl = new fabric.Control({
				x: 0.5,
				y: -0.5,
				offsetY: 0,
				offsetX: 0,
				cursorStyle: "pointer",
				mouseUpHandler: function (eventData, transform) {
					const target = transform.target;
					if (target instanceof fabric.ActiveSelection) {
						const objects = target.getObjects();
						objects.forEach((obj) => newCanvas.remove(obj));
						// 선택 해제
						newCanvas.discardActiveObject();
					} else {
						newCanvas.remove(target);
					}
					newCanvas.requestRenderAll();
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
					ctx.font = "16px Arial";
					ctx.textAlign = "center";
					ctx.textBaseline = "middle";
					ctx.fillText("×", 0, 0);

					ctx.restore();
				},
			});

			// ActiveSelection(다중 선택) 스타일 및 컨트롤 설정
			if (fabric.ActiveSelection) {
				fabric.ActiveSelection.prototype.set({
					borderColor: "#7e22ce",
					cornerColor: "#7e22ce",
					cornerSize: 8,
					cornerStyle: "circle",
					transparentCorners: false,
					padding: 10,
				});

				// ActiveSelection에 삭제 컨트롤 추가
				fabric.ActiveSelection.prototype.controls = {
					...fabric.ActiveSelection.prototype.controls,
					deleteControl: deleteControl,
				};
			}

			// 일반 객체에도 동일한 삭제 컨트롤 적용
			fabric.Object.prototype.controls.deleteControl = deleteControl;
		};

		setSelectionStyles();

		// selection:created 이벤트에 대한 핸들러
		newCanvas.on("selection:created", function (e) {
			const selection = e.target;
			if (selection) {
				selection.set({
					borderColor: "#7e22ce",
					cornerColor: "#7e22ce",
					cornerSize: 8,
					cornerStyle: "circle",
					transparentCorners: false,
					padding: 10,
				});
				newCanvas.requestRenderAll();
			}
		});

		// selection:updated 이벤트에 대한 핸들러 (선택이 변경될 때)
		newCanvas.on("selection:updated", function (e) {
			const selection = e.target;
			if (selection) {
				selection.set({
					borderColor: "#7e22ce",
					cornerColor: "#7e22ce",
					cornerSize: 8,
					cornerStyle: "circle",
					transparentCorners: false,
					padding: 10,
				});
				newCanvas.requestRenderAll();
			}
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

	const handleNext = () => {
		if (canvas) {
			// 현재 캔버스를 이미지로 저장
			const dataURL = canvas.toDataURL({
				format: "png",
				quality: 4,
			});

			// localStorage에 저장
			localStorage.setItem("cardFrontImage", dataURL);

			// 다음 페이지로 이동
			router.push("/create/effect");
		}
	};

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
			default:
				return null;
		}
	};

	return (
		<div className="flex flex-col items-center w-full h-full">
			<canvas
				id="canvas"
				className="rounded-lg"
			></canvas>

			<div className="mt-6 flex w-[320px] place-content-between">
				<div className="flex">
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
							</p>
						</label>
					))}
				</div>
				<div>
					<button
						onClick={() => setShowClearConfirm(true)}
						className="p-2 rounded-lg"
					>
						<Trash
							size={20}
							color="#6EFFBF"
						/>
					</button>
				</div>
			</div>

			<div className="bg-white rounded-lg flex flex-col justify-center items-center w-[320px] h-[320px] pl-4 pr-4 pt-4 pb-4 ">
				{renderActiveComponent()}
			</div>

			<div className="mt-6 w-full flex justify-center mb-8">
				<Button
					text="다음으로"
					color="green"
					size="long"
					font="regular"
					shadow="green"
					onClick={() => setShowNextConfirm(true)}
				/>
			</div>

			{/* 초기화 확인 모달 */}
			{/* {showClearConfirm && (
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
			)} */}

			{showClearConfirm && (
				<Modal>
					<h3 className="text-lg font-nexonBold mb-4">정말 초기화하시겠습니까?</h3>
					<p className="text-gray-600 mb-6 font-nexonRegular">모든 작업내용이 삭제됩니다.</p>
					<div className="flex justify-end gap-2">
						<Button
							text="취소"
							color="gray"
							size="small"
							font="regular"
							shadow="gray"
							onClick={() => {
								setShowClearConfirm(false);
							}}
						/>
						<Button
							text="초기화"
							color="red"
							size="small"
							font="regular"
							shadow="red"
							onClick={handleClear}
						/>
					</div>
				</Modal>
			)}

			{showNextConfirm && (
				<Modal>
					<h3 className="text-lg font-nexonBold mb-4">앞면 꾸미기 완료</h3>
					<p className="text-gray-600 mb-6 font-nexonRegular">
						다음으로 넘어가면 더 이상 수정할 수 없습니다.
						<br />
						다음 단계로 이동하시겠습니까?
					</p>
					<div className="flex justify-end gap-2">
						<Button
							text="취소"
							color="gray"
							size="small"
							font="regular"
							shadow="gray"
							onClick={() => {
								setShowNextConfirm(false);
							}}
						/>
						<Button
							text="다음으로"
							color="green"
							size="small"
							font="regular"
							shadow="green"
							onClick={handleNext}
						/>
					</div>
				</Modal>
			)}
		</div>
	);
};

export default CreateFront;
