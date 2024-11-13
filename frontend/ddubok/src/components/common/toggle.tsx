import { IToggleProps } from "@interface/components/notification";

const Toggle = ({ isChecked, onChange }: IToggleProps) => {
	return (
		<div id="toggle">
			<div className="relative inline-block w-12 h-7">
				<input
					type="checkbox"
					checked={isChecked}
					onChange={(e) => onChange(e.target.checked)}
					className="hidden"
				/>
				<div
					className={`absolute top-0 left-0 right-0 bottom-0 cursor-pointer rounded-full transition-all duration-300 
						${
							!isChecked
								? "bg-gray-100 shadow-[0px_2px_4px_rgba(0,0,0,0.4),0px_7px_13px_rgba(0,0,0,0.3),0px_-3px_0px_inset_rgba(0,0,0,0.2)]"
								: "bg-white shadow-[0px_2px_4px_rgba(0,0,0,0.4),0px_7px_13px_rgba(0,0,0,0.2),0px_-3px_0px_inset_rgba(0,0,0,0.1)]"
						}`}
					onClick={() => onChange(!isChecked)}
				>
					<div
						className={`absolute bottom-1 left-1 h-5 w-5 rounded-full transition-transform duration-500 
						${
							!isChecked
								? "transform translate-x-full bg-red-500 rotate-[360deg] shadow-[0px_2px_4px_rgba(0,0,0,0.3),0px_7px_13px_rgba(0,0,0,0.2),0px_-3px_0px_inset_rgba(0,0,0,0.1)]"
								: "bg-teal-500 shadow-[0px_2px_4px_rgba(0,0,0,0.3),0px_7px_13px_rgba(0,0,0,0.2),0px_-3px_0px_inset_rgba(0,0,0,0.1)]"
						}`}
					></div>
				</div>
			</div>
		</div>
	);
};

export default Toggle;
