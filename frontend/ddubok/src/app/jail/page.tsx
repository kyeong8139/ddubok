"use client";

import { useRouter } from "next/navigation";
import Image from "next/image";
import Link from "next/link";

import Button from "@components/button/button";
import { InstagramLogo, XLogo } from "@phosphor-icons/react";

export default function NotFound() {
	const router = useRouter();

	return (
		<div
			id="not-found"
			className="flex items-center justify-center h-[calc(100vh-56px)]"
		>
			<div className="flex flex-col w-screen max-w-[480px] items-center justify-center text-white">
				<Image
					src="/assets/jail.png"
					alt="감옥에갇힌배달부"
					width={280}
					height={280}
					className="mb-4"
				/>
				<p className="text-xl font-nexonBold mb-1">현재 계정이 차단되었습니다.</p>
				<p className="text-xs font-nexonLight mb-3">(궁금한 점이 있으시면 DM으로 문의해주세요.)</p>
				<ul>
					<div className="flex flex-row gap-3 mb-2">
						<li>
							<Link
								href="https://www.instagram.com/ddubok_official"
								target="_blank"
								className="flex items-center gap-1"
							>
								<InstagramLogo
									size={16}
									color="white"
								/>
								<span className="text-xs">ddubok_offical</span>
							</Link>
						</li>
						<li>
							<Link
								href="https://x.com/ddubokddubok"
								target="_blank"
								className="flex items-center gap-1"
							>
								<XLogo
									size={16}
									color="white"
								/>
								<span className="text-xs">dduboddubok</span>
							</Link>
						</li>
					</div>
				</ul>
				<div className="mt-4 w-full flex justify-center">
					<Button
						text="홈으로 가기"
						color="gradient"
						size="long"
						font="bold"
						shadow="gradient"
						onClick={() => {
							router.push("/");
						}}
					/>
				</div>
			</div>
		</div>
	);
}
