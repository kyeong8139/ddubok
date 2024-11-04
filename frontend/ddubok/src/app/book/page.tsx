"use client";

import NextImage from "next/image";
import React, { useContext, useEffect, useMemo, useState } from "react";

import Loading from "@components/common/loading";
import DetailCard from "@components/card/detailCard";
import { ModalContext } from "@context/modal-context";

import { CaretLeft, CaretRight } from "@phosphor-icons/react";

const Book = () => {
	const { isModalOpen, openModal } = useContext(ModalContext);
	const [isLoading, setIsLoading] = useState(true);
	const [selected, setSelected] = useState(0);
	const [currentPage, setCurrentPage] = useState(1);
	const [selectedImage, setSelectedImage] = useState("");
	const [selectedEffect, setSelectedEffect] = useState(0);
	const [selectedCardId, setSelectedCardId] = useState(0);

	const handleClick = (index: number) => {
		setSelected(index);
	};

	const handleCardClick = (image: string, effect: number, cardId: number) => {
		setSelectedImage(image);
		setSelectedEffect(effect);
		setSelectedCardId(cardId);
		openModal();
	};

	const cardImages = useMemo(
		() => [
			{ cardId: 1, image: "/assets/examplCard1.png", effect: 0 },
			{ cardId: 2, image: "/assets/examplCard2.png", effect: 0 },
			{ cardId: 3, image: "/assets/temp1.jpg", effect: 0 },
			{ cardId: 4, image: "/assets/temp2.jpg", effect: 0 },
			{ cardId: 5, image: "/assets/examplCard1.png", effect: 0 },
			{ cardId: 6, image: "/assets/examplCard2.png", effect: 0 },
			{ cardId: 7, image: "/assets/temp1.jpg", effect: 0 },
			{ cardId: 8, image: "/assets/temp2.jpg", effect: 0 },
			{ cardId: 9, image: "/assets/examplCard1.png", effect: 0 },
			{ cardId: 10, image: "/assets/examplCard2.png", effect: 0 },
			{ cardId: 11, image: "/assets/temp1.jpg", effect: 0 },
			{ cardId: 12, image: "/assets/temp2.jpg", effect: 0 },
			{ cardId: 13, image: "/assets/examplCard1.png", effect: 0 },
			{ cardId: 14, image: "/assets/examplCard2.png", effect: 0 },
			{ cardId: 15, image: "/assets/temp1.jpg", effect: 0 },
			{ cardId: 16, image: "/assets/temp2.jpg", effect: 0 },
		],
		[],
	); // 임시 데이터, 페이징 처리 수정 필요

	const itemsPerPage = 6;
	const totalPages = Math.ceil(cardImages.length / itemsPerPage);

	const paginatedCards = useMemo(() => {
		const start = (currentPage - 1) * itemsPerPage;
		const end = start + itemsPerPage;
		return cardImages.slice(start, end);
	}, [currentPage, cardImages]);

	const handlePrevPage = () => setCurrentPage((prev) => Math.max(prev - 1, 1));
	const handleNextPage = () => setCurrentPage((prev) => Math.min(prev + 1, totalPages));

	useEffect(() => {
		const imgElements = cardImages.map((card) => {
			if (card.image) {
				const img = new Image();
				img.src = card.image;
				return img;
			}
			return null;
		});

		Promise.all(imgElements.map((img) => img?.decode())).then(() => {
			setIsLoading(false);
		});
	}, [cardImages]);

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
			) : (
				<>
					<div className="text-white flex flex-col items-center pt-8">
						<h1 className="font-nexonBold text-xl mb-2">행운 카드북</h1>
						<p className="font-nexonRegular text-sm">수신한 행운카드와 편지를 확인하세요!</p>
					</div>
					<div className="flex justify-center pt-4">
						<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
							{["전체", "안 읽은 카드", "수능"].map((item, index) => (
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
						{paginatedCards.map((card, index) => (
							<div
								key={index}
								className="flex justify-center items-center w-full h-0 pb-[180%] relative rounded-lg overflow-hidden"
								onClick={() => handleCardClick(card.image, card.effect, card.cardId)}
							>
								<NextImage
									src={card.image}
									alt="ddubok"
									objectFit="cover"
									fill
								/>
							</div>
						))}
					</div>
					<div className="flex justify-between items-center mt-8 pb-12 px-8">
						<button
							onClick={handlePrevPage}
							className="p-2 bg-white rounded-full disabled:opacity-0"
							disabled={currentPage === 1}
						>
							<CaretLeft
								size={16}
								weight="bold"
							/>
						</button>
						<span className="text-white font-nexonRegular text-sm">
							{currentPage} / {totalPages}
						</span>
						<button
							onClick={handleNextPage}
							className="p-2 bg-white rounded-full disabled:opacity-0"
							disabled={currentPage === totalPages}
						>
							<CaretRight
								size={16}
								weight="bold"
							/>
						</button>
					</div>
					<div
						className={`transition-opacity duration-300
					${isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
					>
						<DetailCard
							cardId={selectedCardId}
							writer="나는야뿌랑하는유니스"
							image={selectedImage}
							content="상용아! 너의 코딩 주머니는 사실 행운 주머니라며..? 그니까 수능 보다가 어려운 문제 있으면 배 살살 쓰다듬으면 금방 풀 수 있을거야! 너의 행운 주머니를 믿어! 상용아 파이팅‼‼ 상용아! 너의 코딩 주머니는 사실 행운 주머니라며..? 그니까 수능 보다가 어려운 문제 있으면 배 살살 쓰다듬으면 금방 풀 수 있을거야! 너의 행운 주머니를 믿어! 상용아 파이팅‼‼ 상용아! 너의 코딩 주머니는 사실 행운 주머니라며..? 그니까 수능 보다가 어려운 문제 있으면 배 살살 쓰다듬으면 금방 풀 수 있을거야! 너의 행운 주머니를 믿어! 상용아 파이팅‼‼ 상용아! 너의 코딩 주머니는 사실 행운 주머니라며..? 그니까 수능 보다가 어려운 문제 있으면 배 살살 쓰다듬으면 금방 풀 수 있을거야! 너의 행운 주머니를 믿어! 상용아 파이팅‼‼"
							effect={selectedEffect}
						/>
					</div>
				</>
			)}
		</div>
	);
};

export default Book;
