interface IAuthState {
	accessToken: string | null;
	setAccessToken: (token: string) => void;
	clearAccessToken: () => void;
}

export default IAuthState;
