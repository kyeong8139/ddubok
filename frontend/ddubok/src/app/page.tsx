"use client";

import Image from "next/image";

import Button from "@components/button/button";
import Card from "@components/card/card";

import { Clover } from "@phosphor-icons/react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

export default function Home() {
	const cardImages = [
		{ image: "/assets/examplCard1.png", effect: 0 },
		{ image: "/assets/examplCard2.png", effect: 0 },
		{ image: "/assets/temp1.jpg", effect: 0 },
		{ image: "/assets/temp2.jpg", effect: 0 },
	];
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

	return (
		<div
			id="home"
			className="font-nexonRegular text-white"
		>
			<div id="home-01">
				<div className="flex flex-col items-center pt-8">
					<div className="flex flex-row items-center gap-1 absolute">
						<p>
							<span className="font-nexonBold">수능을 앞둔 친구</span>를 위한 행운 배달부
						</p>
						<Clover
							size={20}
							color="white"
						/>
					</div>
					<Image
						src="/assets/ddubok.png"
						alt="ddubok"
						width={136}
						height={136}
						className="pt-2"
					/>
					<p className="font-nexonLight text-sm">
						이벤트 기간: <span>11.01 - 11.15</span>
					</p>
				</div>
				<div className="w-full max-w-[480px] mx-auto mt-8">
					<Slider {...settings}>
						{cardImages.map((card, index) => (
							<Card
								key={index}
								width={260}
								height={443}
								image={card.image}
								effect={card.effect}
							/>
						))}
					</Slider>
				</div>
				<div className="flex justify-center gap-2 pt-12 pb-16">
					<Button
						text="행운 카드<br/>만들어주기"
						color="purple"
						size="short"
						font="both"
						shadow="purple"
						onClick={() => {}}
					/>
					<Button
						text="내 행운 카드<br/>요청하기"
						color="green"
						size="short"
						font="both"
						shadow="green"
						onClick={() => {}}
					/>
				</div>
			</div>
			<div
				id="home-02"
				className="bg-ddubokPurple"
			>
				<div className="relative flex justify-center pt-16 pb-4">
					<Image
						src="/assets/purple_step.svg"
						alt="보라색 말풍선"
						width={64}
						height={32}
					/>
					<span className="absolute top-17 text-xs font-nexonBold">STEP 1</span>
				</div>
				<h1 className="font-pyeongchang text-[2rem] text-black text-center leading-tight mb-6">
					수험생 친구들을 위한
					<br />
					행운 카드 제작하기
				</h1>
				<p className="text-black text-center mb-12">
					수능으로 지친 친구들을 위해
					<br />
					<span className="font-nexonBold">행운 카드</span>를 만들어 보내주세요!
				</p>
				<div className="flex justify-center pb-16">
					<div className="relative w-[calc(100%-64px)] h-64 overflow-hidden rounded-lg">
						<Image
							src="/assets/temp1.jpg"
							alt="행운카드 목록"
							fill
							style={{ objectFit: "cover" }}
						/>
					</div>
				</div>
			</div>
			<div
				id="home-03"
				className="bg-ddubokGreen"
			>
				<div className="relative flex justify-center pt-16 pb-4">
					<Image
						src="/assets/green_step.svg"
						alt="청록색 말풍선"
						width={64}
						height={32}
					/>
					<span className="absolute top-17 text-xs font-nexonBold">STEP 2</span>
				</div>
				<h1 className="font-pyeongchang text-[2rem] text-black text-center leading-tight mb-6">
					수험생인 나를 위한
					<br />
					행운 카드 조르기
				</h1>
				<p className="text-black text-center mb-12">
					수능으로 지친 나를 위해
					<br />
					친구들에게 <span className="font-nexonBold">행운 카드</span>를 요청하세요!
				</p>
				<div className="flex justify-center pb-16">
					<div className="relative w-[calc(100%-64px)] h-64 overflow-hidden rounded-lg">
						<Image
							src="/assets/temp2.jpg"
							alt="행운카드 공유하는 모습"
							fill
							style={{ objectFit: "cover" }}
						/>
					</div>
				</div>
			</div>
			<div
				id="home-04"
				className="my-12 flex flex-col items-center"
			>
				<p className="mb-4">나의 행운 카드를 모아보고 싶다면?</p>
				<Button
					text="회원가입하고 이용하기"
					color="gradient"
					size="long"
					font="bold"
					shadow="gradient"
					onClick={() => {}}
				/>
			</div>
			<div
				id="home-05"
				className="bg-ddubokGray text-black p-8 "
			>
				<h2 className="font-nexonBold mb-2">유의사항</h2>
				<ul>
					<li className="text-xs text-justify mb-2 list-inside list-disc">
						원활한 이용을 위해서 회원가입이 권장됩니다.
					</li>
					<li className="text-xs text-justify mb-2 list-inside list-disc">
						부적절한 사진 및 내용을 첨부한 행운 카드를 제작할 시 서비스 이용에 불이익 및 법적 조치를 받을 수
						있습니다.
					</li>
					<li className="text-xs text-justify mb-2 list-inside list-disc">
						생성한 이미지는 비영리 목적으로 사용할 수 있습니다.
					</li>
					<li className="text-xs text-justify mb-2 list-inside list-disc">
						생성한 이미지를 영리 목적으로 이용하거나, 당사에서 제공하는 범위 외에서 복제, 수정, 변형, 2차적
						저작물 작성등의 방법으로 이용할 수 없습니다.
					</li>
				</ul>
			</div>
		</div>
	);
}
