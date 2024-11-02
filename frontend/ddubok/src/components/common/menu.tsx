"use client";

import Link from "next/link";

import { CaretRight } from "@phosphor-icons/react";
import { decodeAccessToken } from "@lib/utils/authUtils";
import useAuthStore from "@store/auth-store";

const Menu = () => {
	const accessToken = useAuthStore((state) => state.accessToken);
	const decodedToken = accessToken ? decodeAccessToken(accessToken) : null;

	return (
		<div
			id="menu"
			className="flex justify-end"
		>
			<div
				id="overlay"
				className="fixed bottom-0 w-screen max-w-[480px] z-10 h-[calc(100%-56px)] bg-black bg-opacity-30 backdrop-blur-sm"
			></div>
			<div
				id="menu-tab"
				className="fixed z-10 bottom-0 h-[calc(100%-56px)] w-72 text-white bg-ddubokBackground font-nexonRegular"
			>
				{decodedToken ? (
					<div className="my-8 mx-6 border-solid border-b pb-8">
						<Link
							href="/mypage"
							className="flex items-center mr-2 mb-4 font-nexonBold"
						>
							<span>{decodedToken.nickname.toString()} 님</span>
							<CaretRight
								size={20}
								weight="bold"
								color="white"
							/>
						</Link>
						<p className="text-xs leading-5">
							오늘도 뚜복과 함께 <br />
							행운 가득한 하루 보내세요
						</p>
					</div>
				) : (
					<div className="my-8 mx-6 border-solid border-b pb-8">
						<Link
							href="/login"
							className="flex items-center mr-2 mb-4 font-nexonBold"
						>
							<span>SNS로 로그인하기</span>
							<CaretRight
								size={20}
								weight="bold"
								color="white"
							/>
						</Link>
						<p className="text-xs leading-5">
							로그인하고 뚜복 서비스를 <br />
							자유롭게 이용해보세요
						</p>
					</div>
				)}
				<div className="mx-6">
					{decodedToken ? (
						decodedToken.role === "user" ? (
							<ul>
								<li className="mb-4">
									<Link href="/book">행운 카드북</Link>
								</li>
								<li className="mb-4">
									<Link href="/fortune">오늘의 운세</Link>
								</li>
							</ul>
						) : (
							<ul>
								<li className="mb-4">
									<Link href="/admin/user">사용자 관리</Link>
								</li>
								<li className="mb-4">
									<Link href="/admin/report">신고 관리</Link>
								</li>
								<li className="mb-4">
									<Link href="/admin/setting">메인 관리</Link>
								</li>
							</ul>
						)
					) : (
						<ul>
							<li>
								<Link href="/create">행운카드 만들기</Link>
							</li>
						</ul>
					)}
				</div>
			</div>
		</div>
	);
};

export default Menu;
