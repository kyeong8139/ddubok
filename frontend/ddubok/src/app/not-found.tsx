"use client";

import { useRouter } from "next/navigation";
import Image from "next/image";

import Button from "@components/button/button";

export default function NotFound() {
	const router = useRouter();

	return (
		<div
			id="not-found"
			className="flex items-center justify-center h-[calc(100vh-56px)]"
		>
			<div className="flex flex-col w-screen max-w-[480px] items-center justify-center text-white">
				<Image
					src="/assets/error.png"
					alt="길잃은배달부"
					width={230}
					height={100}
					className="mb-4"
				/>
				<p className="text-xl font-nexonBold mb-1">행운배달부가 길을 잃었어요!</p>
				<p className="text-sm font-nexonRegular mb-8">( 준비되지 않은 페이지입니다! )</p>
				<Button
					text="홈으로 가기"
					color="gradient"
					size="long"
					font="bold"
					shadow="gradient"
					onClick={() => {
						router.push("/");
					}}
				/>
			</div>
		</div>
	);
}
