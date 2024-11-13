"use client";

import { useEffect } from "react";

import { onMessageListener } from "@lib/utils/firebase";
import { MessagePayload } from "@firebase/messaging";

const Notification = () => {
	useEffect(() => {
		if (typeof window !== "undefined" && "Notification" in window) {
			(async () => {
				try {
					const payload = await onMessageListener();
					const message = payload as MessagePayload;
					const title = message.notification?.title ?? "알림";
					const body = message.notification?.body ?? "새로운 알림이 있습니다.";
					const link = message.data?.link;

					const notification = new window.Notification(title, {
						body: body,
						icon: "/assets/basic-open.png",
					});

					notification.onclick = (event) => {
						event.preventDefault();
						if (link) window.open(link, "_blank");
					};
				} catch (error) {
					console.error("Failed to receive message:", error);
				}
			})();
		}
	}, []);

	return null;
};

export default Notification;
