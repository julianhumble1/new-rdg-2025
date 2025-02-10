import { format } from "date-fns"
import { Link } from "react-router-dom"

const PerformanceRow = ({ performanceData }) => {

    const formattedDate = format(new Date(performanceData.time), "MMMM d, yyyy, h:mm a")

    return (<>
        <div className="grid grid-cols-5 bg-gray-200 h-fit hover:bg-gray-300">
            <div className="col-span-1 p-1">{formattedDate}</div>
            <div className="col-span-1 p-1">
                <Link to={`/productions/${performanceData.production.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
                    {performanceData.production.name}
                </Link>
            </div>
            <div className="col-span-1 p-1">
                <Link to={`/venues/${performanceData.venue.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
                    {performanceData.venue.name}
                </Link>
                    
            </div>
            <div className="col-span-1 p-1">
                {performanceData.festival ? 
                    <Link to={`/festivals/${performanceData.festival.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
                        {performanceData.festival.name}
                    </Link> : ""
                }
            </div> 
            <div className="col-span-1 p-1">{performanceData.description}</div>
        </div>
  </>)
}

export default PerformanceRow