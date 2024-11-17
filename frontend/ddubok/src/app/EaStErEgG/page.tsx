"use client";

import React, { useState, useEffect, useRef } from "react";
import { useRouter } from "next/navigation";

import Button from "@components/button/button";

type Item = {
	id: number;
	type: string;
	x: number;
	y: number;
	speed: number;
	bounceOffset?: number;
	bouncePhase?: number;
	angle?: number;
	radius?: number;
};

type MousePosition = {
	x: number;
	y: number;
};

const EasterEgg: React.FC = () => {
	const router = useRouter();
	const [gameStarted, setGameStarted] = useState<boolean>(false);
	const [score, setScore] = useState<number>(0);
	const [gameOver, setGameOver] = useState<boolean>(false);
	const [items, setItems] = useState<Item[]>([]);
	const [mousePosition, setMousePosition] = useState<MousePosition>({ x: 0, y: 0 });
	const [gameWidth, setGameWidth] = useState<number>(480);
	const [isBonus, setIsBonus] = useState<boolean>(false);
	const [isBouncing, setIsBouncing] = useState<boolean>(false);
	const [isSpiral, setIsSpiral] = useState<boolean>(false);
	const [isSlowMotion, setIsSlowMotion] = useState<boolean>(false);
	const gameAreaRef = useRef<HTMLDivElement>(null);
	const bonusTimeoutRef = useRef<NodeJS.Timeout | null>(null);
	const bounceTimeoutRef = useRef<NodeJS.Timeout | null>(null);
	const spiralTimeoutRef = useRef<NodeJS.Timeout | null>(null);
	const slowMotionTimeoutRef = useRef<NodeJS.Timeout | null>(null);

	// 게임 영역 너비 설정
	useEffect(() => {
		if (gameAreaRef.current) {
			setGameWidth(gameAreaRef.current.offsetWidth);
		}

		const handleResize = () => {
			if (gameAreaRef.current) {
				setGameWidth(gameAreaRef.current.offsetWidth);
			}
		};

		window.addEventListener("resize", handleResize);
		return () => window.removeEventListener("resize", handleResize);
	}, []);

	// 마우스/터치 위치 추적
	useEffect(() => {
		const handlePointerMove = (e: MouseEvent | TouchEvent) => {
			if (!gameAreaRef.current) return;

			const rect = gameAreaRef.current.getBoundingClientRect();
			let clientX: number;
			let clientY: number;

			if (e instanceof MouseEvent) {
				clientX = e.clientX;
				clientY = e.clientY;
			} else {
				e.preventDefault();
				clientX = e.touches[0].clientX;
				clientY = e.touches[0].clientY;
			}

			setMousePosition({
				x: Math.min(Math.max(0, clientX - rect.left), gameWidth),
				y: Math.min(Math.max(0, clientY - rect.top), rect.height),
			});
		};

		if (gameStarted) {
			window.addEventListener("mousemove", handlePointerMove);
			window.addEventListener("touchmove", handlePointerMove as EventListener);
			window.addEventListener("touchstart", handlePointerMove as EventListener);
		}

		return () => {
			window.removeEventListener("mousemove", handlePointerMove);
			window.removeEventListener("touchmove", handlePointerMove as EventListener);
			window.removeEventListener("touchstart", handlePointerMove as EventListener);
		};
	}, [gameStarted, gameWidth]);

	// 타이머 정리
	useEffect(() => {
		return () => {
			if (bonusTimeoutRef.current) clearTimeout(bonusTimeoutRef.current);
			if (bounceTimeoutRef.current) clearTimeout(bounceTimeoutRef.current);
			if (spiralTimeoutRef.current) clearTimeout(spiralTimeoutRef.current);
			if (slowMotionTimeoutRef.current) clearTimeout(slowMotionTimeoutRef.current);
		};
	}, []);

	// 효과 초기화 함수
	const clearEffects = (excludeSlowMotion = false) => {
		setIsBouncing(false);
		setIsSpiral(false);
		if (!excludeSlowMotion) {
			setIsSlowMotion(false);
		}
		if (bounceTimeoutRef.current) clearTimeout(bounceTimeoutRef.current);
		if (spiralTimeoutRef.current) clearTimeout(spiralTimeoutRef.current);
		if (!excludeSlowMotion && slowMotionTimeoutRef.current) {
			clearTimeout(slowMotionTimeoutRef.current);
		}
	};

	// 아이템 효과 적용 함수
	const applyItemEffect = (type: string) => {
		if (type === "⚡") {
			setGameOver(true);
		} else if (type === "🎁") {
			if (bonusTimeoutRef.current) clearTimeout(bonusTimeoutRef.current);
			setIsBonus(true);
			bonusTimeoutRef.current = setTimeout(() => {
				setIsBonus(false);
			}, 800);
			setScore((prev) => prev + 5);
		} else if (type === "🎪") {
			clearEffects(isSlowMotion);
			setIsBouncing(true);
			bounceTimeoutRef.current = setTimeout(() => {
				setIsBouncing(false);
			}, 3500);
			setScore((prev) => prev + 3);
		} else if (type === "🌀") {
			clearEffects(isSlowMotion);
			setIsSpiral(true);
			spiralTimeoutRef.current = setTimeout(() => {
				setIsSpiral(false);
			}, 3500);
			setScore((prev) => prev + 3);
		} else if (type === "🌈") {
			setItems((prevItems) => {
				return prevItems
					.map((item) => ({
						...item,
						type: "🍀",
					}))
					.filter((item) => item.type !== "🌈");
			});
			setScore((prev) => prev + 3);
		} else if (type === "⏰") {
			setIsSlowMotion(true);
			if (slowMotionTimeoutRef.current) clearTimeout(slowMotionTimeoutRef.current);
			slowMotionTimeoutRef.current = setTimeout(() => {
				setIsSlowMotion(false);
			}, 2500);
			setScore((prev) => prev + 3);
		} else {
			setScore((prev) => prev + 1);
		}
	};

	// 여러 개의 아이템 생성
	const createItems = () => {
		const itemCount = isBonus ? Math.floor(Math.random() * 5) + 10 : Math.floor(Math.random() * 5) + 3;
		const newItems: Item[] = [];

		for (let i = 0; i < itemCount; i++) {
			const random = Math.random();
			let type;
			if (random < 0.62) {
				type = "🍀";
			} else if (random < 0.915) {
				type = "⚡";
			} else if (random < 0.93) {
				type = "🎁";
			} else if (random < 0.956) {
				type = "🎪";
			} else if (random < 0.982) {
				type = "🌀";
			} else if (random < 0.991) {
				type = "🌈";
			} else {
				type = "⏰";
			}

			newItems.push({
				id: Date.now() + i,
				type,
				x: Math.random() * (gameWidth - 40),
				y: -30,
				speed: Math.random() * 2 + 3,
				bounceOffset: Math.random() * Math.PI * 2,
				bouncePhase: 0,
				angle: Math.random() * Math.PI * 2,
				radius: 0,
			});
		}

		return newItems;
	};

	// 아이템 생성 주기
	useEffect(() => {
		let itemInterval: NodeJS.Timeout;

		if (gameStarted && !gameOver) {
			itemInterval = setInterval(() => {
				setItems((prev) => [...prev, ...createItems()]);
			}, 600);
		}

		return () => {
			if (itemInterval) {
				clearInterval(itemInterval);
			}
		};
	}, [gameStarted, gameOver, gameWidth, isBonus]);

	const getRotation = (item: Item) => {
		return "rotate(" + (isSpiral ? (item.angle || 0) * 57.3 : 0) + "deg)";
	};

	// 아이템 움직임
	useEffect(() => {
		let animationFrame: number;

		const moveItems = () => {
			if (gameStarted && !gameOver) {
				setItems((prevItems) => {
					return prevItems
						.map((item) => {
							let newX = item.x;
							let newY = item.y + (isSlowMotion ? item.speed * 0.5 : item.speed);

							if (isBouncing) {
								const bouncePhase = (item.bouncePhase || 0) + 0.1;
								const bounceOffset = item.bounceOffset || 0;
								const bounceHeight = 4;
								newY += Math.sin(bouncePhase + bounceOffset) * bounceHeight;

								return {
									...item,
									x: newX,
									y: newY,
									bouncePhase,
								};
							}

							if (isSpiral) {
								const angle = (item.angle || 0) + 0.05;
								const radius = (item.radius || 0) + 0.1;
								newX = item.x + Math.cos(angle) * radius * 0.3;
								newY =
									item.y +
									(isSlowMotion ? item.speed * 0.5 : item.speed) +
									Math.sin(angle) * radius * 0.3;

								return {
									...item,
									x: newX,
									y: newY,
									angle,
									radius,
								};
							}

							return {
								...item,
								x: newX,
								y: newY,
							};
						})
						.filter((item) => {
							const basketRect = {
								x: mousePosition.x - 25,
								y: mousePosition.y - 25,
								width: 50,
								height: 50,
							};

							const itemRect = {
								x: item.x,
								y: item.y,
								width: 30,
								height: 30,
							};

							if (
								basketRect.x < itemRect.x + itemRect.width &&
								basketRect.x + basketRect.width > itemRect.x &&
								basketRect.y < itemRect.y + itemRect.height &&
								basketRect.y + basketRect.height > itemRect.y
							) {
								applyItemEffect(item.type);
								return false;
							}

							return item.y < window.innerHeight && item.x >= 0 && item.x <= gameWidth;
						});
				});
				animationFrame = requestAnimationFrame(moveItems);
			}
		};

		if (gameStarted && !gameOver) {
			animationFrame = requestAnimationFrame(moveItems);
		}

		return () => {
			if (animationFrame) cancelAnimationFrame(animationFrame);
		};
	}, [gameStarted, gameOver, mousePosition, isBouncing, isSpiral, isSlowMotion, gameWidth]);

	const startGame = (): void => {
		setGameStarted(true);
		setGameOver(false);
		setScore(0);
		setItems([]);
		setIsBonus(false);
		clearEffects();
	};

	return (
		<div
			className="relative h-[calc(100vh-56px)] max-w-[480px] mx-auto overflow-hidden touch-none"
			ref={gameAreaRef}
		>
			{!gameStarted ? (
				<div className="absolute inset-0 flex flex-col items-center justify-center p-6 text-center">
					<h1 className="text-3xl font-bold text-white mb-6 font-nexonBold">행운을 잡아라</h1>
					<div className="bg-white/10 rounded-lg px-6 py-4 mb-6 text-white">
						<h2 className="text-xl font-nexonBold mb-4 mt-2 ">게임 설명</h2>
						<div className="text-sm font-nexonRegular mb-8">
							하늘에서 떨어지는
							<br /> 🍀를 잡아서 점수를 획득해라
						</div>
						<h2 className="text-xl font-nexonBold mb-4">아이템 목록</h2>
						<div className="grid grid-cols-2 gap-4 text-left">
							<div>
								<p className="text-base mb-2 font-nexonRegular">기본 아이템</p>
								<p className="text-sm font-nexonLight">🍀 점수 획득</p>
								<p className="text-sm font-nexonLight">⚡ 게임 오버!</p>
								<p className="text-sm font-nexonLight">🎁 대량발생</p>
							</div>
							<div>
								<p className="text-base mb-2 font-nexonRegular">특수 아이템</p>
								<p className="text-sm font-nexonLight">🎪 바운스 효과</p>
								<p className="text-sm font-nexonLight">🌀 태풍 효과</p>
								<p className="text-sm font-nexonLight">🌈 클로버 변화</p>
								<p className="text-sm font-nexonLight">⏰ 슬로우 모션</p>
							</div>
						</div>
					</div>
					<div className="w-full">
						<Button
							text="게임시작"
							color="green"
							size="long"
							font="bold"
							shadow="green"
							onClick={startGame}
						/>
					</div>
				</div>
			) : (
				<>
					<div className="absolute top-4 left-4 text-white text-2xl font-nexonRegular">점수: {score}</div>
					{isBonus && (
						<div className="absolute top-4 right-4 text-yellow-400 text-xl animate-pulse font-nexonRegular">
							대량 발생!
						</div>
					)}
					{isBouncing && (
						<div className="absolute top-12 right-4 text-pink-400 text-xl animate-bounce font-nexonRegular">
							바운스 효과!
						</div>
					)}
					{isSpiral && (
						<div className="absolute top-20 right-4 text-purple-400 text-xl animate-bounce font-nexonRegular">
							태풍 효과!
						</div>
					)}
					{isSlowMotion && (
						<div className="absolute top-28 right-4 text-blue-400 text-xl animate-pulse font-nexonRegular">
							슬로우 모션!
						</div>
					)}

					{items.map((item) => (
						<div
							key={item.id}
							className="absolute text-2xl"
							style={{
								left: item.x,
								top: item.y,
								transform: getRotation(item),
								transition: "transform 0.1s linear",
							}}
						>
							{item.type}
						</div>
					))}

					<div
						className="absolute w-12 h-12 pointer-events-none"
						style={{
							left: mousePosition.x - 24,
							top: mousePosition.y - 24,
							backgroundImage: `url(/assets/basket.png)`,
							backgroundSize: "contain",
							backgroundRepeat: "no-repeat",
						}}
					/>

					{gameOver && (
						<div className="absolute inset-0 bg-black bg-opacity-50 flex flex-col items-center justify-center">
							<div className="text-red-500 text-4xl mb-4 font-nexonRegular">Game Over</div>
							<div className="text-white text-2xl mb-6 font-nexonRegular">최종 점수: {score}점</div>
							<div className="w-full flex justify-center items-center gap-2 p-4">
								<Button
									text="다시하기"
									color="purple"
									size="long"
									font="bold"
									shadow="purple"
									onClick={startGame}
								/>
								<Button
									text="홈으로"
									color="green"
									size="long"
									font="bold"
									shadow="green"
									onClick={() => {
										router.push("/");
									}}
								/>
							</div>
						</div>
					)}
				</>
			)}
		</div>
	);
};

export default EasterEgg;
