// components/Card.tsx
import React from "react";

import { ICardProps } from "../../interface/components/card";

import Image from "next/image";

const Card = ({ width, height, image, effect }: ICardProps) => {
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

	return (
		<div>
			{image ? (
				<div
					className={`relative rounded-lg shadow-lg overflow-hidden ${effectClasses()}`}
					style={{ width: `${width}px`, height: `${height}px` }}
				>
					{image && (
						<Image
							src={image}
							alt="Card"
							fill
							sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
							style={{ objectFit: "cover" }}
							quality={80}
							priority
						/>
					)}
				</div>
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
