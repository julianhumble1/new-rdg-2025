import { useEffect, useState } from "react"
import ProductionService from "../../services/ProductionService.js"
import DateHelper from "../../utils/DateHelper.js"

const HomeProductionSpotLight = ({ production }) => {
    
    const [performanceStatement, setPerformanceStatement] = useState("")

    useEffect(() => {
        const getPerformanceStatement = async () => {
            const response = await ProductionService.getProductionById(production.id)
            setPerformanceStatement(DateHelper.createPerformanceStatement(response.data.performances))
        }

        getPerformanceStatement()
    }, [production.id])

    return (
        <div className="flex w-full bg-white rounded">
            <img className=" h-full w-40" src="/kafka-flyer.jpg" alt="image 1" />
            <div className="flex flex-col gap-3 justify-center p-3 shadow-lg">
                <h5 className="text-xl font-bold tracking-tight text-gray-900 dark:text-white">
                    {production.name}
                </h5>
                <div className="text-sm italic"> 
                    {performanceStatement}
                </div>
                <div className=" text-gray-700 text-sm overflow-hidden max-h-24">
                    {production.description}
                </div>
            </div>
        </div>
    )
}

export default HomeProductionSpotLight