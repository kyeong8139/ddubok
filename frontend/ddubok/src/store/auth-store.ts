import { create } from "zustand";
import { persist } from "zustand/middleware";

import IAuthState from "@interface/store/auth-store";

const useAuthStore = create(
	persist<IAuthState>(
		(set) => ({
			accessToken: null,
			setAccessToken: (token) => set({ accessToken: token }),
			clearAccessToken: () => set({ accessToken: null }),
		}),
		{
			name: "user-store",
		},
	),
);

export default useAuthStore;
