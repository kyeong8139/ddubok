"use client";

import Link from "next/link";

import { CaretRight } from "@phosphor-icons/react";

const Menu = () => {
	// 로그인 연결 후 로그인 로직 추가 필요
	// 토큰 받으면 토큰에 따른 메뉴 탭 노출 변경 필요

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
				<div className="mx-6">
					<ul>
						{/* 사용자 로그인 시 보여져야 하는 것 */}
						<li className="mb-4">
							<Link href="/book">행운 카드북</Link>
						</li>
						<li className="mb-4">
							<Link href="/fortune">오늘의 운세</Link>
						</li>
						{/* 관리자 로긘 시 보여져야 하는 것 */}
						<li className="mb-4">
							<Link href="/admin/user">사용자 관리</Link>
						</li>
						<li className="mb-4">
							<Link href="/admin/report">신고 관리</Link>
						</li>
						<li className="mb-4">
							<Link href="/admin/setting">메인 관리</Link>
						</li>
						{/* 공통 */}
						<li className="mb-4">로그아웃</li>
					</ul>
				</div>
			</div>
		</div>
	);
};

export default Menu;
