"use client";

import React, { useEffect, useState } from "react";

import Image from "next/image";

const CreateEffect = () => {
	const [frontImage, setFrontImage] = useState<string | null>(null);

	useEffect(() => {
		const savedImage = localStorage.getItem("cardFrontImage");
		if (savedImage) {
			setFrontImage(savedImage);
		}
	}, []);

	return (
		<div className="flex flex-col items-center w-full h-full">
			{frontImage ? (
				<div className="w-[280px] h-[495px]">
					<Image
						src={frontImage}
						alt="카드 앞면"
						width={280}
						height={495}
						className="object-contain rounded-lg"
					/>
				</div>
			) : (
				<div className="w-[280px] h-[495px] bg-gray-200 rounded-lg flex items-center justify-center">
					<p className="text-gray-500">이미지를 불러올 수 없습니다.</p>
				</div>
			)}
		</div>
	);
};

export default CreateEffect;
