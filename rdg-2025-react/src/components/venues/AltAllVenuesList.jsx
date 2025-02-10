import { Table } from 'flowbite-react'
import { useState, useEffect } from 'react';
import VenueService from '../../services/VenueService.js';
import { format } from 'date-fns';
import { Link } from 'react-router-dom';
import ConfirmDeleteModal from '../modals/ConfirmDeleteModal.jsx';

const AltAllVenuesList = () => {

    const [venues, setVenues] = useState([])

    const [showConfirmDelete, setShowConfirmDelete] = useState("");
    const [venueToDelete, setVenueToDelete] = useState(null);

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const handleDelete = (venue) => {
        setShowConfirmDelete(true)
        setVenueToDelete(venue)
    }

    const handleConfirmDelete = async (venue) => {
        try {
            await VenueService.deleteVenue(venue.id)
            setShowConfirmDelete(false)
            setVenueToDelete(null)
            setSuccessMessage(`Successfully deleted '${venue.name}'`)
            fetchAllVenues()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    const fetchAllVenues = async () => {
        try {
            const response = await VenueService.getAllVenues();
           	setVenues(response.data.venues)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    useEffect(() => {
      fetchAllVenues();
    }, [])


	return (
		
		<div className='overflow-x-auto'>
		  	{showConfirmDelete &&
			  	<ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={venueToDelete} handleConfirmDelete={ handleConfirmDelete } />
		  	}
        	<Table hoverable className='border p-2'>
          		<Table.Head className='text-lg'>
					<Table.HeadCell>Venue</Table.HeadCell>
					<Table.HeadCell>Address</Table.HeadCell>
					<Table.HeadCell>Town</Table.HeadCell>
					<Table.HeadCell>Postcode</Table.HeadCell>
					<Table.HeadCell>Date Created</Table.HeadCell>
					<Table.HeadCell>Actions</Table.HeadCell>
					<Table.HeadCell>
					<span className="sr-only">Edit</span>
					</Table.HeadCell>
				</Table.Head>
				<Table.Body className="divide-y">
					
					{venues.map((venue, index) => (
						<Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800" key={index}>
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
					))} 
				</Table.Body>
        	</Table>
      	</div>
    )
}

export default AltAllVenuesList