import { Table } from 'flowbite-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import MonthDateUtils from '../../utils/MonthDateUtils.js'
import { format } from 'date-fns'

const FestivalRow = ({ festival, handleDelete, nameSearch, venueSearch }) => {

    const [hide, setHide] = useState(false)

    useEffect(() => {
        const nameMatches = festival.name.toLowerCase().includes(nameSearch.toLowerCase());
        const venueMatches = festival.venue && festival.venue.name.toLowerCase().includes(venueSearch.toLowerCase());

        if (nameSearch && !nameMatches) {
            setHide(true);
        } else if (venueSearch && !venueMatches) {
            setHide(true);
        } else {
            setHide(false);
        }
    }, [festival, nameSearch, venueSearch])

    return (
        <Table.Row className={`bg-white dark:border-gray-700 dark:bg-gray-800 ${hide && "hidden"}`} >
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link to={`/festivals/${festival.id}`} className='hover:underline' >
                    {festival.name}
                </Link>
            </Table.Cell>
            <Table.Cell >
                {festival.year} 
            </Table.Cell>
            <Table.Cell >
                {festival.venue ?
                    <Link to={`/venues/${festival.venue.id}`} className="whitespace-nowrap font-medium text-gray-900 dark:text-white hover:underline">
                        {festival.venue.name}
                    </Link> : ""}
            </Table.Cell>
            <Table.Cell className='truncate max-w-md'>
                {festival.description} 
            </Table.Cell>
            <Table.Cell >
                {festival.month ? MonthDateUtils.monthMapping[festival.month] : ""}
            </Table.Cell>
            <Table.Cell >
                {format(new Date(festival.createdAt), "dd-MM-yyyy")}
            </Table.Cell>
            <Table.Cell>
                <div className='flex gap-2'>
                    <Link className="text-medium text-black hover:underline font-bold text-end" to={`/festivals/${festival.id}?edit=true`}>
                        Edit
                    </Link>
                    <button className="text-medium text-black hover:underline font-bold text-end" onClick={() => handleDelete(festival)}>Delete</button>
                </div>
            </Table.Cell>
        </Table.Row>

    )
}

export default FestivalRow