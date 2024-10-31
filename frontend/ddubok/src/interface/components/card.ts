export interface ICardProps {
	width: number;
	height: number;
	image?: string;
	content?: string;
	effect: number;
	flip?: boolean;
}

export interface IDetailCardProps {
	writer: string;
	image: string;
	content: string;
	effect: number;
}
