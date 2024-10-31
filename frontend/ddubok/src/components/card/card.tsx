// components/Card.tsx
import Image from "next/image";
import React, { useState } from "react";

import { ICardProps } from "@interface/components/card";

const Card = ({ width, height, image, effect, flip }: ICardProps) => {
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

	return (
		<div onClick={toggleFlip}>
			{image ? (
				isFlipped ? (
					<div
						className={`relative rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
						style={{
							width: `${width}px`,
							height: `${height}px`,
						}}
					>
						<div className="absolute inset-0 flex items-center justify-center bg-gray-100">
							<div className="font-nexonBold text-lg">Card Back</div>
						</div>
					</div>
				) : (
					<div
						className={`relative rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
						style={{
							width: `${width}px`,
							height: `${height}px`,
						}}
					>
						<Image
							src={image}
							alt="Card"
							fill
							sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
							style={{ objectFit: "cover" }}
							quality={80}
							priority
						/>
					</div>
				)
			) : (
				<div
					className={`relative rounded-lg shadow-lg overflow-hidden bg-white flex justify-center items-center`}
					style={{ width: `${width}px`, height: `${height}px` }}
				>
					<div className="font-nexonBold text-xl">직접 만들기</div>
				</div>
			)}
		</div>
	);
};

export default Card;
