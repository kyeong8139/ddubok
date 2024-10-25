"use client";

import React, { useState, useRef, useEffect, useMemo } from "react";
import { useRouter } from "next/navigation";

import { useCardStore } from "@store/card-store";
import Card from "@components/card/card";
import Loading from "@components/common/loading";

import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const Create = () => {
	const router = useRouter();
	const { setSelectedImage, setUserName } = useCardStore();
	const sliderRef = useRef<Slider | null>(null);
	const [currentSlide, setCurrentSlide] = useState(0);
	const [userName, setLocalUserName] = useState("");
	const [isLoading, setIsLoading] = useState(true);

	const cardImages = useMemo(
		() => [
			{ image: "/assets/examplCard1.png", effect: 0 },
			{ image: "/assets/examplCard2.png", effect: 0 },
			{ image: "", effect: 0 },
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

		if (selectedImage) {
			router.push("/create/letter");
		} else {
			router.push("/create/card");
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
					<div className="text-white font-nexonBold text-2xl mt-6">행운 카드 만들기</div>

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
										style={{ width: "280px", margin: "0 20px" }}
									>
										<Card
											width={280}
											height={495}
											image={card.image}
											effect={card.effect}
										/>
									</div>
								))}
							</Slider>
						</div>
					</div>

					<div className="w-9/12 flex flex-col items-center mt-6">
						<label className="text-white text-xl font-nexonRegular mb-2">나의 이름은?</label>
						<input
							type="text"
							placeholder="익명 (최대 11글자)"
							value={userName}
							onChange={(e) => setLocalUserName(e.target.value)}
							maxLength={11}
							className="border-b-2 border-white bg-transparent font-nexonRegular text-white text-center outline-none"
						/>
					</div>

					<button
						className="mt-6 px-6 py-2 bg-blue-500 text-white rounded"
						onClick={handleSelectButton}
					>
						다음으로
					</button>
				</div>
			)}
		</div>
	);
};

export default Create;
