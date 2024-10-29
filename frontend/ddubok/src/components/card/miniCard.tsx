import { IMiniCardProps } from "@interface/components/fortune";
import Image from "next/image";

const MiniCard = ({ day, isAttend }: IMiniCardProps) => {
	return (
		<div id="mini-card">
			{!isAttend ? (
				<div className="border border-dashed border-white flex justify-center items-center text-white text-xs font-nexonLight rounded-full w-14 h-14">
					{day}
				</div>
			) : (
				<div className="border border-solid border-white rounded-full overflow-hidden w-14 h-14 flex items-center justify-center">
					<Image
						src="/assets/temp1.jpg"
						alt="출석 스탬프"
						width={56}
						height={56}
						objectFit="cover"
					/>
				</div>
			)}
		</div>
	);
};

export default MiniCard;
