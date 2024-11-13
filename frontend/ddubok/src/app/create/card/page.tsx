"use client";

import React, { useEffect, useRef, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";

import BackgroundComponent from "@components/card/backgroundComponent";
import StickerComponent from "@components/card/stickerComponent";
import TextComponent from "@components/card/textComponent";
import BrushComponent from "@components/card/brushComponent";
import BorderComponent from "@components/card/borderComponent";
import CharacterComponent from "@components/card/characterComponent";
import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { useCardStore } from "@store/card-store";

import { fabric } from "fabric";
import { Trash } from "@phosphor-icons/react";

const CreateFront = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const type = searchParams?.get("type");
	const id = searchParams?.get("id");

	const [activeComponent, setActiveComponent] = useState<string>("background");
	const [canvas, setCanvas] = useState<fabric.Canvas | null>(null);
	const [showClearConfirm, setShowClearConfirm] = useState(false);
	const [showNextConfirm, setShowNextConfirm] = useState(false);
	const [showExitConfirm, setShowExitConfirm] = useState(false);
	const [isPanelOpen, setIsPanelOpen] = useState(false);
	const setSelectedImage = useCardStore((state) => state.setSelectedImage);
	const hasUnsavedChanges = useRef(false);

	const navigationType = useRef<"back" | "header" | null>(null);

	const [shouldFixButtons, setShouldFixButtons] = useState(false);
	const controlsRef = useRef<HTMLDivElement>(null);

	useEffect(() => {
		if (!isPanelOpen) {
			setShouldFixButtons(false);
		}
	}, [isPanelOpen]);

	useEffect(() => {
		if (!isPanelOpen) return;

		const handleScroll = () => {
			if (controlsRef.current) {
				const controlsBottom = controlsRef.current.getBoundingClientRect().bottom;
				const windowHeight = window.innerHeight;
				const panelHeight = 300;

				setShouldFixButtons(controlsBottom > windowHeight - panelHeight);
			}
		};

		window.addEventListener("scroll", handleScroll);
		handleScroll();

		return () => {
			window.removeEventListener("scroll", handleScroll);
		};
	}, [isPanelOpen]);

	useEffect(() => {
		const handleHeaderNavigation = (e: MouseEvent) => {
			const header = document.getElementById("header");
			if (header?.contains(e.target as Node) && hasUnsavedChanges.current) {
				e.preventDefault();
				e.stopPropagation();
				navigationType.current = "header";
				setShowExitConfirm(true);
			}
		};

		const handleBeforeUnload = (event: BeforeUnloadEvent) => {
			if (hasUnsavedChanges.current) {
				const message = "작성하던 내용이 모두 사라집니다. 계속하시겠습니까?";
				event.preventDefault();
				(event as any).returnValue = message;
				return message;
			}
		};

		const handlePopState = () => {
			if (hasUnsavedChanges.current) {
				navigationType.current = "back";
				setShowExitConfirm(true);
				window.history.pushState(null, "", window.location.href);
			} else {
				router.back();
			}
		};

		window.addEventListener("beforeunload", handleBeforeUnload);
		document.addEventListener("click", handleHeaderNavigation, true);
		window.addEventListener("popstate", handlePopState);
		window.history.pushState(null, "", window.location.href);

		return () => {
			window.removeEventListener("beforeunload", handleBeforeUnload);
			document.removeEventListener("click", handleHeaderNavigation, true);
			window.removeEventListener("popstate", handlePopState);
		};
	}, [router]);

	const handleExitConfirm = () => {
		hasUnsavedChanges.current = false;
		setShowExitConfirm(false);

		if (navigationType.current === "header") {
			router.push("/");
		} else {
			router.back();
		}

		navigationType.current = null;
	};

	useEffect(() => {
		const newCanvas = new fabric.Canvas("canvas", {
			width: 280,
			height: 495,
			isDrawingMode: false,
		});

		const setSelectionStyles = () => {
			(newCanvas as any).set({
				borderColor: "#7e22ce",
				cornerColor: "#7e22ce",
				cornerSize: 8,
				cornerStyle: "circle",
				transparentCorners: false,
				padding: 10,
			});

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

			if (fabric.ActiveSelection) {
				fabric.ActiveSelection.prototype.set({
					borderColor: "#7e22ce",
					cornerColor: "#7e22ce",
					cornerSize: 8,
					cornerStyle: "circle",
					transparentCorners: false,
					padding: 10,
				});

				fabric.ActiveSelection.prototype.controls = {
					...fabric.ActiveSelection.prototype.controls,
					deleteControl: deleteControl,
				};
			}

			fabric.Object.prototype.controls.deleteControl = deleteControl;
		};

		setSelectionStyles();

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

		newCanvas.on("object:added", () => {
			hasUnsavedChanges.current = true;
		});

		newCanvas.on("object:modified", () => {
			hasUnsavedChanges.current = true;
		});

		newCanvas.on("object:removed", () => {
			hasUnsavedChanges.current = true;
		});

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
			const dataURL = canvas.toDataURL({
				format: "png",
				quality: 4,
			});

			setSelectedImage(dataURL);
			hasUnsavedChanges.current = false;
			router.push(`/create/letter?type=${type}&id=${id}`);
		}
	};

	const handleClear = () => {
		if (canvas) {
			canvas.clear();

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
			hasUnsavedChanges.current = true;
		}
	};

	const handleExit = () => {
		hasUnsavedChanges.current = false;
		setShowExitConfirm(false);
		router.back();
	};

	const handleComponentClick = (value: string) => {
		setActiveComponent(value);
		setIsPanelOpen(true);
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
			case "character":
				return <CharacterComponent canvas={canvas} />;
			default:
				return null;
		}
	};

	return (
		<div className="relative flex flex-col items-center w-full h-full">
			<canvas
				id="canvas"
				className="rounded-lg"
			/>

			<div
				ref={controlsRef}
				className={`w-full max-w-[480px]  mt-2 mx-auto ${isPanelOpen ? "duration-300 ease-in-out" : ""} ${
					shouldFixButtons && isPanelOpen
						? "fixed bottom-0 left-0 right-0 translate-y-[-320px] z-50"
						: "translate-y-0"
				}`}
			>
				<div className="mx-auto flex w-[320px] place-content-between">
					<div className="flex">
						{["background", "border", "character", "sticker", "text", "brush"].map((value) => (
							<button
								key={value}
								onClick={() => handleComponentClick(value)}
								className={`px-[6px] py-2 mr-[1px] rounded-lg font-nexonBold text-xs border border-black ${
									activeComponent === value ? "bg-ddubokPurple" : "bg-white"
								}`}
							>
								{value === "background" && "배경"}
								{value === "border" && "테두리"}
								{value === "sticker" && "스티커"}
								{value === "character" && "캐릭터"}
								{value === "text" && "텍스트"}
								{value === "brush" && "브러쉬"}
							</button>
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
			</div>

			{isPanelOpen && (
				<div
					className="fixed inset-0 pointer-events-none"
					onClick={() => setIsPanelOpen(false)}
				>
					<div
						className="absolute inset-0 pointer-events-auto"
						style={{ bottom: "300px" }}
					/>
				</div>
			)}
			<div
				className={`fixed left-1/2 transform -translate-x-1/2 bottom-0 bg-white rounded-t-lg  duration-300 ease-in-out ${
					isPanelOpen ? "translate-y-0" : "translate-y-full"
				}`}
				style={{
					width: "100%",
					maxWidth: "480px",
					height: "320px",
					boxShadow: "0 -4px 6px -1px rgba(0, 0, 0, 0.1)",
					zIndex: 40,
				}}
			>
				<div
					className="relative w-full h-full flex justify-center items-center"
					onClick={(e) => e.stopPropagation()}
				>
					<div className="absolute top-2 left-1/2 transform -translate-x-1/2 w-12 h-1 bg-gray-300 rounded-full" />
					<button
						className="absolute top-4 right-4 text-gray-500"
						onClick={() => setIsPanelOpen(false)}
					>
						✕
					</button>
					<div className=" px-4 flex flex-col justify-center items-center w-full ">
						{renderActiveComponent()}
					</div>
				</div>
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
							text="다음"
							color="green"
							size="small"
							font="regular"
							shadow="green"
							onClick={handleNext}
						/>
					</div>
				</Modal>
			)}

			{showExitConfirm && (
				<Modal>
					<h3 className="text-lg font-nexonBold mb-4">페이지를 나가시겠습니까?</h3>
					<p className="text-gray-600 mb-6 font-nexonRegular">
						저장되지 않은 변경사항이 있습니다.
						<br />
						작업한 내용이 모두 사라집니다.
					</p>
					<div className="flex justify-end gap-2">
						<Button
							text="취소"
							color="gray"
							size="small"
							font="regular"
							shadow="gray"
							onClick={() => {
								setShowExitConfirm(false);
							}}
						/>
						<Button
							text="나가기"
							color="red"
							size="small"
							font="regular"
							shadow="red"
							onClick={handleExitConfirm}
						/>
					</div>
				</Modal>
			)}
		</div>
	);
};

export default CreateFront;
