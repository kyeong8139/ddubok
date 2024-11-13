// firebase.ts
import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";

const firebaseConfig = {
	apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
	authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
	projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
	storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
	messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
	appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

export const requestPermission = async () => {
	try {
		const registration = await navigator.serviceWorker.register("/firebase-messaging-sw.js", {
			scope: "/",
		});

		const token = await getToken(messaging, {
			vapidKey: process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY,
			serviceWorkerRegistration: registration,
		});
		if (token) console.log("Notification token:", token);
	} catch (error) {
		console.error("Failed to get permission for notifications:", error);
	}
};

export const onMessageListener = () =>
	new Promise((resolve) => {
		onMessage(messaging, (payload) => {
			resolve(payload);
		});
	});

export { messaging };
