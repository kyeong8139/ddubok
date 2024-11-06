"use client";

import React, { useState, useRef, useEffect, useMemo } from "react";
import { useRouter, useSearchParams } from "next/navigation";

import { useCardStore } from "@store/card-store";
import Card from "@components/card/card";
import Loading from "@components/common/loading";
import Button from "@components/button/button";

import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const Create = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const type = searchParams?.get("type");
	const id = searchParams?.get("id");

	const { setSelectedImage, setUserName, setLetterContent } = useCardStore();
	const sliderRef = useRef<Slider | null>(null);
	const [currentSlide, setCurrentSlide] = useState(0);
	const [userName, setLocalUserName] = useState("");
	const [isLoading, setIsLoading] = useState(true);

	const cardImages = useMemo(
		() => [
			{ image: "", effect: 0 },
			{ image: "/assets/template/kkm-card.png", effect: 0 },
			{ image: "/assets/template/psh-card.jpg", effect: 0 },
			{ image: "/assets/template/kkm-card-2.png", effect: 0 },
			{ image: "/assets/template/lbk-card.png", effect: 0 },
			{ image: "/assets/template/kkm-card-3.png", effect: 0 },
			{ image: "/assets/template/kde-card.jpg", effect: 0 },
			{ image: "/assets/template/kde-card-2.jpg", effect: 0 },
		],
		[],
	);

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

	const handleSlideChange = (current: number) => {
		setCurrentSlide(current);
	};

	const handleSelectButton = () => {
		const selectedImage = cardImages[currentSlide].image;
		setSelectedImage(selectedImage);

		const finalUserName = userName.trim() === "" ? "익명" : userName;
		setUserName(finalUserName);

		setLetterContent("");

		if (selectedImage) {
			router.push(`/create/letter?type=${type}&id=${id}`);
		} else {
			router.push(`/create/card?type=${type}&id=${id}`);
		}
	};

	const handleCardClick = (index: number) => {
		const currentIndex = currentSlide;
		const totalSlides = cardImages.length;

		if (sliderRef.current) {
			const diff = index - currentIndex;

			if (Math.abs(diff) > totalSlides / 2) {
				if (diff > 0) {
					sliderRef.current.slickPrev();
				} else {
					sliderRef.current.slickNext();
				}
			} else {
				sliderRef.current.slickGoTo(index);
			}
		}
	};

	const settings = {
		dots: false,
		infinite: true,
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
		centerMode: true,
		variableWidth: true,
		arrows: false,
		className: "w-full",
		adaptiveHeight: true,
		afterChange: handleSlideChange,
	};

	return (
		<div>
			{isLoading ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : (
				<div className="flex flex-col items-center w-full h-full">
					<div className="text-white font-nexonBold text-xl mt-6">
						{type === "request" ? "요청 카드 선택하기" : "행운 카드 선택하기"}
					</div>

					<div className="w-full flex items-center justify-center mt-8">
						<div className="w-full max-w-[480px]">
							<Slider
								ref={sliderRef}
								{...settings}
							>
								{cardImages.map((card, index) => (
									<div
										key={index}
										className="!flex justify-center items-center"
										onClick={() => handleCardClick(index)}
										style={{ width: "240px", margin: "0 20px" }}
									>
										<Card
											width={240}
											height={424}
											path={card.image}
											effect={card.effect}
										/>
									</div>
								))}
							</Slider>
						</div>
					</div>

					<div className="w-9/12 flex flex-col items-center mt-10">
						<label className="text-white font-nexonRegular mb-4">받는 이에게 보낼 이름을 쓰세요</label>
						<input
							type="text"
							placeholder="익명 (최대 11글자)"
							value={userName}
							onChange={(e) => setLocalUserName(e.target.value)}
							maxLength={11}
							className="border-b border-white bg-transparent font-nexonRegular text-white text-center outline-none"
						/>
					</div>
					<div className="mt-10 mb-10 w-full flex justify-center">
						<Button
							text="다음으로"
							color="green"
							size="long"
							font="bold"
							shadow="green"
							onClick={handleSelectButton}
						/>
					</div>
				</div>
			)}
		</div>
	);
};

export default Create;
