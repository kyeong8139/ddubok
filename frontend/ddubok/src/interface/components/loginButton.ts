import { colorClass, shadowClass } from "@styles/loginButton";

export interface ILoginButtonProps {
	image: string;
	color: keyof typeof colorClass;
	shadow: keyof typeof shadowClass;
	width: number;
	height: number;
	onClick?: () => void;
}
