"use client";

import { useContext, useEffect, useState } from "react";

import { ModalContext } from "@context/modal-context";
import MiniCard from "@components/card/miniCard";
import Button from "@components/button/button";
import FortuneCard from "@components/card/fortuneCard";
import { checkAttendance, currentMonth, daysInMonth } from "@lib/utils/dateUtils";
import { insertFortune, selectFortuneList } from "@lib/api/fortune-api";
import { IFortuneProps } from "@interface/components/fortune";

const Fortune = () => {
	const { isModalOpen, openModal } = useContext(ModalContext);
	const [itemsCount, setItemsCount] = useState(0);
	const [emptyCount, setEmptyCount] = useState(0);
	const [fortuneList, setFortuneList] = useState([]);
	const [fortuneCount, setFortuneCount] = useState(0);
	const [fortuneDetail, setFortuneDetail] = useState<IFortuneProps>({ sentence: "", score: 0 });

	useEffect(() => {
		const loadFortuneList = async () => {
			try {
				const response = await selectFortuneList();
				console.log(response.data.data);
				setFortuneList(response.data.data.attendanceList);
				setFortuneCount(response.data.data.attendanceCount);
			} catch (error) {
				console.error(error);
			}
		};

		loadFortuneList();
	}, []);

	useEffect(() => {
		const maxWidth = 480;
		const padding = 64;
		const itemWidth = 56;
		const gap = 4;

		const container = Math.min(window.innerWidth, maxWidth) - padding;
		const items = Math.floor((container + gap) / (itemWidth + gap));

		setItemsCount(items);
		setEmptyCount(items - (daysInMonth % items));

		const handleResize = () => {
			const container = Math.min(window.innerWidth, maxWidth) - padding;
			const items = Math.floor((container + gap) / (itemWidth + gap));

			setItemsCount(items);
			setEmptyCount(items - (daysInMonth % items));
		};

		window.addEventListener("resize", handleResize);
		return () => window.removeEventListener("resize", handleResize);
	}, []);

	const createFortune = async () => {
		try {
			const response = await insertFortune();
			setFortuneDetail({
				sentence: response.data.data.fortune.sentence,
				score: response.data.data.fortune.score,
			});
			setFortuneList(response.data.data.attendanceHistory.attendanceList);
			setFortuneCount(response.data.data.attendanceHistory.attendanceCount);

			openModal();
		} catch (error) {
			console.error(error);
		}
	};

	return (
		<div id="fortune">
			<div className="py-6">
				<div className="text-white flex flex-col items-center">
					<h1 className="font-nexonBold text-xl mb-2">오늘의 운세</h1>
					<p className="font-nexonRegular text-sm">매일 오늘의 운세와 행운 지수를 확인하세요!</p>
				</div>
				<div className="px-8">
					<div className="font-nexonRegular flex justify-between items-center px-4 my-8 text-sm bg-white w-full h-12 rounded-lg shadow-custom-gray">
						<span>
							<strong>{currentMonth}월</strong> 의 출석일
						</span>
						<span>
							<strong>{fortuneCount}</strong> 일
						</span>
					</div>
				</div>
				<div className="flex flex-wrap justify-between gap-x-[6px] gap-y-4 w-[calc(100%-64px)] mx-auto">
					{Array.from({ length: daysInMonth }, (_, index) => {
						const day = index + 1;
						const isAttend = checkAttendance(day, fortuneList);

						return (
							<MiniCard
								key={index}
								day={day}
								isAttend={isAttend}
							/>
						);
					})}
					{emptyCount !== itemsCount &&
						Array.from({ length: emptyCount }, (_, index) => (
							<div
								key={index}
								className="w-14 h-14"
							/>
						))}
				</div>
				<div className="text-center mt-8 mb-4">
					<Button
						text="오늘의 운세 확인하기"
						color="gradient"
						size="long"
						font="bold"
						shadow="gradient"
						onClick={createFortune}
					/>
				</div>
			</div>
			<div
				className={`transition-opacity duration-300
					${isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
			>
				<FortuneCard
					sentence={fortuneDetail.sentence}
					score={fortuneDetail.score}
				/>
			</div>
		</div>
	);
};

export default Fortune;
