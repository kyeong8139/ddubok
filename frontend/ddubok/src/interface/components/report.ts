export interface IReportProps {
	id: number;
	title: string;
	content: string;
	state: string;
	report_member_id: number;
	report_member_nickname: string;
	card: {
		card_id: number;
		card_content: string;
		path: string;
	};
}

export interface IReportListProps {
	id: number;
	title: string;
	state: string;
}
