import { create } from "zustand";

import IAuthState from "@interface/store/auth-store";

const useAuthStore = create<IAuthState>((set) => ({
	accessToken: null,
	setAccessToken: (token) => set({ accessToken: token }),
	clearAccessToken: () => set({ accessToken: null }),
}));

export default useAuthStore;
