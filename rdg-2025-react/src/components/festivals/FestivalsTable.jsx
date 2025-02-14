import { Table } from "flowbite-react"
import FestivalRow from "./FestivalRow.jsx"

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