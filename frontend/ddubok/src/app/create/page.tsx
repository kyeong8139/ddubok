"use client";

import React, { useState, useRef, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";

import { useCardStore } from "@store/card-store";
import Card from "@components/card/card";
import Loading from "@components/common/loading";
import Button from "@components/button/button";

import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { ICardImageProps } from "@interface/components/card";
import { selectMainInfo } from "@lib/api/main-api";

const Create = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const type = searchParams?.get("type");
	const id = searchParams?.get("id");

	const { setSelectedImage, setUserName, setLetterContent } = useCardStore();
	const sliderRef = useRef<Slider | null>(null);
	const inputRef = useRef<HTMLInputElement>(null);
	const touchStartRef = useRef<number | null>(null);
	const [currentSlide, setCurrentSlide] = useState(0);
	const [userName, setLocalUserName] = useState("");
	const [isLoading, setIsLoading] = useState(true);
	const [isFocused, setIsFocused] = useState(false);

	const [cardImages, setCardImages] = useState<ICardImageProps[]>([
		{ image: "", effect: 0 },
		{ image: "/assets/template/template (1).png", effect: 0 },
		{ image: "/assets/template/template (2).png", effect: 0 },
		{ image: "/assets/template/template (3).png", effect: 0 },
		{ image: "/assets/template/template (4).png", effect: 0 },
		{ image: "/assets/template/template (5).png", effect: 0 },
		{ image: "/assets/template/template (6).png", effect: 0 },
	]);

	useEffect(() => {
		const getMainInfo = async () => {
			try {
				const response = await selectMainInfo();
				const path = response.data.data.path;
				const updatedCardImages = [
					{ image: "", effect: 0 },
					...path.map((imagePath: string) => ({ image: imagePath, effect: 0 })),
				];
				setCardImages(updatedCardImages);
				setIsLoading(false);
			} catch (error) {
				console.error(error);
				setIsLoading(false);
			}
		};

		getMainInfo();
	}, []);

	const handleSlideChange = (current: number) => {
		setCurrentSlide(current);
	};

	const sanitizeInput = (input: string): string => {
		const sanitized = input
			.replace(/[<>]/g, "")
			.replace(/[&'"]/g, "")
			.replace(/javascript:/gi, "")
			.replace(/on\w+=/gi, "")
			.replace(/data:/gi, "");

		return sanitized.replace(/[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s_-]/g, "");
	};

	const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const sanitizedValue = sanitizeInput(e.target.value);
		setLocalUserName(sanitizedValue);
	};

	const handleSelectButton = () => {
		const selectedImage = cardImages[currentSlide].image;
		setSelectedImage(selectedImage);

		const sanitizedName = sanitizeInput(userName);
		const finalUserName = sanitizedName.trim() === "" ? "익명" : sanitizedName;
		setUserName(finalUserName);

		setLetterContent("");

		if (selectedImage) {
			router.push(`/create/letter?type=${type}&id=${id}`);
		} else {
			router.push(`/create/card?type=${type}&id=${id}`);
		}
	};

	const handleCardClick = (index: number) => {
		if (inputRef.current) {
			inputRef.current.blur();
			setIsFocused(false);
		}

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

			if (currentIndex === index) {
				setTimeout(() => {
					if (inputRef.current) {
						inputRef.current.focus();
						setIsFocused(true);
						inputRef.current.scrollIntoView({
							behavior: "smooth",
							block: "center",
						});
					}
				}, 100);
			}
		}
	};

	const handleTouchStart = (e: React.TouchEvent) => {
		touchStartRef.current = e.touches[0].clientX;
		if (inputRef.current) {
			inputRef.current.blur();
			setIsFocused(false);
		}
	};

	const handleTouchEnd = (e: React.TouchEvent) => {
		if (touchStartRef.current === null) return;
		touchStartRef.current = null;
	};

	const handleFocus = () => setIsFocused(true);
	const handleBlur = () => setIsFocused(false);

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
		swipe: true,
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

					<div
						className="w-full flex items-center justify-center mt-8"
						onTouchStart={handleTouchStart}
						onTouchEnd={handleTouchEnd}
					>
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
						<div className="h-12 flex items-center">
							<input
								ref={inputRef}
								type="text"
								placeholder="익명 (최대 11글자)"
								value={userName}
								onChange={handleNameChange}
								onFocus={handleFocus}
								onBlur={handleBlur}
								maxLength={11}
								className={`bg-transparent font-nexonRegular text-white text-center outline-none px-4 py-2 
        border-b border-white transform-gpu
        ${isFocused ? "animate-[focusEffect_0.8s_cubic-bezier(0.25,0.46,0.45,0.94)_forwards]" : ""}`}
								style={{ transformOrigin: "center bottom" }}
							/>
						</div>
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
