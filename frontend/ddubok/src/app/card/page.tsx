"use client";

import React, { useEffect } from "react";
import { useSearchParams, useRouter } from "next/navigation";

import { decryptCardId } from "@lib/utils/crypto";

const Card = () => {
	const router = useRouter();
	const searchParams = useSearchParams();
	const encryptedId = searchParams?.get("id");
	const cardId = encryptedId ? decryptCardId(encryptedId) : null;

	useEffect(() => {
		if (!cardId) {
			router.replace("/error");
		}
	}, [cardId, router]);

	if (!cardId) {
		return null;
	}

	return (
		<div className="flex flex-col items-center w-full">
			<div className="text-white font-nexonBold text-2xl mt-10">편지 쓰기</div>
		</div>
	);
};

export default Card;
