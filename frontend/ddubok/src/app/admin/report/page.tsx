"use client";

import Image from "next/image";
import { useContext, useEffect, useState } from "react";

import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import { IReportListProps, IReportProps } from "@interface/components/report";
import { selectReport, selectReportList, updateReport } from "@lib/api/admin-api";
import useAuthToken from "@lib/utils/tokenUtils";

import toast from "react-hot-toast";

const Report = () => {
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const { isTokenReady } = useAuthToken();
	const [selected, setSelected] = useState(0);
	const [reportList, setReportList] = useState<IReportListProps[]>([]);
	const [selectedReport, setSelectedReport] = useState<IReportProps | null>(null);
	const [page, setPage] = useState(0);
	const [isLoading, setIsLoading] = useState(false);
	const [hasMore, setHasMore] = useState<boolean>(true);

	const isPageReady = isLoading || !isTokenReady;

	const getReportList = async () => {
		if (isLoading || !hasMore) return;

		setIsLoading(true);
		try {
			const state = selected === 1 ? "미처리" : selected === 2 ? "수락" : selected === 3 ? "반려" : null;
			const response = await selectReportList(state, page, 50);
			let reports = response.data.data;

			if (reports.length === 0) {
				setHasMore(false);
			} else {
				setReportList((prevReports) => [...prevReports, ...reports]);
			}
		} catch (error) {
			console.error(error);
		} finally {
			setIsLoading(false);
		}
	};

	useEffect(() => {
		if (isTokenReady && !isLoading && hasMore) getReportList();
	}, [isTokenReady, selected, page]);

	useEffect(() => {
		const handleScroll = () => {
			if (isLoading || !hasMore) return;

			const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
			if (scrollTop + clientHeight >= scrollHeight - 5) {
				setPage((prevPage) => prevPage + 1);
			}
		};

		window.addEventListener("scroll", handleScroll);
		return () => window.removeEventListener("scroll", handleScroll);
	}, [isLoading, hasMore]);

	const handleClick = (index: number) => {
		setSelected(index);
		setPage(0);
		setReportList([]);
		setHasMore(true);
	};

	const handleDetailClick = async (reportId: number) => {
		try {
			const response = await selectReport(reportId);
			setSelectedReport(response.data.data);
			openModal();
		} catch (error) {
			console.error(error);
		}
	};

	const handleUpdateReport = async (reportId: number, action: "수락" | "반려") => {
		try {
			await updateReport(reportId, action);
			toast.success(`신고가 ${action}되었습니다`);
			setPage(0);
			setReportList([]);
			setHasMore(true);
			getReportList();
			closeModal();
		} catch (error) {
			console.error(error);
			toast.error("신고 처리 중에 오류가 발생했습니다.");
			closeModal();
		}
	};

	return (
		<div id="admin-report">
			{isPageReady ? (
				<div className="flex w-full h-screen items-center justify-center">{/* <Loading /> */}</div>
			) : (
				<div className="py-6">
					<div className="text-white flex flex-col items-center">
						<h1 className="font-nexonBold text-xl mb-2">🛠 신고 관리</h1>
						<p className="font-nexonRegular text-sm">신고된 게시물 정보를 확인할 수 있어요</p>
					</div>
					<div className="flex justify-center pt-4 pb-6">
						<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
							{["전체", "미처리", "수락", "반려"].map((item, index) => (
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
						<table className="text-white font-nexonRegular w-[calc(100%-64px)] mx-auto">
							<thead>
								<tr className="border-y-2 border-solid border-white">
									<th className="px-1 py-[10px]">글번호</th>
									<th className="px-2 py-[10px]">제목</th>
									<th className="px-2 py-[10px]">상태</th>
									<th className="px-1 py-[10px]">상세</th>
								</tr>
							</thead>
							<tbody>
								{reportList.length > 0 ? (
									reportList.map((report) => (
										<tr
											key={report.id}
											className="text-center border-b-[1px] border-solid border-white"
										>
											<td className="px-1 py-[10px]">{report.id}</td>
											<td className="px-2 py-[10px]">{report.title}</td>
											<td className="px-2 py-[10px]">{report.state}</td>
											<td className="px-1 py-[10px]">
												<button
													className="underline"
													onClick={() => handleDetailClick(report.id)}
												>
													상세보기
												</button>
											</td>
										</tr>
									))
								) : (
									<tr>
										<td
											colSpan={4}
											className="text-center py-[10px]"
										>
											데이터가 없습니다.
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>
				</div>
			)}
			<div
				className={`transition-opacity duration-300 ${
					isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
				}`}
			>
				<Modal>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고자 아이디</p>
						<p>{selectedReport?.reportMemberId}</p>
					</div>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold">신고자 닉네임</p>
						<p>{selectedReport?.reportMemberNickname}</p>
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
						<p>{selectedReport?.cardId}</p>
					</div>
					<div className="flex flex-col justify-between text-xs mb-3">
						<p className="font-nexonBold mb-2">신고된 카드 내용</p>
						<p>{selectedReport?.cardContent}</p>
					</div>
					<div className="flex justify-between text-xs mb-3">
						<p className="font-nexonBold mb-2">신고된 카드 이미지</p>
						{selectedReport ? (
							<Image
								src={selectedReport.cardPath}
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
							onClick={() => handleUpdateReport(selectedReport!.id, "반려")}
						/>
						<Button
							text="신고 수락"
							color="green"
							size="small"
							font="small"
							shadow="green"
							onClick={() => handleUpdateReport(selectedReport!.id, "수락")}
						/>
					</div>
				</Modal>
			</div>
		</div>
	);
};

export default Report;
