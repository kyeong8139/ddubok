export interface IUserProps {
	memberId: number;
	nickname: string;
	state: string;
	role: string;
}

export interface IUserDto {
	memberId: number;
	nickname?: string;
	role: string;
}
