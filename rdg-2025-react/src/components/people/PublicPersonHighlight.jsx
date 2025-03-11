import { Link } from "react-router-dom"

const PublicPersonHighlight = ({personData}) => {
    if (personData) return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <div className="text-black text-xl font-bold flex justify-between">
                <div>
                    {personData.firstName} {personData.lastName}
                </div>
            </div>
            {personData.summary &&
                <div className="text-sm">
                    {personData.summary}
                </div>    
            }
            <Link to="/people" className="text-sm hover:underline font-bold text-end">
                See All People
            </Link>
        </div>
    )
}

export default PublicPersonHighlight