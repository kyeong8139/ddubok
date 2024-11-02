"use client";

import { useContext, useMemo, useState } from "react";

import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import { IUserProps } from "@interface/components/user";

import { MagnifyingGlass } from "@phosphor-icons/react";

const User = () => {
	const { isModalOpen, openModal } = useContext(ModalContext);
	const [selected, setSelected] = useState(0);
	const [selectedUser, setSelectedUser] = useState<IUserProps | null>(null);

	const handleClick = (index: number) => {
		setSelected(index);
	};

	const handleDetailClick = (user: IUserProps) => {
		setSelectedUser(user);
		openModal();
	};

	const userInfo = useMemo(
		() => [
			{ id: 1, nickname: "서민정닮은코딩주머니뀨", state: "활성", role: "사용자" },
			{ id: 2, nickname: "다환궁예", state: "활성", role: "사용자" },
			{ id: 3, nickname: "마일리지천만보경", state: "활성", role: "사용자" },
			{ id: 4, nickname: "합성대마법사성혁", state: "활성", role: "사용자" },
			{ id: 5, nickname: "관리자_김경민", state: "활성", role: "사용자" },
			{ id: 6, nickname: "유니스", state: "활성", role: "사용자" },
		],
		[],
	); // 임시 데이터

	return (
		<div id="admin-user">
			<div className="py-6">
				<div className="text-white flex flex-col items-center">
					<h1 className="font-nexonBold text-xl mb-2">사용자 관리</h1>
					<p className="font-nexonRegular text-sm">가입된 사용자의 정보를 확인할 수 있어요</p>
				</div>
				<div className="flex justify-center pt-4">
					<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
						{["전체", "활성화", "비활성화"].map((item, index) => (
							<li
								key={index}
								onClick={() => handleClick(index)}
								className={`p-[6px] rounded-md cursor-pointer ${
									selected === index ? "bg-ddubokPurple text-white font-nexonBold" : "bg-gray-200"
								}`}
							>
								{item}
							</li>
						))}
					</ul>
				</div>
				<div className="bg-white flex items-center gap-2 w-[calc(100%-64px)] mx-auto my-4 p-3 rounded-lg font-nexonRegular">
					<label htmlFor="search">
						<MagnifyingGlass
							size={20}
							color="#AAAAAA"
						/>
					</label>
					<input
						id="search"
						type="text"
						placeholder="사용자 닉네임을 입력하세요"
						className="text-sm w-full outline-none"
					/>
				</div>
				<div>
					<table className="text-white  font-nexonRegular w-[calc(100%-64px)] mx-auto">
						<thead>
							<tr className="text-xs border-b-2 border-solid border-white">
								<th className="px-2 py-[10px]">아이디</th>
								<th className="px-2 py-[10px]">닉네임</th>
								<th className="px-2 py-[10px]">상세</th>
							</tr>
						</thead>
						<tbody>
							{userInfo.map((user) => (
								<tr
									key={user.id}
									className="text-center text-xs border-b-[1px] border-solid border-white"
								>
									<td className="px-2 py-[10px]">{user.id}</td>
									<td className="px-2 py-[10px]">{user.nickname}</td>
									<td className="px-2 py-[10px]">
										<button
											className="underline"
											onClick={() => handleDetailClick(user)}
										>
											상세보기
										</button>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
			<div
				className={`transition-opacity duration-300
					${isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"}`}
			>
				<Modal>
					<div className="flex justify-between text-sm mb-4">
						<p className="font-nexonBold">아이디</p>
						<p>{selectedUser?.id}</p>
					</div>
					<div className="flex justify-between text-sm mb-4">
						<p className="font-nexonBold">닉네임</p>
						<p>{selectedUser?.nickname}</p>
					</div>
					<div className="flex justify-between text-sm mb-4">
						<p className="font-nexonBold">상태</p>
						<p>{selectedUser?.state}</p>
					</div>
					<div className="flex justify-between text-sm mb-4">
						<p className="font-nexonBold">역할</p>
						<p>{selectedUser?.role}</p>
					</div>
					<div className="flex justify-around mt-8">
						<Button
							text="등급 변경"
							color="purple"
							size="small"
							font="small"
							shadow="purple"
							onClick={() => {}}
						/>
						<Button
							text="회원 퇴출"
							color="green"
							size="small"
							font="small"
							shadow="green"
							onClick={() => {}}
						/>
					</div>
				</Modal>
			</div>
		</div>
	);
};

export default User;
