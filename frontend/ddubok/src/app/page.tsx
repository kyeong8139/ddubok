"use client";

import Button from "@components/button/button";

export default function Home() {
	return (
		<div
			id="home"
			className="font-pyeongchang text-white"
		>
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
			<Button
				text="회원가입하고 이용하기"
				color="gradient"
				size="long"
				font="bold"
				shadow="gradient"
				onClick={() => {}}
			/>
		</div>
	);
}
