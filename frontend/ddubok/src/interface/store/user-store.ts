interface IUserState {
	accessToken: string | null;
	nickname: string | null;
	role: string | null;
	setUser: (user: { accessToken: string | null; nickname: string | null; role: string | null }) => void;
	clearUser: () => void;
}

export default IUserState;
