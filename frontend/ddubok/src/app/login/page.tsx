"use client";

import Image from "next/image";
import Link from "next/link";
import React from "react";

import LoginButton from "@components/button/loginButton";
import { xLogin, googleLogin, kakaoLogin, naverLogin } from "@lib/api/login-api";

import { Clover } from "@phosphor-icons/react";

const Login = () => {
	return (
		<div id="login">
			<div className="font-nexonRegular flex flex-col items-center relative pt-[20%]">
				<div className="flex flex-row items-center gap-1 absolute">
					<p className="text-white">당신을 위한 행운 배달부</p>
					<Clover
						size={20}
						color="white"
					/>
				</div>
				<Image
					src="/assets/ddubok.png"
					alt="ddubok"
					width={136}
					height={136}
				/>
			</div>
			<div className="absolute bottom-36 w-full">
				<p className="font-nexonRegular text-white flex justify-center mb-6">SNS 계정으로 간편 가입하기</p>
				<div className="flex justify-center gap-4">
					<LoginButton
						image="/assets/kakao.png"
						color="kakao"
						shadow="kakao"
						width={24}
						height={24}
						onClick={kakaoLogin}
					/>
					<LoginButton
						image="/assets/naver.png"
						color="naver"
						shadow="naver"
						width={32}
						height={32}
						onClick={naverLogin}
					/>
					<LoginButton
						image="/assets/google.png"
						color="google"
						shadow="google"
						width={24}
						height={24}
						onClick={googleLogin}
					/>
					<LoginButton
						image="/assets/x-circle.png"
						color="x"
						shadow="x"
						width={40}
						height={40}
						onClick={xLogin}
					/>
				</div>
				<Link
					href="/"
					className="font-nexonLight text-white text-sm flex justify-center mt-6 underline"
				>
					좀 더 둘러볼게요
				</Link>
			</div>
		</div>
	);
};

export default Login;
