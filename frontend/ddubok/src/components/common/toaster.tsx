import { Toaster } from "react-hot-toast";

const CustomToaster = () => {
	return (
		<Toaster
			position="top-center"
			reverseOrder={false}
			toastOptions={{
				duration: 2000,
				style: {
					fontFamily: "NEXON Lv1 Gothic Regular",
				},
				success: {
					style: {
						background: "#34D399",
						color: "white",
					},
					iconTheme: {
						primary: "#9c90ff",
						secondary: "white",
					},
				},
			}}
		/>
	);
};

export default CustomToaster;
