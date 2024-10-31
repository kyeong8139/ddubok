import Image from "next/image";
import { useEffect, useState } from "react";

import { ICardProps } from "@interface/components/card";

import "@styles/scrollHide.css";
import "@styles/rotateCard.css";

const Card = ({ width, height, image, content, effect, flip }: ICardProps) => {
	const [isFlipped, setIsFlipped] = useState(false);

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
	}, [image]);

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
				{/* 카드 앞면 */}
				<div
					className={`absolute w-full h-full backface-hidden rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
				>
					{image ? (
						<Image
							src={image}
							alt="Card"
							fill
							sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
							style={{ objectFit: "cover" }}
							quality={80}
							priority
						/>
					) : (
						<div className="font-nexonBold text-xl flex justify-center items-center h-full bg-white">
							직접 만들기
						</div>
					)}
				</div>

				{/* 카드 뒷면 */}
				<div
					className="absolute w-full h-full backface-hidden rotate-y-180 flex items-center justify-center rounded-lg shadow-lg overflow-hidden text-justify"
					style={{ backgroundColor: "#f0f0f0" }}
				>
					<div
						className="font-nexonRegular my-10 px-8 leading- text-sm overflow-hidden overflow-y-scroll scrollbar-hide"
						style={{ width: `${width}px`, height: `${height - 80}px` }}
					>
						{content}
					</div>
					<Image
						src="/assets/fortune-reverse.png"
						alt="운세 카드"
						fill
						className="absolute z-[-1]"
					/>
				</div>
			</div>
		</div>
		// <div onClick={toggleFlip}>
		// 	{image ? (
		// 		isFlipped ? (
		// 			<div
		// 				className={`relative rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
		// 				style={{
		// 					width: `${width}px`,
		// 					height: `${height}px`,
		// 				}}
		// 			>
		// 				<div className="absolute inset-0 text-justify">
		// 					<div
		// 						className="font-nexonRegular my-10 px-8 leading-normal text-sm overflow-hidden overflow-y-scroll scrollbar-hide"
		// 						style={{
		// 							width: `${width}px`,
		// 							height: `${height - 80}px`,
		// 						}}
		// 					>
		// 						{content}
		// 					</div>
		// 					<Image
		// 						src="/assets/fortune-reverse.png"
		// 						alt="운세 카드"
		// 						fill
		// 						className="absolute z-[-1]"
		// 					/>
		// 				</div>
		// 			</div>
		// 		) : (
		// 			<div
		// 				className={`relative rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
		// 				style={{
		// 					width: `${width}px`,
		// 					height: `${height}px`,
		// 				}}
		// 			>
		// 				<Image
		// 					src={image}
		// 					alt="Card"
		// 					fill
		// 					sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
		// 					style={{ objectFit: "cover" }}
		// 					quality={80}
		// 					priority
		// 				/>
		// 			</div>
		// 		)
		// 	) : (
		// 		<div
		// 			className={`relative rounded-lg shadow-lg overflow-hidden bg-white flex justify-center items-center`}
		// 			style={{ width: `${width}px`, height: `${height}px` }}
		// 		>
		// 			<div className="font-nexonBold text-xl">직접 만들기</div>
		// 		</div>
		// 	)}
		// </div>
	);
};

export default Card;
