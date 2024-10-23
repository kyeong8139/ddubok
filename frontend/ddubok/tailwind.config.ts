import type { Config } from "tailwindcss";

const config: Config = {
	content: [
		"./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/components/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/app/**/*.{js,ts,jsx,tsx,mdx}",
	],
	theme: {
		extend: {
			backgroundImage: {
				"gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
				"gradient-conic": "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
			},
			colors: {
				ddubokPurple: "#B693FE",
				ddubokGreen: "#6EFFBF",
				ddubokGray: "#DBDBDB",
			},
			boxShadow: {
				"custom-green": "0px 4px 0px 0px #329167",
				"custom-purple": "0px 4px 0px 0px #63489C",
				"custom-gray": "0px 4px 0px 0px #9E9E9E",
				"custom-gradient": "0px 4px 0px 0px #717171",
			},
			fontFamily: {
				pyeongchang: ["var(--font-pyeongchang-peace-bold)", "sans-serif"],
				nexonLight: ["var(--font-nexon-gothic-light)", "sans-serif"],
				nexonRegular: ["var(--font-nexon-gothic-regular)", "sans-serif"],
				nexonBold: ["var(--font-nexon-gothic-bold)", "sans-serif"],
			},
		},
	},
	plugins: [],
};
export default config;
