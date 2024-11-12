export interface ISeasonProps {
	name: string;
	description: string;
	started_at: string;
	ended_at: string;
	opened_at: string;
}

export interface ISeasonDetailProps {
	id: number;
	name: string;
	description: string;
	started_at: string;
	ended_at: string;
	opened_at: string;
	path: string[];
}

export interface ISeasonListProps {
	id: number;
	name: string;
}
