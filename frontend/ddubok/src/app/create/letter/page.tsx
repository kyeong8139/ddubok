"use client";

import React, { useState } from "react";
import Image from "next/image";
import Button from "@components/button/button";
import { sendCard } from "@lib/api/card";
import { useRouter } from "next/navigation";
import { useCardStore } from "@store/card-store";

const CreateBack = () => {
	const router = useRouter();
	const [letterContent, setLetterContent] = useState("");
	const [isLoading, setIsLoading] = useState(false);
	const { selectedImage, userName } = useCardStore();

	const handleSendCard = async () => {
		if (isLoading) return;
		if (!letterContent.trim()) {
			alert("편지 내용을 입력해주세요.");
			return;
		}
		if (!selectedImage) {
			alert("잘못된 접근입니다.");
			return;
		}
		if (!userName.trim()) {
			alert("잘못된 접근입니다.");
			return;
		}

		try {
			setIsLoading(true);
			const response = await sendCard(letterContent, userName, 1, selectedImage);

			if (response.code === "200") {
				alert("카드가 성공적으로 전송되었습니다!");
			}
		} catch (error) {
			console.error("카드 전송 중 오류 발생:", error);
			alert("카드 전송에 실패했습니다. 다시 시도해주세요.");
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<div className="flex flex-col items-center w-full">
			<div className="text-white font-nexonBold text-2xl mt-10">편지 쓰기</div>
			<div className="relative mt-4 flex justify-center">
				<div className="relative w-[280px] h-[495px]">
					<Image
						src="/assets/fortune-reverse.png"
						alt="운세 카드"
						fill
					/>
					<textarea
						value={letterContent}
						onChange={(e) => setLetterContent(e.target.value)}
						className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2
                                w-[206px] h-[398px] bg-transparent text-black 
                                resize-none font-nexonRegular text-base
                                focus:outline-none scrollbar-hide
                                overflow-y-auto [&::-webkit-scrollbar]:hidden"
						placeholder="여기에 편지를 작성해주세요..."
						maxLength={500}
					/>
				</div>
			</div>
			<div className="w-[280px] flex justify-end mt-1">
				<div className="text-white font-nexonLight text-sm">{letterContent.length}/500자</div>
			</div>

			<div className="w-full mt-10 flex justify-center">
				<Button
					text={isLoading ? "전송 중..." : "전송하기"}
					color="green"
					size="long"
					font="bold"
					shadow="green"
					onClick={handleSendCard}
					disabled={isLoading}
				/>
			</div>
		</div>
	);
};

export default CreateBack;
