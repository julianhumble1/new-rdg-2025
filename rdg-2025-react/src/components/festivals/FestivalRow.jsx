import { format } from "date-fns"
import { Link } from "react-router-dom"
import MonthDateUtils from "../../utils/MonthDateUtils.js"

const FestivalRow = ({ festivalData }) => {

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
        <div className="col-span-2 p-1"> {festivalData.month ? MonthDateUtils.monthMapping[festivalData.month] : ""} </div>
        <div className="col-span-2 p-1"> {format(new Date(festivalData.createdAt), "dd-MM-yyyy")} </div>
    </div>

  )
}

export default FestivalRow