"use client";

import { useRouter } from "next/navigation";
import { useContext, useEffect, useState } from "react";

import { ModalContext } from "@context/modal-context";
import Button from "@components/button/button";
import Modal from "@components/common/modal";
import Toggle from "@components/common/toggle";
import { IUserDto } from "@interface/components/user";
import { selectUser, updateUser } from "@lib/api/user-api";
import { getTokenInfo } from "@lib/utils/authUtils";
import useAuthToken from "@lib/utils/tokenUtils";
import { removeToken, insertToken } from "@lib/api/notification-api";
import { requestPermission } from "@lib/utils/firebase";
import { deleteUser, logout } from "@lib/api/login-api";
import { getMessaging, getToken, deleteToken } from "@firebase/messaging";

import { PencilCircle } from "@phosphor-icons/react";

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
					notificationConsent: "",
			  }
			: null,
	);

	const sanitizeInput = (input: string): string => {
		const sanitized = input
			.replace(/[<>]/g, "")
			.replace(/[&'"]/g, "")
			.replace(/javascript:/gi, "")
			.replace(/on\w+=/gi, "")
			.replace(/data:/gi, "");

		return sanitized.replace(/[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s_-]/g, "");
	};

	useEffect(() => {
		const getUser = async () => {
			if (decodedToken && isTokenReady) {
				try {
					const response = await selectUser();
					setUser({
						memberId: decodedToken.memberId,
						nickname: response.data.data.nickname,
						role: decodedToken.role,
						notificationConsent: response.data.data.notificationConsent,
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

	const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const sanitizedValue = sanitizeInput(e.target.value);
		setNewNickname(sanitizedValue);
	};

	const modifyNickname = async () => {
		try {
			const sanitizedNickname = sanitizeInput(newNickname);
			await updateUser(sanitizedNickname);
			setUser((prevUser) => (prevUser ? { ...prevUser, nickname: sanitizedNickname } : prevUser));
			setIsEditing(false);
			window.location.reload();
		} catch (error) {
			console.error(error);
		}
	};

	const modifyNotificationConsent = async (consent: boolean) => {
		try {
			const updatedConsent = consent ? "ENABLED" : "DISABLED";

			if (updatedConsent === "ENABLED") {
				await requestPermission();
				const messaging = getMessaging();
				const firebaseToken = await getToken(messaging, {
					vapidKey: process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY,
				});
				await insertToken(firebaseToken);
			} else if (updatedConsent === "DISABLED") {
				const messaging = getMessaging();
				await deleteToken(messaging);
				await removeToken();
			}

			setUser((prevUser) => (prevUser ? { ...prevUser, notificationConsent: updatedConsent } : null));
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
								onChange={handleNicknameChange}
								maxLength={11}
								className="px-2 py-2 w-48 text-sm rounded-lg text-black mb-1"
							/>
						</div>

						<p className="w-[calc(100%-64px)] flex justify-end font-nexonLight mx-auto text-xs text-white">
							닉네임은 최대 11글자까지 가능합니다.
						</p>
						<button
							className="flex justify-end items-center gap-1 w-[calc(100%-64px)] mx-auto mt-4 mb-12"
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
					</>
				)}
			</div>
			<div className="text-white font-nexonRegular text-lg">
				<div className="w-[calc(100%-64px)] flex items-center justify-between mx-auto">
					<label
						className="font-nexonBold"
						htmlFor="alarm"
					>
						서비스 알림 수신 설정
					</label>
					<Toggle
						isChecked={user?.notificationConsent === "ENABLED" ? true : false}
						onChange={modifyNotificationConsent}
					/>
				</div>
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
