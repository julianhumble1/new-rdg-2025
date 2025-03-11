import { format } from "date-fns"
import { Link } from "react-router-dom"
import AddressHelper from "../../utils/AddressHelper.js"

const DetailedPersonHighlight = ({ personData, setEditMode, handleDelete }) => {
    
    const fullAddress = AddressHelper.getFullAddress(personData.addressStreet, personData.addressTown, personData.addressPostcode)

    if (personData) return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <div className="text-black text-xl font-bold flex justify-between">
                <div>
                    {personData.firstName} {personData.lastName}
                </div>
                <div className="flex gap-2">
                    <button className="text-sm hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-sm hover:underline" onClick={() => handleDelete(personData)}>
                        Delete
                    </button>
                </div>
            </div>
            {personData.summary &&
                <div className="text-sm">
                    {personData.summary}
                </div>    
            }
            {fullAddress &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Address
                    </div>
                    <div>
                        {fullAddress}
                    </div>
                </div>       
            }
            {personData.homePhone &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Home
                    </div>
                    <div>
                        {personData.homePhone}
                    </div>
                </div>    
            }
            {personData.mobilePhone &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Mobile
                    </div>
                    <div>
                        {personData.mobilePhone}
                    </div>
                </div>    
            }
            <div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Created:
                    </div>    
                    <div>
                        {format(new Date(personData.createdAt), "dd-MM-yyyy")}
                    </div>
                </div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Last Updated:
                    </div>    
                    <div>
                        {format(new Date(personData.updatedAt), "dd-MM-yyyy")}
                    </div>
                </div>  
            </div>
            <Link to="/people" className="text-sm hover:underline font-bold text-end">
                See All People
            </Link>
            


        </div>
    )
}

export default DetailedPersonHighlight