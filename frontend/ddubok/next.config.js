/** @type {import('next').NextConfig} */

const nextConfig = {};
const withPWA = require("next-pwa");

module.exports = withPWA({
	dest: "public",
	register: true,
	skipWaiting: true,
	buildExcludes: [/middleware-manifest\.json$/],
	...nextConfig,
});
