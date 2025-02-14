import { format } from "date-fns"
import { Link } from "react-router-dom"

const AltPerformanceRow = ({ performanceData }) => {
    
    const formattedDate = format(new Date(performanceData.time), "MMMM d, yyyy, h:mm a")

    return (
        <div className="flex flex-col text-sm p-2 hover:bg-gray-200 bg-gray-100 border gap-1 md:gap-0">
            <div className="font-bold">
                {formattedDate}
            </div>
            <div>
                <Link to={`/productions/${performanceData.production.id}`} className="hover:underline">
                    {performanceData.production.name}
                </Link>
            </div>
            <div>
                <Link to={`/venues/${performanceData.venue.id}`} className="hover:underline">
                    {performanceData.venue.name}
                </Link>
            </div>
            <div>
                {performanceData.festival ? 
                    <Link to={`/festivals/${performanceData.festival.id}`} className="hover:underline">
                        {performanceData.festival.name} ({performanceData.festival.year})
                    </Link> : ""
                }
            </div>
            <div className="italic">
                {performanceData.description}
            </div>
        </div>
    )
}

export default AltPerformanceRow