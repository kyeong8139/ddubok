import Image from "next/image";

import { ILoginButtonProps } from "@interface/components/loginButton";
import { colorClass, shadowClass } from "@styles/loginButton";

const LoginButton = (props: ILoginButtonProps) => {
	const { image, color, shadow, width, height, onClick } = props;

	return (
		<button
			id="login-button"
			className={`w-14 h-14 flex justify-center items-center border border-solid border-black rounded-full ${colorClass[color]} ${shadowClass[shadow]}`}
			onClick={onClick}
		>
			<Image
				src={image}
				alt="SNS logo"
				width={width}
				height={height}
			/>
		</button>
	);
};

export default LoginButton;
