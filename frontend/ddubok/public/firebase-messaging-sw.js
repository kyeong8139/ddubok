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
	console.log("백그라운드 메시지 수신: " + payload);

	const notificationTitle = payload.notification.title;
	const notificationOptions = {
		body: payload.notification.body,
		icon: "/assets/push.png",
	};

	self.registration.showNotification(notificationTitle, notificationOptions);
});
