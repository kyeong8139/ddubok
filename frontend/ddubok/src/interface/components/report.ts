export interface IReportProps {
	id: number;
	title: string;
	content: string;
	state: string | null;
	reportType: string;
	reportMemberId: number;
	reportMemberNickname: string;
	cardId: number;
	cardContent: string;
	cardPath: string;
}

export interface IReportListProps {
	id: number;
	title: string;
	state: string;
}
