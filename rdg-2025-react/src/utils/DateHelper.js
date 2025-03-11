import { format } from "date-fns";

export default class DateHelper {

    static formatDatabaseDateForDisplay = (dateString) => {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    }

    static createPerformanceStatement = (performances) => {

        const numberOfPerformances = performances.length
        const performanceDates = performances.map((performance) => performance.time)
        performanceDates.sort((a, b) => new Date(a) - new Date(b));
        const firstPerformanceString = format(performanceDates[0], "MMMM d, yyyy, h:mm a")
        const lastPerformanceString = format(performanceDates[performanceDates.length-1], "MMMM d, yyyy, h:mm a")
        
        return `${numberOfPerformances} performances between ${firstPerformanceString} and ${lastPerformanceString}.`

    }

}