import { Table } from "flowbite-react"
import { Link } from "react-router-dom"
import CreditRow from "./CreditRow.jsx"

const CreditsTable = ({ credits }) => {
    
    if (credits.length > 0) {

        const creditType = credits[0].type

        return (
            <Table hoverable className='border overflow-auto max-w-screen'>
                <Table.Head className='text-lg'>
                    <Table.HeadCell>
                        {creditType === "ACTOR" &&
                            "Character Name"
                        }
                        {creditType === "MUSICIAN" &&
                            "Music Credit"
                        }
                        {creditType === "PRODUCER" &&
                            "Producer Credit"
                        }
                    </Table.HeadCell>
                    <Table.HeadCell>Played By</Table.HeadCell>
                    <Table.HeadCell>Production</Table.HeadCell>
                    <Table.HeadCell>Summary</Table.HeadCell>
                </Table.Head>
                <Table.Body className="divide-y">
                    {credits.map((credit, index) => (
                        <CreditRow credit={credit} key={index} />
                    ))}
                </Table.Body>
            </Table>
        )
    } else {
        return (
            <div>No credits of this type to display.</div>
        )
    }
}

export default CreditsTable