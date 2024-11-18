import { initializeApp } from "firebase/app";
import { getMessaging, getToken, Messaging, onMessage } from "firebase/messaging";

const firebaseConfig = {
	apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
	authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
	projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
	storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
	messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
	appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
};

let messaging: Messaging;
if (typeof window !== "undefined") {
	const app = initializeApp(firebaseConfig);
	messaging = getMessaging(app);
}

export const requestPermission = async () => {
	try {
		const registration = await navigator.serviceWorker.register("/firebase-messaging-sw.js", {
			scope: "/",
		});

		getToken(messaging, {
			vapidKey: process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY,
			serviceWorkerRegistration: registration,
		});
	} catch (error) {
		console.error("Failed to get permission for notifications:", error);
	}
};

export const onMessageListener = () =>
	new Promise((resolve) => {
		onMessage(messaging, (payload) => {
			if (document.visibilityState === "visible") resolve(payload);
		});
	});

export { messaging };
