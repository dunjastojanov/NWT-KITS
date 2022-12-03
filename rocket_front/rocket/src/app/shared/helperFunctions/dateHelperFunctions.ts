export function getFullDateAndTimeString(date: Date): string {
  return date.getDay() + "." + date.getMonth() + "." + date.getFullYear() + ". " + date.getHours() + ":" + date.getMinutes();
}
