/** @type {import('next').NextConfig} */

// const nextConfig = {};
const withPWA = require("next-pwa");

const nextConfig = {
	// reactStrictMode: false,
	webpack: (config) => {
		config.externals.push({
			"utf-8-validate": "commonjs utf-8-validate",
			bufferutil: "commonjs bufferutil",
			canvas: "commonjs canvas",
		});

		return config;
	},
};

module.exports = withPWA({
	dest: "public",
	output: "standalone",
	...nextConfig,
});
module.exports = nextConfig;
