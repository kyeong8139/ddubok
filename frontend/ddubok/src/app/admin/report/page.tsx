"use client";

import Image from "next/image";
import { useContext, useMemo, useState } from "react";

import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import { IReportProps } from "@interface/components/report";

const Report = () => {
	const { isModalOpen, openModal } = useContext(ModalContext);
	const [selected, setSelected] = useState(0);
	const [selectedReport, setSelectedReport] = useState<IReportProps | null>(null);

	const handleClick = (index: number) => {
		setSelected(index);
	};

	const handleDetailClick = (report: IReportProps) => {
		setSelectedReport(report);
		openModal();
	};

	const reportInfo = useMemo(
		() => [
			{
				id: 1,
				title: "Report Title 1",
				content: "부적절해요",
				state: "미처리",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
			{
				id: 2,
				title: "Report Title 2",
				content: "부적절해요",
				state: "미처리",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
			{
				id: 3,
				title: "Report Title 3",
				content: "부적절해요",
				state: "수락",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
			{
				id: 4,
				title: "Report Title 4",
				content: "부적절해요",
				state: "반려",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
			{
				id: 5,
				title: "Report Title 5",
				content: "부적절해요",
				state: "수락",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
			{
				id: 6,
				title: "Report Title 6",
				content: "부적절해요",
				state: "반려",
				report_member_id: 501,
				report_member_nickname: "친절한 사자",
				card: {
					card_id: 1001,
					card_content: "재수나 해라!",
					path: "/assets/template/kkm-card-2.png",
				},
			},
		],
		[],
	); // 임시 데이터

	return (
		<div id="admin-report">
			<div className="py-6">
				<div className="text-white flex flex-col items-center">
					<h1 className="font-nexonBold text-xl mb-2">신고 관리</h1>
					<p className="font-nexonRegular text-sm">신고된 게시물 정보를 확인할 수 있어요</p>
				</div>
				<div className="flex justify-center pt-4 pb-6">
					<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
						{["전체", "처리", "미처리"].map((item, index) => (
							<li
								key={index}
								onClick={() => handleClick(index)}
								className={`p-[6px] rounded-md cursor-pointer ${
									selected === index ? "bg-ddubokPurple text-white font-nexonBold" : "bg-gray-200"
								}`}
							>
								{item}
							</li>
						))}
					</ul>
				</div>
				<div>
					<table className="text-white  font-nexonRegular w-[calc(100%-64px)] mx-auto">
						<thead>
							<tr className="text-xs border-y-2 border-solid border-white">
								<th className="px-1 py-[10px]">글번호</th>
								<th className="px-2 py-[10px]">제목</th>
								<th className="px-2 py-[10px]">상태</th>
								<th className="px-1 py-[10px]">상세</th>
							</tr>
						</thead>
						<tbody>
							{reportInfo.map((report) => (
								<tr
									key={report.id}
									className="text-center text-xs border-b-[1px] border-solid border-white"
								>
									<td className="px-1 py-[10px]">{report.id}</td>
									<td className="px-2 py-[10px]">{report.title}</td>
									<td className="px-2 py-[10px]">{report.state}</td>
									<td className="px-1 py-[10px]">
										<button
											className="underline"
											onClick={() => handleDetailClick(report)}
										>
											상세보기
										</button>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
			<div
				className={`transition-opacity duration-300
					${isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
			>
				<Modal>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고자 아이디</p>
						<p>{selectedReport?.report_member_id}</p>
					</div>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고자 닉네임</p>
						<p>{selectedReport?.report_member_nickname}</p>
					</div>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고 제목</p>
						<p>{selectedReport?.title}</p>
					</div>
					<div className="flex flex-col justify-between text-xs mb-3">
						<p className="font-nexonBold mb-2">신고 내용</p>
						<p>{selectedReport?.content}</p>
					</div>
					<hr className="border border-solid border-black mb-3" />
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고된 카드 번호</p>
						<p>{selectedReport?.card.card_id}</p>
					</div>
					<div className="flex flex-col justify-between text-xs mb-3">
						<p className="font-nexonBold mb-2">신고된 카드 내용</p>
						<p>{selectedReport?.card.card_content}</p>
					</div>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold mb-2">신고된 카드 이미지</p>
						{selectedReport ? (
							<Image
								src={selectedReport.card.path}
								alt="신고된 카드 이미지"
								width={70}
								height={200}
								className="rounded"
							/>
						) : null}
					</div>
					<div className="flex justify-center mt-8 gap-4">
						<Button
							text="신고 반려"
							color="purple"
							size="small"
							font="small"
							shadow="purple"
							onClick={() => {}}
						/>
						<Button
							text="신고 수락"
							color="green"
							size="small"
							font="small"
							shadow="green"
							onClick={() => {}}
						/>
					</div>
				</Modal>
			</div>
		</div>
	);
};

export default Report;
