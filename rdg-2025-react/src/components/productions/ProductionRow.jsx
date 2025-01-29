import DateHelper from "../../utils/DateHelper.js"
import { Link } from "react-router-dom"

const ProductionRow = ({ productionData, handleDelete }) => {

  return (
    <div className="bg-gray-200 grid grid-cols-12 h-fit hover:bg-gray-300">
      <div className="col-span-2 p-1">
        <Link to={`/productions/${productionData.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">{productionData.name}</Link>
      </div>
      <div className="col-span-2 p-1">
      {productionData.venue ?
        <Link to={`/venues/${productionData.venue.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
            {productionData.venue.name}
        </Link> : ""}
      </div>
      <div className="col-span-2 p-1"> {productionData.description} </div>
      <div className="col-span-1 p-1"> {productionData.sundowners ? "Yes" : "No"} </div>
      <div className="col-span-2 p-1"> {productionData.author} </div>
      <div className="col-span-1 p-1"> {DateHelper.formatDatabaseDateForDisplay(productionData.createdAt)} </div>
      <div className="flex flex-row gap-3">
          <Link className="underline text-blue-500 hover:text-blue-700 my-auto" to={`/productions/${productionData.id}?edit=true`}>Edit</Link>
          <button className="underline text-blue-500 hover:text-blue-700" onClick={() => handleDelete(productionData)}>Delete</button>
      </div>
    </div>

  )
}

export default ProductionRow