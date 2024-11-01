/** @type {import('next').NextConfig} */

const nextConfig = {
	output: "standalone",
};

const withPWA = require("next-pwa")({
	dest: "public",
	swDest: "public/sw.js",
});

module.exports = withPWA(nextConfig);
