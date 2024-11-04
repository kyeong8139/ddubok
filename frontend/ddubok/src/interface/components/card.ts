export interface ICardProps {
	width: number;
	height: number;
	image?: string;
	content?: string;
	effect: number;
	flip?: boolean;
}

export interface IDetailCardProps {
	cardId: number;
	writer: string;
	image: string;
	content: string;
	effect: number;
}

export interface IReportProps {
	cardId: number;
	title: string;
	reportType: string;
	content: string;
}
