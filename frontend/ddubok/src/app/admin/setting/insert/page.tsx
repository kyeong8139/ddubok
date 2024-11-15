"use client";

import { useRouter } from "next/navigation";
import Image from "next/image";
import { useState } from "react";

import Button from "@components/button/button";
import { ISeasonProps } from "@interface/components/season";
import { insertSeason } from "@lib/api/admin-api";

import { PlusCircle } from "@phosphor-icons/react";
import toast from "react-hot-toast";

const SettingDetail = () => {
	const router = useRouter();
	const [seasonData, setSeasonData] = useState<ISeasonProps>({
		name: "",
		seasonDescription: "",
		startedAt: "",
		endedAt: "",
		openedAt: "",
	});
	const [images, setImages] = useState<File[]>([]);
	const [previewImages, setPreviewImages] = useState<string[]>([]);

	const createSeason = async () => {
		try {
			await insertSeason(images, seasonData);
			toast.success("새로운 시즌이 등록되었습니다");
			router.push("/admin/setting");
		} catch (error) {
			console.error(error);
			toast.error("시즌 등록에 실패했습니다.");
		}
	};

	const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		const files = Array.from(event.target.files || []);
		setImages(files);
		setPreviewImages(files.map((file) => URL.createObjectURL(file)));
	};

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
		const { name, value } = e.target;
		setSeasonData((prev) => ({
			...prev,
			[name]: value,
		}));
	};

	return (
		<div id="setting-detail">
			<div className="w-[calc(100%-64px)] mx-auto pt-4">
				<div className="mb-4">
					<h2 className="font-nexonBold text-white mb-2">시즌 이름 설정</h2>
					<div className="flex flex-col gap-2">
						<input
							type="text"
							name="name"
							placeholder="시즌 이름을 입력하세요"
							className="font-nexonRegular text-sm p-2 rounded-lg"
							value={seasonData.name}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-4">
					<h2 className="font-nexonBold text-white mb-2">시즌 안내문 설정</h2>
					<div className="flex flex-col gap-2">
						<textarea
							name="seasonDescription"
							placeholder="시즌 안내문을 입력하세요"
							className="font-nexonRegular text-sm p-2 rounded-lg"
							value={seasonData.seasonDescription}
							onChange={handleInputChange}
							rows={3}
						/>
					</div>
				</div>
				<div className="mb-4">
					<h2 className="font-nexonBold text-white mb-2">이벤트 기간 설정</h2>
					<div className="flex flex-col justify-between items-center gap-1">
						<input
							type="datetime-local"
							name="startedAt"
							className="font-nexonRegular w-full text-sm p-2 border border-solid border-black rounded-lg"
							value={seasonData.startedAt}
							onChange={handleInputChange}
						/>
						<span className="text-white"> ~ </span>
						<input
							type="datetime-local"
							name="endedAt"
							className="font-nexonRegular w-full text-sm p-2 border border-solid border-black rounded-lg"
							value={seasonData.endedAt}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-4">
					<h2 className="font-nexonBold text-white mb-2">오픈일 설정</h2>
					<div>
						<input
							type="datetime-local"
							name="openedAt"
							className="font-nexonRegular w-full text-sm p-2 border border-solid border-black rounded-lg"
							value={seasonData.openedAt}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-8">
					<h2 className="font-nexonBold text-white mb-2">메인 이미지 설정 (최소 3개)</h2>
					<div className="flex gap-4">
						<label
							htmlFor="banner-image"
							className="block w-[120px] h-[212px] rounded-lg bg-white"
						>
							<PlusCircle
								size={32}
								color="black"
								weight="thin"
								className="mx-auto h-full"
							/>
						</label>
						<input
							id="banner-image"
							type="file"
							accept="image/*"
							multiple
							className="hidden"
							onChange={handleImageChange}
						/>
						{previewImages.map((imageSrc, index) => (
							<Image
								key={index}
								src={imageSrc}
								alt={`Season Image ${index + 1}`}
								width={120}
								height={212}
								className="w-[120px] h-[212px] rounded-lg bg-white object-cover"
							/>
						))}
					</div>
				</div>
				<div className="text-center mb-6">
					<Button
						text="시즌 정보 추가하기"
						color="gradient"
						size="semiLong"
						font="bold"
						shadow="gradient"
						onClick={createSeason}
					/>
				</div>
			</div>
		</div>
	);
};

export default SettingDetail;
