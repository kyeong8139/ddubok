export interface ISeasonProps {
	name: string;
	seasonDescription: string;
	startedAt: string;
	endedAt: string;
	openedAt: string;
}

export interface ISeasonDetailProps {
	id: number;
	name: string;
	seasonDescription: string;
	startedAt: string;
	endedAt: string;
	openedAt: string;
	path: string[];
}

export interface ISeasonListProps {
	id: number;
	name: string;
	isActiveSeason: boolean;
}

export interface ISeasonDefaultProps {
	seasonDescription: string;
}