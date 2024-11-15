"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useContext, useEffect, useState } from "react";

import Button from "@components/button/button";
import Loading from "@components/common/loading";
import Card from "@components/card/card";
import Modal from "@components/common/modal";
import { ModalContext } from "@context/modal-context";
import { selectPreviewList } from "@lib/api/card-load-api";
import { decryptCardId } from "@lib/utils/crypto";

import Slider from "react-slick";
import useAuthToken from "@lib/utils/tokenUtils";
import { selectMainInfo } from "@lib/api/main-api";
import { ICardImageProps } from "@interface/components/card";

const Share = () => {
	const router = useRouter();
	const { accessToken } = useAuthToken();
	const searchParams = useSearchParams();
	const id = searchParams.get("id");
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const [isLoading, setIsLoading] = useState(true);
	const [nickname, setNickname] = useState("");
	const [imageArray, setImageArray] = useState<ICardImageProps[]>([
		{ image: "/assets/template/template (1).png", effect: 0 },
		{ image: "/assets/template/template (2).png", effect: 0 },
		{ image: "/assets/template/template (3).png", effect: 0 },
		{ image: "/assets/template/template (4).png", effect: 0 },
		{ image: "/assets/template/template (5).png", effect: 0 },
		{ image: "/assets/template/template (6).png", effect: 0 },
	]);

	const settings = {
		infinite: true,
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
		centerMode: true,
		variableWidth: true,
		arrows: false,
		autoplay: true,
		autoplaySpeed: 4000,
		adaptiveHeight: true,
	};

	useEffect(() => {
		const loadPriveiwImages = async () => {
			try {
				const memberId = decryptCardId(id as string);

				if (memberId === null) {
					throw new Error("memberId가 없음");
				}

				const response = await selectPreviewList(memberId);
				setNickname(response.data.data.nickname);
				setImageArray(response.data.data.cardUrl);
				setIsLoading(false);
			} catch (error) {
				console.error("Error fetching card images:", error);
				router.push("/login");
			}
		};

		loadPriveiwImages();
	}, []);

	useEffect(() => {
		const getMainInfo = async () => {
			try {
				const response = await selectMainInfo();
				const path = response.data.data.path;

				setImageArray(path.map((imagePath: string) => ({ image: imagePath, effect: 0 })));
				setIsLoading(false);
			} catch (error) {
				console.error(error);
				setIsLoading(false);
			}
		};

		getMainInfo();
	}, []);

	return (
		<div id="request">
			{isLoading ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : (
				<>
					<div className="text-white font-nexonRegular flex flex-col items-center pt-8 text-center">
						<p className="mb-4 text-lg leading-normal">
							<span className="font-nexonBold">{nickname}</span> 님을 위해 <br />
							행운카드를 만들어주세요🍀
						</p>
						<p className="text-sm">
							현재까지 <span className="font-nexonBold">{imageArray.length}</span>개의 행운카드를
							받았어요💌
						</p>
					</div>
					<div className="w-full max-w-[480px] mx-auto mt-8">
						<Slider {...settings}>
							{imageArray.map((card, index) => (
								<Card
									key={index}
									width={250}
									height={445}
									path={card.image}
									effect={card.effect}
								/>
							))}
						</Slider>
					</div>
					<div className="flex justify-center gap-2 py-12">
						{accessToken ? (
							<Button
								text="나의 카드북<br/>보러가기"
								color="purple"
								size="short"
								font="both"
								shadow="purple"
								onClick={() => {
									router.push("/book");
								}}
							/>
						) : (
							<Button
								text="나의 카드북<br/>만들기"
								color="purple"
								size="short"
								font="both"
								shadow="purple"
								onClick={() => openModal()}
							/>
						)}
						<Button
							text="행운카드<br/>만들어주기"
							color="green"
							size="short"
							font="both"
							shadow="green"
							onClick={() => {
								router.push(`/create?type=require&id=${id}`);
							}}
						/>
					</div>
				</>
			)}
			<div
				className={`transition-opacity duration-300
					${isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
			>
				<Modal>
					<p className="text-sm text-center mb-6">
						로그인이 필요한 서비스입니다. <br />
						로그인하시겠습니까?
					</p>
					<div className="flex justify-evenly">
						<Button
							text="아니오"
							color="gray"
							size="small"
							font="small"
							shadow="gray"
							onClick={() => closeModal()}
						/>
						<Button
							text="예"
							color="green"
							size="small"
							font="small"
							shadow="green"
							onClick={() => {
								router.push("/login");
							}}
						/>
					</div>
				</Modal>
			</div>
		</div>
	);
};

export default Share;
