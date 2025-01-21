import DateHelper from "../../utils/DateHelper.js"
import { Link } from "react-router-dom"

const FestivalRow = ({ festivalData }) => {

    const monthMapping = {
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


  return (
        <div className="bg-gray-200 grid grid-cols-12 h-fit hover:bg-gray-300">
            <div className="col-span-2 p-1"> {festivalData.name} </div>
          <div className="col-span-2 p-1">
              {festivalData.venue ?
                <Link to={`/venues/${festivalData.venue.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
                    {festivalData.venue.name}
                </Link> : ""}
          </div>
            <div className="col-span-2 p-1"> {festivalData.description} </div>
            <div className="col-span-2 p-1"> {festivalData.year} </div>
            <div className="col-span-2 p-1"> {festivalData.month ? monthMapping[festivalData.month] : ""} </div>
            <div className="col-span-2 p-1"> {DateHelper.formatDatabaseDateForDisplay(festivalData.createdAt)} </div>
        </div>

  )
}

export default FestivalRow