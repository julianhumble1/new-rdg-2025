import { Table } from "flowbite-react"
import FestivalRow from "./FestivalRow.jsx"
import { Link } from "react-router-dom"
import { PlusCircleIcon } from "@heroicons/react/16/solid"

const FestivalsTable = ({ festivals, handleDelete, nameSearch, venueSearch }) => {
        if (festivals.length > 0) {
        return (
            <Table hoverable className='border p-2 overflow-auto max-w-screen'>
                <Table.Head className='text-lg'>
                    <Table.HeadCell>Festival</Table.HeadCell>
                    <Table.HeadCell>Year</Table.HeadCell>
                    <Table.HeadCell>Venue</Table.HeadCell>
                    <Table.HeadCell>Description</Table.HeadCell>
                    <Table.HeadCell>Month</Table.HeadCell>
                    <Table.HeadCell>Date Created</Table.HeadCell>
                    <Table.HeadCell>Actions</Table.HeadCell>
                </Table.Head>
                <Table.Body className="divide-y">
                    <Table.Row className="">
                        <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                            <Link className='hover:underline flex flex-row gap-2' to="/festivals/new">
                                <PlusCircleIcon className="max-h-5 text-black text-opacity-75" />
                                <div className="my-auto">
                                    Add New Festival...
                                </div>
                            </Link>
                        </Table.Cell>
                    </Table.Row>
                    {festivals.map((festival, index) => (
                        <FestivalRow festival={festival} handleDelete={handleDelete} key={index} nameSearch={nameSearch} venueSearch={venueSearch} />
                    ))} 
                </Table.Body>
            </Table>
        )
        
    } else {
        return (
            <div className="w-full text-center text-bold">
                No festivals to display.
            </div>

        )

    }
}

export default FestivalsTable