/** @type {import('next').NextConfig} */

const nextConfig = {};
const withPWA = require("next-pwa");

module.exports = withPWA({
	dest: "public",
	output: "standalone",
	...nextConfig,
});
