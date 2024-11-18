"use client";

import { useEffect } from "react";

import { onMessageListener } from "@lib/utils/firebase";
import { MessagePayload } from "@firebase/messaging";

const Notification = () => {
	useEffect(() => {
		if (typeof window !== "undefined" && "Notification" in window) {
			(async () => {
				try {
					onMessageListener().then((payload) => {
						const message = payload as MessagePayload;

						const notificationTitle = message.notification?.title ?? "알림";
						const notificationOptions = {
							body: message.notification?.body ?? "새로운 알림이 있습니다.",
						};

						new window.Notification(notificationTitle, notificationOptions);
					});
				} catch (error) {
					console.error("Failed to receive message:", error);
				}
			})();
		}
	}, []);

	return null;
};

export default Notification;
