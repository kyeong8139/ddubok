"use client";

import { useRouter } from "next/navigation";
import { useContext, useEffect, useState } from "react";

import { ModalContext } from "@context/modal-context";
import Button from "@components/button/button";
import Modal from "@components/common/modal";
import { IUserDto } from "@interface/components/user";
import { selectUser, updateUser } from "@lib/api/user-api";
import { getTokenInfo } from "@lib/utils/authUtils";
import useAuthToken from "@lib/utils/tokenUtils";

import { PencilCircle } from "@phosphor-icons/react";
import { deleteUser, logout } from "@lib/api/login-api";
import Link from "next/link";

const Mypage = () => {
	const route = useRouter();
	const { accessToken, isTokenReady, clearAccessToken } = useAuthToken();
	const [isEditing, setIsEditing] = useState(false);
	const [newNickname, setNewNickname] = useState("");
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const decodedToken = accessToken ? getTokenInfo(accessToken) : null;
	const [user, setUser] = useState<IUserDto | null>(
		decodedToken
			? {
					memberId: decodedToken.memberId,
					nickname: "",
					role: decodedToken.role,
			  }
			: null,
	);

	useEffect(() => {
		const getUser = async () => {
			if (decodedToken && isTokenReady) {
				try {
					const response = await selectUser();
					setUser({
						memberId: decodedToken.memberId,
						nickname: response.data.data.nickname,
						role: decodedToken.role,
					});
				} catch (error) {
					console.error(error);
				}
			}
		};

		getUser();
	}, [accessToken, isTokenReady]);

	const handleNicknameEdit = () => {
		setIsEditing(true);
		if (user?.nickname) {
			setNewNickname(user.nickname);
		}
	};

	const modifyNickname = async () => {
		try {
			await updateUser(newNickname);
			setUser((prevUser) => (prevUser ? { ...prevUser, nickname: newNickname } : prevUser));
			setIsEditing(false);
			route.refresh();
		} catch (error) {
			console.error(error);
		}
	};

	const handleLogout = async () => {
		try {
			await logout();
			clearAccessToken();
			route.push("/");
		} catch (error) {
			console.error(error);
		}
	};

	const removeUser = async () => {
		try {
			await deleteUser();
			clearAccessToken();
			route.push("/");
		} catch (error) {
			console.error(error);
		}
	};

	return (
		<div id="mypage">
			<div className="h-full py-6">
				<div className="text-white flex flex-col items-center">
					<h1 className="font-nexonBold text-xl mb-2">내 정보</h1>
					<p className="font-nexonRegular text-sm">내 정보를 확인하고 수정할 수 있어요</p>
				</div>
			</div>
			<div className="text-white font-nexonRegular text-lg">
				{isEditing ? (
					<div>
						<div className="w-[calc(100%-64px)] flex items-center justify-between mx-auto">
							<label
								className="font-nexonBold"
								htmlFor="nickname"
							>
								닉네임
							</label>
							<input
								id="nickname"
								type="text"
								placeholder="최대 11글자"
								value={newNickname}
								onChange={(e) => setNewNickname(e.target.value)}
								maxLength={11}
								className="px-2 py-2 w-48 text-sm rounded-lg text-black mb-1"
							/>
						</div>

						<p className="w-[calc(100%-64px)] flex justify-end font-nexonLight mx-auto text-xs text-white">
							닉네임은 최대 11글자까지 가능합니다.
						</p>
						<button
							className="flex justify-end items-center gap-1 w-[calc(100%-64px)] mx-auto mt-4"
							onClick={modifyNickname}
						>
							<PencilCircle
								size={20}
								color="white"
								weight="fill"
							/>
							<span className="text-sm">정보 수정완료</span>
						</button>
					</div>
				) : (
					<>
						<div>
							<div className="w-[calc(100%-64px)] flex justify-between mx-auto">
								<span className="font-nexonBold">닉네임</span>
								<span>{user?.nickname} 님</span>
							</div>
							<button
								className="flex justify-end items-center gap-1 w-[calc(100%-64px)] mx-auto mt-4 mb-12"
								onClick={handleNicknameEdit}
							>
								<PencilCircle
									size={20}
									color="white"
									weight="fill"
								/>
								<span className="text-sm">정보 수정하기</span>
							</button>
						</div>
						<div className="w-[calc(100%-64px)] flex items-center justify-between mx-auto text-white mb-4">
							<label className="font-nexonBold">피드백</label>
							<Link
								href="https://forms.gle/5E6PDmnWWTw273uS6"
								target="_blank"
							>
								@구글폼 바로가기
							</Link>
						</div>
						<div className="w-[calc(100%-64px)] flex items-center justify-between mx-auto text-white mb-4">
							<label className="font-nexonBold">인스타그램</label>
							<Link
								href="https://www.instagram.com/ddubok_official"
								target="_blank"
							>
								@ddubok_official
							</Link>
						</div>
						<div className="w-[calc(100%-64px)] flex items-center justify-between mx-auto text-white mb-4">
							<label className="font-nexonBold">트위터</label>
							<Link
								href="https://x.com/ddubokddubok"
								target="_blank"
							>
								@ddubokddubok
							</Link>
						</div>
					</>
				)}
			</div>
			<div className="flex justify-center gap-12 pt-12 pb-16 absolute left-1/2 bottom-0 -translate-x-1/2">
				<p
					className="text-white border-b border-white border-solid font-nexonLight text-sm"
					onClick={handleLogout}
				>
					로그아웃
				</p>
				<p
					className="text-white border-b border-white border-solid font-nexonLight text-sm"
					onClick={openModal}
				>
					회원탈퇴
				</p>
			</div>
			{isModalOpen && (
				<Modal>
					<h1 className="text-center font-nexonBold mb-1">서비스를 탈퇴하겠습니까?</h1>
					<p className="text-sm text-center mb-4">
						탈퇴하면 서비스 이용에
						<br />
						어려움이 발생할 수 있습니다.
					</p>
					<div className="flex justify-center gap-4">
						<Button
							text="아니요"
							color="gray"
							size="small"
							font="regular"
							shadow="gray"
							onClick={closeModal}
						/>
						<Button
							text="네"
							color="green"
							size="small"
							font="regular"
							shadow="green"
							onClick={removeUser}
						/>
					</div>
				</Modal>
			)}
		</div>
	);
};

export default Mypage;
