/** @type {import('next').NextConfig} */

const withPWA = require("next-pwa")({
	dest: "public",
	swDest: "public/sw.js",
});

const nextConfig = {
	output: "standalone",
	images: {
		domains: ["ddubok.s3.ap-northeast-2.amazonaws.com"],
	},
	webpack: (config) => {
		config.externals.push({
			"utf-8-validate": "commonjs utf-8-validate",
			bufferutil: "commonjs bufferutil",
			canvas: "commonjs canvas",
		});

		return config;
	},
};

module.exports = withPWA(nextConfig);
