import DateHelper from "../../utils/DateHelper.js"

const ProductionRow = ({ productionData }) => {

  return (
        <div className="bg-gray-200 grid grid-cols-12 h-fit hover:bg-gray-300">
            <div className="col-span-2 p-1"> {productionData.name} </div>
            <div className="col-span-2 p-1"> {productionData.venue ? productionData.venue.name : ""} </div>
            <div className="col-span-2 p-1"> {productionData.description} </div>
            <div className="col-span-2 p-1"> {productionData.sundowners ? "Yes" : "No"} </div>
            <div className="col-span-2 p-1"> {productionData.author} </div>
            <div className="col-span-2 p-1"> {DateHelper.formatDatabaseDateForDisplay(productionData.createdAt)} </div>
        </div>

  )
}

export default ProductionRow