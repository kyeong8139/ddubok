import { create } from "zustand";
import { ICardState } from "../interface/store/card-store";

export const useCardStore = create<ICardState>()((set) => ({
	selectedImage: null,
	userName: "",
	setSelectedImage: (image: string | null) => set({ selectedImage: image }),
	setUserName: (name: string) => set({ userName: name }),
}));
