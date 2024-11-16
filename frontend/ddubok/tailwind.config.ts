import type { Config } from "tailwindcss";

const config: Config = {
	mode: "jit",
	content: [
		"./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/components/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/app/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/styles/**/*.{js,ts,jsx,tsx}",
	],
	theme: {
		extend: {
			spacing: {
				"0.25": "0.0625rem",
				"0.375": "0.09375rem",
				"0.5": "0.125rem",
				"17": "4.25rem",
				"18": "4.5rem",
				"21": "5.25rem",
			},
			blur: {
				custom: "8px",
			},
			backgroundImage: {
				"gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
				"gradient-conic": "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
			},
			colors: {
				ddubokPurple: "#B693FE",
				ddubokGreen: "#6EFFBF",
				ddubokGray: "#DBDBDB",
				ddubokBackground: "#17153B",
			},
			boxShadow: {
				"custom-purple": "0px 4px 0px 0px #63489C",
				"custom-green": "0px 4px 0px 0px #329167",
				"custom-gray": "0px 4px 0px 0px #9E9E9E",
				"custom-gradient": "0px 4px 0px 0px #717171",
				"custom-red": "0px 4px 0px 0px #6e0000",
				kakao: "0px 4px 0px 0px #7D7319",
				naver: "0px 4px 0px 0px #0D6102",
				google: "0px 4px 0px 0px #6F6F6F",
				x: "0px 4px 0px 0px #888888",
			},
			fontFamily: {
				pyeongchang: ["var(--font-pyeongchang-peace-bold)", "sans-serif"],
				nexonLight: ["var(--font-nexon-gothic-light)", "sans-serif"],
				nexonRegular: ["var(--font-nexon-gothic-regular)", "sans-serif"],
				nexonBold: ["var(--font-nexon-gothic-bold)", "sans-serif"],
			},
			keyframes: {
				focusEffect: {
					"0%": {
						transform: "scaleX(0.95)",
						borderBottomColor: "white",
						borderBottomWidth: "1px",
						boxShadow: "0 2px 0 -1px rgba(110, 255, 191, 0)",
					},
					"25%": {
						// 30%에서 25%로 변경
						transform: "scaleX(1.05)",
						borderBottomColor: "#6EFFBF",
						borderBottomWidth: "2px",
						boxShadow: "0 4px 0 -1px rgba(110, 255, 191, 0.6)",
					},
					"50%": {
						// 60%에서 50%로 변경
						transform: "scaleX(0.98)",
						borderBottomColor: "#6EFFBF",
						borderBottomWidth: "2px",
						boxShadow: "0 4px 0 -1px rgba(110, 255, 191, 0.4)",
					},
					"75%": {
						// 새로운 키프레임 추가
						transform: "scaleX(1.02)",
						borderBottomColor: "#6EFFBF",
						borderBottomWidth: "2px",
						boxShadow: "0 3px 0 -1px rgba(110, 255, 191, 0.35)",
					},
					"100%": {
						transform: "scaleX(1)",
						borderBottomColor: "#6EFFBF",
						borderBottomWidth: "2px",
						boxShadow: "0 2px 0 -1px rgba(110, 255, 191, 0.3)",
					},
				},
			},
			animation: {
				focusEffect: "focusEffect 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards", // 0.6s에서 0.8s로 변경
			},
		},
	},
	plugins: [],
};
export default config;
