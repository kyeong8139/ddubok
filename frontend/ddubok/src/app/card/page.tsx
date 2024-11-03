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

	return <div className="text-white">카드 받는 페이지 {cardId}</div>;
};

export default Card;
