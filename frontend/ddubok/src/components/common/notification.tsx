"use client";

import { useEffect } from "react";

import { onMessageListener, requestPermission } from "@lib/utils/firebase";
import { MessagePayload } from "@firebase/messaging";

const Notification = () => {
	useEffect(() => {
		requestPermission();

		onMessageListener().then((payload) => {
			const message = payload as MessagePayload;
			const title = message.notification?.title ?? "";
			const body = message.notification?.body ?? "";
			const link = message.data?.link;

			const notification = new window.Notification(title, {
				body: body,
				icon: "/assets/basic-open.png",
			});

			notification.onclick = (event) => {
				event.preventDefault();
				if (link) window.open(link, "_blank");
			};
		});
	}, []);

	return null;
};

export default Notification;
