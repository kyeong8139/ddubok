"use client";

import { useContext, useEffect, useState } from "react";

import Modal from "@components/common/modal";
import Button from "@components/button/button";
import { ModalContext } from "@context/modal-context";
import { IUserProps } from "@interface/components/user";
import { selectMember, selectMemberList, updateMemberRole, updateMemberState } from "@lib/api/admin-api";
import useAuthToken from "@lib/utils/tokenUtils";

import { MagnifyingGlass } from "@phosphor-icons/react";
import toast from "react-hot-toast";

const User = () => {
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const { isTokenReady } = useAuthToken();
	const [selected, setSelected] = useState(0);
	const [userList, setUserList] = useState<IUserProps[]>([]);
	const [selectedUser, setSelectedUser] = useState<IUserProps | null>(null);
	const [searchName, setSearchName] = useState("");
	const [page, setPage] = useState(0);
	const [isLoading, setIsLoading] = useState(false);
	const [hasMore, setHasMore] = useState<boolean>(true);

	const isPageReady = isLoading || !isTokenReady;

	const getMemberList = async () => {
		if (isLoading || !hasMore) return;

		setIsLoading(true);
		try {
			const state = selected === 1 ? "활성" : selected === 2 ? "비활성" : selected === 3 ? "차단" : null;
			const response = await selectMemberList(state, page, 50, searchName);
			let users = response.data.data;

			if (users.length === 0) {
				setHasMore(false);
			} else {
				setUserList((prevUsers) => [...prevUsers, ...users]);
			}
		} catch (error) {
			console.error(error);
		} finally {
			setIsLoading(false);
		}
	};

	useEffect(() => {
		if (isTokenReady && !isLoading && hasMore) getMemberList();
	}, [isTokenReady, selected, page, searchName]);

	useEffect(() => {
		const handleScroll = () => {
			if (isLoading || !hasMore) return;

			const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
			if (scrollTop + clientHeight >= scrollHeight - 5) {
				setPage((prevPage) => prevPage + 1);
			}
		};

		window.addEventListener("scroll", handleScroll);
		return () => window.removeEventListener("scroll", handleScroll);
	}, [isLoading, hasMore]);

	const handleClick = (index: number) => {
		setSelected(index);
		setPage(0);
		setUserList([]);
		setHasMore(true);
	};

	const handleDetailClick = async (memberId: number) => {
		try {
			const response = await selectMember(memberId);
			setSelectedUser(response.data.data);
			openModal();
		} catch (error) {
			console.error(error);
		}
	};

	const handleUpdateMemberState = async () => {
		if (!selectedUser) return;
		try {
			await updateMemberState(selectedUser.memberId);
			toast.success("회원 상태가 변경되었습니다.");
			setPage(0);
			setUserList([]);
			setHasMore(true);
			getMemberList();
			closeModal();
		} catch (error) {
			console.error(error);
			toast.error("회원 상태 변경 중에 오류가 발생했습니다.");
			closeModal();
		}
	};

	const handleUpdateMemberRole = async () => {
		if (!selectedUser) return;
		try {
			await updateMemberRole(selectedUser.memberId);
			toast.success("회원 등급이 변경되었습니다.");
			setPage(0);
			setUserList([]);
			setHasMore(true);
			getMemberList();
			closeModal();
		} catch (error) {
			console.error(error);
			toast.error("회원 등급 변경 중에 오류가 발생했습니다.");
			closeModal();
		}
	};

	return (
		<div id="admin-user">
			{isPageReady ? (
				<div className="flex w-full h-screen items-center justify-center">{/* <Loading /> */}</div>
			) : (
				<div className="py-6">
					<div className="text-white flex flex-col items-center">
						<h1 className="font-nexonBold text-xl mb-2">🛠 사용자 관리</h1>
						<p className="font-nexonRegular text-sm">가입된 사용자의 정보를 확인할 수 있어요</p>
					</div>
					<div className="flex justify-center pt-4">
						<ul className="bg-white font-nexonRegular inline-flex justify-center gap-1 text-xs rounded-lg p-1">
							{["전체", "활성화", "비활성화", "차단"].map((item, index) => (
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
							value={searchName}
							onChange={(e) => setSearchName(e.target.value)}
						/>
					</div>
					<div>
						<table className="text-white font-nexonRegular w-[calc(100%-64px)] mx-auto">
							<thead>
								<tr className="border-b-2 border-solid border-white">
									<th className="px-2 py-[10px]">번호</th>
									<th className="px-2 py-[10px]">닉네임</th>
									<th className="px-2 py-[10px]">상세</th>
								</tr>
							</thead>
							<tbody>
								{userList.length > 0 ? (
									userList.map((user) => (
										<tr
											key={user.memberId}
											className="text-center border-b-[1px] border-solid border-white"
										>
											<td className="px-2 py-[10px]">{user.memberId}</td>
											<td className="px-2 py-[10px]">{user.nickname}</td>
											<td className="px-2 py-[10px]">
												<button
													className="underline"
													onClick={() => handleDetailClick(user.memberId)}
												>
													상세보기
												</button>
											</td>
										</tr>
									))
								) : (
									<tr>
										<td
											colSpan={3}
											className="text-center py-[10px]"
										>
											데이터가 없습니다.
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>
				</div>
			)}
			<div
				className={`transition-opacity duration-300 ${
					isModalOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
				}`}
			>
				<Modal>
					<div className="flex justify-between text-sm mb-4">
						<p className="font-nexonBold">번호</p>
						<p>{selectedUser?.memberId}</p>
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
					<div className="flex justify-center mt-8 gap-4">
						<Button
							text="등급 변경"
							color="purple"
							size="small"
							font="small"
							shadow="purple"
							onClick={handleUpdateMemberRole}
						/>
						<Button
							text="상태 변경"
							color="green"
							size="small"
							font="small"
							shadow="green"
							onClick={handleUpdateMemberState}
						/>
					</div>
				</Modal>
			</div>
		</div>
	);
};

export default User;
