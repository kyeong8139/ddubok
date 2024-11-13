"use client";

import NextImage from "next/image";
import { useRouter } from "next/navigation";
import { useContext, useEffect, useMemo, useState } from "react";

import Button from "@components/button/button";
import Card from "@components/card/card";
import Loading from "@components/common/loading";
import Modal from "@components/common/modal";
import useAuthToken from "@lib/utils/tokenUtils";
import { getTokenInfo } from "@lib/utils/authUtils";
import { selectUser } from "@lib/api/user-api";

import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { ModalContext } from "@context/modal-context";
import { LinkSimple } from "@phosphor-icons/react";
import toast from "react-hot-toast";
import useKakaoInit from "src/hooks/useKakaoInit";

const Home = () => {
	const router = useRouter();
	const shareLink = process.env.NEXT_PUBLIC_SHARE_URL;
	const { accessToken, isTokenReady } = useAuthToken();
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const [isLoading, setIsLoading] = useState(true);
	const decodedToken = accessToken ? getTokenInfo(accessToken) : null;
	const [user, setUser] = useState<{ memberId: number; nickname: string; role: string } | null>(null);

	const isPageReady = isLoading || !isTokenReady;

	const settings = {
		infinite: true,
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
		centerMode: true,
		variableWidth: true,
		arrows: false,
		autoplay: true,
		autoplaySpeed: 2000,
		adaptiveHeight: true,
	};

	const cardImages = useMemo(
		() => [
			// { image: "/assets/template/kkm-card.png", effect: 0 },
			// { image: "/assets/template/psh-card.jpg", effect: 0 },
			// { image: "/assets/template/kkm-card-2.png", effect: 0 },
			// { image: "/assets/template/lbk-card.png", effect: 0 },
			// { image: "/assets/template/kkm-card-3.png", effect: 0 },
			// { image: "/assets/template/kde-card.jpg", effect: 0 },
			// { image: "/assets/template/kde-card-2.jpg", effect: 0 },
			{ image: "/assets/template/template (1).png", effect: 0 },
			{ image: "/assets/template/template (2).png", effect: 0 },
			{ image: "/assets/template/template (3).png", effect: 0 },
			{ image: "/assets/template/template (4).png", effect: 0 },
			{ image: "/assets/template/template (5).png", effect: 0 },
			{ image: "/assets/template/template (6).png", effect: 0 },
		],
		[],
	);

	useEffect(() => {
		const getUser = async () => {
			if (decodedToken && isTokenReady) {
				try {
					const response = await selectUser();
					setUser({
						memberId: decodedToken.memberId,
						nickname: response.data.data.nickname,
						role: decodedToken.role,
					});
				} catch (error) {
					console.error(error);
				}
			}
		};

		getUser();
	}, [accessToken, isTokenReady]);

	const encryptCardId = (cardId: number) => {
		const salt = process.env.NEXT_PUBLIC_SALT_KEY;
		const dataToEncode = `${salt}-${cardId}`;

		if (typeof window !== "undefined") {
			return btoa(dataToEncode);
		}
		return "";
	};

	const getShareUrl = () => {
		if (!user?.memberId) return "";

		const encryptedId = encryptCardId(user?.memberId);
		return `${shareLink}/share?id=${encryptedId}`;
	};

	const handleCopyLink = async () => {
		const shareUrl = getShareUrl();
		try {
			await navigator.clipboard.writeText(shareUrl);
			toast.success("링크가 복사되었습니다");
		} catch (error) {
			console.error("링크 복사 실패:", error);
			toast.error("링크 복사에 실패했습니다");
		}
	};

	const isKakaoInitialized = useKakaoInit();

	const handleShareKakao = async () => {
		if (!window.Kakao) {
			toast.error("카카오톡 SDK가 로드되지 않았습니다");
			return;
		}

		if (!isKakaoInitialized) {
			toast.error("카카오톡 초기화 중입니다");
			return;
		}

		if (!user?.memberId) {
			toast.error("카드 정보가 없습니다");
			return;
		}

		try {
			const fullShareUrl = getShareUrl();
			const splitKey = process.env.NEXT_PUBLIC_SPLIT_KEY;
			const shareUrl = splitKey ? fullShareUrl.split(splitKey)[1] : fullShareUrl;

			window.Kakao.Share.sendCustom({
				templateId: 113841,
				templateArgs: {
					LINK_URL: shareUrl,
					IMAGE_URL: "https://ddubok.s3.ap-northeast-2.amazonaws.com/common/kakao.png",
					TITLE: `${user?.nickname}님이 행운카드를 요청했어요!`,
					DESCRIPTION: "응원을 담은 카드를 만들어주세요🍀",
				},
			});
		} catch (error) {
			console.error("카카오톡 공유 중 오류 발생:", error);
			toast.error("카카오톡 공유에 실패했습니다");
		}
	};

	// const handleShareInstagram = () => {
	// 	const shareText = "${user?.nickname}님이 행운카드를 요청했어요! 응원을 담은 카드를 만들어주세요🍀";
	// 	const shareUrl = getShareUrl();

	// 	const isIOS = /iPhone|iPad|iPod/.test(navigator.userAgent);

	// 	if (isIOS) {
	// 		window.location.href = `instagram://stories?text=${encodeURIComponent(
	// 			shareText,
	// 		)}&content_url=${encodeURIComponent(shareUrl)}`;
	// 	} else {
	// 		window.location.href = `intent://instagram.com/stories?text=${encodeURIComponent(
	// 			shareText,
	// 		)}&content_url=${encodeURIComponent(shareUrl)}#Intent;package=com.instagram.android;scheme=https;end`;
	// 	}
	// };

	const handleShareX = () => {
		const shareUrl = getShareUrl();
		const text = `${user?.nickname}님이 행운카드를 요청했어요! 응원이 담긴 카드를 만들어주세요🍀`;

		const twitterUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(text)}&url=${encodeURIComponent(
			shareUrl,
		)}`;

		window.open(twitterUrl, "_blank", "width=600,height=400");
	};

	useEffect(() => {
		if (accessToken) {
			const redirectPath = localStorage.getItem("redirectAfterLogin");
			if (redirectPath) {
				localStorage.removeItem("redirectAfterLogin");
				router.push(redirectPath);
			}
		}
	}, [accessToken, router]);

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

	return (
		<div
			id="home"
			className="font-nexonRegular text-white"
		>
			{isPageReady ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : (
				<>
					<div id="home-01">
						<div className="flex flex-col items-center pt-8">
							<div className="absolute">
								<p>
									<span className="font-nexonBold">당신</span>을 위한 행운 배달부🍀
								</p>
							</div>
							<NextImage
								src="/assets/ddubok.png"
								alt="ddubok"
								width={136}
								height={136}
								className="pt-2"
							/>
							{/* <p className="font-nexonLight text-sm mb-2">
								수능 이벤트 기간: <span>11.06 - 11.13</span>
							</p> */}
							<p className="font-nexonLight text-xs text-center">
								행운카드 뒷면의 메세지는 <br />
								수신 후 24시간이 지나면 확인 가능합니다.
							</p>
						</div>
						<div className="w-full max-w-[480px] mx-auto mt-8">
							<Slider {...settings}>
								{cardImages.map((card, index) => (
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
						<div className="flex justify-center gap-2 pt-12 pb-20">
							<Button
								text="내 행운카드<br/>조르기"
								color="purple"
								size="short"
								font="both"
								shadow="purple"
								onClick={openModal}
							/>
							<Button
								text="행운카드<br/>만들기"
								color="green"
								size="short"
								font="both"
								shadow="green"
								onClick={() => {
									router.push("/create?type=normal");
								}}
							/>
						</div>
					</div>
					<div
						id="home-02"
						className="bg-ddubokPurple"
					>
						<div className="relative flex justify-center pt-20 pb-4">
							<NextImage
								src="/assets/purple_step.svg"
								alt="보라색 말풍선"
								width={64}
								height={32}
							/>
							<span className="absolute top-21 text-xs font-nexonBold">STEP 1</span>
						</div>
						<h1 className="font-pyeongchang text-2xl text-black text-center leading-tight mb-6">
							응원이 필요한 친구들에게
							<br />
							행운카드 제작하기
						</h1>
						<p className="text-black text-center mb-12 text-sm">
							삶에 지친 친구들을 위해
							<br />
							<span className="font-nexonBold">행운카드</span>를 만들어 보내주세요!
							<br />
						</p>
						<div className="flex justify-center pb-20">
							<div className="relative w-[calc(100%-64px)] h-64 overflow-hidden rounded-lg">
								<NextImage
									src="/assets/image-step1.png"
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
						<div className="relative flex justify-center pt-20 pb-4">
							<NextImage
								src="/assets/green_step.svg"
								alt="청록색 말풍선"
								width={64}
								height={32}
							/>
							<span className="absolute top-21 text-xs font-nexonBold">STEP 2</span>
						</div>
						<h1 className="font-pyeongchang text-2xl text-black text-center leading-tight mb-6">
							응원이 필요한 나를 위해
							<br />
							행운카드 조르기
						</h1>
						<p className="text-black text-center mb-12 text-sm">
							삶에 지친 나를 위해
							<br />
							친구들에게 <span className="font-nexonBold">행운카드</span>를 요청하세요!
						</p>
						<div className="flex justify-center pb-20">
							<div className="relative w-[calc(100%-64px)] h-64 overflow-hidden rounded-lg">
								<NextImage
									src="/assets/image-step2-ver2.png"
									alt="행운카드 공유하는 모습"
									fill
									style={{ objectFit: "cover" }}
								/>
							</div>
						</div>
					</div>
					<div
						id="home-04"
						className="my-16 flex flex-col items-center"
					>
						<p className="mb-6">나의 행운카드를 모아보고 싶다면?</p>
						{accessToken ? (
							<Button
								text="행운 카드북 보러가기"
								color="gradient"
								size="long"
								font="bold"
								shadow="gradient"
								onClick={() => {
									(router as any).push("/book");
								}}
							/>
						) : (
							<Button
								text="회원가입하고 이용하기"
								color="gradient"
								size="long"
								font="bold"
								shadow="gradient"
								onClick={() => {
									(router as any).push("/login");
								}}
							/>
						)}
					</div>
					<div
						id="home-05"
						className="bg-ddubokGray text-black px-8 py-12 "
					>
						<h2 className="font-nexonBold mb-4">유의사항</h2>
						<ul className="list-inside list-disc space-y-2">
							<li
								className="text-xs text-justify"
								style={{ textIndent: "-1rem", paddingLeft: "1rem" }}
							>
								원활한 이용을 위해서 회원가입이 권장됩니다.
							</li>
							<li
								className="text-xs text-justify"
								style={{ textIndent: "-1rem", paddingLeft: "1rem" }}
							>
								부적절한 사진 및 내용을 첨부한 행운카드를 생성할 시 서비스 이용에 불이익 및 법적 조치를
								받을 수 있습니다.
							</li>
							<li
								className="text-xs text-justify"
								style={{ textIndent: "-1rem", paddingLeft: "1rem" }}
							>
								생성한 행운카드 이미지는 비영리 목적으로 사용할 수 있습니다.
							</li>
							<li
								className="text-xs text-justify"
								style={{ textIndent: "-1rem", paddingLeft: "1rem" }}
							>
								생성한 행운카드 이미지를 영리 목적으로 이용하거나, 당사에서 제공하는 범위 외에서 복제,
								수정, 변형, 2차적 저작물 작성등의 방법으로 이용할 수 없습니다.
							</li>
							<li
								className="text-xs text-justify"
								style={{ textIndent: "-1rem", paddingLeft: "1rem" }}
							>
								행운카드 생성에 사용하는 이미지의 저작권에 대한 책임은 사용자 본인에게 있습니다.
							</li>
						</ul>
					</div>

					{/* 모달 */}
					{isModalOpen && accessToken && (
						<Modal>
							<div className="flex flex-col items-center max-w-[360px] w-full">
								<div className="w-full">
									<h2 className="text-2xl font-nexonBold text-black">공유</h2>
								</div>
								<div className="flex gap-4 sm:gap-8 mt-6">
									<button
										onClick={handleShareKakao}
										className="flex flex-col items-center"
									>
										<div className="w-10 sm:w-12 h-10 sm:h-12 relative mb-2">
											<NextImage
												src="/assets/kakao-circle.png"
												alt="Kakao"
												fill
												className="rounded-full"
											/>
										</div>
										<span className="text-black font-nexonLight text-sm sm:text-base">
											카카오톡
										</span>
									</button>
									<button
										onClick={handleShareX}
										className="flex flex-col items-center"
									>
										<div className="w-10 sm:w-12 h-10 sm:h-12 relative mb-2">
											<NextImage
												src="/assets/x-circle.png"
												alt="X"
												fill
												className="rounded-full"
											/>
										</div>
										<span className="text-black font-nexonLight text-sm sm:text-base">X</span>
									</button>
								</div>
								<div className="w-full mb-4 flex items-center gap-2 mt-6 justify-center ">
									<div className="w-full max-w-[240px] sm:max-w-[300px] bg-gray-100 p-3 rounded-lg text-gray-600 font-nexonLight h-[50px] flex items-center overflow-hidden">
										<div className="truncate w-full text-sm sm:text-base">{getShareUrl()}</div>
									</div>
									<button
										onClick={handleCopyLink}
										className="whitespace-nowrap px-3 h-[50px] w-[50px] bg-gray-300 text-white rounded-lg font-nexonRegular hover:bg-gray-400 transition-colors flex items-center justify-center flex-shrink-0"
									>
										<LinkSimple
											size={16}
											color="#black"
										/>
									</button>
								</div>
							</div>
						</Modal>
					)}

					{isModalOpen && !accessToken && (
						<Modal>
							<h3 className="text-black text-lg font-nexonBold mb-4">로그인이 필요한 기능입니다</h3>
							<p className="text-gray-600 mb-6 font-nexonRegular">로그인하고 내 카드북을 만들어보세요!</p>
							<div className="flex justify-end gap-2">
								<Button
									text="취소"
									color="gray"
									size="small"
									font="regular"
									shadow="gray"
									onClick={closeModal}
								/>
								<Button
									text="로그인"
									color="green"
									size="small"
									font="regular"
									shadow="green"
									onClick={() => {
										closeModal();
										router.push("/login");
									}}
								/>
							</div>
						</Modal>
					)}
				</>
			)}
		</div>
	);
};

export default Home;
