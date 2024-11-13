"use client";

import { useRouter } from "next/navigation";
import Image from "next/image";
import { useEffect, useState } from "react";

import { ISeasonDefaultProps } from "@interface/components/season";
import { selectSeasonDefault, updateSeasonDefault } from "@lib/api/admin-api";
import Button from "@components/button/button";
import Loading from "@components/common/loading";
import useAuthToken from "@lib/utils/tokenUtils";

import { PlusCircle } from "@phosphor-icons/react";
import toast from "react-hot-toast";

const SettingDefault = () => {
	const router = useRouter();
    const { isTokenReady } = useAuthToken();
    const [seasonDefault, setSeasonDefault] = useState<ISeasonDefaultProps>({
		seasonDescription: ""
	});    
    const [images, setImages] = useState<File[]>([]);
	const [previewImages, setPreviewImages] = useState<string[]>([]);
    const [isLoading, setIsLoading] = useState(true);
	
    const isPageReady = isLoading || !isTokenReady;

    const getSeasonDefault = async () => {
        setIsLoading(true);
		try {
			const response = await selectSeasonDefault();
			console.log(response.data.data);
			let season = response.data.data;
			setSeasonDefault(season.seasonDescription);
            setPreviewImages(season.path);
		} catch (error) {
			console.error(error);
		} finally {
			setIsLoading(false);
		}
	};

    useEffect(() => {
		if (isTokenReady) getSeasonDefault();
	}, [isTokenReady]);

	const handleSeasonDefault = async () => {
		try {
			await updateSeasonDefault(images, seasonDefault);
			toast.success("기본 시즌이 등록되었습니다");
			router.push("/admin/setting");
		} catch (error) {
			console.error(error);
			toast.error("기본 시즌 설정에 실패했습니다.");
		}
	};

	const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		const files = Array.from(event.target.files || []);
		setImages(files);
		setPreviewImages(files.map((file) => URL.createObjectURL(file)));
	};

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setSeasonDefault((prev) => ({
			...prev,
			[name]: value,
		}));
	};

    return (
		<div id="setting-detail">
			{!isPageReady ? (
				<div className="flex w-full h-screen items-center justify-center">
					<Loading />
				</div>
			) : (
				<div className="w-[calc(100%-64px)] mx-auto pt-4">
					<div className="mb-4">
						<h2 className="font-nexonBold text-white mb-2">기본 안내문 설정</h2>
						<div className="flex flex-col gap-2">
							<input
								type="text"
								name="seasonDescription"
								placeholder="기본 안내문을 입력하세요"
								className="font-nexonRegular text-sm p-2 rounded-lg"
								value={seasonDefault.seasonDescription}
								onChange={handleInputChange}
							/>
						</div>
					</div>
					<div className="mb-8">
						<h2 className="font-nexonBold text-white mb-2">메인 이미지 설정 (최소 3개)</h2>
						<div className="overflow-x-auto">
							<div className="flex gap-4 w-max">
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
					</div>
					<div className="text-center mb-6">
						<Button
							text="기본 시즌 정보 수정하기"
							color="gradient"
							size="semiLong"
							font="bold"
							shadow="gradient"
							onClick={handleSeasonDefault}
						/>
					</div>
				</div>
			)}
		</div>
	);
};

export default SettingDefault;
