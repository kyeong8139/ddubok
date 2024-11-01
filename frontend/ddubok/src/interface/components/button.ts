import { colorClass, fontClass, shadowClass, sizeClass } from "@styles/button";

export interface IButtonProps {
	text: string;
	color: keyof typeof colorClass;
	size: keyof typeof sizeClass;
	font: keyof typeof fontClass;
	shadow: keyof typeof shadowClass;
	onClick: () => void;
}
