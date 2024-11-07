export interface ICardState {
	selectedImage: string | null;
	userName: string;
	letterContent: string;
	cardId: number | null;
	setSelectedImage: (image: string | null) => void;
	setUserName: (name: string) => void;
	setLetterContent: (content: string) => void;
	setCardId: (id: number | null) => void;
}
