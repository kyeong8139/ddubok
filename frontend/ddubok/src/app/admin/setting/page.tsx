"use client";

import Button from "@components/button/button";
import { PlusCircle } from "@phosphor-icons/react";

const Setting = () => {
	return (
		<div id="admin-setting">
			<div className="py-6">
				<div className="text-white flex flex-col items-center mb-8">
					<h1 className="font-nexonBold text-xl mb-2">메인 설정</h1>
					<p className="font-nexonRegular text-sm">이벤트마다 메인의 설정을 변경할 수 있어요</p>
				</div>
				<div className="text-white w-[calc(100%-64px)] mx-auto mb-8">
					<h2 className="font-nexonBold text-lg mb-2">메인 텍스트 설정</h2>
					<div className="flex flex-col gap-4">
						<input
							type="text"
							placeholder="메인 키워드를 입력하세요"
							className="font-nexonRegular p-3 bg-transparent border border-solid border-white rounded-lg"
						/>
						<input
							type="text"
							placeholder="서브 키워드를 입력하세요"
							className="font-nexonRegular p-3 bg-transparent border border-solid border-white rounded-lg"
						/>
					</div>
				</div>
				<div className="text-white w-[calc(100%-64px)] mx-auto mb-8">
					<h2 className="font-nexonBold text-lg mb-2">이벤트 날짜 설정</h2>
					<div className="flex justify-between items-center gap-4">
						<input
							type="date"
							className="font-nexonRegular w-full p-3 bg-transparent border border-solid border-white rounded-lg"
						/>
						<span> ~ </span>
						<input
							type="date"
							className="font-nexonRegular w-full p-3 bg-transparent border border-solid border-white rounded-lg"
						/>
					</div>
				</div>
				<div className="text-white w-[calc(100%-64px)] mx-auto mb-8">
					<h2 className="font-nexonBold text-lg mb-2">메인 이미지 설정 (최소 3개)</h2>
					<div>
						<label
							htmlFor="banner-image"
							className="block w-[140px] h-[247.5px] rounded-lg bg-transparent border border-solid border-white"
						>
							<PlusCircle
								size={32}
								color="white"
								className="mx-auto h-full"
							/>
						</label>
						<input
							id="banner-image"
							type="file"
							accept="image/*"
							multiple
							className="hidden"
						/>
					</div>
				</div>
				<div className="text-center">
					<Button
						text="메인 정보 수정하기"
						color="gradient"
						size="long"
						font="bold"
						shadow="gradient"
						onClick={() => {}}
					/>
				</div>
			</div>
		</div>
	);
};

export default Setting;
