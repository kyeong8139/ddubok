"use client";

import React, { useState, useContext } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import Image from "next/image";

import { saveCard, getCard } from "@lib/api/card";
import { useCardStore } from "@store/card-store";
import Card from "@components/card/card";
import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import useAuthToken from "@lib/utils/tokenUtils";

import { LinkSimple } from "@phosphor-icons/react";
import { toast } from "react-hot-toast";
import useKakaoInit from "../../../hooks/useKakaoInit";

const CardDetail = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const type = searchParams?.get("type");
	const { accessToken } = useAuthToken();
	const { selectedImage, letterContent, cardId } = useCardStore();
	const [isLoading, setIsLoading] = useState(false);
	const { isModalOpen, openModal } = useContext(ModalContext);
	const shareLink = process.env.NEXT_PUBLIC_SHARE_URL;
	const [showLoginModal, setShowLoginModal] = useState(false);

	const titleText =
		type === "normal" ? "행운카드를 공유해보세요" : type === "require" ? "행운카드가 배달중이예요" : "행운카드";

	const cardImage = selectedImage || "";
	const currentUrl = typeof window !== "undefined" ? window.location.href : "";

	const encryptCardId = (cardId: number) => {
		const salt = process.env.NEXT_PUBLIC_SALT_KEY;
		const dataToEncode = `${salt}-${cardId}`;

		if (typeof window !== "undefined") {
			return btoa(dataToEncode);
		}
		return "";
	};

	const getShareUrl = () => {
		if (!cardId) return "";

		const encryptedId = encryptCardId(cardId);
		const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || window.location.origin;
		return `${shareLink}/card?id=${encryptedId}`;
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

		if (!cardId) {
			toast.error("카드 정보가 없습니다");
			return;
		}

		try {
			const response = await getCard(cardId);

			if (response.code !== "200") {
				throw new Error(response.message || "카드를 불러올 수 없습니다.");
			}

			const fullShareUrl = getShareUrl();
			const splitKey = process.env.NEXT_PUBLIC_SPLIT_KEY;
			const shareUrl = splitKey ? fullShareUrl.split(splitKey)[1] : fullShareUrl;

			window.Kakao.Share.sendCustom({
				templateId: 113932,
				templateArgs: {
					LINK_URL: shareUrl,
					IMAGE_URL: response.data.path,
					TITLE: "행운카드가 도착했어요! 🍀",
					DESCRIPTION: "접속해서 받아가세요",
				},
			});
		} catch (error) {
			console.error("카카오톡 공유 중 오류 발생:", error);
			toast.error("카카오톡 공유에 실패했습니다");
		}
	};

	// const handleShareInstagram = () => {
	// 	// const shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(currentUrl)}`;
	// 	// window.open(shareUrl, "_blank", "width=600,height=400");
	// 	if (/Android|iPhone|iPad|iPod/i.test(navigator.userAgent)) {
	// 		const shareUrl = getShareUrl();
	// 		const instagramUrl = `instagram://story-camera`;

	// 		window.location.href = instagramUrl;
	// 	} else {
	// 		toast.error("인스타그램은 모바일에서만 가능합니다");
	// 	}
	// };

	// const handleShareInstagram = async () => {
	// 	const isMobile = /Android|iPhone|iPad|iPod/i.test(navigator.userAgent);

	// 	if (!isMobile) {
	// 		toast.error("인스타그램 스토리 공유는 모바일에서만 가능합니다");
	// 		return;
	// 	}

	// 	try {
	// 		const response = await fetch(cardImage);
	// 		const blob = await response.blob();

	// 		const shareData = {
	// 			files: [
	// 				new File([blob], "fortune-card.png", {
	// 					type: "image/png",
	// 				}),
	// 			],
	// 		};

	// 		if (navigator.canShare && navigator.canShare(shareData)) {
	// 			try {
	// 				await navigator.share(shareData);
	// 				toast.success("인스타그램 스토리에 공유할 수 있습니다");
	// 			} catch (err) {
	// 				if (err instanceof Error) {
	// 					if (err.name === "AbortError") {
	// 						return;
	// 					}
	// 					throw err;
	// 				}
	// 				throw new Error("Unknown error occurred");
	// 			}
	// 		} else {
	// 			const instagramUrl = `instagram://story-camera`;

	// 			const shareUrl = getShareUrl();
	// 			await navigator.clipboard.writeText(shareUrl);

	// 			window.location.href = instagramUrl;
	// 			toast.success("인스타그램이 열립니다. 카메라에서 최근 저장된 이미지를 선택해주세요");
	// 		}
	// 	} catch (err) {
	// 		const error = err instanceof Error ? err : new Error("Unknown error occurred");
	// 		console.error("인스타그램 공유 중 오류 발생:", error);
	// 		toast.error("인스타그램 공유에 실패했습니다");
	// 	}
	// };

	const handleShareX = () => {
		const shareUrl = getShareUrl();
		const text = `행운카드가 도착했어요! 접속해서 받아가세요🍀`;

		const twitterUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(text)}&url=${encodeURIComponent(
			shareUrl,
		)}`;

		window.open(twitterUrl, "_blank", "width=600,height=400");
	};

	const handleSaveCard = async () => {
		if (!cardId) {
			toast.error("카드 정보가 없습니다");
			return;
		}

		if (isLoading) return;

		if (!accessToken) {
			setShowLoginModal(true);
			return;
		}

		try {
			setIsLoading(true);
			await saveCard(cardId);
			toast.success("카드가 보관되었습니다.");
		} catch (error) {
			console.error("카드 보관 중 오류 발생:", error);
			toast.error("카드 보관에 실패했습니다");
		} finally {
			setIsLoading(false);
		}
	};

	const handleDownloadImage = () => {
		if (!cardImage) return;

		if (cardImage.startsWith("data:image")) {
			const link = document.createElement("a");
			link.href = cardImage;
			link.download = "fortune-card.png";
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
			toast.success("이미지 다운에 성공하였습니다.");
		} else {
			fetch(cardImage)
				.then((response) => response.blob())
				.then((blob) => {
					const url = window.URL.createObjectURL(blob);
					const link = document.createElement("a");
					link.href = url;
					link.download = "fortune-card.png";
					document.body.appendChild(link);
					link.click();
					window.URL.revokeObjectURL(url);
					document.body.removeChild(link);
					toast.success("이미지 다운에 성공하였습니다.");
				})

				.catch((error) => {
					console.error("이미지 다운로드 중 오류 발생:", error);
					toast.error("이미지 다운로드에 실패했습니다");
				});
		}
	};

	const handleLoginClick = () => {
		const currentPath = window.location.pathname + window.location.search;
		localStorage.setItem("redirectAfterLogin", currentPath);
		router.push("/login");
	};

	return (
		<div>
			<div className="flex flex-col items-center w-full">
				<div className="text-white font-nexonBold text-2xl mt-10">{titleText}</div>
				<div className="mt-8">
					<Card
						width={270}
						height={478}
						path={cardImage}
						content={letterContent}
						effect={0}
						flip={true}
					/>
				</div>
				<button
					onClick={handleDownloadImage}
					className="mt-2 text-white font-nexonLight text-lg hover:underline cursor-pointer"
				>
					이미지 저장
				</button>

				<div className="mt-4 flex flex-row gap-4 w-full justify-center mb-8">
					{type === "normal" ? (
						<>
							<Button
								text="행운카드<br/>보관하기"
								color="purple"
								size="short"
								font="both"
								shadow="purple"
								onClick={handleSaveCard}
								disabled={isLoading}
							/>
							<Button
								text="행운카드<br/>공유하기"
								color="green"
								size="short"
								font="both"
								shadow="green"
								onClick={() => openModal()}
							/>
						</>
					) : (
						type === "require" && (
							<>
								<Button
									text="홈으로"
									color="gradient"
									size="long"
									font="regular"
									shadow="gradient"
									onClick={() => router.push("/")}
								/>
							</>
						)
					)}
				</div>
				<button
					onClick={() => router.push("/")}
					className="mt-2 mb-8 text-white font-nexonLight text-sm hover:underline cursor-pointer"
				>
					홈으로
				</button>
				{isModalOpen && (
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
										<Image
											src="/assets/kakao-circle.png"
											alt="Kakao"
											fill
											className="rounded-full"
										/>
									</div>
									<span className="text-black font-nexonLight text-sm sm:text-base">카카오톡</span>
								</button>
								{/* <button
									onClick={handleShareInstagram}
									className="flex flex-col items-center"
								>
									<div className="w-10 sm:w-12 h-10 sm:h-12 relative mb-2">
										<Image
											src="/assets/insta-circle.png"
											alt="Instagram"
											fill
											className="rounded-full"
										/>
									</div>
									<span className="text-black font-nexonLight text-sm sm:text-base">인스타그램</span>
								</button> */}
								<button
									onClick={handleShareX}
									className="flex flex-col items-center"
								>
									<div className="w-10 sm:w-12 h-10 sm:h-12 relative mb-2">
										<Image
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

				{showLoginModal && (
					<Modal>
						<h3 className="text-lg font-nexonBold mb-4">로그인이 필요한 기능입니다</h3>
						<p className="text-gray-600 mb-6 font-nexonRegular">모든 작업내용은 유지됩니다.</p>
						<div className="flex justify-end gap-2">
							<Button
								text="취소"
								color="gray"
								size="small"
								font="regular"
								shadow="gray"
								onClick={() => {
									setShowLoginModal(false);
								}}
							/>
							<Button
								text="로그인"
								color="green"
								size="small"
								font="regular"
								shadow="green"
								onClick={handleLoginClick}
							/>
						</div>
					</Modal>
				)}
			</div>
		</div>
	);
};

export default CardDetail;
