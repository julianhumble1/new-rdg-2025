import { Table } from "flowbite-react"
import FestivalAwardRow from "./FestivalAwardRow.jsx"
import Cookies from "js-cookie"

const FestivalAwardsTable = ({ awards, handleDelete }) => {

    if (awards.length > 0) {
        const role = Cookies.get("role")

        return (
            <Table hoverable className='border overflow-auto max-w-screen'>
                <Table.Head className='text-lg'>
                    <Table.HeadCell>Award Name</Table.HeadCell>
                    <Table.HeadCell>Production</Table.HeadCell>
                    <Table.HeadCell>Person</Table.HeadCell>
                    {role === "ROLE_ADMIN" &&
                        <Table.HeadCell>Actions</Table.HeadCell>
                    }
                </Table.Head>
                <Table.Body className="divide-y">
                    {awards.map((award, index) => (
                        <FestivalAwardRow award={award} key={index} handleDelete={handleDelete} />
                    ))}
                </Table.Body>
            </Table>
        )
    } else {
        return (
            <div>No awards to display.</div>
        )
    }
}

export default FestivalAwardsTable
