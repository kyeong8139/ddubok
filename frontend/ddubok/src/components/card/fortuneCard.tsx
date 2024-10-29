"use client";

import Image from "next/image";
import { useContext } from "react";

import { ModalContext } from "@context/modal-context";
import { IFortuneProps } from "@interface/components/fortune";
import { currentday, currentMonth } from "@lib/utils/dateUtils";

const FortuneCard = ({ sentence, score }: IFortuneProps) => {
	const { closeModal } = useContext(ModalContext);

	return (
		<div id="fortune-card">
			<div
				id="overlay"
				className="fixed bottom-0 w-screen max-w-[480px] z-10 h-full bg-black bg-opacity-30 backdrop-blur-sm"
				onClick={closeModal}
			></div>
			<div
				id="content"
				className="fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[280px] h-[450px] rounded-lg p-8 z-10 overflow-hidden"
			>
				<div className="flex flex-col items-center font-nexonRegular">
					<div className="my-6">
						<p className="text-sm text-center mb-2">
							<strong>{currentMonth}</strong>월 <strong>{currentday}</strong>일
						</p>
						<h1 className="font-pyeongchang text-3xl">오늘의 운세</h1>
					</div>
					<div className="my-18">
						<p className="whitespace-pre-line break-keep font-nexonBold text-xl text-center leading-snug">
							{sentence}
						</p>
					</div>
					<div className="my-6">
						당신의 행운지수는 <strong>{score}</strong>
					</div>
				</div>

				<Image
					src="/assets/fortune-reverse.png"
					alt="운세 카드"
					fill
					className="absolute z-[-1]"
				/>
			</div>
		</div>
	);
};

export default FortuneCard;
