import { Table } from "flowbite-react"
import ProductionRow from "./ProductionRow.jsx"
import { Link } from "react-router-dom"
import { PlusCircleIcon } from "@heroicons/react/16/solid"

const ProductionsTable = ({ productions, handleDelete, nameSearch, venueSearch, sundownersSearch }) => {
    if (productions.length > 0) {
        return (
            <Table hoverable className='border overflow-auto max-w-screen'>
                <Table.Head className='text-lg'>
                    <Table.HeadCell>Production</Table.HeadCell>
                    <Table.HeadCell>Venue</Table.HeadCell>
                    <Table.HeadCell>Description</Table.HeadCell>
                    <Table.HeadCell>Sundowners</Table.HeadCell>
                    <Table.HeadCell>Author</Table.HeadCell>
                    <Table.HeadCell>Date Created</Table.HeadCell>
                    <Table.HeadCell>Actions</Table.HeadCell>
                </Table.Head>
                <Table.Body className="divide-y">
                    <Table.Row className="">
                        <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                            <Link className='hover:underline flex flex-row gap-2' to="/productions/new">
                                <PlusCircleIcon className="max-h-5 text-black text-opacity-75" />
                                <div className="my-auto">
                                    Add New Production...
                                </div>
                            </Link>
                        </Table.Cell>
                    </Table.Row>
                    {productions.map((production, index) => (
                        <ProductionRow production={production} handleDelete={handleDelete} key={index} nameSearch={nameSearch} venueSearch={venueSearch} sundownersSearch={sundownersSearch} />
                    ))} 
                </Table.Body>
            </Table>
        )
        
    } else {
        return (
            <div className="w-full text-center text-bold">
                No productions to display.
            </div>
        )
    }
}

export default ProductionsTable