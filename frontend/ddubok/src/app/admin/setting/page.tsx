"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

import { ISeasonListProps } from "@interface/components/season";
import { selectSeasonList } from "@lib/api/admin-api";
import Button from "@components/button/button";
import Loading from "@components/common/loading";
import useAuthToken from "@lib/utils/tokenUtils";

const Setting = () => {
	const router = useRouter();
	const { isTokenReady } = useAuthToken();
	const [seasonList, setSeasonList] = useState<ISeasonListProps[]>([]);
	const [isLoading, setIsLoading] = useState(true);

	const isPageReady = isLoading || !isTokenReady;

	const getSeasonList = async () => {
		try {
			const response = await selectSeasonList();
			let seasons = response.data.data;
			setSeasonList(seasons);
		} catch (error) {
			console.error(error);
		} finally {
			setIsLoading(false);
		}
	};

	useEffect(() => {
		if (isTokenReady) getSeasonList();
	}, [isTokenReady]);

	const handleDefaultClick = () => {
		router.push(`/admin/setting/default`);
	};

	const handleDetailClick = (seasonId: number) => {
		router.push(`/admin/setting/${seasonId}`);
	};

	const handleInsertClick = () => {
		router.push(`/admin/setting/insert`);
	};

	return (
		<div id="admin-setting">
			{isPageReady ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : (
				<div className="py-6">
					<div className="text-white flex flex-col items-center mb-8">
						<h1 className="font-nexonBold text-xl mb-2">🛠 메인 설정</h1>
						<p className="font-nexonRegular">이벤트마다 메인의 설정을 변경할 수 있어요</p>
					</div>
					<div className="flex-grow overflow-y-auto">
						<table className="text-white font-nexonRegular w-[calc(100%-64px)] mx-auto">
							<thead>
								<tr className="border-y-2 border-solid border-white">
									<th className="px-1 py-[10px]">글번호</th>
									<th className="px-2 py-[10px]">제목</th>
									<th className="px-1 py-[10px]">상세보기</th>
								</tr>
							</thead>
							<tbody>
								{seasonList.length > 0 ? (
									seasonList.map((season) => (
										<tr
											key={season.id}
											className="text-center border-b-[1px] border-solid border-white"
										>
											<td className="px-1 py-[10px]">{season.id}</td>
											<td className="px-2 py-[10px]">
												<span
													className={
														season.isActiveSeason ? "text-green-400 font-nexonBold" : ""
													}
												>
													{season.name}
													{season.isActiveSeason && " (활성)"}
												</span>
											</td>
											<td className="px-1 py-[10px]">
												<button
													className="underline"
													onClick={() => handleDetailClick(season.id)}
												>
													상세보기
												</button>
											</td>
										</tr>
									))
								) : (
									<tr>
										<td
											colSpan={3}
											className="text-center py-[10px]"
										>
											데이터가 없습니다.
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>
					<div className="absolute bottom-8 left-1 right-1 flex justify-center text-center mx-8 space-x-4">
						<Button
							text="기본 시즌 수정하기"
							color="purple"
							size="long"
							font="bold"
							shadow="gradient"
							onClick={handleDefaultClick}
						/>
						<Button
							text="시즌 정보 추가하기"
							color="green"
							size="long"
							font="bold"
							shadow="gradient"
							onClick={handleInsertClick}
						/>
					</div>
				</div>
			)}
		</div>
	);
};

export default Setting;
