export interface ICardState {
	selectedImage: string | null;
	userName: string;
	setSelectedImage: (image: string | null) => void;
	setUserName: (name: string) => void;
}
