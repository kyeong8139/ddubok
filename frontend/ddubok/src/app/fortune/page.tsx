"use client";

import React, { useEffect, useState } from "react";

import MiniCard from "@components/card/miniCard";
import Button from "@components/button/button";
import { checkAttendance, currentMonth, daysInMonth } from "@lib/utils/dateUtils";

const Fortune = () => {
	const attendanceList = ["2024-10-23", "2024-10-29"]; // 임시 데이터
	const [itemsCount, setItemsCount] = useState(0);
	const [emptyCount, setEmptyCount] = useState(0);

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

	return (
		<div
			id="fortune"
			className="py-6"
		>
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
						<strong>{attendanceList.length}</strong> 일
					</span>
				</div>
			</div>
			<div className="flex flex-wrap justify-between gap-x-[6px] gap-y-3 w-[calc(100%-64px)] mx-auto">
				{Array.from({ length: daysInMonth }, (_, index) => {
					const day = index + 1;
					const isAttend = checkAttendance(day, attendanceList);

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
					onClick={() => {}}
				/>
			</div>
		</div>
	);
};

export default Fortune;
