"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import { IDetailCardDto } from "@interface/components/card";
import "@styles/scrollHide.css";
import "@styles/rotateCard.css";
import { PlusCircle } from "@phosphor-icons/react";

const Card = ({ width, height, path, content, state, effect, flip, flag, openedAt }: IDetailCardDto) => {
	const [isFlipped, setIsFlipped] = useState(false);
	const [timeLeft, setTimeLeft] = useState<string>("");

	const calculateTimeLeft = () => {
		if (!openedAt) return "";

		const now = new Date();
		const openDate = new Date(openedAt);
		const difference = openDate.getTime() - now.getTime();

		if (difference <= 0) return "0:00:00";

		const hours = Math.floor(difference / (1000 * 60 * 60));
		const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
		const seconds = Math.floor((difference % (1000 * 60)) / 1000);

		return `${hours}:${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
	};

	useEffect(() => {
		if (!openedAt) return;

		const timer = setInterval(() => {
			setTimeLeft(calculateTimeLeft());
		}, 1000);

		setTimeLeft(calculateTimeLeft());

		return () => clearInterval(timer);
	}, [openedAt]);

	const escapeHTML = (str: string = "") => {
		return str
			.replace(/&/g, "&amp;")
			.replace(/</g, "&lt;")
			.replace(/>/g, "&gt;")
			.replace(/"/g, "&quot;")
			.replace(/'/g, "&#039;")
			.replace(/\n/g, "<br>");
	};

	const effectClasses = () => {
		switch (effect) {
			case 1:
				return "bg-yellow-500";
			case 2:
				return "bg-red-500";
			default:
				return "bg-transparent";
		}
	};

	const toggleFlip = () => {
		if (flip) {
			setIsFlipped(!isFlipped);
		}
	};

	useEffect(() => {
		setIsFlipped(false);
	}, [path]);

	const renderContent = () => {
		if (flag) {
			return (
				<div
					className="font-nexonRegular my-10 px-8 leading-tight text-sm overflow-hidden overflow-y-scroll scrollbar-hide"
					style={{ width: `${width}px`, height: `calc(${height}px - 80px)` }}
					dangerouslySetInnerHTML={{
						__html: escapeHTML(content),
					}}
				/>
			);
		}

		return state === "OPEN" ? (
			<div
				className="font-nexonRegular my-10 px-8 leading-tight text-sm overflow-hidden overflow-y-scroll scrollbar-hide"
				style={{ width: `${width}px`, height: `calc(${height}px - 80px)` }}
				dangerouslySetInnerHTML={{
					__html: escapeHTML(content),
				}}
			/>
		) : state === "FILTERED_OPEN" ? (
			<div
				className="font-nexonRegular flex justify-center items-center h-full flex-col text-center"
				style={{ width: `${width}px`, height: `calc(${height}px - 80px)` }}
			>
				<p className="mb-4">
					편지에 부적절한 내용이
					<br />
					포함되어 있습니다.
				</p>
				<p className="font-xs">
					카드 내용을 확인하시려면 <br />
					상단의 별 버튼을 클릭하세요!
				</p>
			</div>
		) : (
			<div className="font-nexonRegular flex justify-center items-center h-full flex-col text-center">
				<p className="mb-2">편지를 확인하기까지</p>
				<div className="font-bold text-2xl mb-2">
					<span className="bg-gray-100 px-3 py-1 rounded">{timeLeft.split(":")[0]}</span>
					<span className="mx-1">:</span>
					<span className="bg-gray-100 px-3 py-1 rounded">{timeLeft.split(":")[1]}</span>
					<span className="mx-1">:</span>
					<span className="bg-gray-100 px-3 py-1 rounded">{timeLeft.split(":")[2]}</span>
				</div>
				<p>남았습니다</p>
			</div>
		);
	};

	return (
		<div
			className="perspective"
			style={{ width: `${width}px`, height: `${height}px` }}
			onClick={toggleFlip}
		>
			<div
				className={`relative preserve-3d transition-transform duration-700 ${isFlipped ? "rotate-y-180" : ""}`}
				style={{ width: `${width}px`, height: `${height}px` }}
			>
				<div
					className={`absolute w-full h-full backface-hidden rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
				>
					{path ? (
						<Image
							src={path}
							alt="Card"
							fill
							sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
							style={{ objectFit: "cover" }}
							quality={80}
							priority
							unoptimized
						/>
					) : (
						<div className="font-nexonBold text-xl flex flex-col gap-2 justify-center items-center h-full bg-white">
							<PlusCircle size={32} />
							<span>직접 만들기</span>
						</div>
					)}
				</div>

				<div
					className="absolute w-full h-full backface-hidden rotate-y-180 flex items-center justify-center rounded-lg shadow-lg overflow-hidden text-justify"
					style={{ backgroundColor: "#f0f0f0" }}
				>
					{renderContent()}
					<Image
						src="/assets/fortune-reverse.png"
						alt="운세 카드"
						fill
						className="absolute z-[-1]"
						unoptimized
					/>
				</div>
			</div>
		</div>
	);
};

export default Card;
