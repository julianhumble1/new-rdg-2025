import { format } from 'date-fns'
import { Table } from 'flowbite-react'
import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'

const VenueRow = ({ venue, handleDelete, nameSearch }) => {
    
    const [hide, setHide] = useState(false)

    useEffect(() => {
        if (venue.name.toLowerCase().includes(nameSearch.toLowerCase())) {
            setHide(false) 
        } else {
            setHide(true)
        }
    }, [nameSearch, venue])

    return (
        <Table.Row className={`bg-white dark:border-gray-700 dark:bg-gray-800 ${hide && "hidden"}`} >
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link to={`/venues/${venue.id}`} className='hover:underline' >
                    {venue.name}
                </Link>
            </Table.Cell>
            <Table.Cell >
                {venue.address}
            </Table.Cell>
            <Table.Cell >
                {venue.town}
            </Table.Cell>
            <Table.Cell >
                {venue.postcode}
            </Table.Cell>
            <Table.Cell >
                {format(new Date(venue.createdAt), "dd-MM-yyyy")} 
            </Table.Cell>
            <Table.Cell>
                <div className='flex gap-2'>
                    <Link className="font-medium text-cyan-600 hover:underline dark:text-cyan-500" to={`/venues/${venue.id}?edit=true`}>
                        Edit
                    </Link>
                    <button className="font-medium text-cyan-600 hover:underline dark:text-cyan-500" onClick={() => handleDelete(venue)}>Delete</button>
                </div>
            </Table.Cell>
        </Table.Row>
    )
}

export default VenueRow