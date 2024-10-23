/** @type {import('next').NextConfig} */

const nextConfig = {};
const withPWA = require("next-pwa");

module.exports = withPWA({
	dest: "public",
	register: true,
	skipWaiting: true,
	buildExcludes: [/middleware-manifest\.json$/],
	disable: process.env.NODE_ENV !== "production",
	...nextConfig,
});
