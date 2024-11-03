export const decryptCardId = (encryptedId: string): number | null => {
	try {
		const salty = process.env.NEXT_PUBLIC_SALT_KEY;
		const decoded = atob(encryptedId);
		const lastHyphenIndex = decoded.lastIndexOf("-");
		const salt = decoded.substring(0, lastHyphenIndex);
		const cardId = decoded.substring(lastHyphenIndex + 1);

		if (salt !== salty) {
			return null;
		}

		const parsedId = parseInt(cardId, 10);
		if (isNaN(parsedId)) {
			return null;
		}

		return parsedId;
	} catch (error) {
		return null;
	}
};
