import { create } from "zustand";
import { persist } from "zustand/middleware";

import IUserState from "@interface/store/user-store";

const useUserStore = create(
	persist<IUserState>(
		(set) => ({
			accessToken: null,
			nickname: null,
			role: null,
			setUser: ({ accessToken, nickname, role }) => set({ accessToken, nickname, role }),
			clearUser: () => set({ accessToken: null, nickname: null, role: null }),
		}),
		{
			name: "user-store",
		},
	),
);

export default useUserStore;
