import ProductionRow from "./ProductionRow.jsx"

const ProductionsTable = ({ productions, handleDelete }) => {
    
    if (productions.length > 0) {
        return (
          <div className="mx-3">
                <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                    <div className="col-span-2 p-1">Name</div>
                    <div className="col-span-2 p-1">Venue</div>
                    <div className="col-span-2 p-1">Description</div>
                    <div className="col-span-1 p-1">Sundowners</div>
                    <div className="col-span-2 p-1">Author</div>
                    <div className="col-span-1 p-1">Date Created</div>
                    <div className="col-span-2 p-1">Actions</div>
                </div>
                {productions.map((production, index) => (
                    <ProductionRow productionData={production} key={index} handleDelete={handleDelete}/>
                ))}
            </div>
        )
    } else {
        return <div className="font-bold m-3">No productions to display</div>
    }

}

export default ProductionsTable