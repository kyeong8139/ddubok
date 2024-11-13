// public/firebase-messaging-sw.js
importScripts("https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js");

const firebaseConfig = {
	apiKey: "AIzaSyAu0wK36SBnlDmTISehWKxX_zU8nD_AQ7Q",
	authDomain: "ddubok-eb6c0.firebaseapp.com",
	projectId: "ddubok-eb6c0",
	storageBucket: "ddubok-eb6c0.firebasestorage.app",
	messagingSenderId: "562327452231",
	appId: "1:562327452231:web:3c243f321f7b6dde470f62",
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
	const notificationTitle = payload.notification.title;
	const notificationOptions = {
		body: payload.notification.body,
		icon: "/assets/basic-open.png",
	};

	self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener("notificationClick", (event) => {
	event.notification.close();

	const link = event.notification.data.link;
	if (link) {
		event.waitUntil(
			clients.matchAll({ type: "window" }).then((windowClients) => {
				for (let client of windowClients) {
					if (client.url === link && "focus" in client) return client.focus();
				}

				if (clients.openWindow) return clients.openWindow(link);
			}),
		);
	}
});
