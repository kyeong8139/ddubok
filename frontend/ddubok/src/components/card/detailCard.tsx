import { useContext, useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import Card from "@components/card/card";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import { IDetailCardDto } from "@interface/components/card";
import { deleteCard } from "@lib/api/card-load-api";

import { DotsThreeCircleVertical, Siren, Star } from "@phosphor-icons/react";
import toast from "react-hot-toast";

const DetailCard = ({ id, writerName, state, content, path, effect }: IDetailCardDto) => {
	const router = useRouter();
	const { closeModal } = useContext(ModalContext);
	const [tempState, setTempState] = useState<{ [key: number]: string }>({});
	const [showOption, setShowOption] = useState(false);

	useEffect(() => {
		if (id !== undefined && state !== undefined) {
			setTempState({ [id]: state });
		}
	}, [id, state]);

	useEffect(() => {
		setShowOption(false);
	}, []);

	const download = () => {
		const link = document.createElement("a");
		link.href = path;
		link.download = "행운카드.png";
		link.click();
	};

	const clickUnlockContent = (cardId: number) => {
		setTempState((prevState) => ({
			...prevState,
			[cardId]: prevState[cardId] === "FILTERED" ? "OPEN" : "FILTERED",
		}));
	};

	const handleHide = async (cardId: number) => {
		try {
			await deleteCard(cardId);
			toast.success("카드를 영구 삭제했습니다");
		} catch (error) {
			console.error(error);
			toast.error("카드를 영구 삭제하지 못했습니다");
		}
	};

	const handleReport = () => {
		router.push(`report?cardId=${id}`);
	};

	return (
		<div id="detail-card">
			<div
				id="overlay"
				className="fixed bottom-0 w-screen max-w-[480px] z-10 h-full bg-black bg-opacity-30 backdrop-blur-sm"
				onClick={closeModal}
			></div>
			<div
				id="content"
				className="fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 rounded-lg z-10 overflow-hidden text-center"
			>
				<div className="font-nexonBold text-white flex items-center justify-between mb-4">
					<p>From. {writerName}</p>
					<div className="flex gap-x-1">
						{/* <span
							className="bg-white rounded-full p-1 shadow-[0px_3px_0px_0px_#9E9E9E]"
							onClick={() => (id ? clickUnlockContent(id) : undefined)}
						>
							<Star
								size={14}
								color="#d5b207"
								weight="fill"
							/>
						</span> */}
						<span
							className="bg-white rounded-full p-1 shadow-[0px_3px_0px_0px_#9E9E9E] mr-1"
							onClick={() => setShowOption(!showOption)}
						>
							<DotsThreeCircleVertical
								size={14}
								color="black"
								weight="fill"
							/>
						</span>
					</div>
					{showOption && (
						<div className="z-10 absolute right-2 top-12 border border-black border-solid bg-white p-3 rounded-lg text-black text-right font-nexonRegular text-xs">
							<p
								className="pb-2 mb-2 border-b border-black border-solid"
								onClick={handleReport}
							>
								카드 신고하기
							</p>
							<p onClick={() => id !== undefined && handleHide(id)}>카드 영구 삭제하기</p>
						</div>
					)}
				</div>
				<Card
					width={270}
					height={478}
					path={path}
					content={content}
					state={id !== undefined ? tempState[id] : "FILTERED"}
					effect={effect}
					flip={true}
				/>
				<p className="font-nexonRegular text-xs text-white text-center my-4">
					행운카드를 클릭하면 뒷면을 확인할 수 있어요!
				</p>
				<Button
					text="행운카드 저장하기"
					color="gradient"
					size="long"
					font="bold"
					shadow="gradient"
					onClick={download}
				/>
			</div>
		</div>
	);
};

export default DetailCard;
