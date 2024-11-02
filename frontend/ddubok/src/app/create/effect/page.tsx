"use client";

import React, { useEffect, useState } from "react";
import Image from "next/image";
import { useCardStore } from "@store/card-store";
import Loading from "@components/common/loading";

const CreateEffect = () => {
	const selectedImage = useCardStore((state) => state.selectedImage);
	const [isLoading, setIsLoading] = useState(true);

	useEffect(() => {
		setIsLoading(false);
	}, []);

	if (isLoading) {
		return (
			<div className="flex w-full h-screen items-center justify-center">
				<Loading />
			</div>
		);
	}

	return (
		<div className="flex flex-col items-center w-full h-full">
			{selectedImage ? (
				<div className="w-[280px] h-[495px]">
					<Image
						src={selectedImage}
						alt="카드 앞면"
						width={280}
						height={495}
						className="object-contain rounded-lg"
					/>
				</div>
			) : (
				<div className="w-[280px] h-[495px] bg-gray-200 rounded-lg flex items-center justify-center">
					<p className="text-gray-500">이전 페이지에서 이미지를 먼저 생성해주세요</p>
				</div>
			)}
		</div>
	);
};

export default CreateEffect;
