import { Table } from 'flowbite-react'
import { Link } from 'react-router-dom'

const PersonRow = ({person, responseType}) => {
    if (responseType === "DETAILED") {

        return (
            <Table.Row className={`bg-white dark:border-gray-700 dark:bg-gray-800 `} >
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                    <Link to={`/people/${person.id}`} className='hover:underline' >
                        {person.firstName} {person.lastName}
                    </Link>
                </Table.Cell>
                <Table.Cell >
                    {person.homePhone}
                </Table.Cell>
                <Table.Cell >
                    {person.mobilePhone}
                </Table.Cell>
                <Table.Cell >
                    {person.addressStreet}
                </Table.Cell>
                <Table.Cell >
                    {person.addressTown}
                </Table.Cell>
                <Table.Cell >
                    {person.addressPostcode}
                </Table.Cell>
    
            </Table.Row>
        
        )   
    } else {
        return (
            <Table.Row className={`bg-white dark:border-gray-700 dark:bg-gray-800 `} >
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                    <Link to={`/people/${person.id}`} className='hover:underline' >
                        {person.firstName} {person.lastName}
                    </Link>
                </Table.Cell>
                <Table.Cell className='truncate max-w-md'>
                    {person.summary} 
                </Table.Cell>
            </Table.Row>
        )   
    }
}


export default PersonRow