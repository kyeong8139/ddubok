import { useContext, useState } from "react";
import { useRouter } from "next/navigation";

import Card from "@components/card/card";
import { ModalContext } from "@context/modal-context";
import { IDetailCardProps } from "@interface/components/card";
import { Siren } from "@phosphor-icons/react";
import Button from "@components/button/button";

const DetailCard = ({ cardId, writer, image, content, effect }: IDetailCardProps) => {
	const router = useRouter();
	const { closeModal } = useContext(ModalContext);
	const [showOption, setShowOption] = useState(false);

	const download = () => {
		const link = document.createElement("a");
		link.href = image;
		link.download = "행운카드.png";
		link.click();
	};

	const handleReport = () => {
		router.push(`report?cardId=${cardId}`);
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
					<p>From. {writer}</p>
					<span
						className="bg-white rounded-full p-1 shadow-[0px_3px_0px_0px_#9E9E9E]"
						onClick={() => setShowOption(!showOption)}
					>
						<Siren
							size={14}
							color="red"
							weight="fill"
						/>
					</span>
					{showOption && (
						<div className="z-10 absolute right-8 top-18 bg-white p-3 rounded-lg text-black text-right font-nexonRegular text-xs">
							<p className="mb-2">카드 숨기기</p>
							<p onClick={handleReport}>카드 신고하기</p>
						</div>
					)}
				</div>
				<Card
					width={270}
					height={478}
					image={image}
					content={content}
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
