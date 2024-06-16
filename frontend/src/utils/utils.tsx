export function formatDate(date: Date): string {
  const options: Intl.DateTimeFormatOptions = {
    month: "long", // Full month name (e.g., "January")
    day: "numeric", // Numeric day of the month (e.g., "1")
    year: "numeric", // Full numeric year (e.g., "2024")
  };

  // Use toLocaleDateString to format the date according to options
  return date.toLocaleDateString("en-US", options);
}
