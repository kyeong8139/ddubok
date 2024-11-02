// store/card-store.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";

export interface ICardState {
	selectedImage: string | null;
	userName: string;
	setSelectedImage: (image: string | null) => void;
	setUserName: (name: string) => void;
}

export const useCardStore = create<ICardState>()(
	persist(
		(set) => ({
			selectedImage: null,
			userName: "",
			setSelectedImage: (image: string | null) => set({ selectedImage: image }),
			setUserName: (name: string) => set({ userName: name }),
		}),
		{
			name: "card-storage",
		},
	),
);
