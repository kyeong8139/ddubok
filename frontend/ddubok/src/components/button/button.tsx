import { IButtonProps } from "@interface/components/button";
import { colorClass, fontClass, shadowClass, sizeClass } from "@styles/button";

const Button = (props: IButtonProps) => {
	const { text, color, size, font, shadow, onClick, disabled } = props;
	const textParts = text.split("<br/>");

	return (
		<button
			id="button"
			className={`rounded-lg border border-solid border-black text-black 
                       ${colorClass[color]} ${sizeClass[size]} ${shadowClass[shadow]}
                       ${disabled ? "opacity-50 cursor-not-allowed" : ""}`}
			onClick={onClick}
			disabled={disabled}
		>
			{font === "both" ? (
				<>
					<span className="font-nexonRegular">{textParts[0]}</span>
					<br />
					<span className="font-nexonBold">{textParts[1]}</span>
				</>
			) : (
				<>
					<span className={`${fontClass[font]}`}>{text}</span>
				</>
			)}
		</button>
	);
};

export default Button;
