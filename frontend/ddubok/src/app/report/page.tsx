"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";

import Button from "@components/button/button";
import { IReportProps } from "@interface/components/card";
import { insertReport } from "@lib/api/report-api";

const Report = () => {
	const router = useRouter();
	const searchParms = useSearchParams();
	const cardId = searchParms.get("cardId");
	const [title, setTitle] = useState("");
	const [reportType, setReportType] = useState("");
	const [content, setContent] = useState("");

	const createReport = async () => {
		try {
			const reportData: IReportProps = {
				cardId: Number(cardId),
				title,
				reportType,
				content,
			};

			alert("신고가 접수되었습니다.");
			await insertReport(reportData);
			router.push("/book");
		} catch (error) {
			console.error(error);
		}
	};

	return (
		<div id="report">
			<div className="text-white flex flex-col items-center pt-8 mb-8">
				<h1 className="font-nexonBold text-xl mb-2">신고하기</h1>
				<p className="font-nexonRegular text-sm">신고된 행운카드는 검토 후 처리됩니다</p>
			</div>
			<div className="text-white font-nexonRegular w-[calc(100%-64px)] mx-auto">
				<input
					type="number"
					className="hidden bg-transparent border border-solid border-white rounded-lg p-2 w-full mb-2"
					value={Number(cardId)}
					readOnly
				/>
				<input
					type="text"
					placeholder="신고 제목을 입력하세요 (10자 이내)"
					className="bg-transparent border border-solid border-white rounded-lg p-2 w-full mb-2 text-sm"
					value={title}
					onChange={(e) => setTitle(e.target.value)}
				/>
				<div className="grid grid-cols-2 p-3 border border-solid border-white rounded-lg gap-y-2 text-sm mb-2">
					<div className="flex gap-2">
						<input
							id="비속어"
							type="radio"
							value={"비속어"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="비속어">비속어</label>
					</div>
					<div className="flex gap-2">
						<input
							id="광고"
							type="radio"
							value={"광고"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="광고">광고</label>
					</div>
					<div className="flex gap-2">
						<input
							id="음란성 내용"
							type="radio"
							value={"음란성 내용"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="음란성 내용">음란성 내용</label>
					</div>
					<div className="flex gap-2">
						<input
							id="사생활 침해"
							type="radio"
							value={"사생활 침해"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="사생활 침해">사생활 침해</label>
					</div>
					<div className="flex gap-2">
						<input
							id="도배"
							type="radio"
							value={"도배"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="도배">도배</label>
					</div>
					<div className="flex gap-2">
						<input
							id="기타"
							type="radio"
							value={"기타"}
							name="report"
							onChange={(e) => setReportType(e.target.value)}
						/>
						<label htmlFor="기타">기타</label>
					</div>
				</div>
				<textarea
					name="content"
					id="content"
					rows={8}
					className="bg-transparent border border-solid border-whitee rounded-lg w-full p-3 mb-2"
					placeholder="신고 사유를 상세히 작성해주세요 (200자 이내)"
					value={content}
					onChange={(e) => setContent(e.target.value)}
				></textarea>
			</div>
			<div className="text-center">
				<Button
					text="신고 접수하기"
					color="gradient"
					size="long"
					font="bold"
					shadow="gradient"
					onClick={createReport}
				/>
			</div>
		</div>
	);
};

export default Report;
