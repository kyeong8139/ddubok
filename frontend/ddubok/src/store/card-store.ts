import { create } from "zustand";
import { persist } from "zustand/middleware";

import { ICardState } from "@interface/store/card-store";

export const useCardStore = create<ICardState>()(
	persist(
		(set) => ({
			selectedImage: null,
			userName: "",
			letterContent: "",
			cardId: null,
			setSelectedImage: (image: string | null) => set({ selectedImage: image }),
			setUserName: (name: string) => set({ userName: name }),
			setLetterContent: (content: string) => set({ letterContent: content }),
			setCardId: (id: number | null) => set({ cardId: id }),
		}),
		{
			name: "card-storage",
		},
	),
);
