// 월별 일수 계산하는 메서드
const currentDate = new Date();
const currentYear = currentDate.getFullYear();
export const currentMonth = currentDate.getMonth() + 1;
export const daysInMonth = new Date(currentYear, currentMonth, 0).getDate();

// 일자에 맞는 출석 여부를 확인하는 메서드
export const checkAttendance = (day: number, attendanceList: string[]) => {
	const dateStr = `${currentYear}-${String(currentMonth).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
	return attendanceList.includes(dateStr);
};
