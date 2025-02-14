import { format } from 'date-fns'
import { Table } from 'flowbite-react'
import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

const ProductionRow = ({ production, handleDelete, nameSearch, venueSearch, sundownersSearch }) => {
    
    const [hide, setHide] = useState(false)

    useEffect(() => {
        const nameMatches = production.name.toLowerCase().includes(nameSearch.toLowerCase());
        const venueMatches = production.venue && production.venue.name.toLowerCase().includes(venueSearch.toLowerCase());

        if (sundownersSearch && !production.sundowners) {setHide(true)}
        else if (nameSearch && !nameMatches) {
            setHide(true);
        } else if (venueSearch && !venueMatches) {
            setHide(true);
        } else {
            setHide(false);
        }
    }, [nameSearch, venueSearch, sundownersSearch, production])

    return (
        <Table.Row className={`bg-white dark:border-gray-700 dark:bg-gray-800 ${hide && "hidden"}`} >
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link to={`/productions/${production.id}`} className='hover:underline' >
                    {production.name}
                </Link>
            </Table.Cell>
            <Table.Cell >
                {production.venue ?
                    <Link to={`/venues/${production.venue.id}`} className="whitespace-nowrap font-medium text-gray-900 dark:text-white hover:underline">
                        {production.venue.name}
                    </Link> : ""}
            </Table.Cell>
            <Table.Cell className='truncate max-w-md'>
                {production.description} 
            </Table.Cell>
            <Table.Cell >
                {production.sundowners ? "Yes" : "No"}
            </Table.Cell>
            <Table.Cell >
                {production.author} 
            </Table.Cell>
            <Table.Cell >
                {format(new Date(production.createdAt), "dd-MM-yyyy")}
            </Table.Cell>
            <Table.Cell>
                <div className='flex gap-2'>
                    <Link className="text-medium text-black hover:underline font-bold text-end" to={`/productions/${production.id}?edit=true`}>
                        Edit
                    </Link>
                    <button className="text-medium text-black hover:underline font-bold text-end" onClick={() => handleDelete(production)}>Delete</button>
                </div>
            </Table.Cell>
        </Table.Row>
    )
}

export default ProductionRow