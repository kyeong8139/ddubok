"use client";

import { useRouter } from "next/navigation";
import { useContext, useEffect, useState } from "react";

import { ModalContext } from "@context/modal-context";
import Button from "@components/button/button";
import Modal from "@components/common/modal";
import { IUserDto } from "@interface/components/user";
import { selectUser, updateUser } from "@lib/api/user-api";
import { getTokenInfo } from "@lib/utils/authUtils";
import useAuthStore from "@store/auth-store";

import { PencilCircle } from "@phosphor-icons/react";
import { deleteUser, logout } from "@lib/api/login-api";

const Mypage = () => {
	const route = useRouter();
	const [isEditing, setIsEditing] = useState(false);
	const [newNickname, setNewNickname] = useState("");
	const { isModalOpen, openModal, closeModal } = useContext(ModalContext);
	const accessToken = useAuthStore((state) => state.accessToken);
	const clearAccessToken = useAuthStore((state) => state.clearAccessToken);
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
			if (decodedToken) {
				try {
					const response = await selectUser();
					console.log(response.data.data.nickname);
					setUser({
						memberId: decodedToken.memberId,
						nickname: response.data.data.nickname,
						role: decodedToken.role,
					});
					console.log(user);
				} catch (error) {
					console.error(error);
				}
			}
		};

		getUser();
	}, [accessToken]);

	const handleNicknameEdit = () => {
		setIsEditing(true);
	};

	const modifyNickname = async () => {
		try {
			await updateUser();
			setUser((prevUser) => (prevUser ? { ...prevUser, nickname: newNickname } : prevUser));
			setIsEditing(false);
			console.log("닉네임 수정하기");
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
			await logout();
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
								className="px-2 py-2 w-48 text-sm rounded-lg text-black"
							/>
						</div>
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
					<div>
						<div className="w-[calc(100%-64px)] flex justify-between mx-auto">
							<span className="font-nexonBold">닉네임</span>
							<span>{user?.nickname} 님</span>
						</div>
						<button
							className="flex justify-end items-center gap-1 w-[calc(100%-64px)] mx-auto mt-4"
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
				)}
			</div>
			<div className="flex justify-center gap-2 pt-12 pb-16 absolute left-1/2 bottom-0 -translate-x-1/2">
				<Button
					text="로그아웃"
					color="purple"
					size="short"
					font="bold"
					shadow="purple"
					onClick={handleLogout}
				/>
				<Button
					text="회원 탈퇴"
					color="green"
					size="short"
					font="bold"
					shadow="green"
					onClick={openModal}
				/>
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
