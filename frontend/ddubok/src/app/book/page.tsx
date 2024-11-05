"use client";

import NextImage from "next/image";
import React, { useContext, useEffect, useMemo, useState } from "react";

import Loading from "@components/common/loading";
import DetailCard from "@components/card/detailCard";
import { ModalContext } from "@context/modal-context";
import { ICardDto } from "@interface/components/card";
import { selectCardDetail, selectCardList, selectCardSeasonList } from "@lib/api/card-load-api";

import { CaretLeft, CaretRight } from "@phosphor-icons/react";
import Button from "@components/button/button";

const Book = () => {
	const { isModalOpen, openModal } = useContext(ModalContext);
	const [isLoading, setIsLoading] = useState(true);
	const [selected, setSelected] = useState(0);
	const [currentPage, setCurrentPage] = useState(0);
	const [cardList, setCardList] = useState<ICardDto[]>([]);
	const [card, setCard] = useState<ICardDto>({
		id: 0,
		content: "",
		openedAt: "",
		path: "",
		state: "",
		writerName: "",
		isRead: false,
	});
	const [hasNext, setHasNext] = useState(false);

	useEffect(() => {
		const loadCardList = async () => {
			try {
				setIsLoading(true);

				const response =
					selected === 0
						? await selectCardList(6, currentPage)
						: await selectCardSeasonList(6, currentPage, selected);

				if (response.data.code === "702") {
					setCardList([]);
					setHasNext(false);
				} else {
					let cards = response.data.data.cards || [];
					setCardList(cards);
					setHasNext(response.data.data.hasNext || false);
				}

				setIsLoading(false);
			} catch (error) {
				setCardList([]);
				setIsLoading(false);
				console.error(error);
			}
		};

		loadCardList();
	}, [selected, currentPage]);

	const handleClick = (index: number) => {
		setSelected(index);
		setCurrentPage(0);
	};

	const handleCardClick = (cardId: number) => {
		selectCardDetail(cardId).then((response) => {
			console.log(response.data.data);
			setCard(response.data.data);
		});

		openModal();
	};

	useEffect(() => {
		if (isModalOpen) {
			document.body.style.overflow = "hidden";
		} else {
			document.body.style.overflow = "auto";
		}

		return () => {
			document.body.style.overflow = "auto";
		};
	}, [isModalOpen]);

	return (
		<div id="book">
			{isLoading ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : cardList.length === 0 ? (
				<>
					<div className="flex justify-center pt-4">
						<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
							{["전체", "수능"].map((item, index) => (
								<li
									key={index}
									onClick={() => handleClick(index)}
									className={`p-[6px] rounded-md cursor-pointer ${
										selected === index ? "bg-ddubokPurple text-white font-nexonBold" : "bg-gray-200"
									}`}
								>
									{item}
								</li>
							))}
						</ul>
					</div>
					<div className="font-nexonBold flex flex-col items-center justify-center h-[calc(100vh-150px)] text-white">
						<p className="mb-4 text-lg">받은 행운카드가 없습니다.</p>
						<Button
							text="행운카드<br/>조르기"
							color="gradient"
							size="short"
							font="both"
							shadow="gradient"
							onClick={() => {}}
						/>
					</div>
				</>
			) : (
				<>
					<div className="text-white flex flex-col items-center pt-8">
						<h1 className="font-nexonBold text-xl mb-2">행운 카드북</h1>
						<p className="font-nexonRegular text-sm">수신한 행운카드와 편지를 확인하세요!</p>
					</div>
					<div className="flex justify-center pt-4">
						<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
							{["전체", "수능"].map((item, index) => (
								<li
									key={index}
									onClick={() => handleClick(index)}
									className={`p-[6px] rounded-md cursor-pointer ${
										selected === index ? "bg-ddubokPurple text-white font-nexonBold" : "bg-gray-200"
									}`}
								>
									{item}
								</li>
							))}
						</ul>
					</div>
					<div className="w-[calc(100%-64px)] max-w-[416px] mx-auto mt-12 grid grid-cols-2 gap-4">
						{cardList.map((card, index) => (
							<div
								key={index}
								className="flex justify-center items-center w-full h-0 pb-[180%] relative rounded-lg overflow-hidden"
								onClick={() => handleCardClick(card.id)}
							>
								<NextImage
									src={card.path}
									alt="ddubok"
									objectFit="cover"
									fill
									unoptimized
								/>
							</div>
						))}
					</div>
					<div className="flex justify-between items-center mt-8 pb-12 px-8">
						<button
							onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
							className="p-2 bg-white rounded-lg disabled:opacity-0"
							disabled={currentPage === 0}
						>
							<div className="flex items=-center gap-1 font-nexonRegular">
								<CaretLeft
									size={16}
									weight="bold"
								/>
								<span className="text-sm">이전으로</span>
							</div>
						</button>
						<button
							onClick={() => setCurrentPage((prev) => prev + 1)}
							className="p-2 bg-white rounded-lg disabled:opacity-0"
							disabled={!hasNext}
						>
							<div className="flex items-center gap-1 font-nexonRegular">
								<span className="text-sm">다음으로</span>
								<CaretRight
									size={16}
									weight="bold"
								/>
							</div>
						</button>
					</div>
					<div
						className={`transition-opacity duration-300 ${
							isModalOpen && card ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
						}`}
					>
						<DetailCard
							id={card.id}
							state={card.state}
							writerName={card.writerName}
							path={card.path}
							content={card.content}
							effect={0}
						/>
					</div>
				</>
			)}
		</div>
	);
};

export default Book;
