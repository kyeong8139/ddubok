"use client";

import React, { useState, useContext } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import Image from "next/image";

import { saveCard } from "@lib/api/card";
import { useCardStore } from "@store/card-store";
import Card from "@components/card/card";
import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";

import { LinkSimple } from "@phosphor-icons/react";

const CardDetail = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const type = searchParams?.get("type");
	const { selectedImage, letterContent, cardId } = useCardStore();
	const [isLoading, setIsLoading] = useState(false);
	const { isModalOpen, openModal } = useContext(ModalContext);

	// const { selectedImage, letterContent, cardId, setCardId } = useCardStore();
	// useEffect(() => {
	// 	// 임시데이터
	// 	if (!cardId) {
	// 		setCardId(609);
	// 	}
	// }, []);

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
		return `${baseUrl}/card?id=${encryptedId}`;
	};

	const handleCopyLink = async () => {
		const shareUrl = getShareUrl();
		try {
			await navigator.clipboard.writeText(shareUrl);
			alert("링크가 복사되었습니다.");
		} catch (error) {
			console.error("링크 복사 실패:", error);
			alert("링크 복사에 실패했습니다.");
		}
	};

	const handleShareKakao = () => {
		// if (window.Kakao) {
		// 	window.Kakao.Share.sendDefault({
		// 		objectType: "feed",
		// 		content: {
		// 			title: "행운카드",
		// 			description: "행운카드가 도착했어요!",
		// 			imageUrl: cardImage,
		// 			link: {
		// 				mobileWebUrl: currentUrl,
		// 				webUrl: currentUrl,
		// 			},
		// 		},
		// 		buttons: [
		// 			{
		// 				title: "자세히 보기",
		// 				link: {
		// 					mobileWebUrl: currentUrl,
		// 					webUrl: currentUrl,
		// 				},
		// 			},
		// 		],
		// 	});
		// }
	};

	const handleShareFacebook = () => {
		const shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(currentUrl)}`;
		window.open(shareUrl, "_blank", "width=600,height=400");
	};

	const handleShareX = () => {
		const text = "행운카드가 도착했어요!";
		const shareUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(text)}&url=${encodeURIComponent(
			currentUrl,
		)}`;
		window.open(shareUrl, "_blank", "width=600,height=400");
	};

	const handleSaveCard = async () => {
		if (!cardId) {
			alert("카드 정보가 없습니다.");
			return;
		}

		if (isLoading) return;

		try {
			setIsLoading(true);
			await saveCard(cardId);
			alert("카드가 보관되었습니다.");
		} catch (error) {
			console.error("카드 보관 중 오류 발생:", error);
			alert("카드 보관에 실패했습니다. 다시 시도해주세요.");
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
				})
				.catch((error) => {
					console.error("이미지 다운로드 중 오류 발생:", error);
					alert("이미지 다운로드에 실패했습니다.");
				});
		}
	};

	return (
		<div className="flex flex-col items-center w-full">
			<div className="text-white font-nexonBold text-2xl mt-10">{titleText}</div>
			<div className="mt-8">
				<Card
					width={270}
					height={478}
					image={cardImage}
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

			<div className="mt-10 flex flex-row gap-4 w-full justify-center">
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
			{isModalOpen && (
				<Modal>
					<div className="flex flex-col items-center">
						<div className="w-full ">
							<h2 className="text-2xl font-nexonBold text-black">공유</h2>
						</div>
						<div className="flex gap-8 mt-6">
							<button
								onClick={handleShareKakao}
								className="flex flex-col items-center"
							>
								<div className="w-12 h-12 relative mb-2">
									<Image
										src="/assets/kakao-circle.png"
										alt="Kakao"
										fill
										className="rounded-full"
									/>
								</div>
								<span className="text-black font-nexonLight">카카오톡</span>
							</button>
							<button
								onClick={handleShareFacebook}
								className="flex flex-col items-center"
							>
								<div className="w-12 h-12 relative mb-2">
									<Image
										src="/assets/facebook-circle.png"
										alt="Facebook"
										fill
										className="rounded-full"
									/>
								</div>
								<span className="text-black font-nexonLight">페이스북</span>
							</button>
							<button
								onClick={handleShareX}
								className="flex flex-col items-center"
							>
								<div className="w-12 h-12 relative mb-2">
									<Image
										src="/assets/x-circle.jpg"
										alt="X (Twitter)"
										fill
										className="rounded-full"
									/>
								</div>
								<span className="text-black font-nexonLight">X</span>
							</button>
						</div>
						<div className="w-full mb-4 flex items-center gap-2 mt-6 justify-center">
							<div className="flex-1 bg-gray-100 p-3 rounded-lg text-gray-600 font-nexonLight h-[56px] flex items-center overflow-hidden">
								<div className="truncate w-full">{getShareUrl()}</div>
							</div>
							<button
								onClick={handleCopyLink}
								className="whitespace-nowrap px-3 h-[56px] bg-gray-300 text-white rounded-lg font-nexonRegular hover:bg-gray-400 transition-colors flex items-center justify-center"
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
		</div>
	);
};

export default CardDetail;
