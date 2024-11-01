/** @type {import('next').NextConfig} */

const withPWA = require("next-pwa")({
	dest: "public",
	swDest: "public/sw.js",
});

const nextConfig = {
	output: "standalone",
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
