"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

import { ISeasonListProps } from "@interface/components/season";
import { selectSeasonList } from "@lib/api/admin-api";
import Button from "@components/button/button";

const Setting = () => {
	const router = useRouter();
	const [seasonList, setSeasonList] = useState<ISeasonListProps[]>([]);

	const getSeasonList = async () => {
		try {
			const response = await selectSeasonList();
			console.log(response.data);
			console.log(response.data.data);
			let seasons = response.data;
			setSeasonList(seasons);
		} catch (error) {
			console.error(error);
		}
	};

	useEffect(() => {
		getSeasonList();
	}, []);

	const handleDetailClick = (seasonId: number) => {
		router.push(`/admin/setting/${seasonId}`);
	};

	const handleInsertClick = () => {
		router.push(`/admin/setting/insert`);
	};

	return (
		<div id="admin-setting">
			<div className="py-6">
				<div className="text-white flex flex-col items-center mb-8">
					<h1 className="font-nexonBold text-xl mb-2">메인 설정</h1>
					<p className="font-nexonRegular text-sm">이벤트마다 메인의 설정을 변경할 수 있어요</p>
				</div>
				<div className="flex-grow overflow-y-auto">
					<table className="text-white  font-nexonRegular w-[calc(100%-64px)] mx-auto">
						<thead>
							<tr className="text-xs border-y-2 border-solid border-white">
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
										className="text-center text-xs border-b-[1px] border-solid border-white"
									>
										<td className="px-1 py-[10px]">{season.id}</td>
										<td className="px-2 py-[10px]">{season.name}</td>
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
				<div className="absolute bottom-8 left-0 right-0 text-center">
					<Button
						text="시즌 정보 추가하기"
						color="gradient"
						size="long"
						font="bold"
						shadow="gradient"
						onClick={handleInsertClick}
					/>
				</div>
			</div>
		</div>
	);
};

export default Setting;
