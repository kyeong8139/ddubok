import React from "react";

import LoginButton from "@components/button/loginButton";

const Login = () => {
	return (
		<div id="login">
			<div className="flex">
				<LoginButton
					image="/assets/kakao.png"
					color="kakao"
					shadow="kakao"
					width={24}
					height={24}
				/>
				<LoginButton
					image="/assets/naver.png"
					color="naver"
					shadow="naver"
					width={32}
					height={32}
				/>
				<LoginButton
					image="/assets/google.png"
					color="google"
					shadow="google"
					width={24}
					height={24}
				/>
				<LoginButton
					image="/assets/instagram.png"
					color="meta"
					shadow="meta"
					width={32}
					height={32}
				/>
			</div>
		</div>
	);
};

export default Login;
