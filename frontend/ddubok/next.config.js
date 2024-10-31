/** @type {import('next').NextConfig} */

const nextConfig = {
	output: 'standalone'
};
const withPWA = require("next-pwa");

module.exports = withPWA({
	dest: "public",
	...nextConfig,
});
