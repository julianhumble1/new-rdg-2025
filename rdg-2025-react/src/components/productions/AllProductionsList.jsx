import { useState, useEffect } from "react"
import ProductionService from "../../services/ProductionService.js"
import ProductionRow from "./ProductionRow.jsx"

const AllProductionsList = () => {

    const [productions, setProductions] = useState([])

    const [fetchError, setFetchError] = useState("")

    useEffect(() => {
        const fetchAllProductions = async () => {
            try {
                const response = await ProductionService.getAllProductions()
                console.log(response)
                setProductions(response.data.productions)
            } catch (e) {
                console.log(e)
                setFetchError(e.message)
            }
        }
        fetchAllProductions()
    }, [])

    return (<>
        <div>List of all productions</div>
        {fetchError && 
            <div className="text-red-500">
                {fetchError}
            </div>
        }

        {productions && 
            <div className="mx-3">
                <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                    <div className="col-span-2 p-1">Name</div>
                    <div className="col-span-2 p-1">Venue</div>
                    <div className="col-span-2 p-1">Description</div>
                    <div className="col-span-2 p-1">Sundowners</div>
                    <div className="col-span-2 p-1">Author</div>
                    <div className="col-span-2 p-1">Date Created</div>
                </div>
                {productions.map((production, index) => (
                    <ProductionRow productionData={production} key={index} />
                ))}
            </div>
        }

  </>)
}

export default AllProductionsList