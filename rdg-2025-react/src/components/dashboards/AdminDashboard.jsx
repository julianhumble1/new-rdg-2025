import { Link } from "react-router-dom"
import { ArrowLeftCircleIcon } from "@heroicons/react/16/solid"

const AltAdminDashboard = () => {
    return (
        <div className="w-full bg-slate-200 grid lg:grid-cols-4 sm:grid-cols-2 grid-cols-1">
            <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 flex justify-center">
                <div className="w-1/2 flex flex-col p-6 my-6 text-lg">
                    <div className="font-bold py-3 w-full italic">
                        Venues
                    </div>
                    <Link to="/venues/new" className="underline text-blue-500 hover:text-blue-700 w-full transition">
                        Add New Venue
                    </Link>
                    <Link to="/venues" className="underline text-blue-500 hover:text-blue-700 w-full">See All Venues</Link>
                </div>
            </div>
            <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 flex justify-center">
                <div className="w-1/2 flex flex-col p-6 my-6 text-lg">
                    <div className="font-bold py-3 w-full italic">
                        Productions
                    </div>
                    <Link to="/productions/new" className="underline text-blue-500 hover:text-blue-700">Add New Production</Link>
                    <Link to="/productions" className="underline text-blue-500 hover:text-blue-700">See All Productions</Link>
                </div>
            </div>
            <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 flex justify-center">
                <div className="w-1/2 flex flex-col p-6 my-6 text-lg">
                    <div className="font-bold py-3 w-full italic">
                        Festivals
                    </div>
                    <Link to="/festivals/new" className="underline text-blue-500 hover:text-blue-700">Add New Festival</Link>
                    <Link to="/festivals" className="underline text-blue-500 hover:text-blue-700">See All Festivals</Link>
                </div>
            </div>
            <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 flex justify-center">
                <div className="w-1/2 flex flex-col p-6 my-6 text-lg">
                    <div className="font-bold py-3 w-full italic">
                        Performances
                    </div>
                    <Link to="/performances/new" className="underline text-blue-500 hover:text-blue-700">Add New Performance</Link>
                </div>
            </div>

            <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 flex justify-center">
                <div className="w-1/2 flex flex-col p-6 my-6 text-lg">
                    <div className="font-bold py-3 w-full italic">
                        People
                    </div>
                    <Link to="/people/new" className="underline text-blue-500 hover:text-blue-700">Add New Person</Link>
                </div>
            </div>
        </div>
    )
}

export default AltAdminDashboard