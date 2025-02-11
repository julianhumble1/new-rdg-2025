import { Table } from "flowbite-react"
import AltVenueRow from "./AltVenueRow.jsx"

const AltVenueTable = ({venues, handleDelete, nameSearch}) => {
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
}

export default AltVenueTable