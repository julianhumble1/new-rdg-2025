import { Table } from "flowbite-react"
import AltVenueRow from "./VenueRow.jsx"
import { Link } from "react-router-dom"
import { PlusCircleIcon } from "@heroicons/react/16/solid"

const AllVenuesTable = ({ venues, handleDelete, nameSearch }) => {
    
    if (venues.length > 0) {
        return (
            <Table hoverable className='border p-2'>
                <Table.Head className='text-lg'>
                    <Table.HeadCell>Venue</Table.HeadCell>
                    <Table.HeadCell>Address</Table.HeadCell>
                    <Table.HeadCell>Town</Table.HeadCell>
                    <Table.HeadCell>Postcode</Table.HeadCell>
                    <Table.HeadCell>Date Created</Table.HeadCell>
                    <Table.HeadCell>Actions</Table.HeadCell>
                </Table.Head>
                <Table.Body className="divide-y">
                    {venues.map((venue, index) => (
                        <AltVenueRow venue={venue} handleDelete={handleDelete} key={index} nameSearch={nameSearch} />
                    ))} 
                    <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
                        <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                            <Link className='hover:underline flex flex-row gap-2' to="/venues/new">
                                <PlusCircleIcon className="max-h-5 text-black text-opacity-75" />
                                <div className="my-auto">
                                    Add New Venue...
                                </div>
                            </Link>
                        </Table.Cell>

                    </Table.Row>
                </Table.Body>
            </Table>
        )
        
    } else {
        return (
            <div className="w-full text-center text-bold">
                No venues to display.
            </div>

        )

    }

}

export default AllVenuesTable