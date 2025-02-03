export default class MonthDateUtils {

    static monthMapping = {
        1: "January", 
        2: "February",
        3: "March",
        4: "April",
        5: "May",
        6: "June",
        7: "July",
        8: "August",
        9: "September",
        10: "October",
        11: "November",
        12: "December"
    }
    
    static monthOptions = [
        { value: 1, label: "January" },
        { value: 2, label: "February" },
        { value: 3, label: "March" },
        { value: 4, label: "April" },
        { value: 5, label: "May" },
        { value: 6, label: "June" },
        { value: 7, label: "July" },
        { value: 8, label: "August" },
        { value: 9, label: "September" },
        { value: 10, label: "October" },
        { value: 11, label: "November" },
        { value: 12, label: "December" }
    ]
    
    
    static getYearsArray = () => {
        const currentYear = new Date().getFullYear();
        const yearsArray = [];
        for (let i = 0; i < 10; i++) {
            yearsArray.push({"value": currentYear + 1, "label": currentYear + i})
        }
        return yearsArray
        
    }

}


