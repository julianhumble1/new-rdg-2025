import { format } from "date-fns"

const PerformanceRow = ({ performanceData }) => {

    const formattedDate = format(new Date(performanceData.time), "MMMM d, yyyy, h:mm a")

    return (<>
        <div className="grid grid-cols-4 bg-gray-200 h-fit hover:bg-gray-300">
            <div className="col-span-1">{formattedDate}</div>
            <div className="col-span-1">{performanceData.venue.name} </div>
            {performanceData.festival ? 
                <div className="col-span-1">{performanceData.festival.name}</div>
                :
                <div className="col-span-1"></div>
            }
            <div className="col-span-1">{performanceData.description}</div>
        </div>
  </>)
}

export default PerformanceRow