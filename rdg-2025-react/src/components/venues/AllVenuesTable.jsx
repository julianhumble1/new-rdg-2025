import { Table } from "flowbite-react"
import AltVenueRow from "./VenueRow.jsx"

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