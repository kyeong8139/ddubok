"use client";

import React, { useEffect, useState, useContext } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import toast from "react-hot-toast";

import { decryptCardId } from "@lib/utils/crypto";
import { getCard, saveCard } from "@lib/api/card";
import Button from "@components/button/button";
import Card from "@components/card/card";
import Loading from "@components/common/loading";
import Modal from "@components/common/modal";
import { ModalContext } from "@context/modal-context";
import useAuthToken from "@lib/utils/tokenUtils";
import { ICardDto } from "@interface/components/card";

const SharedCard = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const encryptedId = searchParams?.get("id");
	const cardId = encryptedId ? decryptCardId(encryptedId) : null;
	const { accessToken } = useAuthToken();
	const [cardData, setCardData] = useState<ICardDto | null>(null);
	const [isLoading, setIsLoading] = useState(true);
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const [isTallScreen, setIsTallScreen] = useState(false);

	useEffect(() => {
		const checkScreenHeight = () => {
			setIsTallScreen(window.innerHeight >= 740);
		};

		checkScreenHeight();

		window.addEventListener("resize", checkScreenHeight);

		return () => window.removeEventListener("resize", checkScreenHeight);
	}, []);

	const cardSize = {
		width: isTallScreen ? 280 : 255,
		height: isTallScreen ? 495 : 451,
	};

	const handleSaveCard = async () => {
		if (!cardId) {
			toast.error("카드 정보가 없습니다");
			return;
		}

		if (isLoading) return;

		if (!accessToken) {
			openModal();
			return;
		}

		try {
			setIsLoading(true);
			await saveCard(cardId);
			toast.success("카드가 보관되었습니다.");
		} catch (error: any) {
			console.error("카드 보관 중 오류 발생:", error);
			if (error.response?.data?.code === "703") {
				toast.error("이미 받은 카드입니다");
			} else {
				toast.error("카드 보관에 실패했습니다");
			}
		} finally {
			setIsLoading(false);
		}
	};

	const handleLoginClick = () => {
		const currentPath = window.location.pathname + window.location.search;
		localStorage.setItem("redirectAfterLogin", currentPath);
		router.push("/login");
	};

	useEffect(() => {
		const fetchCardData = async () => {
			if (!cardId) {
				router.replace("/error");
				return;
			}

			try {
				const response = await getCard(cardId);

				if (response.code === "200") {
					setCardData(response.data);
				} else {
					throw new Error(response.message || "카드를 불러올 수 없습니다.");
				}
			} catch (error) {
				console.error("카드 데이터 불러오기 실패:", error);
				router.replace("/error");
			} finally {
				setIsLoading(false);
			}
		};

		fetchCardData();
	}, [cardId, router]);

	if (!cardId) {
		return null;
	}

	if (isLoading) {
		return (
			<div className="flex w-full h-screen items-center justify-center">
				<Loading />
			</div>
		);
	}

	return (
		<div className="flex flex-col items-center w-full">
			<div className="text-white font-nexonBold text-xl mt-2 text-center">
				{cardData?.writerName}님이
				<br /> 보낸 행운카드
			</div>
			<div className="mt-2">
				<Card
					width={cardSize.width}
					height={cardSize.height}
					path={cardData?.path || ""}
					content={cardData?.content || ""}
					state={cardData?.state}
					openedAt={cardData?.openedAt}
					effect={0}
					flip={true}
				/>
			</div>

			<div className="mt-6 mb-6 flex flex-row gap-4 w-full justify-center">
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
					text="홈으로<br/>이동하기"
					color="green"
					size="short"
					font="both"
					shadow="green"
					onClick={() => router.push("/")}
				/>
			</div>

			{isModalOpen && (
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
								closeModal();
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
	);
};

export default SharedCard;
